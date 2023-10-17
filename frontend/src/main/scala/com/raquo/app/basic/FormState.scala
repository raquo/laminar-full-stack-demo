package com.raquo.app.basic

case class FormState(
  city: String = "",
  zip: String = "",
  showErrors: Boolean = false
) {

  def hasErrors: Boolean = cityError.nonEmpty || zipError.nonEmpty

  def cityError: Option[String] = {
    if (city.nonEmpty) {
      None
    } else {
      Some("City must not be empty.")
    }
  }

  def zipError: Option[String] = {
    if (zip.forall(Character.isDigit) && zip.length == 5) {
      None
    } else {
      Some("Zip code must consist of 5 digits.")
    }
  }

  def displayError(error: FormState => Option[String]): Option[String] = {
    error(this).filter(_ => showErrors)
  }
}
