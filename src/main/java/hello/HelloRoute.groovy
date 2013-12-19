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
                print(request.uri());
                request.response().end("bye!");
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
    }
}