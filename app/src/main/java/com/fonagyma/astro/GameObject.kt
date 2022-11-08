package com.fonagyma.astro

import android.graphics.PointF

class GameObject (direction: PointF){

        var velocity: PointF = PointF()
        val position: PointF = PointF()

        init {
            // Determine the direction
            velocity.x = direction.x
            velocity.y = direction.y
        }

        fun force(a: PointF){
            velocity.x+=a.x
            velocity.y+=a.y
        }

        fun update() {
            // Move the particle
            position.x += velocity.x
            position.y += velocity.y
        }

}