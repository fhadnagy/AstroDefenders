package com.fonagyma.astro

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

abstract class GameObject(pos: PointF){

        var sizeX : Float = 1f
        var sizey : Float = 1f
        var turn : Float = 0f
        var imageR : Int = -1
        abstract fun log()
        abstract fun draw(canvas : Canvas, paint: Paint)
        abstract fun update(fps: Long)
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