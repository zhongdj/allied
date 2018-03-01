package hr.zhaopin.company

package object reader {
  val siteNameReg = """(?s)(?i)<a href="(.+?)" .+?>(.+)</a>""".r
  val typeReg = """(?s)(?i)<a href=".+?>(.+)</a>""".r
  val industriesReg = """(?s)(?i)<span class="fleft width55">(.+)</span>""".r
  val scaleReg = """(?s)(?i)<span class="fleft width105">(.+)</span>""".r
}
