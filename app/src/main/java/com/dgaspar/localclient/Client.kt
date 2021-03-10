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
    private var ip : String = "localhost"
    private var port : Int = 8080

    var reader : Scanner? = null
    var writer : OutputStream? = null

    constructor(ip : String = "localhost", port : Int = 8080) {
        this.ip = ip
        this.port = port
    }

    /*******************************************************************************************/

    fun connect() : Boolean {
        return try {
            server = Socket(ip, port)
            reader = Scanner(server?.getInputStream())
            //writer = server?.getOutputStream()
            connected = true
            println("Connected on $ip:${port}")
            true
        } catch (e : IOException) {
            false
        }
    }

    /*******************************************************************************************/

    fun disconnect() {
        writer?.close()
        server?.close()
    }

    /*******************************************************************************************/

    fun sendJson(json : String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                println("sendJson: $json")
                writer = server?.getOutputStream()
                val input = readLine() ?: ""
                //val input = reader?.nextLine().toString()
                writer?.write(json.toByteArray(Charset.defaultCharset()))
                writer?.flush()
                //writer?.close()

            } catch (e : IOException) {
                e.printStackTrace()
            }
        }
    }
}