package com.github.p3t.akkadocs.mongo;

import java.util.LinkedList;
import java.util.List;

import akka.actor.UntypedActor;

import com.allanbank.mongodb.MongoClient;
import com.allanbank.mongodb.MongoCollection;
import com.allanbank.mongodb.MongoDatabase;
import com.allanbank.mongodb.MongoFactory;
import com.allanbank.mongodb.MongoIterator;
import com.allanbank.mongodb.bson.Document;

public class MongoActor extends UntypedActor {

   MongoClient mongo = MongoFactory.createClient( "mongodb://localhost:27017/db?maxConnectionCount=2" );
   MongoDatabase db = mongo.getDatabase( "testDB" );
   MongoCollection collection = db.getCollection( "testCol" );

   @Override
   public void onReceive( Object msg ) throws Exception {
      if ( msg instanceof MongoMessage ) {
         Document query = ((MongoMessage) msg).getQuery();
         try ( MongoIterator<Document> resultIter = collection.find( query ) ) {
            boolean stop = false;
            List<Document> results = new LinkedList<>();
            while ( resultIter.hasNext() || stop ) {
               Document result = resultIter.next();
               results.add( result );
               stop = result.size() > 100;
            }
            sender().tell( new MongoResponse( results), self() );
         }
      } else {
         unhandled( msg );
      }
   }
}
