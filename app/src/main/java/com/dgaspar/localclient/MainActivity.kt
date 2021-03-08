package com.dgaspar.localclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

//import com.fasterxml.jackson.core.JsonGenerationException
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.node.ObjectNode
//import kotlinx.coroutines.*
//import org.apache.commons.io.IOUtils

// jackson - json
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun btnPressed(view : View) {
        println("SENDING JSON COMMAND")

        var key : String = ""
        when(view.id) {
            R.id.W -> key = "W"
            R.id.S -> key = "S"
        }

        var urlEditText : EditText = findViewById(R.id.url)
        var url : String = urlEditText.text.toString()

        var client : Client = Client(host = url, port = 8080)
        client.run(key)

        println("SENDING JSON COMMAND (OK)")
    }
}