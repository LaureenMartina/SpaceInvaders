package com.example.lays.spaceinvaders.model

import android.view.View
import android.widget.ImageView
import com.example.lays.spaceinvaders.R

class Monster(var position_x: Int = 0, var image: ImageView){


    fun disappear() {
        image.visibility = View.GONE
    }

    fun appear(limit_x: Int, screenHeight: Int) {
        //image.setY(position_y.toFloat())
        image.setX(position_x.toFloat())
        image.getLayoutParams().height = screenHeight / 10
        image.getLayoutParams().width = screenHeight / 10
        image.setImageResource(R.drawable.monster)

        image.visibility = View.VISIBLE
    }
}

