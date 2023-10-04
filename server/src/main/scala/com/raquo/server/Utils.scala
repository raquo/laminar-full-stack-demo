package com.raquo.server

import cats.effect.IO
import cats.effect.std.Dispatcher
import cats.effect.unsafe.IORuntime
import io.javalin.http.{Context, Handler}
import com.github.plokhotnyuk.jsoniter_scala.core.*

import java.io.{BufferedReader, IOException, InputStreamReader}
import java.nio.charset.Charset
import java.util.concurrent.CompletableFuture
import java.util.function.{Consumer, Supplier}
import java.util.stream.Collectors
import scala.concurrent.Future
import scala.jdk.FutureConverters.{*, given}

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

  // implicit def futureFnToJavaSupplier[A](f: () => Future[A]): Supplier[CompletableFuture[A]] = new Supplier[CompletableFuture[A]]:
  //   override def get(): CompletableFuture[A] = f().asJava.toCompletableFuture

  extension [A] (io: IO[A])

    def tap(sideEffect: A => Unit): IO[A] = {
      io.flatTap(v => IO { sideEffect(v) })
    }

    def tapError(sideEffect: Throwable => Unit): IO[A] = {
      io.onError(err => IO {
        sideEffect(err)
      })
    }

  // TODO: This does not work because Context => IO[_] already... qualifies as Handler?
  // given ioFnToHandler: Conversion[Context => IO[_], Handler] with
  //   def apply(f: Context => IO[_]): Handler = new Handler:
  //     override def handle(ctx: Context): Unit = {
  //       f(ctx).unsafeRunSync()(IORuntime.global)
  //     }

  extension (ctx: Context)

    def future(f: () => Future[_]): Unit = {
      val javaFutureSupplier = new Supplier[CompletableFuture[_]] {
        override def get(): CompletableFuture[_] = f().asJava.toCompletableFuture
      }
      ctx.future(javaFutureSupplier)
    }

    def io(f: IO[_]): Unit = {
      val runtime = IORuntime.global
      f.unsafeRunSync()(runtime)
    }
    
    def jsonResult[@specialized A](v: A, config: WriterConfig = WriterConfig)(implicit codec: JsonValueCodec[A]): Unit =
      ctx.result(writeToString(v, config))

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
