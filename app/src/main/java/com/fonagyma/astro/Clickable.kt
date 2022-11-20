package com.fonagyma.astro

import android.graphics.*
import android.util.Log
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

abstract class Clickable(pos: PointF, rect: RectF) : GameObject(pos) {
    var hitBox : RectF = rect
    abstract fun onClick(p : PointF)
}

class Joystick(pos: PointF, hitBox : RectF) : Clickable(pos, hitBox){
    var midP = PointF((hitBox.left+hitBox.right)/2,(hitBox.bottom+hitBox.top)/2)
    var cursorP : PointF = PointF(midP.x,midP.y)
    var colorBg = Color.argb(255,255,155,155)
    var colorFg = Color.argb(255,0,0,52)
    var strokeWidth = 5f
    var padRadius : Float = 140f
    var margin = 5f
    var cursorRadius: Float
    init {
        //cursorRadius = (hitBox.bottom-hitBox.top)/3f
        cursorRadius = 25f
    }

    override fun draw(canvas: Canvas,paint: Paint) {
        //paint.color= Color.argb(255,255,255,255)
        //canvas.drawRect(hitBox,paint)
        paint.color= colorBg
        canvas.drawCircle(midP.x,midP.y,padRadius,paint)
        paint.color = colorFg
        canvas.drawCircle(cursorP.x,cursorP.y,cursorRadius,paint)
        paint.color = colorBg
        canvas.drawCircle(cursorP.x,cursorP.y,cursorRadius-strokeWidth,paint)
        paint.color = colorFg
        canvas.drawCircle(midP.x,midP.y,strokeWidth,paint)
        paint.strokeWidth= strokeWidth
        canvas.drawLine(midP.x,midP.y,cursorP.x,cursorP.y,paint)
    }

    override fun log() {
        TODO("Not yet implemented")
    }

    override fun update(fps: Long) {
        TODO("Not yet implemented")
    }

    override fun onClick(p: PointF) {
        val d= sqrt(abs(midP.x-p.x).pow(2)+abs(midP.y-p.y).pow(2))
        if(d > padRadius - margin - cursorRadius)
        {
            cursorP.x = (p.x-midP.x)/d* (padRadius-margin-cursorRadius)+ midP.x
            cursorP.y = (p.y-midP.y)/d* (padRadius-margin-cursorRadius)+ midP.y
        }else{
            cursorP.x = p.x
            cursorP.y = p.y
        }
        Log.d("onCLick","you hit a joystick")
    }
}