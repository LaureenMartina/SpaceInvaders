package com.example.lays.spaceinvaders.model

import android.widget.ImageView
import android.view.View
import com.example.lays.spaceinvaders.R

class Ship(var img: ImageView) {

    fun displayShip(screenWidth: Int, screenHeight: Int) {
        img.getLayoutParams().height = screenHeight / 10
        img.getLayoutParams().width = screenWidth / 6
        img.setImageResource(R.drawable.ufo)

        img.visibility = View.VISIBLE
    }
}

