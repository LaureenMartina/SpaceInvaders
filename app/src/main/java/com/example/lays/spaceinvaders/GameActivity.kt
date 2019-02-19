package com.example.lays.spaceinvaders

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.example.lays.spaceinvaders.model.Monster
import com.example.lays.spaceinvaders.model.Ship
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.concurrent.schedule

class GameActivity : AppCompatActivity() {

    private val logger = KotlinLogging.logger {}

    private var detector: GestureDetector? = null
    private var screenWidth = 0
    private var screenHeight = 0
    var anim : TranslateAnimation? = null

    private lateinit var timer: TimerTask
    private lateinit var mainLayout: RelativeLayout

    private lateinit var imgViewShip: ImageView

    private var monsterList: ConcurrentLinkedQueue<Monster> = ConcurrentLinkedQueue()
    lateinit var ship: Ship


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        /* ----- Récupérer la taille de l'écran ----- */
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        mainLayout = findViewById(R.id.gameActivity)

        /* ----- Création de l'objet image Vaisseau ----- */
        imgViewShip = ImageView(this)

        ship = Ship(imgViewShip)

        val layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL)
        mainLayout.addView(imgViewShip, layoutParams)
        ship.displayShip(screenWidth, screenHeight)


        createMonsterLine()

        ship.img.setOnClickListener {
            val imgShoot = ImageView(this)

            imgShoot.setY((screenHeight - ship.img.height).toFloat())
            imgShoot.setX((ship.img.x + (ship.img.width/2)))
            imgShoot.setImageResource(R.drawable.bullet)

            mainLayout.addView(imgShoot)

            imgShoot.getLayoutParams().height = 100
            imgShoot.getLayoutParams().width = 40

            Timer("SettingShooting", false).schedule(0, 300) {

                val pas = screenHeight / 12

                imgShoot.y = (imgShoot.y - pas)

                for (monster in monsterList) {

                    if((monster.image.x + (monster.image.width/2)) > ship.img.x &&
                            (monster.image.x + (monster.image.width/2)) < (ship.img.x + ship.img.width) /*&&
                            (monster.image.y + monster.image.height + 10) == imgShoot.y*/)
                    {
                        this@GameActivity.runOnUiThread {
                            monster.disappear()
                            mainLayout.removeView(imgShoot)
                        }

                        cancel()
                        monsterList.remove(monster)
                        break
                    }
                }

                if(imgShoot.y <= 0) {
                    this@GameActivity.runOnUiThread {
                        mainLayout.removeView(imgShoot)
                    }
                    cancel()
                }
            }
        }

        detector = GestureDetector(this@GameActivity, MyGestureDetector())

        ship.img.setOnTouchListener {
            _, aEvent ->
            detector!!.onTouchEvent(aEvent)
        }

        timer = Timer("SettingUp", false).schedule(1000, 2000) {
            createMonsterLine()

            val pas = screenHeight / 10

            for (monster in monsterList) {
                monster.image.setY(monster.image.getY() + pas)

                if(monster.image.y.toInt() >= (screenHeight - (pas * 2  + ship.img.height))) {
                    gameOver()
                    cancel()
                }
            }
        }
    }

    private fun createMonsterLine() {
        this@GameActivity.runOnUiThread {

            var maxLimit = 10

            for (i in 1..3) {
                val rand = Random().nextInt(maxLimit)

                val imageViewMonster = ImageView(this@GameActivity)
                val monster = Monster(rand, imageViewMonster)
                mainLayout.addView(monster.image)
                monster.appear(screenWidth, screenHeight)
                monsterList.add(monster)

                maxLimit = screenWidth - monster.image.getLayoutParams().width
            }
        }
    }

    private fun gameOver() {
        this@GameActivity.runOnUiThread {
            Toast.makeText(this, "...GAME OVER...", Toast.LENGTH_SHORT).show()
        }
    }

    fun onSwipeRight(dX: Float) {
        val pos = ship.img.x + dX
        if (pos > screenWidth ) {

            anim = TranslateAnimation(0F, screenWidth.toFloat() - ship.img.getX(), 0F, 0F)
            anim!!.setDuration(700)
            ship.img.startAnimation(anim)
            ship.img.setX(screenWidth.toFloat())
        } else {
            anim = TranslateAnimation(0F, dX, 0F, 0F)
            anim!!.setDuration(700)
            ship.img.startAnimation(anim)
            ship.img.setX(pos)
        }

    }

    fun onSwipeLeft(dX: Float) {
        val pos = ship.img.getX() + dX
        if (pos < 0) {
            anim = TranslateAnimation(0F, 0F - ship.img.getX(), 0F, 0F)
            anim!!.setDuration(700)
            ship.img.startAnimation(anim)
            ship.img.setX(0F)
        }else {
            anim = TranslateAnimation(0F, dX, 0F, 0F)
            anim!!.setDuration(700)
            ship.img.startAnimation(anim)
            ship.img.setX(pos)
        }
    }

    inner class MyGestureDetector : GestureDetector.SimpleOnGestureListener() {
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
            }
            return false
        }
    }
}