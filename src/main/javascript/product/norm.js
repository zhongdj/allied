db.product.find({}).forEach(function(product){
    var nonEmptyString = function(str) { return str.length > 0;};
    var rmIllegalChars = function(str) { return str.replace("\|", "").replace("[^ | $]", "");}
	var prodNorm = {};
	prodNorm.name = product.title;
	prodNorm.productLogo = product.imageUrl;
	prodNorm.description = product.description;
	prodNorm.category = {};
	prodNorm.category.group = product.secondaryCategory;
	prodNorm.category.category = product.category;
	prodNorm.category.level1_subcategory = "";
	prodNorm.category.level2_subcategory = "";
	prodNorm.category.level3_subcategory = "";
	prodNorm.tags = product.keywords
                 .map(rmIllegalChars)
                 .filter(nonEmptyString);

    prodNorm.company = {};
    prodNorm.company.name = product.companyName;
    prodNorm.company.logo = "";
    prodNorm.company.website = "";
    prodNorm.customers = {};
    prodNorm.customers.name = [];
    prodNorm.customers.industry = [];
    prodNorm.customers.type = product.targetCustomers;
    db.product_norm.insert(prodNorm);
});