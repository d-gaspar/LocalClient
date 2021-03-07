package com.dgaspar.localclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

//import com.fasterxml.jackson.core.JsonGenerationException
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.node.ObjectNode
import kotlinx.coroutines.*
import org.apache.commons.io.IOUtils

// jackson - json
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.StringWriter
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnPressed(view : View) {
        println("SENDING JSON COMMAND")

        CoroutineScope(Dispatchers.IO).launch {
            sendJson()
        }

        println("SENDING JSON COMMAND (OK)")
    }


    private fun sendJson() {
        // curl test
        // curl -X POST https://reqbin.com/echo/post/json -H "Content-Type: application/json" -d "{\"login\":\"my_login\",\"password\":\"my_password\"}"
        println("aaaa")
        var url : String = "https://reqbin.com/echo/post/json"
        //var url : String = "192.168.0.13:8080"
        println("bbbb")
        var obj : URL = URL(url)
        println("cccc")

        var connection : HttpURLConnection = obj.openConnection() as HttpURLConnection
        println("dddd")
        connection.doInput = true
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        connection.requestMethod = "POST"

        var cred = JSONObject()
        cred.put("login", "my_login")
        cred.put("password", "my_password")
        //cred.put("key", "A")

        println("1111")
        var writer = OutputStreamWriter(connection.outputStream)
        println("2222")
        writer.write(cred.toString())
        println("3333")

        // read the response
        println("4444")
        if(connection.responseCode == HttpURLConnection.HTTP_OK) {
            println("OK")

            var input : InputStream = BufferedInputStream(connection.inputStream)
            println("OK-2")
            var res : String = IOUtils.toString(input)
            println("OK-3")
            println("OUTPUT: $res")

        } else {
            println("5555")
            println(connection.responseMessage)
        }
    }

}