package com.dgaspar.localclient

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GameController : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_controller)

        buttonListener(R.id.left)
        buttonListener(R.id.up)
        buttonListener(R.id.right)
        buttonListener(R.id.down)

        buttonListener(R.id.a)
        buttonListener(R.id.b)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun buttonListener(id : Int) {
        var button : Button = findViewById(id)

        var key : String = ""
        when (id) {
            R.id.left -> "LEFT"
            R.id.up -> "UP"
            R.id.right -> "RIGHT"
            R.id.down -> "DOWN"

            R.id.a -> key = "A"
            R.id.b -> key = "B"
        }

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
    }
}