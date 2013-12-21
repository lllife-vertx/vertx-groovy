package hello

import org.vertx.java.core.http.HttpServer
import org.vertx.java.core.Handler
import org.vertx.java.core.eventbus.Message
import org.vertx.java.core.json.JsonArray
import org.vertx.java.core.json.JsonObject
import org.vertx.java.platform.Verticle

class HelloVerticle extends  Verticle {

    def private _messages = [];
    def private _helloEvent = "hello.event"
    def private _eventPrefix = "/eventbus"

    def HelloVerticle(){
        super();

    }

    def init() {
        org.vertx.groovy.core.http.HttpServerResponse.metaClass.invokeMethod = { String name, args ->
            def metaMethod = delegate.class.metaClass.getMetaMethod(name, args)
            if(name=="end"){
                delegate.headers.put("Access-Control-Allow-Origin", "*");
            }
            def result
            if(metaMethod) result = metaMethod.invoke(delegate,args)
            else {
                result = "bar"
            }
            println "invoke method:"+name+"  result:"+result
            result

        }
    }

    def void deployMongo(){
        def configs = [
                    "address": "hello.persistor",
                    "host": "localhost",
                    "port": 27000,
                    "pool_size": 20,
                    "db_name": "vert"
        ]
        def jsonConfig = new JsonObject(configs);
        this.container.deployModule("io.vertx~mod-mongo-persistor~2.1.0", jsonConfig)
    }

    def void registerEventBus(){
        // define event bus.
        def eb = this.vertx.eventBus();

        eb.registerHandler(_helloEvent, new Handler<Message>() {
            @Override
            void handle(Message message) {
                _messages.push(message)

                print("I received a message -> ${message.body()}\n")
                message.reply("I'm fine")
            }
        })

        eb.registerHandler("save.message", new Handler<Message>() {
            @Override
            void handle(Message message) {

                container.logger().info("Received -> ${message.body()}")

                def json = new JsonObject();
                json.putString("action", "save")
                json.putString("collection", "Messages")
                json.putObject("document", new JsonObject().putString("name", "Somnuk Wongkhan"))

//               eb.send("hello.persistor", json, new Handler<Message>() {
//                   @Override
//                   void handle(Message ms) {
//                       println("Result ${ms}")
//                   }
//               })
            }
        })

        eb.send("save.message", "Hi save message")
    }

    def void registerInterval(){
        def eb = this.vertx.eventBus();

        vertx.setPeriodic(1000, new Handler<Long>() {

            def i = 100

            @Override
            void handle(Long aLong) {
                //eb.publish(_helloEvent, "Hello -> ${i++}\n")
            }
        })
    }

    def void registerSockJS(HttpServer server){
        // create sock js service.
        def config = new JsonObject()
        config.putValue("prefix", _eventPrefix)

        def inc = new JsonArray()
//        inc.add("address": _helloEvent)

        def out = new JsonArray()
//        out.add("address": _helloEvent)

        def socketServer = vertx.createSockJSServer(server)
        socketServer.bridge(config, inc, out)
    }

    @Override
    def void start(){

        //this.init()
        this.deployMongo()

        // get log instance.
        def log = this.container.logger()
        log.info("Start server @8877...")

        this.registerEventBus()
        this.registerInterval()

        // create server.
        def server = this.vertx.createHttpServer()

        // register http handler
        def hello = new HelloRoute()
        server.requestHandler(hello)

        this.registerSockJS(server)
        server.listen(8877, "0.0.0.0")
    }
}

