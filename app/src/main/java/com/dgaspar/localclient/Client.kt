package com.dgaspar.localclient

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class Client {
    private var server : Socket? = null
    private var connected : Boolean = false
    private var host : String = "localhost"
    private var port : Int = 8080

    constructor(host : String = "localhost", port : Int = 8080) {
        this.host = host
        this.port = port
    }

    fun run(key : String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                server = Socket(host, port)
                connected = true
                println("Connected on $host:${port.toString()}")

                var reader : Scanner = Scanner(server!!.getInputStream())
                var writer : OutputStream = server!!.getOutputStream()

                while(connected) {
                    connected = false

                    val input = readLine() ?: ""
                    writer.write("{\"key\":\"$key\"}".toByteArray(Charset.defaultCharset()))

                    reader.close()
                    server?.close()
                }
            } catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }
}