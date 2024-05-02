package com.raquo.weather

import CityStation.*

import scala.scalajs.js.annotation.JSExportAll

// BEGIN[wind-gradient]
@JSExportAll
enum Gradient(
  val id: String,
  val name: String,
  val cities: List[CityStation]
) {

  val cityIds: List[String] = cities.map(_.id)

  // Note: Lytton burned down and does not have current observations, so its usefulness is questionable
  case Squamish extends Gradient(
    id = "squamish",
    name = "Squamish",
    cities = List(Kamloops, Lytton, Merritt, Hope, Abbotsford, Vancouver, CityStation.Squamish, Whistler, Pemberton, Lillooet)
  )

  case Nitinat extends Gradient(
    id = "nitinat",
    name = "Nitinat",
    cities = List(Ucluelet, PortAlberni)
  )
}

object Gradient {

  //given codec: Codec[Gradient] = Codec.of[String].bimap(_.id, forId(_))

  val gradientIds: List[String] = Gradient.values.map(_.id).toList

  def forId(gradientId: String): Option[Gradient] = {
    Gradient.values.find(_.id == gradientId)
  }

  def forIdGet(gradientId: String): Gradient = {
    forId(gradientId).getOrElse(throw new Exception(s"Unknown gradient id: `${gradientId}`"))
  }
}
// END[wind-gradient]
