package hello

import org.vertx.java.core.Handler
import org.vertx.java.core.http.HttpServerRequest
import org.vertx.java.core.http.RouteMatcher

class HelloRoute extends  RouteMatcher {
    def HelloRoute(){

        super()

        this.get("/hello" , new Handler<HttpServerRequest>() {
            @Override
            void handle(HttpServerRequest request) {
                print(request.uri())
                request.response().end("bye!")
            }
        })

        this.get("/hi", new Handler<HttpServerRequest>() {
            @Override
            void handle(HttpServerRequest request) {
                def x = request.uri()
                print(x)
                request.response().end("good!")
            }
        })

//        this.get("/index", new Handler<HttpServerRequest>() {
//            @Override
//            void handle(HttpServerRequest request) { request.response().sendFile("index.html"); }
//        })
//
//        this.get("/sockjs-0.3.min.js", new Handler<HttpServerRequest>() {
//            @Override
//            void handle(HttpServerRequest request) { request.response().sendFile("sockjs-0.3.min.js"); }
//        })
//
//        this.get("/vertxbus-2.1.js", new Handler<HttpServerRequest>() {
//            @Override
//            void handle(HttpServerRequest request) { request.response().sendFile("vertxbus-2.1.js"); }
//        })
    }
}