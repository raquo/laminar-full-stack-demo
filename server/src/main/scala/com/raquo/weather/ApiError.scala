package com.raquo.weather

class ApiError(val message: String, val httpStatusCode: Int) extends Exception(message) {

  def this(message: String) = this(message, 500)
}
