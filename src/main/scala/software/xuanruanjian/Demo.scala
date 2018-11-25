package software.xuanruanjian

import io.happy.Spider.ExtractorState
import software.XRJProductParser.Product

import scala.io.Source
import scala.util.matching.Regex

object Demo extends App {
  /*

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


    val e = """						<a href="/soft/tag6025.phtml?p=1" class=""><上一页</a><span><a class="on">1</a><a href="/soft/tag6025.phtml?p=2">2</a><a href="/soft/tag6025.phtml?p=3">3</a><a href="/soft/tag6025.phtml?p=4">4</a><a href="/soft/tag6025.phtml?p=5">5</a></span><a href="/soft/tag6025.phtml?p=2" class="">下一页></a>"""
    val last = """.*class=\"on\">(.+?)</a>.*>(.*)?</a></span>.*""".r
    e match {
      case last(a, b) => println(s" on = ${a}, last = ${b}")
      case _ => println("not matched")
    }

    val f = """          <a href="/tag/erp/" style="color:#e4393c" target="_blank">ERP系统</a>"""
    val secondaryCategory = """.*?<a href=\"/tag/.*\".*?>(.*?)</a>.*""".r

    f match {
      case secondaryCategory(name) => println(name)
      case _ => println("not matched")
    }

  //  val g = """						<li>开发商：上海驷惠软件科技开发有限公司</li>						<li>类别：<a href="/tag/car/" style="color:#e4393c" target="_blank">汽修汽配</a></li>"""
    val g = """						<li>开发商：星城软件</li>						<li>类别：<a href="/soft/tag5943.phtml" style="color:#e4393c" target="_blank">食品行业</a></li>"""
    val company = """.*?<li>开发商：(.*?)</li>.*?<a href=\"/soft/.*\">(.*?)</a>.*""".r
    g match {
      case company(name, cate) => println(s"${name}, ${cate}")
      case _ => println("not found")
    }
  Source.fromURL("https://www.xuanruanjian.com/soft/msg/48621.phtml").getLines().foreach(println)


  type Prod = Product

  val prod = """<div class="prod">""".r
  val name = """.*?<h3>(.*?)</h3>.*""".r
  val company = """.*?<li>开发商：(.*?)</li>.*?<a href=\"/tag/.*\">(.*?)</a>.*""".r
  val company2 =
    """.*?<li>开发商：(.*?)</li>.*?<a href=\"/soft/.*\">(.*?)</a>.*""".r

  {
    import io.happy.StateBuilder._

    val prodExtractor: ExtractorState[Prod] =
      initialize[Prod] {
        case (p, prod(_)) => p
      }.andThen {
        case (p, name(n)) => p.copy(title = n)
      }.andThen {
        case (p, company(c, s)) => p.copy(companyName = c, secondaryCategory = s)
        case (p, company2(c, s)) => p.copy(companyName = c, secondaryCategory = s)
      }.build(Product())

    println(
      Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/detail.html", "GB2312")
        .getLines()
        .foldLeft[ExtractorState[Prod]](prodExtractor)((s, l) => s.read(l))
        .extracted
    )
  }
*/
  /*
    {
      import StateBuilderM._

      val productsExtractor: ExtractorState[List[Product]] =
        initialize[Product] {
          case (p, prod(_)) => p
        }.andThen {
          case (p, name(n)) => p.copy(title = n)
        }.andThen {
          case (p, company(c, s)) => p.copy(companyName = c, secondaryCategory = s)
          case (p, company2(c, s)) => p.copy(companyName = c, secondaryCategory = s)
        }.repeat()
          .build(Product())

      Source.fromFile("/Users/barry/Workspaces/frank/allied/src/main/resources/detail.html", "UTF8")
        .getLines()
        .foldLeft[ExtractorState[List[Prod]]](productsExtractor)((s, l) => s.read(l))
        .extracted
        .foreach(_.foreach(println))

    }
    */

  val category = "工具软件-实用生活服务"

  def subCate: String =
    if (category.contains("-")) category.substring(category.indexOf("-") + 1)
    else ""

  println(subCate)


  println("世纪空联（北京）航空电子科技有限公司"
      .replaceAll("（", "(")
      .replaceAll("）", ")")
    .equalsIgnoreCase("世纪空联(北京)航空电子科技有限公司"))


  println(
    "CC542537520.htm".replace(""".""", """\.""")
  )
}