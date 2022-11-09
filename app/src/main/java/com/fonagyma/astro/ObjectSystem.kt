package com.fonagyma.astro


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import java.lang.Math.sqrt
import java.util.*
import kotlin.math.sqrt


class ObjectSystem(){

    var gameObjects:
            ArrayList<GameObject> = ArrayList()
    private val random = Random()
    var sp = PointF()
    var drawing = false
    var afterD = false
    var clear= false
    private val gC = 2000f
    lateinit var midPointF: PointF

   constructor(mp: PointF) : this() {
        midPointF=mp
    }

    fun addObject(startPosition: PointF){
            sp= startPosition
            afterD= true
    }

    fun update(fps: Long) {
        for (g in gameObjects) {
            /*gravity
            val a= PointF(midPointF.x-g.position.x,midPointF.y-g.position.y)
            val xr = 1/sqrt(a.x*a.x+a.y*a.y)
            a.x *= gC*xr*xr*xr
            a.y *= gC*xr*xr*xr
            g.force(a)*/
            g.update(fps)
            if(g.position.x>(midPointF.x*2)){
                g.velocity.x *= -.9f
                g.position.x = midPointF.x*2
            }
            if(g.position.y>(midPointF.y*2)){
                g.velocity.y *= -.9f
                g.position.y = midPointF.y*2
            }
            if(g.position.x<0f){
                g.velocity.x *= -.9f
                g.position.x = 0f
            }
            if(g.position.y<0f){
                g.velocity.y *= -.9f
                g.position.y = 0f
            }
        }
    }

    fun draw(canvas: Canvas, paint: Paint) {
        drawing= true
        for (g in gameObjects) {
            // Option 1 - Colored particles
            paint.setARGB(255, random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256))
            // Option 2 - White particles
            //paint.color = Color.argb(255, 255, 255, 255)
            // How big is each particle?
            // Option 1 - Big particles
            //val sizeX = 25f
            //val sizeY = 25f
            // Option 2 - Medium particles
            //val sizeX = 10f
            //val sizeY = 10f
            // Option 3 - Tiny particles
            val sizeX = 30f
            val sizeY = 12f
            // Draw the particle
            // Option 1 - Square particles
            //canvas.drawRect(g.position.x,g.position.y,
                //g.position.x + sizeX,
                //g.position.y + sizeY,
                //paint)
            // Option 2 - Circular particles
            canvas.drawCircle(g.position.x, g.position.y, sizeX, paint)
        }
        drawing=false
        if(afterD){
            /*var angle: Double = random.nextInt(360).toDouble()
            angle *= (3.14 / 180)
            // Option 1 - Slow particles
            val speed = random.nextFloat() / 3
            // Option 2 - Fast particles
            //val speed = (random.nextInt(10)+1);
            val direction: PointF
            direction = PointF(Math.cos(angle).toFloat() * speed,
                Math.sin(angle).toFloat() * speed)*/

            /*val a= PointF(midPointF.x-sp.x,midPointF.y-sp.y)
            val xr = 1/ sqrt(a.x*a.x+a.y*a.y)
            val g=GameObject(PointF(xr*(-1f)*a.y*gC*.0015f,xr*a.x*gC*.0015f))*/
            val gTarget= PointF(midPointF.x*2*.25f+random.nextFloat().times(midPointF.x),midPointF.y*2f)
            val startD = PointF(gTarget.x-sp.x,gTarget.y-sp.y)
            val g = GameObject(startD)
            g.position.x=sp.x
            g.position.y=sp.y
            gameObjects.add(g)
            afterD=false
        }
        if (clear){
            gameObjects.clear()
            clear = false
        }

    }

}