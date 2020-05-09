package com.train.verticle.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.eventbus.Message
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient


class MongoDbVerticle: AbstractVerticle() {

    private lateinit var client: MongoClient
    override fun start() {
        client = MongoClient.createShared(
            vertx, JsonObject()
                .put("db_name", "my_DB")
        )
        vertx.eventBus().consumer("database.save", this::saveDb);
        vertx.eventBus().consumer("getHistory", this::getHistory);
    }

    private fun getHistory(message: Message<String>) {
        client.find("message", JsonObject()) { result: AsyncResult<List<JsonObject?>?> ->
            message.reply(Json.encode(result.result()))
        }
    }

    private fun saveDb(message: Message<String>) {
        client.insert("message", JsonObject(message.body())) {
                stringAsyncResult: AsyncResult<String> -> handler(stringAsyncResult) }
    }

    private fun handler(stringAsyncResult: AsyncResult<String>) {
        if (stringAsyncResult.succeeded()) {
            println("MongoDB save: " + stringAsyncResult.result())
        } else {
            println("ERROR MongoDB: " + stringAsyncResult.cause())
        }
    }
}