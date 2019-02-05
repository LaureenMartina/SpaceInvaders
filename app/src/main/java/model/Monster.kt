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

    fun appear(limit_x: Int) {
        position_x = Random().nextInt(limit_x - 250) //TODO faire varier la position X (random)
        position_y = 0
        visible = true

        image.setY(position_y.toFloat())
        image.setX(position_x.toFloat())
        image.getLayoutParams().height = 250
        image.getLayoutParams().width = 250
        image.setImageResource(R.drawable.monster)

        image.visibility = View.VISIBLE
    }
}

