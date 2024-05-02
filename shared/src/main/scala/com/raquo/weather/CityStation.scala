package com.raquo.weather

import io.bullet.borer.*
import io.bullet.borer.derivation.MapBasedCodecs.*

import scala.scalajs.js.annotation.JSExportAll

/** Note: the coordinates are the geographic center of the city, not the station location.
  *
  * @param id       e.g. s0000141
  * @param province e.g. BC
  * @param name     e.g. Vancouver
  * @param cityLat  in decimal degrees, e.g. 49.25N
  * @param cityLon  in decimal degrees, e.g. 123.12W
  */
@JSExportAll
case class CityStation(
  id: String,
  province: String,
  name: String,
  cityLat: String,
  cityLon: String
)

object CityStation {

  given codec: Codec[CityStation] = deriveCodec

  lazy val Ucluelet: CityStation = CityStation(
    id = "s0000482",
    province = "BC",
    name = "Ucluelet",
    cityLat = "48.94N",
    cityLon = "125.55W"
  )

  lazy val PortAlberni: CityStation = CityStation(
    id = "s0000294",
    province = "BC",
    name = "Port Alberni",
    cityLat = "49.24N",
    cityLon = "124.80W"
  )

  lazy val Kamloops: CityStation = CityStation(
    id = "s0000568",
    province = "BC",
    name = "Kamloops",
    cityLat = "50.67N",
    cityLon = "120.33W"
  )

  lazy val Merritt: CityStation = CityStation(
    id = "s0000006",
    province = "BC",
    name = "Merritt",
    cityLat = "50.11N",
    cityLon = "120.79W"
  )

  lazy val Lytton: CityStation = CityStation(
    id = "s0000242",
    province = "BC",
    name = "Lytton",
    cityLat = "50.23N",
    cityLon = "121.57W"
  )

  lazy val Hope: CityStation = CityStation(
    id = "s0000547",
    province = "BC",
    name = "Hope",
    cityLat = "49.38N",
    cityLon = "121.44W"
  )

  lazy val Abbotsford: CityStation = CityStation(
    id = "s0000758",
    province = "BC",
    name = "Abbotsford",
    cityLat = "49.06N",
    cityLon = "122.25W"
  )

  lazy val Vancouver: CityStation = CityStation(
    id = "s0000141",
    province = "BC",
    name = "Vancouver",
    cityLat = "49.25N",
    cityLon = "123.12W"
  )

  lazy val Squamish: CityStation = CityStation(
    id = "s0000323",
    province = "BC",
    name = "Squamish",
    cityLat = "49.75N",
    cityLon = "123.13W"
  )

  lazy val Whistler: CityStation = CityStation(
    id = "s0000078",
    province = "BC",
    name = "Whistler",
    cityLat = "50.12N",
    cityLon = "122.97W"
  )

  lazy val Pemberton: CityStation = CityStation(
    id = "s0000173",
    province = "BC",
    name = "Pemberton",
    cityLat = "50.32N",
    cityLon = "122.80W"
  )

  lazy val Lillooet: CityStation = CityStation(
    id = "s0000222",
    province = "BC",
    name = "Lillooet",
    cityLat = "50.68N",
    cityLon = "121.93W"
  )

}
