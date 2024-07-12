package com.example.carracegame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c:Context, var gameTask: GameTask):View(c)
{
    private var myPaint: Paint? = null
    var speed = 1
    private var time = 0
    var score = 0
    private var myBombPosition = 0
    val otherBomb = ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0
    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed){
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherBomb.add(map)
        }
        time = time + 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.alian3,null)

        d.setBounds(
            myBombPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight-2 - carHeight,
            myBombPosition * viewWidth / 3 + viewWidth/15 + carWidth -25,
            viewHeight-2
        )
        d.draw(canvas)
        myPaint!!.color=Color.GREEN
        var highScore = 0

        for (i in otherBomb.indices){
            try {
                val carX = otherBomb[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                val carY = time - otherBomb[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.bomb,null)

                d2.setBounds(
                    carX + 25 , carY - carHeight, carX + carWidth -25 , carY
                )
                d2.draw(canvas)
                if(otherBomb[i]["lane"] as Int == myBombPosition){
                    if (carY > viewHeight - 2 - carHeight
                        && carY < viewHeight - 2){

                        gameTask.closeGame(score)
                    }
                }
                if (carY > viewHeight + carHeight)
                {
                    otherBomb.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score/8)
                    if (score > highScore){
                        highScore = score
                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val x1 = event.x
                if (x1 < viewWidth/2){
                    if (myBombPosition> 0){
                        myBombPosition--
                    }
                }
                if (x1 > viewWidth / 2){
                    if (myBombPosition<2){
                        myBombPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP->{}
        }


        return true
    }
}