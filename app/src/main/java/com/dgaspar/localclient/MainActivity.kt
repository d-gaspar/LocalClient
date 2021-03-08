package com.dgaspar.localclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//import com.fasterxml.jackson.core.JsonGenerationException
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.node.ObjectNode
//import kotlinx.coroutines.*
//import org.apache.commons.io.IOUtils

// jackson - json
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import org.json.JSONObject

var CLIENT : Client? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onCreateOnRestart()
    }

    override fun onRestart() {
        super.onRestart()

        onCreateOnRestart()
    }

    /*******************************************************************************************/

    private fun onCreateOnRestart() {
        // reset connection if connected
        CLIENT?.disconnect()
    }

    /*******************************************************************************************/

    fun connectBtn(view : View) {
        // message
        var message : TextView = findViewById(R.id.output)
        message.text = "Connecting ..."

        // editText
        var ip1 : EditText = findViewById(R.id.ip1)
        var ip2 : EditText = findViewById(R.id.ip2)
        var ip3 : EditText = findViewById(R.id.ip3)
        var ip4 : EditText = findViewById(R.id.ip4)

        var ip : String = "${ip1.text}.${ip2.text}.${ip3.text}.${ip4.text}"

        println("IP: $ip")

        CLIENT = Client(ip, 8080)

        // connect
        var intent = Intent(this, GameController::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            if (CLIENT!!.connect()) {
                println("OPEN MODULES")
                withContext(Dispatchers.Main) {
                    message.text = ""
                    startActivity(intent)
                }
            } else {
                withContext(Dispatchers.Main) {
                    message.text = "Connection failed!"
                }
            }
        }
    }
}