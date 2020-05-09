package com.train.verticle.verticle

import com.train.verticle.model.Data
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json

class RouterVerticle: AbstractVerticle() {
    override fun start() {
        vertx.eventBus().consumer("router", this::router);
    }

    private fun router(message: Message<String>) {
        if (message.body() != null && message.body().isNotEmpty()) {
            println("Router message: " + message.body())
            val data: Data = Json.decodeValue(message.body(), Data::class.java)
            println(data)
            vertx.eventBus().send("/token/" + data.address, message.body())

            // Сохраняем сообщение в БД
            vertx.eventBus().send("database.save", message.body())
        }
    }
}