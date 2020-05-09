package com.train.verticle

import com.train.verticle.verticle.*
import io.vertx.core.Vertx

fun main(args: Array<String>) {
    deploy(Vertx.vertx())
}

fun deploy(vertx: Vertx) {
    vertx.deployVerticle(ClientServerVerticle())
    vertx.deployVerticle(MongoDbVerticle())
    vertx.deployVerticle(RestServerVerticle())
    vertx.deployVerticle(RouterVerticle())
    vertx.deployVerticle(WsServerVerticle())
}
