package hello
/**
 * Created by recovery on 12/19/13.
 */

vertx.createHttpServer().requestHandler { req ->
    def file = req.uri == "/" ? "index.html" : req.uri
    req.response.sendFile "webroot/$file"

}.listen(8080)


