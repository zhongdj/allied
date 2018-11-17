package software.xuanruanjian

import scala.util.matching.Regex

object Demo extends App {


  val cate: Regex = """.*<a href=\"([^javascript].+?)\".+?>(.+?)<.*""".r
  val c = """                                <div class="tit"><a href="/tag/erp/" target="_blank">ERP系统</a></div>"""
  printCat(c)

  private def printCat(line: String) = {
    line match {
      case cate(url, title) =>
        println(url)
        println(title)
      case _ =>
        println("not found")
    }
  }

  val d = """<a href="javascript:;" class="ig"><img src="https://www.xuanruanjian.com/images/v2016/sell_img_17.jpg" alt=""/></a>}"""

  printCat(d)

}