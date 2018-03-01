package hr.zhaopin.company

case class PageRequest(city: City, industry: Industry, page: Int) {
  override def toString = s"http://company.zhaopin.com/${city.code}/${industry.code}/p${page}"
}

object PageRequest {
  val data = for {
    city <- City.data
    industry <- Industry.data
    page <- 1 to 100
  } yield PageRequest(city, industry, page)


}