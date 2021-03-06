package io.happy

import Spider.Esi.parse
import Spider.ExtractorState

case class StateBuilder[T](fs: List[PartialFunction[(T, String), T]]) {

  def andThen(partialFunction: PartialFunction[(T, String), T]): StateBuilder[T] = copy(fs = partialFunction :: this.fs)

  def end(partialFunction: PartialFunction[(T, String), T]): StateBuilder[T] = andThen(partialFunction)

  def last: T => ExtractorState[T] = t => parse(PartialFunction.empty)(Some(t))

  def build: T => ExtractorState[T] = {
    fs.foldLeft(last) { case (acc, previous) => (t: T) =>
      parse[T] {
        case line if previous.isDefinedAt((t, line)) => acc(previous.apply((t, line)))
      }(Some(t))
    }
  }
}

object StateBuilder {
  def initialize[T](partialFunction: PartialFunction[(T, String), T]): StateBuilder[T] = StateBuilder(List({
    case (defaultValue: T, line: String) => partialFunction.applyOrElse((defaultValue, line), (_: (T, String)) => defaultValue)
  }))
}

