package com.train.verticle.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler


class ClientServerVerticle: AbstractVerticle() {
    override fun start() {
        val httpServer: HttpServer = vertx.createHttpServer()
        val httpRouter = Router.router(vertx)

        httpRouter.route("/*")
            .handler(
                StaticHandler.create()
                    .setCachingEnabled(false)
                    .setWebRoot("static")
            )
        httpServer.requestHandler { request: HttpServerRequest? -> httpRouter.handle(request) }

        httpServer.listen(8082)
    }
}