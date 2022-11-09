package com.fonagyma.astro

import android.graphics.PointF
import android.graphics.RectF
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

class GameObject (direction: PointF){

        var velocity: PointF = PointF()
        val position: PointF = PointF()
        var hitBoxR = 5f
        val speed = .002f

        init {
            // Determine the direction
            velocity.x = direction.x * speed
            velocity.y = direction.y * speed
        }

        fun force(a: PointF){
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
        }

}