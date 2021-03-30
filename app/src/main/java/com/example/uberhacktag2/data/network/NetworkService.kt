package com.example.uberhacktag2.data.network

import com.example.uberhacktag2.simulator.WebSocket
import com.example.uberhacktag2.simulator.WebSocketListener

class NetworkService {

    fun createWebSocket(webSocketListener: WebSocketListener): WebSocket {
        return WebSocket(webSocketListener)
    }

}