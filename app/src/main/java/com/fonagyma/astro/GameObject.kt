package com.fonagyma.astro

import android.content.Context
import android.graphics.*
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

abstract class GameObject(pos: PointF, context: Context){

        var sizeX : Float = 1f
        var sizey : Float = 1f
        var turn : Float = 0f
        var position = pos
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
        init {
            imageR= R.drawable.cannonfull
            imageBitmap = BitmapFactory.decodeResource(context.resources,imageR)
        }

        override fun update(millisPassed: Long, vararg plus: Float) {
                rotation = if (plus.isNotEmpty()){
                        plus[0]
                }else{
                        0f
                }
        }


        override fun draw(canvas: Canvas, paint: Paint) {
                val matrix = Matrix()
                matrix.preRotate(rotation)
                matrix.preScale(sizeX,sizey)
                val myB = Bitmap.createBitmap(imageBitmap,0, 0, imageBitmap.width, imageBitmap.height, matrix, true)
                canvas.drawBitmap(myB,position.x-myB.width/2,position.y-myB.height/2,paint)
        }

        override fun log() {
                TODO("Not yet implemented")
        }
}