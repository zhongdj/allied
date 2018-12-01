//删除属性
db.company_zp.update(
   {},
   { $unset: {
     description: "",
     industryIds: "",
     introduceUrl: "",
     province: "",
     provinceId: "",
     rootCompanyId: "",
     rootCompanyNumber: "",
     status: "",
     vipInfo: "",
     dataRefer: "",
     companyTypeId: "",
     orgInfoPics: "",
     cityId: "",
     companyId: "",
     companyNumber: "",
     companySizeId: ""
   } },
   {multi:true}
);


//删除title不存在的数据
db.company_zp.remove({"title": {$exists: false} });

/*
- loading fields from startup
+category
+foundOn
+investment
*/db.company_zp.find({$and: [

    {"title": {$exists: true}}

  ]}).forEach( function(company) {
    var startup = db.startup.find({"name" : company.title}).forEach( function(startup) {
        company.category = {};
        if (startup.category != undefined) {
           company.category.level1 = startup.category;
        } else {
          company.category.level1 = "";
        }
        if (startup.subCategory != undefined) {
           company.category.level2 = startup.subCategory;
        } else {
          company.category.level2 = "";
        }
        if (startup.foundOn != undefined) {
            company.foundOn = startup.foundOn;
        } else {
            company.foundOn = "";
        }
        if (startup.investment != undefined) {
            company.investment = startup.investment;
        } else {
            company.investment = "";
        }
        if ((company.companyUrl == undefined || company.companyUrl == "") && startup.website != undefined) {
            company.companyUrl = startup.website;
        }
        db.company_zp.save(company);
    })
  });