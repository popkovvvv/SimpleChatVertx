package com.train.verticle.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler




class RestServerVerticle: AbstractVerticle() {
    override fun start() {
        val httpServer = vertx.createHttpServer()
        val httpRouter = Router.router(vertx)
        httpRouter.route().handler(BodyHandler.create())
        httpRouter.post("/sendMessage")
            .handler { request: RoutingContext ->
                vertx.eventBus().send("router", request.bodyAsString)
                request.response().end("ok")
            }
        httpRouter.get("/getHistory").handler { request: RoutingContext ->
                vertx.eventBus().send("getHistory", request.bodyAsString,
                    Handler { result: AsyncResult<Message<Any>> ->
                        request.response().end(result.result().body().toString())
                    }
                )
            }
        httpServer.requestHandler { request: HttpServerRequest? ->
            httpRouter.handle(
                request
            )
        }
        httpServer.listen(8081)
    }
}