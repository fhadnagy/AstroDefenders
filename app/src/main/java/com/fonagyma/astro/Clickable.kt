package com.fonagyma.astro

import android.content.Context
import android.graphics.*
import android.util.Log
import kotlin.math.*

abstract class Clickable(pos: PointF, rect: RectF,context: Context) : GameObject(pos,context) {
    var hitBox : RectF = rect
    abstract fun onClick(p : PointF)
}

class CounterButton(pos: PointF, hitBox : RectF,context: Context,_ImageR: Int,_x:Float,_y:Float,_ctrStart:Float,_icrA:Float) : Clickable(pos, hitBox,context){

    var midP = PointF((hitBox.left+hitBox.right)/2,(hitBox.bottom+hitBox.top)/2)
    var strokeWidth = 5f
    var showHitbox = true
    var incrAmonunt = 1f
    var counter = 1f
    init {
        sizeX = _x
        sizeY = _y
        counter = _ctrStart
        incrAmonunt = _icrA
        imageR= _ImageR
        imageBitmap = BitmapFactory.decodeResource(context.resources,imageR)
        Log.d("inf","${imageBitmap.height} ${imageBitmap.width} ")

        cP= PointF(imageBitmap.width*(.50f)-imageBitmap.width/2f,imageBitmap.height*(.5f)-imageBitmap.height/2f)

    }
    override fun log() {
        TODO("Not yet implemented")
    }

    override fun onClick(p: PointF) {
        counter+=incrAmonunt
    }

    override fun update(millisPassed: Long, vararg plus: Float) {
        return
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        val matrix = Matrix()
        matrix.preScale(sizeX,sizeY)
        val myB = Bitmap.createBitmap(imageBitmap,0, 0, imageBitmap.width, imageBitmap.height, matrix, true)

        val c = rotateVector(PointF(cP.x*sizeX,cP.y*sizeY),-turn/180f* Math.PI)
        canvas.drawBitmap(myB,midP.x-myB.width/2-c.x,midP.y-myB.height/2-c.y,paint)

        paint.color=Color.argb(255,255,255,0)
        paint.textSize= hitBox.height()/3f
        canvas.drawText("$counter",midP.x-30f,midP.y,paint)
        if (showHitbox){
            paint.color= Color.argb(255,255,255,0)
            paint.strokeWidth=strokeWidth
            paint.style=Paint.Style.STROKE
            canvas.drawRect(hitBox,paint)
            paint.style=Paint.Style.FILL

        }
    }

}

class Joystick(pos: PointF, hitBox : RectF,context: Context) : Clickable(pos, hitBox,context){
    var midP = PointF((hitBox.left+hitBox.right)/2,(hitBox.bottom+hitBox.top)/2)
    var cursorP : PointF = PointF(midP.x,midP.y)
    var colorBg = Color.argb(255,255,155,155)
    var colorFg = Color.argb(255,0,0,52)
    var strokeWidth = 5f
    var padRadius : Float = 140f
    var margin = 5f
    var cursorRadius: Float
    var showHitbox = true
    var rotation = 0f
    init {
        //cursorRadius = (hitBox.bottom-hitBox.top)/3f
        cursorRadius = 40f
    }
    //constructor(pos: PointF,hitBox: RectF,)

    override fun draw(canvas: Canvas,paint: Paint) {

        paint.color= colorBg
        canvas.drawCircle(midP.x,midP.y,padRadius,paint)
        paint.color = colorFg
        canvas.drawCircle(cursorP.x,cursorP.y,cursorRadius,paint)
        paint.color = colorBg
        canvas.drawCircle(cursorP.x,cursorP.y,cursorRadius-strokeWidth,paint)
        paint.color = colorFg
        canvas.drawCircle(midP.x,midP.y,strokeWidth*2,paint)
        //paint.strokeWidth= strokeWidth
        //canvas.drawLine(midP.x,midP.y,cursorP.x,cursorP.y,paint)
        if (showHitbox){
            paint.color= Color.argb(255,255,255,0)
            paint.strokeWidth=strokeWidth
            canvas.drawLine(hitBox.left,hitBox.top,hitBox.right,hitBox.top,paint)
            canvas.drawLine(hitBox.left,hitBox.bottom,hitBox.right,hitBox.bottom,paint)
            canvas.drawLine(hitBox.left,hitBox.top,hitBox.left,hitBox.bottom,paint)
            canvas.drawLine(hitBox.right,hitBox.top,hitBox.right,hitBox.bottom,paint)
        }
    }

    override fun log() {
        TODO("Not yet implemented")
    }

    override fun update(millisPassed: Long, vararg plus: Float) {

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
        val dy= midP.y-cursorP.y
        val dx= cursorP.x-midP.x

        if (cursorP.x==midP.x && cursorP.y==midP.y)
        {
            rotation=0f
        }else{

            val rt = atan(dx/abs(dy))*180f/ PI
            Log.d("onCLickJs","rt: $rt")
            if(rt==0.toDouble() && dy<0){
                rotation=180f
            }else if(rt<0 && dy>0){
                rotation=rt.toFloat()
            }else if(rt>0 && dy>0){
                rotation=rt.toFloat()
            }else if(rt<0 && dy<0){
                rotation=-180f-rt.toFloat()
            }else if(rt>0 && dy<0){
                rotation=180f-rt.toFloat()
            }

        }
        Log.d("onCLickJs","dy:$dy dx:$dx  rotation: $rotation")

    }
}
