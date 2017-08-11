package example

trait ContentReader {

  def read(line: String): ContentReader

  def getGroup: Option[Group] = None

  private val IMG_TAG = "(?s)(?i)<img ([^<]+)>".r
  private val IMG_SRC = """\s*(?i)src\s*=\s*(?:"([^"]*)"|'([^']*)'|([^'">\s]+))\s*""".r
  private val ANCHOR_GREEDY_TAG = """(?s)(?i)<a (.*)>(.+)?</a>""".r
  private val ANCHOR_NON_GREEDY_TAG = """(?s)(?i)<a (.+?)>(.+?|)</a>""".r
  private val HREF_ATTR = """(?s)\s*(?i)href\s*=\s*\\?(?:"([^"\\]*)\\?"|'([^'\\]*)\\?'|([^'">\s]+))\s*""".r
  private val COMP_TAG = """(?s)(?i)(?:.*?<span>(.*)?</span>.*|(.*))""".r

  def findImageLinks(body: String): Iterator[String] = {
    for {
      IMG_TAG(anchor) <- IMG_TAG.findAllIn(body)
      IMG_SRC(dquot, quot, bare) <- IMG_SRC findAllIn (anchor)
    } yield if (dquot != null) dquot trim
    else if (quot != null) quot trim
    else bare trim
  }

}

case class EmptyReader(id: Int) extends ContentReader {
  override def read(line: String) = {
    if (line contains ("""img src="upload/""")) {
      SquareCodeReader(id, findImageLinks(line).toList.head)
    } else if (line contains("""<img src="http://img2.weixinqun.com""")) {
      SquareCodeReader(id, findImageLinks(line).toList.head)
    }
    else {
      this
    }
  }
}

case class SquareCodeReader(id: Int, squareCode: String) extends ContentReader {
  override def read(line: String) = if (line contains """<span class="des_info_text" style=" font-size:20px;">""") AwaitingNameReader(id, squareCode) else this

}

case class AwaitingNameReader(id: Int, squareCode: String) extends ContentReader {
  override def read(line: String) = NameReader(id, squareCode, extractName(line))

  private def extractName(line: String): String = {

    val NAME_TAG = """(?s)\s*(?i)<b>(.*)?</b>""".r
    for {
      NAME_TAG(name) <- NAME_TAG.findAllIn(line)
    } yield name.trim
  }.toList.head

}

case class NameReader(id: Int, squareCode: String, name: String) extends ContentReader {
  override def read(line: String) = if (line contains("""<span class="des_info_text2" style="width:380px; margin-left:48px;">""")) AwaitingDescReader(id, squareCode, name) else this
}

case class AwaitingDescReader(id: Int, squareCode: String, name: String) extends ContentReader {
  override def read(line: String) = DescReader(id, squareCode, name, line.trim)
}

case class DescReader(id: Int, squareCode: String, name: String, desc: String) extends ContentReader {

  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, "N/A", "", "", 0))
  override def read(line: String) = if (line contains """<a href="/group?m=1""") AwaitingIndustryReader(id, squareCode, name, desc) else this
}

case class AwaitingIndustryReader(id: Int, squareCode: String, name: String, desc: String) extends ContentReader {
  override def read(line: String) = IndustryReader(id, squareCode, name, desc, line.trim)

}

case class IndustryReader(id: Int, squareCode: String, name: String, desc: String, industry: String) extends ContentReader {
  override def read(line: String) = if (line.matches("""\s*\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\s*""")) TimeReader(id, squareCode, name,desc, industry, line.trim) else this
  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, industry, "", "", 0))
}

case class TimeReader(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String) extends ContentReader {
  override def read(line: String) = if (line contains ("""<a href="/group""")) AwaitingLocation(id, squareCode, name, desc, industry, createdOn) else this
  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, industry, "", createdOn, 0))
}

case class AwaitingLocation(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String) extends ContentReader {
  override def read(line: String) = LocationReader(id, squareCode, name, desc, industry, createdOn, line.trim)
  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, industry, "N/A", createdOn, 0))
}

case class LocationReader(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String, location: String) extends ContentReader {
  override def read(line: String) = if (line contains """标签：""") AwaitingLabelReader(id, squareCode, name, desc, industry, createdOn, location) else this
  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, industry, location, createdOn, 0))
}

case class AwaitingLabelReader(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String, location: String, acc: String = "") extends ContentReader {
  override def read(line: String) =
    if ( (line contains """</a>""") || (line contains """</li>""")) LabelReader(id, squareCode, name, desc, industry, createdOn, location, acc.replaceAll("""<a href""", ""))
  else LabelReader(id, squareCode, name, desc, industry, createdOn, location, acc + line.trim)
}

case class LabelReader(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String, location: String, label: String) extends ContentReader {
  override def read(line: String) = if (line contains """<span class="Pink">""") AwaitingPopularity(id, squareCode, name, desc, industry, createdOn, location, label) else this
  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, industry, createdOn, location, 0, List(label)))
}

case class AwaitingPopularity(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String, location: String, label: String) extends ContentReader {
  override def read(line: String) = PopularityReader(id, squareCode, name, desc, industry, createdOn, location, label, line.trim)
}

case class PopularityReader(id: Int, squareCode: String, name: String, desc: String, industry: String, createdOn: String, location: String, label: String, trim: String) extends  ContentReader {
  override def read(line: String) = this
  override def getGroup: Option[Group] = Some(Group(id, squareCode, name, desc, industry, createdOn, location, trim.toInt, List(label)))
}
