package com.raquo.weather

import CityStation.*

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

  val gradientIds: Array[String] = Gradient.values.map(_.id)

  def byId(gradientId: String): Gradient = {
    Gradient.values
      .find(_.id == gradientId)
      .getOrElse(throw new Exception(s"Unknown gradient id: `${gradientId}`"))
  }
}
