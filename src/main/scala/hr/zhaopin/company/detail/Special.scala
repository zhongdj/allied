package hr.zhaopin.company.detail

object Special extends App {

  val wids = Set("5bfabad724990f67bb0b12c4",
    "5bfabad724990f67bb0b12d4",
    "5bfabad824990f67bb0b13ce",
    "5bfabad924990f67bb0b14f6",
    "5bfabad924990f67bb0b1576",
    "5bfabada24990f67bb0b16a1",
    "5bfabadb24990f67bb0b1872",
    "5bfabadc24990f67bb0b1a09",
    "5bfabadd24990f67bb0b1adf",
    "5bfabade24990f67bb0b1cb2",
    "5bfabade24990f67bb0b1cf0",
    "5bfabadf24990f67bb0b1e97",
    "5bfabae124990f67bb0b2179",
    "5bfabae324990f67bb0b24ef",
    "5bfabaea24990f67bb0b2c12",
    "5bfabaec24990f67bb0b2ca4",
    "5bfabaed24990f67bb0b2ccf",
    "5bfabaef24990f67bb0b2d45",
    "5bfabaef24990f67bb0b2d47",
    "5bfabaf024990f67bb0b2d8a",
    "5bfabaf424990f67bb0b2ed9",
    "5bfabaf424990f67bb0b2efa",
    "5bfabbd624990f67bb0b36d8",
    "5bfabbd924990f67bb0b379d",
    "5bfabbdf24990f67bb0b3904",
    "5bfabbe224990f67bb0b39de",
    "5bfabbe424990f67bb0b3a82",
    "5bfabbe724990f67bb0b3b3b",
    "5bfabbea24990f67bb0b3bdf",
    "5bfabbef24990f67bb0b3d52",
    "5bfabbfb24990f67bb0b4093",
    "5bfabbfd24990f67bb0b40ed",
    "5bfabc0424990f67bb0b42e2",
    "5bfabc0524990f67bb0b42fa",
    "5bfabc0524990f67bb0b4300",
    "5bfabc0524990f67bb0b4315",
    "5bfabc1324990f67bb0b463d",
    "5bfabc1524990f67bb0b46a9",
    "5bfabc1a24990f67bb0b47f6",
    "5bfabc2524990f67bb0b4b1b",
    "5bfabc2724990f67bb0b4b86",
    "5bfabc2d24990f67bb0b4d1a",
    "5bfabc2f24990f67bb0b4dcc",
    "5bfabc3324990f67bb0b4ed6",
    "5bfabc3f24990f67bb0b51d3",
    "5bfabc5824990f67bb0b58cb",
    "5bfabc6624990f67bb0b5c4c",
    "5bfabc6724990f67bb0b5ca5",
    "5bfabc6b24990f67bb0b5dbc",
    "5bfabc6c24990f67bb0b5ddc",
    "5bfabc6e24990f67bb0b5ea8",
    "5bfabc7024990f67bb0b5f08",
    "5bfabc7024990f67bb0b5f4e",
    "5bfabc7124990f67bb0b5f90",
    "5bfabc7424990f67bb0b6074",
    "5bfabc7b24990f67bb0b6220",
    "5bfabc7c24990f67bb0b625b",
    "5bfabc7d24990f67bb0b62b6",
    "5bfabc8324990f67bb0b6455",
    "5bfabc8324990f67bb0b6459",
    "5bfabc8724990f67bb0b6569",
    "5bfabc8824990f67bb0b65b3",
    "5bfabc8824990f67bb0b65c4",
    "5bfabc8f24990f67bb0b6774",
    "5bfabc9024990f67bb0b680b",
    "5bfabc9424990f67bb0b6921",
    "5bfabc9524990f67bb0b6975",
    "5bfabc9d24990f67bb0b6b9c",
    "5bfabca624990f67bb0b6de7",
    "5bfabcaa24990f67bb0b6f09",
    "5bfabcad24990f67bb0b700f",
    "5bfabcc124990f67bb0b75a7",
    "5bfabcc224990f67bb0b75da",
    "5bfabccb24990f67bb0b7822",
    "5bfabccf24990f67bb0b790f",
    "5bfabcd424990f67bb0b7a73",
    "5bfabcde24990f67bb0b7d86",
    "5bfabce124990f67bb0b7e43",
    "5bfabce824990f67bb0b802f",
    "5bfabceb24990f67bb0b80f5",
    "5bfabcf224990f67bb0b82dd",
    "5bfabd0424990f67bb0b8811",
    "5bfabd0424990f67bb0b8817",
    "5bfabd0624990f67bb0b88a7",
    "5bfabd1324990f67bb0b8bca",
    "5bfabd1524990f67bb0b8c5d",
    "5bfabd1624990f67bb0b8c9b",
    "5bfabd1924990f67bb0b8d76",
    "5bfabd2224990f67bb0b8fc8",
    "5bfabd2824990f67bb0b9177",
    "5bfabd2824990f67bb0b9192",
    "5bfabd2d24990f67bb0b929c",
    "5bfabd2d24990f67bb0b92c0",
    "5bfabd3624990f67bb0b9530",
    "5bfabd3924990f67bb0b95f8",
    "5bfabd5024990f67bb0b9bb8",
    "5bfabd5524990f67bb0b9cec",
    "5bfabd5724990f67bb0b9d8c",
    "5bfabd5d24990f67bb0b9f71",
    "5bfabd6224990f67bb0ba090",
    "5bfabd6c24990f67bb0ba367",
    "5bfabd6d24990f67bb0ba3a1",
    "5bfabd7024990f67bb0ba435",
    "5bfabd7124990f67bb0ba483",
    "5bfabd7524990f67bb0ba553",
    "5bfabde224990f67bb0bb089",
    "5bfac10624990f67bb0bbf1c",
    "5bfac10824990f67bb0bbfa0",
    "5bfac10d24990f67bb0bc0dd",
    "5bfac10f24990f67bb0bc1a9",
    "5bfac11024990f67bb0bc1d3",
    "5bfac11024990f67bb0bc1ed",
    "5bfac11124990f67bb0bc203",
    "5bfac11424990f67bb0bc2dc",
    "5bfac11e24990f67bb0bc55a",
    "5bfac12d24990f67bb0bc929",
    "5bfac13024990f67bb0bc9fd",
    "5bfac13724990f67bb0bcbe7",
    "5bfac13824990f67bb0bcc08",
    "5bfac13c24990f67bb0bcd41",
    "5bfac14324990f67bb0bcf1b",
    "5bfac14924990f67bb0bd0ac",
    "5bfac14c24990f67bb0bd166",
    "5bfac19524990f67bb0bdc41",
    "5bfac19924990f67bb0bdd6c",
    "5bfac19a24990f67bb0bdd98",
    "5bfac19a24990f67bb0bddac",
    "5bfac19d24990f67bb0bde4c",
    "5bfac1a324990f67bb0be011",
    "5bfac1a424990f67bb0be04d",
    "5bfac1b124990f67bb0be38c",
    "5bfac1c124990f67bb0be7de",
    "5bfac1c924990f67bb0bea02",
    "5bfac1dc24990f67bb0beed6",
    "5bfac1e624990f67bb0bf1c0",
    "5bfac1f024990f67bb0bf44f",
    "5bfac1f924990f67bb0bf763",
    "5bfac1ff24990f67bb0bf930",
    "5bfac21524990f67bb0c009c",
    "5bfac21524990f67bb0c00a5",
    "5bfac21824990f67bb0c01b1",
    "5bfac21d24990f67bb0c034c",
    "5bfac22124990f67bb0c048d",
    "5bfac22324990f67bb0c0523",
    "5bfac22924990f67bb0c0776",
    "5bfac23224990f67bb0c0ac8",
    "5bfac23d24990f67bb0c0e27",
    "5bfac24e24990f67bb0c13b3",
    "5bfac24f24990f67bb0c13f3",
    "5bfac25124990f67bb0c14bd",
    "5bfac25324990f67bb0c1591",
    "5bfac25524990f67bb0c1616",
    "5bfac25524990f67bb0c1633",
    "5bfac25524990f67bb0c1659",
    "5bfac25a24990f67bb0c17ad",
    "5bfac25d24990f67bb0c18c9",
    "5bfac27324990f67bb0c1e6f",
    "5bfac2b824990f67bb0c276d",
    "5bfac2ba24990f67bb0c28c3",
    "5bfac2ba24990f67bb0c2951",
    "5bfac2bb24990f67bb0c29c7",
    "5bfac2c024990f67bb0c2b92",
    "5bfac2c524990f67bb0c2d0f",
    "5bfac2c924990f67bb0c2e8b",
    "5bfac2cd24990f67bb0c3054",
    "5bfac2dc24990f67bb0c3544",
    "5bfac2e224990f67bb0c3704",
    "5bfac2e524990f67bb0c3817",
    "5bfac2eb24990f67bb0c39c7",
    "5bfac2f124990f67bb0c3b2d",
    "5bfac30224990f67bb0c3fd4",
    "5bfac30324990f67bb0c4023",
    "5bfac30f24990f67bb0c4339",
    "5bfac31224990f67bb0c4454",
    "5bfac31c24990f67bb0c46c2",
    "5bfac31c24990f67bb0c46f9",
    "5bfac33224990f67bb0c4cd3",
    "5bfac34c24990f67bb0c53ef",
    "5bfac35124990f67bb0c5547",
    "5bfac36624990f67bb0c5a8f",
    "5bfac37724990f67bb0c5f23",
    "5bfac37824990f67bb0c5f44",
    "5bfac37b24990f67bb0c6044",
    "5bfac39f24990f67bb0c6abb",
    "5bfac3a824990f67bb0c6dd0",
    "5bfac3a924990f67bb0c6e1a",
    "5bfac3a924990f67bb0c6e34",
    "5bfac3aa24990f67bb0c6eba",
    "5bfac3b324990f67bb0c7180",
    "5bfac3b424990f67bb0c71d5",
    "5bfac3b724990f67bb0c72cc",
    "5bfac3b824990f67bb0c733f",
    "5bfac3b924990f67bb0c7359",
    "5bfac3b924990f67bb0c7388",
    "5bfac3bf24990f67bb0c753e",
    "5bfac3c524990f67bb0c775f",
    "5bfac3c624990f67bb0c779d",
    "5bfac3d424990f67bb0c7c32",
    "5bfac3df24990f67bb0c7fe1",
    "5bfac3e524990f67bb0c821b",
    "5bfac3e524990f67bb0c8225",
    "5bfac3f424990f67bb0c860f",
    "5bfac3f924990f67bb0c87d6",
    "5bfac3fe24990f67bb0c8973",
    "5bfac40f24990f67bb0c8f6b",
    "5bfac40f24990f67bb0c8fa5",
    "5bfac41324990f67bb0c909c",
    "5bfac41924990f67bb0c92cf",
    "5bfac41f24990f67bb0c9462",
    "5bfac42a24990f67bb0c97d4",
    "5bfac43124990f67bb0c9a91",
    "5bfac4bd24990f67bb0c9c32",
    "5bfac4be24990f67bb0c9caf",
    "5bfac4c124990f67bb0c9f4d",
    "5bfac4c224990f67bb0ca0ca",
    "5bfac4c724990f67bb0ca4b6",
    "5bfac4ca24990f67bb0ca7df",
    "5bfac4cb24990f67bb0ca808",
    "5bfac4cb24990f67bb0ca889",
    "5bfac4cd24990f67bb0caa45",
    "5bfac4d424990f67bb0cb02e",
    "5bfac4d724990f67bb0cb2cf",
    "5bfac95c24990f67bb0cb50c",
    "5bfac97824990f67bb0cb5b4",
    "5bfac98124990f67bb0cb881",
    "5bfac98624990f67bb0cba25",
    "5bfac98f24990f67bb0cbd4c",
    "5bfac99224990f67bb0cbe20",
    "5bfac99324990f67bb0cbe98",
    "5bfac99524990f67bb0cbf2d",
    "5bfac99b24990f67bb0cc148",
    "5bfac99b24990f67bb0cc172",
    "5bfac99d24990f67bb0cc1f3",
    "5bfac9a224990f67bb0cc3ac",
    "5bfac9b624990f67bb0cc995",
    "5bfac9be24990f67bb0ccc46")

  private val value = """<script>__INITIAL_STATE__={"loaded":true,"cookieCity":{"id":489,"name":"全国"},"user":{"name":"","Resume":{}},"cityInfo":"","industryInfo":"","company":{"address":"北京市朝阳区百子湾路33号万利中心1层A103","city":"北京","cityId":530,"companyId":66776032,"companyNumber":"CZ667760320","companySize":"20-99人","companySizeId":2,"companyTelephone":"68372722","companyType":"技术总监","companyTypeId":7,"companyUrl":"http://www.parbattech.com/","coordinate":{"latitude":39.903079,"longitude":116.469064},"dataRefer":1,"description":"<br><br>&nbsp; &nbsp; &nbsp; 团队有10年以上移动互联网从业经验，分别分布在北京、深圳，上海 , 香港；AppStore/Google Play等海外应用市场运营超过5年；线下预装业务超过 10 年积累很多资源和经验；聚焦海外业务：面向中国外销的手机/平板厂商CP/发行商，提供盈利产品；合作模式为CPA + CPS，CPA按照产品预装数量及CPS分成；广告收益为运营商计费（支持78个国家 &nbsp;300个运营 &nbsp;商）Paypal /Google Wallet/Facebook等信用卡支付","industries":["互联网/电子商务","计算机软件"],"industryIds":[210500,160400],"introduceUrl":"http://company.zhaopin.com/CZ667760320.htm","logoUrl":"","province":"北京","provinceId":530,"rootCompanyId":66776032,"rootCompanyNumber":"CZ66776032","status":30,"tags":[],"title":"北京帕尔加特科技有限公司","vipInfo":{"level":1002,"name":"白银会员"},"orgInfoPics":[]},"comLayer":{"refreshType":false,"targetType":"","collectParam":"","applyParam":{"num":"","cityId":""},"show":false,"loginState":0,"switchOff":0,"title":"","apllyWarn":"","resume":[],"coverletter":[],"subsidiaryFlag":false},"recruitmentJob":[{"number":"CC667760322J90250151000","id":"31197126730","jobType":{"items":[{"code":"4084000","name":"公关/媒介"},{"code":"164","name":"媒介经理/主管"}],"display":"公关/媒介,媒介经理/主管"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/667760322250151.htm","workingExp":{"code":"103","name":"1-3年"},"eduLevel":{"code":"4","name":"本科"},"salary":{"min":8001,"max":10000,"discuss":0},"emplType":"全职","jobDesc":"  岗位职责：1、拓展及维护国内主要移动互联网广告媒体，熟悉这些媒体平台的优劣势、工作方式、竞价原理及后台等；                    2、根据公司业务需求与广告主推广的要求，制定精准有效的媒介策划与投放方案，并保证媒介投放的实施、执行、跟进以及优化；                    3、及时把握媒体的动向，了解新媒体的市场情况。                        任职要求：1、大学本科以上学历，广告、市场营销、新闻、传播等专业优先；                   2、2年以上移动互联网广告媒体推广相关经验；                    3、极强的沟通能力，良好的营销规划能力，行业信息、数据分析能力；                      4、自我驱动力强。    ","jobName":"媒介客户经理","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"}],"display":"北京"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-08 10:52:54","endDate":"2018-12-15 10:52:54","welfare":"五险一金,年底双薪,绩效奖金,交通补助,弹性工作,定期体检,员工旅游","saleType":0,"feedbackRation":0,"score":0.35355338,"resumeCount":5,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["五险一金","年底双薪","绩效奖金","交通补助","弹性工作","定期体检","员工旅游"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J90250175000","id":"31246384160","jobType":{"items":[{"code":"3010000","name":"行政/后勤/文秘"},{"code":"116","name":"助理/秘书/文员"}],"display":"行政/后勤/文秘,助理/秘书/文员"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/667760322250175.htm","workingExp":{"code":"-1","name":"不限"},"eduLevel":{"code":"-1","name":"不限"},"salary":{"min":70001,"max":100000,"discuss":0},"emplType":"全职","jobDesc":"  岗位职责：   1.收集各种相关信息、数据、情报，为董事长决策提供参考、建议；  2.直接对接领导交代的商务合作，负责部分网络文学，签约作家及出版社工作。在授权范围内协助董事长进行商务谈判，做好各项汇报、联络工作；      3.协调董事长及集团副总经理及各部门、分公司负责人之间的工作关系；       4.协��董事长开展各项工作，处理日常事务；       5.协助董事长对投资、营运、资本运作、财经审核、行政人事管理等工作做综合性的协调与组织工作，掌握主要经营活动情况；       6.检查、督促董事长布置的工作任务的贯彻、落实、执行情况；    7.在董事长授权范围内与新闻媒体进行沟通联络；       8.负责董事长有关文件的起草、修改、审核，整理各类文书、文件、报告、总结及其他材料，负责董事长文件的督办、处理与反馈；     9.协助董事长做好工作日程安排；  任职要求：  1.形象气质佳，对英文水平要求较高；  2..喜欢热爱文学作品；  3.了解一定法律和财务相关知识；  4.性格活泼开朗，与人沟通、交流能力强； ","jobName":"总裁助理","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"}],"display":"北京"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-08 10:52:19","endDate":"2018-12-26 10:52:19","welfare":"五险一金,年底双薪,绩效奖金,交通补助,餐补,定期体检,员工旅游,节日福利","saleType":0,"feedbackRation":0.0071,"score":0.35355338,"resumeCount":141,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["五险一金","年底双薪","绩效奖金","交通补助","餐补","定期体检","员工旅游","节日福利"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00024177110","id":"24177110","jobType":{"items":[{"code":"160000","name":"软件/互联网开发/系统集成"},{"code":"671","name":"游戏策划"}],"display":"软件/互联网开发/系统集成,游戏策划"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00024177110.htm","workingExp":{"code":"103","name":"1-3年"},"eduLevel":{"code":"4","name":"本科"},"salary":{"min":8001,"max":10000,"discuss":0},"emplType":"全职","jobDesc":"   岗位职责：  1、脑洞大，不做换皮项目，能提出产品的创作理念；  2、见识广，懂得一款游戏吸引人的地方是什么；  3、够自信，相信自己有能力做一款有创新又卖座的产品；  4、能做好游戏策划案主体架构系统与细节设计，眼观六路耳听八方；  5、有耐心，根据需求反馈，指定明确的系统修改方案，并执行实施；   任职资格：  1、热爱游戏事业，注重一个策划的自我修养；  2、热爱动漫、二次元优先 3、良好的个人表达能力及较强的文字功底，能与程序美术保持紧密高效的沟通；  4、游戏体验丰富，熟悉玩家心理，对各类用户群体的游戏需求有深入了解；  5、熟悉游戏基本构成模块，熟悉游戏研发流程；  6、具备良好的抗压性、学习能力及解决问题的能力；  7、有强烈的责任心与团队意识，深刻了解团队协作的重要性。   ","jobName":"游戏策划","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903650","lon":"116.470357"},"city":{"items":[{"code":"530","name":"北京"}],"display":"北京"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-08 10:51:04","endDate":"2018-12-08 10:51:04","welfare":"五险一金,年底双薪,绩效奖金,交通补助,餐补,带薪年假,定期体检,员工旅游","saleType":0,"feedbackRation":0,"score":0.35355338,"resumeCount":86,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["五险一金","年底双薪","绩效奖金","交通补助","餐补","带薪年假","定期体检","员工旅游"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00088459910","id":"88459910","jobType":{"items":[{"code":"7002000","name":"销售行政/商务"},{"code":"460","name":"商务专员/助理"}],"display":"销售行政/商务,商务专员/助理"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00088459910.htm","workingExp":{"code":"-1","name":"不限"},"eduLevel":{"code":"-1","name":"不限"},"salary":{"min":4001,"max":6000,"discuss":0},"emplType":"全职","jobDesc":"  任职要求：  1.负责电商业务线的合同、结算、商务对接； 2.负责协助商务经理处理 公司日常商务工作。   3.完成领导交给的其他工作。  任职资格： 1.大专及以上学历  2.熟练运用Excel等办公软件； 3.聪明灵活，认真踏实，具备较强的执行力； 4.较高的职业道德和敬业精神； 5.有相关工作经验者优先考虑。 ","jobName":"商务助理（应届毕业生优先）","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"},{"code":"2006","name":"朝阳区"}],"display":"北京-朝阳区"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-08 10:50:53","endDate":"2018-12-08 10:50:53","welfare":"交通补助,餐补,定期体检,员工旅游,周末双休,年底双薪,绩效奖金","saleType":0,"feedbackRation":0.1538,"score":0.35355338,"resumeCount":4,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["交通补助","餐补","定期体检","员工旅游","周末双休","年底双薪","绩效奖金"],"companyTag":null,"skill":null,"refreshLevel":0,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00033694610","id":"33694610","jobType":{"items":[{"code":"160000","name":"软件/互联网开发/系统集成"},{"code":"57","name":"游戏设计/开发"}],"display":"软件/互联网开发/系统集成,游戏设计/开发"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00033694610.htm","workingExp":{"code":"103","name":"1-3年"},"eduLevel":{"code":"5","name":"大专"},"salary":{"min":10001,"max":15000,"discuss":0},"emplType":"全职","jobDesc":"   岗位职责：   AVG剧本创作和游戏化内容设计制作。      岗位要求：    1、对  AVG  的形式体裁有一定程度的认识。    2、有充分的学习能力，能够快速掌握开发工具。    3、沟通和表达能力好，具备团队合作精神，稳定踏实。    4、对时下流行的影视、文学等艺术形式和文化有一定程度的关注和了解。    5、兴趣广泛，爱游戏，爱玩游戏，对游戏文化和趋势有一定的涉猎。      有橙光、KRKR之类AVG编辑工具使用经验者优先。  女性，或有女性向文学、编剧或AVG作品创作经验者优先。  橙光签约作者或类似工作经验者优先。    ","jobName":"AVG编剧","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903650","lon":"116.470357"},"city":{"items":[{"code":"530","name":"北京"}],"display":"北京"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-08 10:50:35","endDate":"2018-12-08 10:50:35","welfare":"五险一金,年底双薪,交通补助,餐补,定期体检,员工旅游,带薪年假","saleType":0,"feedbackRation":0.098,"score":0.35355338,"resumeCount":27,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["五险一金","年底双薪","交通补助","餐补","定期体检","员工旅游","带薪年假"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00064839910","id":"64839910","jobType":{"items":[{"code":"160200","name":"互联网产品/运营管理"},{"code":"552","name":"SEO/SEM"}],"display":"互联网产品/运营管理,SEO/SEM"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00064839910.htm","workingExp":{"code":"305","name":"3-5年"},"eduLevel":{"code":"4","name":"本科"},"salary":{"min":15001,"max":20000,"discuss":0},"emplType":"全职","jobDesc":"   1、负责产品的Google AdWords、Facebook广告投放，在控制成本, 保证投放效果的前提下完成投放任务； 2、对广告数据进行监控，对渠道及广告创意表现进行数据分析与总结，不断优化推广效果，为新的营销方案提供数据支持；  3、团队协作设计高效果的广告创意和推广文案；  4、定期检测广告投放效果，使用Excel等分析工具生成项目报表，分析整理数据，为新的营销方案提供数据支持；   5、研究移动互联网市场变化和行业发展趋势。    岗位要求： 1、有两年及以上Facebook/Google广告投放和优化经验。 2、投放过工具类app，游戏类app优先 3、对数据敏感，分析能力强，有良好的沟通能力。 4、工作积极主动,有耐心,能承受较强工作压力，责任心强，良好的团队合作精神 5、英语读写能力良好，对不同国家的素材有理解。     ","jobName":"google优化","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903650","lon":"116.470357"},"city":{"items":[{"code":"530","name":"北京"}],"display":"北京"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-08 10:50:19","endDate":"2018-12-08 10:50:19","welfare":"五险一金,年底双薪,交通补助,餐补,带薪年假,定期体检,员工旅游","saleType":0,"feedbackRation":0.0667,"score":0.35355338,"resumeCount":7,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["五险一金","年底双薪","交通补助","餐补","带薪年假","定期体检","员工旅游"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00086697610","id":"86697610","jobType":{"items":[{"code":"160200","name":"互联网产品/运营管理"},{"code":"677","name":"电子商务专员/助理"}],"display":"互联网产品/运营管理,电子商务专员/助理"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00086697610.htm","workingExp":{"code":"-1","name":"不限"},"eduLevel":{"code":"5","name":"大专"},"salary":{"min":5000,"max":8000,"discuss":0},"emplType":"全职","jobDesc":"   工作内容：        1  、按照运营平台规则，协助运营经理完成产品上线工作；        2  、协助运营经理整理平台促销活动所需的各项资料；        3  、平台在线产品的基本信息、图片、价格、库存等的日常维护；        4  、平台运营数据的汇总统计；        5  、协助运营经理进行每月的对账结算；        6  、处理并回复平台管理方的各项业务邮件；        7  、平台运营所需的其他支撑工作；        8  、商城订单的售前、售后问题处理、汇总与总结。        任职要求：        1  、大专以上学历，不限工作经验；        2  、熟练使用 word 、 excel 、 photoshop 等办公软件；        3  、会简单的图片处理和书面文件撰写；        4  、能够对销售数据进行归类整理和简单分析；        5  、踏实勤奋，做事细致、有耐心；        6  、不需要出差。             ","jobName":"运营助理","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"},{"code":"2006","name":"朝阳区"}],"display":"北京-朝阳区"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-06 16:34:44","endDate":"2018-12-06 16:34:44","welfare":"交通补助,餐补,定期体检,员工旅游,周末双休,年底双薪,绩效奖金","saleType":0,"feedbackRation":0.047,"score":0.35355338,"resumeCount":78,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["交通补助","餐补","定期体检","员工旅游","周末双休","年底双薪","绩效奖金"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J90250171000","id":"31228224460","jobType":{"items":[{"code":"4000000","name":"客服/售前/售后技术支持"},{"code":"846","name":"网络/在线客服"}],"display":"客服/售前/售后技术支持,网络/在线客服"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/667760322250171.htm","workingExp":{"code":"-1","name":"不限"},"eduLevel":{"code":"-1","name":"不限"},"salary":{"min":4001,"max":6000,"discuss":0},"emplType":"全职","jobDesc":"  岗位职责：  1、接受客户咨询，回复电话咨询和网络咨询。记录客户咨询、投诉内容，按照相应流程给予客户反馈; 　　 2、能及时发现来电客户的需求及意见，并记录整理及汇报。记录汇总咨询事件，及时分析并反馈给上级主管职责  　　 3、为客户提供完整准确的方案及信息，回访和维护客户，服务订单，解决客户问题，提供高质量服务;接受电话订单和网络订单，处理订单;  　　 4、良好的工作执行力，严格按规范及流程进行工作或相关操作; 　　 5、与同事或主管共享信息，进行知识积累，提供流程改善依据; 　　   任职要求：  1、具有客服工作经验，了解客户需求，熟悉企业运作方式和服务途径；  2、能进行店铺日常维护：及时准确地跟进订单，接受顾客咨询，回复顾客留言，保证网店的正常运作；  3、强烈的客户服务意识，具备突发事件处理能力；工作耐心细致，能吃苦，较强的亲和力、应变能力和文字及语言沟通能力；    4、为人诚实守信，专心敬业，思维敏捷，有创新思想；  5、有相关网店工作经验者优先；     6、普通话标准；     7、熟悉office办公软件。             ","jobName":"客服专员","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"}],"display":"北京"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-06 16:33:57","endDate":"2018-12-06 16:33:57","welfare":"五险一金,年终分红,加班补助,交通补助,餐补,补充医疗保险","saleType":0,"feedbackRation":0.5,"score":0.35355338,"resumeCount":2,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["五险一金","年终分红","加班补助","交通补助","餐补","补充医疗保险"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00087034110","id":"87034110","jobType":{"items":[{"code":"4000000","name":"客服/售前/售后技术支持"},{"code":"257","name":"客户服务专员/助理"}],"display":"客服/售前/售后技术支持,客户服务专员/助理"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00087034110.htm","workingExp":{"code":"103","name":"1-3年"},"eduLevel":{"code":"5","name":"大专"},"salary":{"min":6001,"max":8000,"discuss":0},"emplType":"全职","jobDesc":"   岗位职责：        1.  在线与顾客交流并了解详细的顾客需求信息，进行有效跟踪，做好售前、售后服务工作；        2.  电子商务网上平台的销售订单出单、订单处理、结算、数据统计；        3.  负责进行退换货处理，核实买家提交的退换信息；负责发票的开具并做好发票开具登记工作；        4.  接受处理各类投诉，解答客户提问并落实解决问题；        5.  协助运作专业进行客户关系的维护以及潜在客户的挖掘；        6.  负责市场信息、网上平台销售政策及时整理公司销售平台新产品相关信息；         7  .  平台在线产品的基本信息、图片、价格、库存等的日常维护；        8.  加强公司内部沟通和联系促进部门之间友好共进模式；            9.  积极完成上级交办的其他工作任务。        任职要求：        1.  大专以上学历，电子商务、市场营销、工商管理、公共关系等相关专业；接受过系统的市场营销或电子商务专业知识或岗位技能的培训；        2.  工作经验： 2 年以���相关岗位工作经验；        3.  知识技能：了解电子商务、网络营销与互联网络文化，熟悉网络平台操作；          4.  熟练使用 word 、 excel 、 photoshop 等办公软件；        3.  会简单的图片处理和书面文件撰写；        5.  能够对销售数据进行归类整理和简单分析；       6.  态度个性：较强沟通协调、谈判能力，抗压性强，有耐心、学习能力强，踏实敬业。 ","jobName":"电商运营客服","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"},{"code":"2006","name":"朝阳区"}],"display":"北京-朝阳区"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-11-05 15:20:09","endDate":"2018-12-05 15:20:09","welfare":"交通补助,餐补,定期体检,员工旅游,周末双休,年底双薪,绩效奖金","saleType":0,"feedbackRation":0.2609,"score":0.35355338,"resumeCount":12,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["交通补助","餐补","定期体检","员工旅游","周末双休","年底双薪","绩效奖金"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false},{"number":"CC667760322J00084754910","id":"84754910","jobType":{"items":[{"code":"2120000","name":"影视/媒体/出版/印刷"},{"code":"152","name":"文案策划"}],"display":"影视/媒体/出版/印刷,文案策划"},"company":{"number":"CZ667760320","id":66776032,"url":"http://company.zhaopin.com/CZ667760320.htm","name":"北京帕尔加特科技有限公司","size":{"code":"2","name":"20-99人"},"type":{"code":"7","name":"其它"}},"positionURL":"http://jobs.zhaopin.com/CC667760322J00084754910.htm","workingExp":{"code":"305","name":"3-5年"},"eduLevel":{"code":"4","name":"本科"},"salary":{"min":9000,"max":15000,"discuss":0},"emplType":"全职","jobDesc":"  岗位职责：  1、组织参与项目的创意构思、文案、设计创意等；  2、为微信、微博等社会化媒体进行文字、图片等多种形式的内容创作；  3、挖掘和分析用户使用习惯、体验，即时掌握新闻热点，完成专题策划、编辑制作；  4、及时了解微信、微博、论坛用户需求；     任职要求：  1、中文、新闻、广告、营销等相关专业毕业，有网络营销、文案相关工作经验；  2、热爱网络营销，疯狂的创意头脑，超强的文字驾驭能力，沟通及协调能力，良好的学习能力，随时掌握互联网最新资讯，能快速并准确地领悟产品（或客户）信息；  3、有撰写媒体软文经验，对产品进行包装，对企业进行宣传，熟练使用易企秀、MAKA等制作H5页工具，有较好审美能力。  4、有强大的ppt制作能力，擅长ppt制作，写作。    ","jobName":"电商文案策划","industry":"210500,160400","recruitCount":0,"geo":{"lat":"39.903079","lon":"116.469064"},"city":{"items":[{"code":"530","name":"北京"},{"code":"2006","name":"朝阳区"}],"display":"北京-朝阳区"},"applyType":"1","updateDate":"2018-11-19 14:56:16","createDate":"2018-09-06 11:39:37","endDate":"2019-03-24 23:59:59","welfare":"交通补助,餐补,定期体检,员工旅游,周末双休,年底双薪,绩效奖金","saleType":0,"feedbackRation":0.0972,"score":0.35355338,"resumeCount":26,"showLicence":0,"interview":0,"companyLogo":"","tags":[],"vipLevel":1002,"expandCount":0,"positionLabel":"{"qualifications":null,"role":null,"level":null,"jobLight":["交通补助","餐补","定期体检","员工旅游","周末双休","年底双薪","绩效奖金"],"companyTag":null,"skill":null,"refreshLevel":1,"chatWindow":20}","duplicated":false,"futureJob":false}],"recruitmentCityList":{"data":[{"name":"北京","code":"530","total":13}],"total":13,"method":"rd_position"},"recruitmentCitySelected":{"name":"北京","code":"530","total":13},"userCityPage":{},"hasFetchedUserCityPage":false,"basic":{"uid":"2323","appid":"A23","pagecode":4021,"wdgtid":"A2-4","evtid":"click","chnlname":""},"props":{"jobid":"","isgray":1,"companyid":""}}</script><script src="//desktop-bucket.zhaopin.cn/manifest.web.54250c.js"></script><script src="//desktop-bucket.zhaopin.cn/vendors.web.5173a0.js"></script><script src="//desktop-bucket.zhaopin.cn/[code].web.e6f29c.js"></script>"""
  """<script>_(.*)_.*""".r.unapplySeq(
    value
  ).foreach(println)

  println(value.matches(""".*"""))
}
