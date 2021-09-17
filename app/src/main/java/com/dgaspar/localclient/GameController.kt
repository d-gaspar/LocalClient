package com.dgaspar.localclient

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class GameController : AppCompatActivity() {

    var keysDownHashMap : HashMap<String, Int> = HashMap()

    /*******************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_controller)

        // player color
        var backgroundColor : RelativeLayout = findViewById(R.id.body)
        var playerColor : String = intent.getStringExtra("playerColor").let { it ?: "#000000" }
        backgroundColor.setBackgroundColor(Color.parseColor(playerColor))

        buttonListener(R.id.left)
        buttonListener(R.id.up)
        buttonListener(R.id.right)
        buttonListener(R.id.down)

        buttonListener(R.id.a)
        buttonListener(R.id.b)
    }

    /*******************************************************************************************/

    @SuppressLint("ClickableViewAccessibility")
    fun buttonListener(id : Int) {
        var button : Button = findViewById(id)

        var key : String = ""
        when (id) {
            R.id.left -> key = "LEFT"
            R.id.up -> key = "UP"
            R.id.right -> key = "RIGHT"
            R.id.down -> key = "DOWN"

            R.id.a -> key = "A"
            R.id.b -> key = "B"
        }

        // continue to send when action DOWN
        var keyAux : Int = 0
        button.setOnTouchListener(OnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    keyAux++
                    keysDownHashMap[key] = keyAux
                    CoroutineScope(Dispatchers.IO).launch {
                        keyPress(key, keyAux)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    keysDownHashMap[key] = 0
                }
                //MotionEvent.ACTION_UP -> keysDown.remove(key) //CLIENT?.sendJson(key, "UP")
            }

            true
        })

        /*
        // only send json when command is alternate, which means, when is UP and then DOWN
        var previousEvent = MotionEvent.ACTION_UP
        button.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action != previousEvent) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> CLIENT?.sendJson(key, "DOWN")
                    MotionEvent.ACTION_UP -> CLIENT?.sendJson(key, "UP")
                }
            }
            previousEvent = event.action

            true
        })
        */
    }

    /*******************************************************************************************/

    private fun keyPress(key : String, keyPressedId : Int) {
        CLIENT?.sendJson("{\"key\":\"controllerDown\",\"value\":\"$key\"}\n")
        Thread.sleep(500)

        while (keysDownHashMap[key] == keyPressedId) {
            CLIENT?.sendJson("{\"key\":\"controllerDown\",\"value\":\"$key\"}\n")

            Thread.sleep(100)
        }
    }

}