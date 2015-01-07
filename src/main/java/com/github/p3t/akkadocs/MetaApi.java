package com.github.p3t.akkadocs;

import static com.allanbank.mongodb.builder.QueryBuilder.where;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.http.model.japi.HttpMethods;
import akka.http.model.japi.HttpResponse;
import akka.http.model.japi.headers.AccessControlAllowHeaders;
import akka.http.model.japi.headers.AccessControlAllowMethods;
import akka.http.model.japi.headers.AccessControlAllowOrigin;
import akka.http.model.japi.headers.HttpOriginRange;
import akka.http.server.japi.HttpApp;
import akka.http.server.japi.RequestContext;
import akka.http.server.japi.Route;
import akka.pattern.Patterns;

import com.github.p3t.akkadocs.mongo.MongoActor;
import com.github.p3t.akkadocs.mongo.MongoMessage;

public class MetaApi extends HttpApp {

   
   private static ActorSystem actorSystem;
   private static ActorRef mongoActor;
   private static ActorRef handlerActor;
 
   public static class HandlerActor extends UntypedActor {
      @Override
      public void onReceive( Object message ) throws Exception {
         if ( message instanceof RequestContext ) {
            RequestContext ctx = (RequestContext)message;
            Patterns.ask( mongoActor, 
                  MongoMessage.newQuery(
                        where( "@type" ).equals( "TypeDef" )), 15_000 );
         } else {
            unhandled( message );
         }
      }
   }
   @Override
   public Route createRoute() {
      
      return route( path( "meta" ).route( 
            get( handleWith( ctx -> {
               handlerActor.tell( ctx, ActorRef.noSender());
               HttpResponse response = HttpResponse.create()
                     .addHeader( AccessControlAllowOrigin.create( HttpOriginRange.ALL ) )
                     .addHeader(
                           AccessControlAllowHeaders.create( "Access-Control-Allow-Origin",
                                 "Access-Control-Allow-Method", "Content-Type" ) )
                     .addHeader(
                           AccessControlAllowMethods.create( HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT,
                                 HttpMethods.OPTIONS, HttpMethods.DELETE ) )
                     .withEntity( "Response of actor?" ); 
               return ctx.complete( response );
            } ) ) ) );
   }

   public static void main(String ... args) throws Exception {
      MetaApi api = new MetaApi();
      actorSystem = ActorSystem.create();
      mongoActor = actorSystem.actorOf( Props.apply( MongoActor.class, null ), "My Mongo Actor" );
      handlerActor = actorSystem.actorOf( Props.create( HandlerActor.class ), "Get Handler" );
      api.bindRoute("localhost",8080, actorSystem);
    }

}
