package com.example.lays.spaceinvaders

import android.content.Context
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import model.Monster
import model.Ship
import mu.KotlinLogging
import java.util.*
import kotlin.concurrent.schedule

class GameActivity : AppCompatActivity() {

    var detector: GestureDetector? = null
    var screenWidth = 0
    var screenHeight = 0
    var anim : TranslateAnimation? = null

    lateinit var timer: TimerTask
    lateinit var ship_img: ImageView
    lateinit var mainLayout: RelativeLayout

    lateinit var imageViewMonster: ImageView
    lateinit var imgViewShip: ImageView

    lateinit var monster: Monster // TODO changer en tableau liste de monstres
    lateinit var ship: Ship

    // Log.DEBUG message en Kotlin
    private val logger = KotlinLogging.logger {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        mainLayout = findViewById(R.id.gameActivity)

        // ********** TEST **********
        val paramShip = mainLayout.getLayoutParams() as RelativeLayout.LayoutParams
        paramShip.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        paramShip.addRule(RelativeLayout.CENTER_HORIZONTAL)


        imageViewMonster = ImageView(this)
        monster = Monster(false, 0,0, imageViewMonster)

        imgViewShip = ImageView(this)
        ship = Ship(0, 0, false, imgViewShip)

        mainLayout.addView(monster.image)
        mainLayout.addView(ship.img)

        monster.appear(screenWidth)
        ship.displayShip()



        ship_img.setOnClickListener(View.OnClickListener {
            ship.shootMonster()

            //TODO à corriger
            Log.d("TOTO monster",  monster.position_x.toString())
            Log.d("TOTO ship",  ship_img.x.toString())

            if (ship_img.x >= monster.position_x + 20 || ship_img.x  <= monster.position_x - 20) { // TODO lier img ship à l'objet
                monster.disappear()
            }
        })

        detector = GestureDetector(this@GameActivity, MyGestureDetector())
        ship_img.setOnTouchListener() {
            v, aEvent ->
            detector!!.onTouchEvent(aEvent)
        }

        timer = Timer("SettingUp", false).schedule(1000, 1000) {
            monster.image.setY(monster.image.getY() + 50)

            //Log.d("TOTO SCREEN H", monster.image.getY().toString())
            // Log.d("TOTO screenHeight", screenHeight.toString())

            if(monster.image.getY().toInt() >= 1450) { // TODO trouver le moyen d'avoir la bonne limit en fct des écrans (screenHeight - 200)
                // monster.disappear()
                Log.d("TIMER", "arret monstre")
                // Toast.makeText(this@GameActivity, "GAME OVER", Toast.LENGTH_SHORT).show()
                cancel()
            }

            //TODO créer de nouveaux monster + déplacement
            //TODO rajouter les image view monster au fir et a mesure + les insérer dans une list
        }
    }

    fun onSwipeRight(dX: Float) {
        var pos = ship_img.getX() + dX
        if (pos > screenWidth ) {
            anim = TranslateAnimation(0F, screenWidth.toFloat() - ship_img.getX(), 0F, 0F)
            anim!!.setDuration(700);
            ship_img.startAnimation(anim)
            ship_img.setX(screenWidth.toFloat())
        } else {
            anim = TranslateAnimation(0F, dX, 0F, 0F)
            anim!!.setDuration(700);
            ship_img.startAnimation(anim)
            ship_img.setX(pos)
        }

    }

    fun onSwipeLeft(dX: Float) {
        var pos = ship_img.getX() + dX
        if (pos < 0) {
            anim = TranslateAnimation(0F, 0F - ship_img.getX(), 0F, 0F)
            anim!!.setDuration(700);
            ship_img.startAnimation(anim)
            ship_img.setX(0F)
        }else {
            anim = TranslateAnimation(0F, dX, 0F, 0F)
            anim!!.setDuration(700);
            ship_img.startAnimation(anim)
            ship_img.setX(pos)
        }
    }

    inner class MyGestureDetector() : GestureDetector.SimpleOnGestureListener() {
        private var mLastOnDownEvent: MotionEvent? = null
        private val SWIPE_MIN_DISTANCE = 100
        private val SWIPE_MAX_OFF_PATH = 100
        private val SWIPE_THRESHOLD_VELOCITY = 100


        override fun onDown(e: MotionEvent): Boolean {
            mLastOnDownEvent = e
            return super.onDown(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if ( e1 == null || e2 == null) return false
            val dX = e2.getX() - e1.getX()
            val dY = e1.getY() - e2.getY()

            if (Math.abs(dY) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
                if (dX > 0) {
                    //Swipe Right
                    logger.debug {"right click"}
                    onSwipeRight(dX)
                } else {
                    // Swipe Left
                    logger.debug {"left click"}
                    onSwipeLeft(dX)
                }
                return true
            } else
                if (Math.abs(dX) < SWIPE_MAX_OFF_PATH && Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dY) >= SWIPE_MIN_DISTANCE) {
                    if (dY > 0) {
                        // Swipe UP
                    } else {
                        // Swipe DOWN
                    }
                    return true
                }
            return false
        }
    }
}