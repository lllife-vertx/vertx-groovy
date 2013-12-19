package hello

import org.vertx.java.platform.Verticle

class HelloVerticle extends  Verticle {

    def HelloVerticle(){
        super();
    }
    @Override
    def void start(){
        def server = vertx.createHttpServer();
        server.requestHandler(new HelloRoute()).listen(8877, "0.0.0.0");
    }
}

