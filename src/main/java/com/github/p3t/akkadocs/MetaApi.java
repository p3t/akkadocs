package com.github.p3t.akkadocs;

import akka.actor.ActorSystem;
import akka.http.model.japi.HttpMethods;
import akka.http.model.japi.HttpResponse;
import akka.http.model.japi.headers.AccessControlAllowHeaders;
import akka.http.model.japi.headers.AccessControlAllowMethods;
import akka.http.model.japi.headers.AccessControlAllowOrigin;
import akka.http.model.japi.headers.HttpOriginRange;
import akka.http.server.japi.HttpApp;
import akka.http.server.japi.Route;

public class MetaApi extends HttpApp {

   @Override
   public Route createRoute() {
      return route( path( "meta" ).route(
            get( handleWith( ctx -> {
               HttpResponse response = HttpResponse.create()
                     .addHeader( AccessControlAllowOrigin.create( HttpOriginRange.ALL ) )
                     .addHeader(
                           AccessControlAllowHeaders.create( "Access-Control-Allow-Origin",
                                 "Access-Control-Allow-Method", "Content-Type" ) )
                     .addHeader(
                           AccessControlAllowMethods.create( HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT,
                                 HttpMethods.OPTIONS, HttpMethods.DELETE ) )
                     .withEntity( "bla" );   

               return ctx.complete( response );

            } ) ) ) );
   }

   public static void main(String ... args) throws Exception {
      MetaApi api = new MetaApi();
      api.bindRoute("localhost",8080, ActorSystem.create());
    }

}
