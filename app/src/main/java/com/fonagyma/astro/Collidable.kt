package com.fonagyma.astro

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class Collidable (pos: PointF,context: Context): GameObject(pos,context) {

    override fun draw(canvas: Canvas, paint: Paint) {
        TODO("Not yet implemented")
    }

    override fun log() {

    }

    override fun update(millisPassed: Long, vararg plus: Float) {
        TODO("Not yet implemented")
    }
}