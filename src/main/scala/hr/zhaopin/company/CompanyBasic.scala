package hr.zhaopin.company

import hr.zhaopin.company.Max99.name

case class CompanyBasic(name: String, city: City, cType: String, industries: List[Industry], scale: Scale, site: String, addr: String, introduction: String) {
  override def toString = s"""${name}\t${city.code}\t${city.name}\t${cType}\t${industries.map({ i => i.code + "\t" + i.name }).mkString(",")}\t${scale.name}\t${site}\t${addr}\t${introduction}"""
}

object CompanyBasic {
  def apply(name: String, city: City, site: String): CompanyBasic =
    CompanyBasic(name, city, "", Nil, TBD, site, "", "")
}

trait CompanyType

case object 股份制企业

case object 民营

case object 代表处

case object 上市公司

case object 其他

case object 外商独资

case object 合资


sealed trait Scale {
  def name: String
}

object Scale {
  val all = Max20 :: Max99 :: Max499 :: Max999 :: Max9999 :: Min10000 :: Nil

  def from(txt: String): Scale = all.filter(_.name.equals(txt)).headOption.getOrElse(TBD)
}


case object Max20 extends Scale {
  override def name: String = "20人以下"
}

case object Max99 extends Scale {
  override def name: String = "20-99人"
}

case object Max499 extends Scale {
  override def name: String = "100-499人"
}

case object Max999 extends Scale {
  override def name: String = "500-999人"
}

case object Max9999 extends Scale {
  override def name: String = "1000-9999人"
}

case object Min10000 extends Scale {
  override def name: String = "10000人以上"
}

case object TBD extends Scale {
  override def name: String = "TBD"
}