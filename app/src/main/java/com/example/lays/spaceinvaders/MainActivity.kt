package com.example.lays.spaceinvaders

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    lateinit var jouer: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jouer = findViewById<ImageButton>(R.id.btn_jouer)
        jouer.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        })
    }


}
