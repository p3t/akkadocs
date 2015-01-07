package com.github.p3t.akkadocs.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.allanbank.mongodb.bson.Document;

public class MongoResponse {

   private List<Document> results;

   public MongoResponse( List<Document> results ) {
      this.results = new ArrayList<>(results);
   }

   public List<Document> getResults() {
      return Collections.unmodifiableList( results );
   }
}
