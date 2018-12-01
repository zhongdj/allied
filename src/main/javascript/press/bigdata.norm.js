db.bigdatacompany.find({}).forEach( function(bigDataCompany) {

    var existsCount = db.company_norm.count({"title" : bigDataCompany.name});
    if (existsCount <= 0) {
	  var level1 = bigDataCompany.category;
	  var level2 = bigDataCompany.subCategory;
	  var company = {};
	  company.title = bigDataCompany.name;
	  company.address = "";
	  company.city = bigDataCompany.location;
	  company.companySize = "";
	  company.companyTelephone = "";
	  company.companyType = "";
	  company.companyUrl = "";
	  company.coordinate = {};
	  company.coordinate.latitude = -1;
	  company.coordinate.longitude = -1;
	  company.industries = [];
	  company.logoUrl = "";
	  company.tags = [];
	  company.category = {};
	  company.category.level1 = level1;
	  company.category.level2 = level2;
	  company.foundOn = bigDataCompany.foundOn;
	  company.investment = bigDataCompany.investment;

      db.company_norm.insert(company);
    }
});