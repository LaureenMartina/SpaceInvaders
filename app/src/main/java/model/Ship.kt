package model

import android.os.Handler
import android.widget.ImageView
import android.view.View
import com.example.lays.spaceinvaders.R

class Ship(var position_x: Int = 0, var position_y: Int = 0, var isShooting: Boolean = false, var img: ImageView) {

    fun shootMonster(){
        isShooting = true
        Handler().postDelayed({
            isShooting = false
        }, 3000)
    }

    fun displayShip() {
        img.setY(position_y.toFloat())
        img.setX(position_x.toFloat())
        img.getLayoutParams().height = 250
        img.getLayoutParams().width = 250
        img.setImageResource(R.drawable.ship)

        img.visibility = View.VISIBLE
    }
}

