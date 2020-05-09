package com.train.verticle.verticle

import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message
import io.vertx.core.http.ServerWebSocket


class WsServerVerticle: AbstractVerticle() {
    override fun start() {
        vertx.createHttpServer()
            .webSocketHandler(this::createWebSocketServer)
            .listen(8080);
    }

    private fun createWebSocketServer(wsServer: ServerWebSocket) {
        println("Create WebSocket: " + wsServer.path())
        wsServer.frameHandler { wsFrame ->
            println(wsFrame.textData())
            vertx.eventBus().send("router", wsFrame.textData())
        }

        val consumerSendMessage = vertx.eventBus().consumer(wsServer.path()) { data: Message<String?> ->
                wsServer.writeFinalTextFrame(data.body())
                data.reply("ok")
            }

        wsServer.closeHandler { aVoid ->
            println("Close WebSocket: " + consumerSendMessage.address())
            consumerSendMessage.unregister()
        }
    }


}