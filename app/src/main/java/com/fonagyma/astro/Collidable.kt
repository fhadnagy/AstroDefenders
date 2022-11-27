package com.fonagyma.astro

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

abstract class Collidable (pos: PointF,context: Context): GameObject(pos,context) {
    val velocity = PointF()
    val exists = true
    val type : Int = 0
    var hR =1f

    abstract fun collide(other : Collidable): Boolean
}