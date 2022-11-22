package com.fonagyma.astro

import android.content.Context
import android.graphics.*
import android.util.Log
import java.lang.Math.*

abstract class GameObject(pos: PointF, context: Context){

        var sizeX : Float = 1f
        var sizeY : Float = 1f
        var turn : Float = 0f
        var position = pos
        lateinit var cP : PointF
        lateinit var imageBitmap : Bitmap
        var imageR : Int = -1
        abstract fun log()
        abstract fun draw(canvas : Canvas, paint: Paint)
        abstract fun update(millisPassed: Long, vararg plus: Float)
        /*fun force(a: PointF){
            velocity.x+=a.x
            velocity.y+=a.y
        }

        fun update(fps: Long) {
            // Move the particle
            val dt = 1f/fps
            position.x += velocity.x
            position.y += velocity.y
        }

        fun collides(other: GameObject): Boolean{
            val d= sqrt(abs(position.x-other.position.x).pow(2f) + abs(position.y-other.position.y).pow(2f))
            if (d<2*hitBoxR)
            {
                return true
            }
            return false
        }*/

}

class Cannon(pos: PointF, context: Context) : GameObject(pos,context){

        var rotation : Float = 0f
        var millisSinceStart: Long= 0
        var aimSpeed : Float = 1f
        var aimDotN : Int = 12
        var aimLengthToCannonLength : Float = 2f
        init {
                sizeX=1f
                sizeY=1.5f

            imageR= R.drawable.cannon
            imageBitmap = BitmapFactory.decodeResource(context.resources,imageR)
                Log.d("inf","${imageBitmap.height} ${imageBitmap.width} ")
                //20 10 so
                cP= PointF(imageBitmap.width*(25f/50f)-imageBitmap.width/2f,imageBitmap.height*(75f/100f)-imageBitmap.height/2f)
        }

        override fun update(millisPassed: Long, vararg plus: Float) {
                millisSinceStart+=millisPassed
                Log.d("ms", "millisSinceStart")
                rotation = if (plus.isNotEmpty()){
                        plus[0]
                }else{
                        0f
                }
                //sizeY= 2f+sin(millisSinceStart.toDouble()/1000f*2* PI).toFloat()
                //sizeX= 2f+sin(millisSinceStart.toDouble()/1000f*2* PI).toFloat()
        }


        override fun draw(canvas: Canvas, paint: Paint) {
                paint.color=Color.argb(255,255,0,0)
                //direction
                val v = rotateVector(PointF(0f,-imageBitmap.height.toFloat()*aimLengthToCannonLength),-rotation/180f* PI)

                //draw dots at timeRatio of all aimDotN segments
                val timeRatio: Float = (millisSinceStart%2000).toFloat()/(1000f/aimSpeed)

                paint.color=Color.argb(255,0,255,0)
                if (aimDotN>0){
                        for(a in 1..aimDotN){
                                val ratio = (a-1+timeRatio)/aimDotN.toFloat()
                                val k= PointF(position.x+v.x*ratio,position.y+v.y*ratio)
                                canvas.drawCircle(k.x,k.y,15f*(1-ratio)+5,paint)

                        }
                }else{
                        //line on full length
                        canvas.drawLine(position.x,position.y,position.x+v.x,position.y+v.y,paint)
                }

                val matrix = Matrix()
                matrix.preRotate(rotation)
                matrix.preScale(sizeX,sizeY)
                val myB = Bitmap.createBitmap(imageBitmap,0, 0, imageBitmap.width, imageBitmap.height, matrix, true)

                val c = rotateVector(PointF(cP.x*sizeX,cP.y*sizeY),-rotation/180f* PI)
                canvas.drawBitmap(myB,position.x-myB.width/2-c.x,position.y-myB.height/2-c.y,paint)

                /*paint.color=Color.argb(255,255,0,0)
                canvas.drawLine(position.x,position.y,position.x+myB.width/2,
                position.y+myB.height/2,paint)
                paint.color=Color.argb(255,255,255,0)
                canvas.drawLine(position.x+myB.width/2, position.y+myB.height/2,
                position.x+myB.width/2+c.x, position.y+myB.height/2+c.y, paint)*/

        }

        override fun log() {
                TODO("Not yet implemented")
        }
}

fun rotateVector(v : PointF, rad: Double): PointF{
        return PointF((cos(rad)*v.x+ sin(rad)*v.y).toFloat(), (cos(rad)*v.y- sin(rad)*v.x).toFloat())
}