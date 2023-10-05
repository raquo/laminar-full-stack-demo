package com.raquo.weather

import cats.Parallel
import cats.effect.IO
import com.raquo.weather.ecapi.{CityStationReportXml, DateTimeXml}
import sttp.client3.*
import sttp.client3.httpclient.cats.HttpClientCatsBackend

import scala.util.{Failure, Success, Try}

object WeatherFetcher {

  /**
   * @param cityStationId  [[CityStation]] ID
   * @return (cityStationId, reportXml). IO can fail with ApiError
   */
  def fetchCityWeather(cityStationId: String): IO[(String, CityStationReportXml)] = {
    HttpClientCatsBackend.resource[IO]().use { backend =>
      basicRequest
        .get(uri"https://dd.weather.gc.ca/citypage_weather/xml/BC/${cityStationId}_e.xml")
        .send(backend)
        .map { response =>
          response.body match {
            case Left(err) =>
              throw ApiError(err, response.code.code)
            case Right(xmlString) =>
              ecapi.CityStationReportDecoder.decode(xmlString) match {
                case Left(decodingError) =>
                  throw ApiError(s"City id ${cityStationId}: ${decodingError.getMessage}")
                case Right(reportXml) =>
                  (cityStationId, reportXml)
              }

          }
        }
    }
  }

  def fetchGradient(gradientId: String): IO[GradientReport] = {
    Try(Gradient.byId(gradientId)) match {
      case Failure(_) =>
        throw ApiError(s"Unknown gradient id: `$gradientId`.", 400)

      case Success(gradient) =>
        val cityStationIds = gradient.cityIds
        Parallel
          .parTraverse(cityStationIds) { cityStationId =>
            fetchCityWeather(cityStationId)
          }
          .map { cityStationXmlReports =>
            cityStationReportsToGradientReport(gradient, cityStationXmlReports.toMap)
          }
    }
  }

  private def findLocalDateTime(datetimes: List[DateTimeXml]): DateTimeXml = {
    val localDateTimes = datetimes.filter(_.zone != "UTC")
    assert(
      localDateTimes.length == 1,
      s"Expected 1 local datetime, found ${localDateTimes.length}:\n\n${localDateTimes.toString()}"
    )
    localDateTimes.head
  }

  /**
   * @param gradient
   * @param cityReportXmls
   * @throws ApiError if unable to compile report
   * @return
   */
  private def cityStationReportsToGradientReport(
    gradient: Gradient,
    cityReportXmls: Map[String, CityStationReportXml]
  ): GradientReport = {
    // #TODO[Integrity] Assert that we're getting the expected units
    //  (e.g. that the API reports degrees in Celsius – we have this data, just not checking right now.)

    val currentConditionsByCity = cityReportXmls.collect {
      case (cityStationId, CityStationReportXml(Some(currentConditionsXml), _)) =>
        val dateObserved = findLocalDateTime(currentConditionsXml.dateTime)
        val maybeWind = if (currentConditionsXml.wind.speed.value.nonEmpty) {
          Some(EcWindConditions(
            speedKmh = Try(currentConditionsXml.wind.speed.value.toDouble).getOrElse(0),
            direction = currentConditionsXml.wind.direction.value,
            bearingDegrees = Try(currentConditionsXml.wind.bearing.value.toDouble).getOrElse(0),
          ))
        } else {
          None
        }
        val currentConditions = CityCurrentConditions(
          datetimeObserved = dateObserved.toLocalDateTime.toString,
          temperatureC = Try(currentConditionsXml.temperature.value.toDouble).toOption,
          pressureKPa = Try(currentConditionsXml.pressure.value.toDouble).toOption,
          relativeHumidityPercent = Try(currentConditionsXml.relativeHumidity.value.toDouble).toOption,
          wind = maybeWind
        )

        (cityStationId, currentConditions)
    }

    GradientReport(
      cities = gradient.cities,
      currentConditionsByCity = currentConditionsByCity,
      forecastDays = Nil, // #nc
      forecastsByDay = Map.empty // #nc
    )
  }

}
