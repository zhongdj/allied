package software.xuanruanjian

import software.xuanruanjian.DetailParser2.Esi.parse
import software.xuanruanjian.DetailParser2.{Esi, ExtractorState}

case class StateBuilderM[T](fs: List[PartialFunction[(T, String), T]]) {
  def repeat(): StateBuilderM[T] = ???

  def andThen(partialFunction: PartialFunction[(T, String), T]): StateBuilderM[T] = copy(fs = partialFunction :: this.fs)

  def end(partialFunction: PartialFunction[(T, String), T]): StateBuilderM[T] = andThen(partialFunction)

  def last: T => ExtractorState[T] = t => parse(PartialFunction.empty)(Some(t))

  def build: T => ExtractorState[List[T]] = {
    fs.foldLeft(last) { case (acc, previous) => (t: T) =>
      parse[T] {
        case line if previous.isDefinedAt((t, line)) => acc(previous.apply((t, line)))
      }(Some(t))
    }
    ???
  }
}

object StateBuilderM {
  def initialize[T](partialFunction: PartialFunction[(T, String), T]): StateBuilderM[T] = StateBuilderM(List({
    case (defaultValue: T, line: String) => partialFunction.applyOrElse((defaultValue, line), (_: (T, String)) => defaultValue)
  }))
}
