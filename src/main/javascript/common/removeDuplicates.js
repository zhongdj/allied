
db.集合.aggregate([
    {
        $group: { _id: {字段1: '字段1',字段2: '$字段2'}, count: {$sum: 1}, dups: {$addToSet: '$_id'}}
    },
    {
        $match: {count: {$gt: 1}}
    }
]).forEach(function(doc){
    doc.dups.shift();
    db.集合.remove({_id: {$in: doc.dups}});
});


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
