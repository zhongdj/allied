package hr.zhaopin.company

case class Industry(name: String, code: String)

object Industry {
  val _data : List[(String, String)] = List(
    ("互联网","210500"),
    ("电子商务","2105001"),
    ("计算机软件","160400"),
    ("it服务","160000")
  )

  val data: List[Industry] = _data.map(t => Industry(t._1, t._2))

  def from(names: String): List[Industry] = {
    val set = names.trim.split(",").toSet
    data.filter(x => set.contains(x.name))
  }
}
