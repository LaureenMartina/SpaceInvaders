package com.example.lays.spaceinvaders

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import model.Monster
import model.Ship
import mu.KotlinLogging
import java.util.*
import kotlin.collections.AbstractList
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class GameActivity : AppCompatActivity() {

    var detector: GestureDetector? = null
    var screenWidth = 0
    var screenHeight = 0
    var anim : TranslateAnimation? = null

    lateinit var timer: TimerTask
    lateinit var ship_img: ImageView
    lateinit var mainLayout: RelativeLayout

    //lateinit var imageViewMonster: ImageView
    lateinit var imgViewShip: ImageView

    // lateinit var monster: Monster // TODO changer en tableau liste de monstres
    var monsterList: ArrayList<Monster> = ArrayList()
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


        imgViewShip = ImageView(this)

        ship = Ship(0, 0, false, imgViewShip)

        val layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        mainLayout.addView(imgViewShip, layoutParams)
        ship.displayShip(screenWidth, screenHeight)

        createMonsterLine()

        ship.img.setOnClickListener(View.OnClickListener {
            ship.shootMonster()

            //TODO à corriger
            // Log.d("TOTO monster",  monster.position_x.toString())
            Log.d("TOTO ship",  ship.img.x.toString())

            /*if (ship.img.x >= monster.position_x + 20 || ship.img.x  <= monster.position_x - 20) {
                monster.disappear()
            }*/
        })

        detector = GestureDetector(this@GameActivity, MyGestureDetector())

        ship.img.setOnTouchListener() {
            v, aEvent ->
            detector!!.onTouchEvent(aEvent)
        }

        timer = Timer("SettingUp", false).schedule(1000, 1000) {
            //Log.d("TITI limit",  (screenHeight - monster.image.layoutParams.height).toString())
            //Log.d("TITI monster",  monster.image.getY().toInt().toString())

            createMonsterLine()

            val pas = screenHeight / 20

            for (monster in monsterList) {
                monster.image.setY(monster.image.getY() + pas)

                if(monster.image.getY().toInt() >= (screenHeight - (pas * 4))) { // TODO enlever le title bar et mettre pas * 2
                    gameOver()
                    cancel()
                }
            }

            //TODO créer de nouveaux monster + déplacement
            //TODO rajouter les image view monster au fir et a mesure + les insérer dans une list
        }
    }

    fun createMonsterLine() {
        this@GameActivity.runOnUiThread(java.lang.Runnable {
            for (i in 1..5) {
                val imageViewMonster = ImageView(this@GameActivity)
                val monster = Monster(false, 0, 0, imageViewMonster)
                mainLayout.addView(monster.image)
                monster.appear(screenWidth, screenHeight)
                monsterList.add(monsterList.size, monster)

            }
        })
    }

    fun gameOver() {
        this@GameActivity.runOnUiThread(java.lang.Runnable {
            //TODO afficher le GAMEOVER : faire appel à une méthode de l'activité (voir error d'exception toast)
            Toast.makeText().show()
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