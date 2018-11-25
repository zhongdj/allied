package baidu

import java.net.URLEncoder

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, PoisonPill, Props, SupervisorStrategy}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.MediaRanges._
import akka.http.scaladsl.model.MediaType.NotCompressible
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.stream.ActorMaterializer
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONArray, BSONDocument, BSONObjectID}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.io.Source
import scala.util.matching.Regex // use any appropriate context

object XinQuery extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  val driver = new reactivemongo.api.MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost:27017"))
  val coll: Future[BSONCollection] = connection.database("company").map(_.collection("startup"))

  def eventualCompanies(): Future[List[BSONDocument]] =
    coll.flatMap { bColl =>
      bColl.find(BSONDocument("$and" -> BSONArray(
        BSONDocument("pid" -> BSONDocument("$exists" -> false)),
        BSONDocument("searched" -> BSONDocument("$exists" -> false))
      )))
        .cursor[BSONDocument]()
        .collect[List](-1, Cursor.FailOnError[List[BSONDocument]]())
    }


  def exists(cName: String): Boolean = Await.result(
    coll.flatMap[Boolean] { bColl =>
      bColl.count(
        selector = Some(
          BSONDocument("$and" -> BSONArray(
            BSONDocument("name" -> cName),
            BSONDocument("pid" -> BSONDocument("$exists" -> true))
          ))
        )
      ).map(_ > 0)
    }
    , 10 second)

  /*
  def exists(cName: String): Boolean = Await.result(
    coll.flatMap[Boolean] { bColl =>
      bColl.count(
        selector = Some(
          BSONDocument("$and" -> BSONArray(
            BSONDocument("name" -> cName),
            BSONDocument("pid" -> BSONDocument("$exists" -> "true"))
          ))
        )
      ).map(_ > 0)
    }
    , 10 second)
 */

  def markSearched(id: BSONObjectID) = {
    val updateR = coll.flatMap { bColl =>
      println(s"markSearched ${id}")
      bColl.update(false)
        .one(BSONDocument("_id" -> id),
          BSONDocument("$set" -> BSONDocument("searched" -> "true")), false, false)
    }
    Await.result(updateR, 10 seconds)
  }

  private def queryThenWrite(id: BSONObjectID, name: String): String => Unit = line => {
    val regex: Regex = link(name)
    line match {
      case regex(pid, cname) =>
        if (same(name, cname)) {
          update(id, pid)
          markSearched(id)
        }
        else if (!exists(cname)) {
          insert(pid, cname)
          markSearched(id)
        }
        else {
          println(s"${cname} exists")
          markSearched(id)
        }
      case _ =>
      //println(s"${name} ignored")
    }

  }

  private def same(name: String, cname: String) = {
    cname.replaceAll("（", "(")
      .replaceAll("）", ")").trim()
      .equalsIgnoreCase(name.trim())
  }

  private def update(id: BSONObjectID, pid: String): Unit = {
    println(s"update ${id} ${pid}")
    val updateR = coll.flatMap { bColl =>
      bColl.update(false)
        .one(BSONDocument("_id" -> id),
          BSONDocument("$set" -> BSONDocument("pid" -> pid)), false, false)
    }
    Await.result(updateR, 10 seconds)
  }

  private def insert(pid: String, cname: String): Unit = {
    println(s"insert ${cname}, ${pid}")
    Await.result(
      coll.flatMap { bColl =>
        bColl.insert(BSONDocument(
          "pid" -> pid,
          "name" -> cname
        ))
      }, 10 seconds
    )
  }

  class Root extends Actor {
    val monitor = context.actorOf(Props(new WorkerSup))
    eventualCompanies
      .map { companies =>
        monitor ! companies
      }

    override def receive: Receive = {
      case e => println(e)
    }

    override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case e =>
        e.printStackTrace()
        Stop
    }
  }

  class WorkerSup extends Actor {

    var counter = 0;
    var work: Map[BSONObjectID, BSONDocument] = Map.empty
    var activeWork: Map[BSONObjectID, BSONDocument] = Map.empty

    override def receive: Receive = {
      case cs: List[BSONDocument] =>
        work = cs.groupBy(idOf(_))
          .map(p => (p._1, p._2.head))
        work.toList.take(32).foreach(cc => dispatchWork(cc._2))
      case id: BSONObjectID =>
        activeWork -= id
        if (work.nonEmpty) {
          println(s"dispatching from ${work.size}...")
          val head = work.head
          dispatchWork(head._2)
        }
        else {
          println("Finished")
        }
    }


    override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
      case e =>
        e.printStackTrace()
        Stop
    }

    private def dispatchWork(company: BSONDocument) = {
      val worker = context.actorOf(Props(new Worker))
      activeWork += (idOf(company) -> company)
      work -= idOf(company)
      worker ! company
    }
  }

  private def idOf(company: BSONDocument) = {
    company.getAs[BSONObjectID]("_id").get
  }

  class Worker extends Actor {
    override def receive: Receive = {
      case company: BSONDocument =>
        loadCompany(company)
        context.parent ! idOf(company)
        self ! PoisonPill
    }
  }

  private def loadCompany = {
    company: BSONDocument =>
      try {
        val id: BSONObjectID = idOf(company)
        val name: String = company.getAs[String]("name").get
        aGet(url(name))
          .foreach {
            queryThenWrite(id, name)
          }
      }
      catch {
        case e: Throwable => e.printStackTrace()
      } finally {
        //Thread.sleep(1000L)
      }
  }

  private def wget(name: String) = {
    Source.fromURL(url(name), "UTF-8")
      .getLines().toStream
  }

  private def url(name: String) = {
    println(s"--------------------------Querying for ${name} --------------------------------------------")
    val query = URLEncoder.encode(name, "UTF-8")
    s"https://xin.baidu.com/s?q=${query}"
  }

  var value = System.currentTimeMillis()
  var last = value

  private def aGet(url: String): Stream[String] = {
    last = 1543072082 //value
    value = 1543087092 //System.currentTimeMillis()
    val responseFuture: Future[HttpResponse] = Http()
      .singleRequest(HttpRequest(uri = url, headers = List(
        `User-Agent`("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36"),
        Accept(MediaType.applicationBinary("json", NotCompressible), `*/*;q=MIN`),
        Connection("keep-alive"),
        Cookie(("BAIDUID", "C3ABEDBFAF44E54316540BA602BE7386"),
          ("FG", "1"),
          ("BIDUPSID", "C3ABEDBFAF44E54316540BA602BE7386"),
          ("PSTM", "1537576071"),
          ("BDUSS", "DBYaFRGMXUwYnJmfkVIMTV1Vk45dWVZbmZHd0VYMmdTNXVaOX5yaThWb2Z0QTljQVFBQUFBJCQAAAAAAAAAAAEAAAAE4n5pcG9sYXJpc2FkbWluAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB8n6FsfJ-hbW"),
          ("BDORZ", "B490B5EBF6F3CD402E515D22BCDA1598"),
          ("BDPPN", "ff2666a693c8990a80739a227857b9c2"),
          ("log_guid", "74dd0981f812820bc0d28eb70c7e786a"),
          ("Hm_lvt_baca6fe3dceaf818f5f835b0ae97e4cc", s"${last}"),
          ("delPer", "0"),
          ("PSINO", "2"),
          ("H_PS_PSSID", "26524_1445_21087_18560_26350_27245_22159"),
          ("Hm_lpvt_baca6fe3dceaf818f5f835b0ae97e4cc", s"${value}")),
        //          ("Hm_lpvt_baca6fe3dceaf818f5f835b0ae97e4cc", s"1543081903")),
        Referer(uri = "https://xin.baidu.com/detail/compinfo?pid=XVpaiXHG5rzIrRgJUIS23S1LNZJwbIVFvA2u&tab=basic&fr=hotword")
      ))
      )
    responseFuture.onFailure {
      case e: Throwable => e.printStackTrace()
    }
    Await.result(
      responseFuture.flatMap { res =>
        res.entity.dataBytes.fold("")(_ ++ _.utf8String).runReduce[String](_ + _)
      }, 20 seconds).split("""\n""").toStream
  }

  def link(name: String): Regex =
    s""".*href=\"/detail/compinfo\\?pid=(.+?)\" title=\"(.+?)\".*""".r

  system.actorOf(Props(new Root))
  /*
    val reg = link("广德天运新技术股份有限公司")
    val sample = """<div class="zx-list-title"><div class="index-search-type"><span class="type-name cur ZX_LOG_TAB" data-type="0" data-holder="输入企业名、注册号、法定代表人等" data-click='{"cate": "all_result", "action": "click"}' data-log-an="infotab" data-log-idx="0">全部</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="1" data-holder="输入企业名、注册号" data-click='{"cate": "enterprise_name_result", "action": "click"}' data-log-an="infotab" data-log-idx="1">企业名/注册号</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="2" data-holder="输入法定代表人" data-click='{"cate": "legal_represent_result", "action": "click"}' data-log-an="infotab" data-log-idx="2">法定代表人</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="3" data-holder="输入地址" data-click='{"cate": "address_result", "action": "click"}' data-log-an="infotab" data-log-idx="3">地址</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="4" data-holder="输入经营范围" data-click='{"cate": "scope_business_result", "action": "click"}' data-log-an="infotab" data-log-idx="4">经营范围</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="5" data-holder="输入股东" data-click='{"cate": "shareholders_result", "action": "click"}' data-log-an="infotab" data-log-idx="5">股东</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="6" data-holder="输入主要人员" data-click='{"cate": "main_members_result", "action": "click"}' data-log-an="infotab" data-log-idx="6">主要人员</span><span class="type-space"></span><span class="type-name  ZX_LOG_TAB" data-type="7" data-holder="输入商标名称" data-click='{"cate": "mark_result", "action": "click"}' data-log-an="infotab" data-log-idx="7">商标</span></div></div><div class="zx-list-op-wrap"><div class="zx-list-filter"><div class="filter-title"><span class="filter-title-content">筛选</span><div class="filter-title-wrap"><span class="filter-title-tag-wrap"></span><a class="filter-clear hide">清除全部</a></div></div><div class="filter-wrap"><div class="filter-item oneline" data-key="regCapLevel" data-name="注册资本"><div class="filter-label">注册资本</div><a class="filter-op hide"><span class="filter-op-open">展开<i class="arrow"><em></em><ins></ins></i></span></a><div class="filter-content ZX_LOG_BTN" data-click='{"cate": "filter", "action": "click&pv"}'><a class="filter-tag" data-value="level2" data-name="10万-100万" data-log data-log-an="filter" data-log-idx="0">10万-100万(1)</a><div class="filter-mask"></div></div></div><div class="filter-item oneline" data-key="provinceCode" data-name="省份地区"><div class="filter-label">省份地区</div><a class="filter-op hide"><span class="filter-op-open">展开<i class="arrow"><em></em><ins></ins></i></span></a><div class="filter-content ZX_LOG_BTN" data-click='{"cate": "filter", "action": "click&pv"}'><a class="filter-tag" data-value="110000" data-name="北京市" data-log data-log-an="filter" data-log-idx="0">北京市(1)</a><div class="filter-mask"></div></div></div></div></div><div class="zx-list-count"><div class="zx-list-count-left"><span>搜索</span><span class="zx-search-query-quote">&nbsp;"</span><span class="zx-search-query" title="北京喜柚艺术有限公司">北京喜柚艺术有限公司</span><span class="zx-search-query-quote">"&nbsp;</span><span>为你找到相关信息</span><em class="zx-result-counter">1</em><span>条</span></div><div class="zx-list-count-filter"><div class="zx-list-filter-show"><span class="filter-show-content">默认排序</span><i class="arrow"><em></em></i></div><ul class="zx-list-filter-wrap ZX_LOG_LINK" data-click='{"cate": "sort", "action": "click&pv"}'><li class="zx-list-filter-item" data-value="0" data-log-an="filter" data-log-idx="0">默认排序</li><li class="zx-list-filter-item" data-value="1" data-log-an="filter" data-log-idx="1">注册资本升序</li><li class="zx-list-filter-item" data-value="2" data-log-an="filter" data-log-idx="2">注册资本降序</li><li class="zx-list-filter-item" data-value="3" data-log-an="filter" data-log-idx="3">成立时间升序</li><li class="zx-list-filter-item last" data-value="4" data-log-an="filter" data-log-idx="4">成立时间降序</li></ul></div></div><div class="zx-list-op-mask"></div></div><!-- 宽泛词 --><div class="zx-list-wrap"><div class="zx-list-item"><div class="zx-ent-logo"><a target="_blank" style="background: url(/static/pc/photo/logo.png) center/contain no-repeat; filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/static/pc/photo/logo.png',sizingMethod='scale');background: none\9;" href="/detail/compinfo?pid=3-cNjR25rVDA6fdQ9WD5Ao6wi3yV4PAQJQHs" title="北京喜柚艺术有限公司"></a></div><div class="zx-ent-info"><div class="zx-ent-items"><h3 class="zx-ent-title"><a class="zx-list-item-url" target="_blank" href="/detail/compinfo?pid=3-cNjR25rVDA6fdQ9WD5Ao6wi3yV4PAQJQHs" title="北京喜柚艺术有限公司" data-log-an="company" data-log-idx="0"><em>北京</em><em>喜</em><em>柚</em><em>艺术</em><em>有限公司</em></a></h3><div class="zx-ent-props"><span class="zx-ent-item zx-ent-person long"><span class="zx-ent-pre-title">法定代表人：</span><span class="legal-txt" title="林慧雯">林慧雯</span></span><span class="zx-ent-item zx-ent-date"><span class="zx-ent-pre-title">成立日期：</span>2014-01-23</span><span class="zx-ent-item zx-ent-type long"><span class="zx-ent-pre-title">企业类型：</span>有限责任公司(自然人独资)</span><span class="zx-ent-item zx-ent-status"><span class="zx-ent-pre-title">营业状态：</span>开业</span></div></div><div class="zx-ent-address"><i class="zx-ent-address-icon"></i><span class="zx-ent-address-text" title="北京市朝阳区樱花园28号楼(樱花集中办公区1002号)">北京市朝阳区樱花园28号楼(樱花集中办公区1002号)</span></div></div></div>"""
    sample match {
      case reg(a, b) => println(a, " ", b)
      case _ => println("not found")
    }
  */
}
