package hr.zhaopin.company

case class City(name: String, code: String)

object City {
  val _data: List[(String, String)] = List(
    ("北京", "beijing"),
    ("上海", "shanghai"),
    ("广州", "guangzhou"),
    ("深圳", "shenzhen"),
    ("杭州", "hangzhou"),
    ("成都", "chengdu"),
    ("武汉", "wuhan"),
    ("天津", "tianjin")
  )

  val data = _data.map(t => City(t._1, t._2))
}