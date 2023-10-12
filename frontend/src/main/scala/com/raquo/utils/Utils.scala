package com.raquo.utils

object Utils {

  extension [A](a: A)
    def some: Some[A] = Some(a)
}
