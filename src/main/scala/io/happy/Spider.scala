package io.happy

import scala.util.matching.Regex

object Spider {

  type ExtractorPair[T] = (Regex, List[String] => ExtractorState[T])

  trait ExtractorState[T] {

    def read(line: String): ExtractorState[T] = (goto orElse stay) (line)

    lazy val stay: PartialFunction[String, ExtractorState[T]] = {
      case _ => this
    }

    def goto: PartialFunction[String, ExtractorState[T]]

    def extracted: Option[T]
  }

  case class Esi[T](goto: PartialFunction[String, ExtractorState[T]], extracted: Option[T] = None) extends ExtractorState[T]

  object Esi {
    def parse[T](goto: PartialFunction[String, ExtractorState[T]])(implicit extracted: Option[T] = None): Esi[T] = Esi(goto, extracted)
  }

}
