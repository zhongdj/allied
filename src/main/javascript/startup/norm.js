//remove duplicates, currently 26744 records
db.startup.aggregate([
    {
        $group: { _id: "$name", count: {$sum: 1}, dups: {$addToSet: '$_id'}}
    },
    {
        $match: {count: {$gt: 1}}
    }
]).forEach(function(doc){
      doc.dups.shift();
      db.startup.remove({_id: {$in: doc.dups}});
});

db.startup.find({}).forEach( function(startup) {

    var existsCount = db.company_zh.count({"title" : startup.name});
    if (existsCount <= 0) {

	  var level1 = startup.category;
	  var level2 = startup.subCategory;
	  var company = {};
	  company.title = startup.name;
	  company.address = "";
	  company.city = startup.location;
	  company.companySize = "";
	  company.companyTelephone = "";
	  company.companyType = "";
	  company.companyUrl = startup.website;
	  company.coordinate = {};
	  company.coordinate.latitude = -1;
	  company.coordinate.longitude = -1;
	  company.industries = [];
	  company.logoUrl = "";
	  company.tags = [];
	  company.category = {};
	  company.category.level1 = level1;
	  company.category.level2 = level2;
	  company.foundOn = startup.foundOn;
	  company.investment = startup.investment;

      db.company_norm.insert(company);
    }
});