package com.dgaspar.localclient

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

val mapper = jacksonObjectMapper()

data class JsonData (
    val key : String,
    val value : String
)

class Client {
    private var server : Socket? = null
    private var connected : Boolean = false
    private var ip : String = "localhost"
    private var port : Int = 8080
    private var pass : String = "123456"

    var reader : Scanner? = null
    var writer : OutputStream? = null

    constructor (ip : String = "localhost", port : Int = 8080, pass : String = "123456") {
        this.ip = ip
        this.port = port
        this.pass = pass
    }

    /*******************************************************************************************/

    fun connect () : Pair<Boolean, String> {
        try {
            server = Socket(ip, port)
            reader = Scanner(server?.getInputStream())
            //writer = server?.getOutputStream()
            connected = true
            println("Connected on $ip:${port}")

            // send password
            CLIENT?.sendJson("{\"key\":\"pass\",\"value\":\"$pass\"}\n")

            // check if password is correct
            var jsonStringPass = reader?.nextLine().toString()
            println("PASS: $jsonStringPass")
            var jsonPass : JsonData = mapper.readValue(jsonStringPass)
            if (jsonPass.key != "pass" || jsonPass.value != "true") {
                return Pair(false, "Wrong password!")
            }

            // check if there is a player slot available
            var jsonStringPlayer = reader?.nextLine().toString()
            println("PLAYER: $jsonStringPlayer")
            var jsonPlayer : JsonData = mapper.readValue(jsonStringPlayer)
            if (jsonPlayer.key != "playerColor" || jsonPlayer.value == "null") {
                return Pair(false, "Player slot unavailable!")
            }
            var playerColor : String = jsonPlayer.value

            return Pair(true, playerColor)
        } catch (e : IOException) {
            return Pair(false, "Connection failed!")
        }
    }

    /*******************************************************************************************/

    fun disconnect () {
        writer?.close()
        server?.close()
    }

    /*******************************************************************************************/

    fun sendJson (json : String) {
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