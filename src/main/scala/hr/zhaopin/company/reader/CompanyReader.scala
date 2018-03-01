package hr.zhaopin.company.reader

import hr.zhaopin.company.{City, CompanyBasic, Industry, Scale}

trait CompanyReader {
  def read(line: String): CompanyReader
  def acc: List[CompanyBasic]
}

object CompanyReader {


}

case class EmptyReader(city: City, acc: List[CompanyBasic] = Nil) extends CompanyReader {
  def read(line: String): CompanyReader = {
    if (line.contains("""<div class="jobs-list-box">"""))
      NameReader(city, acc)
    else this
  }
}

case class NameReader(city: City, acc: List[CompanyBasic] = Nil) extends CompanyReader {
  def read(line: String): CompanyReader = {
    if (line.contains("""<a href="http://company.zhaopin.com/""")) {
      val c = {
        for {
          siteNameReg(site, name) <- siteNameReg.findFirstIn(line)
        } yield CompanyBasic(name, city, site)
      }.get
      TypeReader(city, c, acc)
    }
    else this
  }
}

case class TypeReader(city: City, c: CompanyBasic, acc: List[CompanyBasic]) extends CompanyReader {
  def read(line: String): CompanyReader = {
    if (line.contains( """<a href="javascript:void(0)"""")) {
      val t = {
        for {
          typeReg(types) <- typeReg.findFirstIn(line)
        } yield types
      }.get
      ScaleReader(city, c.copy(cType = t), acc)
    } else this
  }
}
case class ScaleReader(city: City, c: CompanyBasic, acc: List[CompanyBasic]) extends CompanyReader {
  def read(line: String): CompanyReader = {
    if (line.contains( """<span class="fleft width105">""")) {
      val i = {
        for {
          scaleReg(industries) <- scaleReg.findFirstIn(line)
        } yield industries
      }.getOrElse("")
      IndustryReader(city, c.copy(scale = Scale.from(i)), acc)
    } else this
  }
}

case class IndustryReader(city: City, c: CompanyBasic, acc: List[CompanyBasic]) extends CompanyReader {
  def read(line: String): CompanyReader = {
    if (line.contains( """<span class="fleft width55">""")) {
      val i = {
        for {
          industriesReg(industries) <- industriesReg.findFirstIn(line)
        } yield industries
      }.get
      EmptyReader(city, c.copy(industries = Industry.from(i)) :: acc)
    } else this
  }
}

