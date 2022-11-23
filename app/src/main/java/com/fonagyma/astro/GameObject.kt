package com.fonagyma.astro

import android.content.Context
import android.graphics.*
import android.util.Log
import java.lang.Math.*
import kotlin.math.atan
import kotlin.math.pow
import kotlin.random.Random

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
        var aimSpeed : Float = .7f
        var ballStartV = PointF()
        var aimDotN : Int = 15
        var aimLengthToCannonLength : Float = 3f
        init {
                sizeX=1f
                sizeY=1f

            imageR= R.drawable.cannon
            imageBitmap = BitmapFactory.decodeResource(context.resources,imageR)
                Log.d("inf","${imageBitmap.height} ${imageBitmap.width} ")
                //20 10 so
                cP= PointF(imageBitmap.width*(25f/50f)-imageBitmap.width/2f,imageBitmap.height*(75f/100f)-imageBitmap.height/2f)
        }

        override fun update(millisPassed: Long, vararg plus: Float) {
                millisSinceStart+=millisPassed
                Log.d("ms", "$millisSinceStart")
                rotation = if (plus.isNotEmpty()){
                        plus[0]
                }else{
                        0f
                }
                ballStartV= rotateVector(PointF(0f,-imageBitmap.height.toFloat()*.8f),-rotation/180f* PI)
                //sizeY= 2f+sin(millisSinceStart.toDouble()/1000f*2* PI).toFloat()
                //sizeX= 2f+sin(millisSinceStart.toDouble()/1000f*2* PI).toFloat()
        }


        override fun draw(canvas: Canvas, paint: Paint) {
                //paint.color=Color.argb(255,255,0,0)
                //canvas.drawCircle(position.x,position.y,10f,paint)
                //direction
                val v = rotateVector(PointF(0f,-imageBitmap.height.toFloat()*aimLengthToCannonLength),-rotation/180f* PI)
                //canvas.drawLine(position.x,position.y,position.x+v.x,position.y+v.y,paint)

                //draw dots at timeRatio of all aimDotN segments
                val mm : Long = (1000/aimSpeed).toLong()
                val timeRatio: Float = (millisSinceStart%mm).toFloat()/mm

                paint.color=Color.argb(255,0,255,0)
                //canvas.drawCircle(position.x+v.x*timeRatio,position.y+v.y*timeRatio,20f,paint)
                if (aimDotN>0){
                        for(a in 1..aimDotN){
                                val ratio : Float = (a-1+timeRatio)/aimDotN.toFloat()
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

fun mirrorVectorToVector(v:PointF,e:PointF):PointF{
        val angle = atan(e.y.toDouble()/e.y.toDouble())
        val va= rotateVector(v,angle)
        va.y*=-1f
        return rotateVector(va,-angle)
}

class Ball(pos: PointF, context: Context, velocity :PointF, walle : PointF) : GameObject(pos,context){
        var direction : PointF
        var hitBoxR : Float
        var speed = 500f
        private var wall : PointF
        private val colorM = Color.argb(255,Random.nextInt(255),Random.nextInt(255),Random.nextInt(255))
        init {
                hitBoxR = 50f
                direction =PointF( velocity.x/(kotlin.math.sqrt(
                        kotlin.math.abs(velocity.x).pow(2) + kotlin.math.abs(velocity.y).pow(2))),
                 velocity.y/(kotlin.math.sqrt(
                        kotlin.math.abs(velocity.x).pow(2) + kotlin.math.abs(velocity.y).pow(2))))
                wall = walle
        }
        override fun draw(canvas: Canvas, paint: Paint) {
                paint.color=colorM
                canvas.drawCircle(position.x,position.y,hitBoxR,paint)
        }

        override fun log() {
                TODO("Not yet implemented")
        }

        override fun update(millisPassed: Long, vararg plus: Float) {
                position.x+=direction.x*speed*millisPassed/1000
                position.y+=direction.y*speed*millisPassed/1000

                        if(position.x>wall.x){
                                direction.x *= -.9f
                                position.x = wall.x
                        }
                        if(position.y>wall.y){
                                direction.y *= -.9f
                                position.y = wall.y
                        }
                        if(position.x<0f){
                                direction.x *= -.9f
                                position.x = 0f
                        }
                        if(position.y<0f){
                                direction.y *= -.9f
                                position.y = 0f
                        }

        }
        fun collide(other: Ball){
                val d= kotlin.math.sqrt(
                        kotlin.math.abs(position.x - other.position.x).pow(2) + kotlin.math.abs(position.y - other.position.y).pow(2)
                )

                if (hitBoxR+other.hitBoxR-1f>d){
                        val mx = (position.x + other.position.x)/2f
                        val my = (position.y + other.position.y)/2f
                        val vx = position.x-mx
                        val vy = position.y-my
                        val e = PointF(position.y-other.position.y,other.position.x-position.x)
                        val dv = kotlin.math.sqrt(kotlin.math.abs(vx).pow(2) + kotlin.math.abs(vy).pow(2))

                        val angle = atan(e.y.toDouble()/e.y.toDouble())
                        val va= rotateVector(direction,angle)
                        val vb= rotateVector(other.direction,angle)

                        var tempV = va.y
                        va.y=vb.y
                        vb.y=tempV

                        direction = rotateVector(va,-angle)
                        other.direction = rotateVector(vb,-angle)
                        position.x = mx + vx /dv * hitBoxR*1.01f
                        position.y = my + vy /dv * hitBoxR*1.01f
                        other.position.x = mx - vx /dv * other.hitBoxR*1.2f
                        other.position.y = my - vy /dv * other.hitBoxR*1.2f
                }
        }
}