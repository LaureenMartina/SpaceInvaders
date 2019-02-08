package model

import android.view.View
import android.widget.ImageView
import com.example.lays.spaceinvaders.R
import java.util.*

class Monster(var visible: Boolean = false, var position_x: Int = 0, var position_y: Int = 0, var image: ImageView){


    fun disappear() {
        visible = false
        image.visibility = View.GONE
    }

    fun appear(limit_x: Int, screenHeight: Int) {
        image.setY(position_y.toFloat())
        image.setX(position_x.toFloat())
        image.getLayoutParams().height = screenHeight / 10
        image.getLayoutParams().width = screenHeight / 10
        image.setImageResource(R.drawable.monster)

        position_x = Random().nextInt(limit_x - image.getLayoutParams().width) //TODO a corriger
        position_y = 0
        visible = true //TODO enlever plus tard

        image.visibility = View.VISIBLE
    }
}

