db.getCollection('qichacha_detail').find({"url" : ""}).forEach( function(doc) {
    db.company_norm_active.update({"_id" : doc.pageId}, {$unset : { "url" : "" } }, {multi : true});
    } );

db.getCollection('qichacha_detail').remove({"url" : ""});