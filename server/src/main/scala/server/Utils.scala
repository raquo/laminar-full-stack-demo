package server

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.nio.charset.Charset
import java.util.function.Consumer
import java.util.stream.Collectors

object Utils {

  // #TODO I thought these were needed to make conversions, but not my code works without them? How so???
  // implicit def functionToKotlin[I, O](f: I => O): kotlin.jvm.functions.Function1[I, O] = {
  //   new kotlin.jvm.functions.Function1[I, O] {
  //     override def invoke(v: I): O = f(v)
  //   }
  // }
  //
  // implicit def functionToConsumer[A](f: A => Unit): Consumer[A] = new Consumer[A] {
  //   override def accept(v: A): Unit = f(v)
  // }

  /**
   * Reads given resource file as a string.
   *
   * @param fileName path to the resource file
   * @return the file's contents
   * @throws IOException if read fails for any reason
   */
  def getResourceFileAsString(fileName: String, charset: Charset): String = {
    // From https://stackoverflow.com/a/46613809/2601788
    val is = ClassLoader.getSystemClassLoader.getResourceAsStream(fileName)
    try {
      if (is == null) {
        throw new IOException(s"Resource `${fileName}` not found.`")
      }
      val isr = new InputStreamReader(is, charset)
      val reader = new BufferedReader(isr)
      try {
        reader.lines.collect(Collectors.joining(System.lineSeparator))
      } finally {
        if (isr != null) isr.close()
        if (reader != null) reader.close()
      }
    } finally {
      if (is != null) is.close()
    }
  }
}
