package com.github.p3t.akkadocs.mongo;

import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.builder.ConditionBuilder;


public class MongoMessage {
   private Document query;
   
   public MongoMessage(Document query) {
      this.query = query;
   }
   
   public Document getQuery() {
      return query;
   }

   public static MongoMessage newQuery(ConditionBuilder builder ) {
      return new  MongoMessage( builder.asDocument() );
   }
}
