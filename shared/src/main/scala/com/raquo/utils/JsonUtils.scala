package com.raquo.utils

import io.bullet.borer.*
import io.bullet.borer.Json.DecodingConfig

import java.nio.charset.StandardCharsets

object JsonUtils {

  extension (json: Json.type)

    // #TODO Not sure if this is the most efficient way to do this.
    //  - In JS, you can also use https://developer.mozilla.org/en-US/docs/Web/API/TextEncoder
    //  - Most decoding should be done on byte arrays, you can get those from network
    def decodeString(value: String): DecodingSetup.Api[DecodingConfig] = {
      json.decode(value.getBytes(StandardCharsets.UTF_8))
    }
}
