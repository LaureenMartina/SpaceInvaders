package model

import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.view.View
import com.example.lays.spaceinvaders.R
import java.util.*
import kotlin.concurrent.schedule

class Ship(/*var position_x: Int = 0, var position_y: Int = 0, */ var img: ImageView) {

    /*fun shootMonster(imgShoot: ImageView, screenWidth: Int, screenHeight: Int){
        isShooting = true

        if(isShooting === true) {
            Log.d("TOTO pos x", img.getX().toString())
            Log.d("TOTO pos y", img.getY().toString())
            Log.d("TOTO size", img.width.toString())
            Log.d("TOTO screenHeight", screenHeight.toString())
            Log.d("TOTO screenWidth", screenWidth.toString())
        }


        Handler().postDelayed({
            isShooting = false
        }, 3000)
    }*/

    fun displayShip(screenWidth: Int, screenHeight: Int) {
        img.getLayoutParams().height = screenHeight / 10
        img.getLayoutParams().width = screenWidth / 6
        img.setImageResource(R.drawable.vaisseau)

        img.visibility = View.VISIBLE
    }
}

