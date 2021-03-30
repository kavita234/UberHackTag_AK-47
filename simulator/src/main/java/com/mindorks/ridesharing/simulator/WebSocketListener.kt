package com.example.uberhacktag2.simulator

interface WebSocketListener {

    fun onConnect()

    fun onMessage(data: String)

    fun onDisconnect()

    fun onError(error: String)

}