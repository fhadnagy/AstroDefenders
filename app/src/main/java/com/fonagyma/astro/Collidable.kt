package com.fonagyma.astro

import android.content.Context
import android.graphics.*
import android.util.Log
import kotlin.math.atan
import kotlin.math.pow
import kotlin.random.Random

abstract class Collidable (pos: PointF,context: Context, _velocity : PointF, _hR : Float): GameObject(pos,context) {
    var velocity = _velocity
    var exists = true
    var type : Int = 0
    var hR = _hR
    abstract fun collides(oC : Collidable): Boolean
    abstract fun onCollide(oC: Collidable)
}

class Astroid(pos: PointF,context: Context, _velocity : PointF, _mass: Float, _hR : Float, walle : PointF) : Collidable(pos, context, _velocity, _hR){
    var omega = 50f
    val mxhp : Int = 3
    var hp : Int = 3
    var mass :Float
    val random = Random(System.currentTimeMillis())
    private var wall : PointF

    init {
        type = 1
        omega = random.nextFloat()*30f-60f
        sizeX=.01f*hR
        sizeY=.01f*hR
        mass= _mass
        wall = walle

        imageR= R.drawable.astroid0
        imageBitmap = BitmapFactory.decodeResource(context.resources,imageR)
        Log.d("inf","${imageBitmap.height} ${imageBitmap.width} ")

        cP= PointF(imageBitmap.width*(.48f)-imageBitmap.width/2f,imageBitmap.height*(.49f)-imageBitmap.height/2f)
    }


    override fun log() {
        TODO("Not yet implemented")
    }

    override fun draw(canvas: Canvas, paint: Paint) {
        if(!exists) return
        //paint.color=Color.argb(255,255,0,0)
        //canvas.drawCircle(position.x,position.y,hR,paint)


        val matrix = Matrix()
        matrix.preRotate(turn)
        matrix.preScale(sizeX,sizeY)
        val myB = Bitmap.createBitmap(imageBitmap,0, 0, imageBitmap.width, imageBitmap.height, matrix, true)

        val c = rotateVector(PointF(cP.x*sizeX,cP.y*sizeY),-turn/180f* Math.PI)
        canvas.drawBitmap(myB,position.x-myB.width/2-c.x,position.y-myB.height/2-c.y,paint)

        paint.color=Color.argb(255,255,0,0)

        paint.style= Paint.Style.STROKE
        canvas.drawCircle(position.x,position.y,hR,paint)
        paint.style= Paint.Style.FILL_AND_STROKE
        paint.color=Color.argb(255,255,0,0)

        canvas.drawRect(position.x-hR,position.y-hR*1.4f,position.x+hR,position.y-hR*1.2f,paint)
        paint.color=Color.argb(255,0,255,0)

        canvas.drawRect(position.x-hR,position.y-hR*1.4f,position.x+2*hR*(-.5f+hp.toFloat()/mxhp.toFloat()),position.y-hR*1.2f,paint)


        /*paint.color=Color.argb(255,255,0,0)
        canvas.drawLine(position.x,position.y,position.x+myB.width/2,
        position.y+myB.height/2,paint)
        paint.color=Color.argb(255,255,255,0)
        canvas.drawLine(position.x+myB.width/2, position.y+myB.height/2,
        position.x+myB.width/2+c.x, position.y+myB.height/2+c.y, paint)*/
    }

    override fun update(millisPassed: Long, vararg plus: Float) {
        if (hp<=0) exists= false
        if(!exists) return
        //omega *= 1f-.05f *(millisPassed/1000f)
        turn += omega *(millisPassed/1000f)
        if (turn >360f) turn-=360f
        position.x += velocity.x
        position.y += velocity.y

        if(position.x>wall.x || position.y>wall.y|| position.x<0 || position.y<0){
            exists = false
        }

        /**
        if(position.x>wall.x-hR){
            velocity.x *= -.9f
            position.x = wall.x-hR
        }
        if(position.y>wall.y-hR){
            velocity.y *= -.9f
            position.y = wall.y-hR
        }
        if(position.x<hR){
            velocity.x *= -.9f
            position.x = hR
        }
        if(position.y<hR){
            velocity.y *= -.9f
            position.y = hR
        }*/
    }

    override fun collides(oC: Collidable): Boolean {
        if (!exists or !oC.exists){
            return false
        }
        val d = kotlin.math.sqrt(
            kotlin.math.abs(position.x - oC.position.x).pow(2) + kotlin.math.abs(
                position.y - oC.position.y
            ).pow(2)
        )
        if (hR+ oC.hR - 1f > d) return true

        return false
    }

    override fun onCollide(oC: Collidable) {
        if (oC.type == 1)
        {
            val other= oC as Astroid
            val d = kotlin.math.sqrt(
                kotlin.math.abs(position.x - other.position.x).pow(2) + kotlin.math.abs(
                    position.y - other.position.y
                ).pow(2)
            )

            /*val mid = PointF(
                    (position.x * (other.hitBoxR + mass.pow(2)) + other.position.x * (hitBoxR + other.mass.pow(
                            2
                    ))) / (other.hitBoxR + hitBoxR + mass.pow(2) + other.mass.pow(2)),
                    (position.y * (other.hitBoxR + mass.pow(2)) + other.position.y * (hitBoxR + other.mass.pow(
                            2
                    ))) / (other.hitBoxR + hitBoxR + mass.pow(2) + other.mass.pow(2))
            )*/
            val mid = PointF(
                (position.x * (other.hR) + other.position.x * (hR)) / (other.hR + hR),
                (position.y * (other.hR)+ other.position.y * (hR)) / (other.hR + hR))

            val vx = position.x - mid.x
            val vy = position.y - mid.y
            val dv = kotlin.math.sqrt(kotlin.math.abs(vx).pow(2) + kotlin.math.abs(vy).pow(2))

            val e = PointF(position.y - other.position.y, other.position.x - position.x)
            val angle = atan(e.y.toDouble() / e.x.toDouble())

            val va = rotateVector(velocity, angle)
            val vb = rotateVector(other.velocity, angle)
            val k = 1f
            val v1 = va.y
            val v2 = vb.y
            val C = (1 + k) * (mass * v1 + other.mass * v2) / (mass + other.mass)
            va.y = C - k * v1
            vb.y = C - k * v2

            velocity = rotateVector(va, -angle)
            other.velocity = rotateVector(vb, -angle)

            position.x = mid.x + vx / dv * hR * 1.001f
            position.y = mid.y + vy / dv * hR * 1.001f
            other.position.x = mid.x - vx / dv * other.hR * 1.001f
            other.position.y = mid.y - vy / dv * other.hR * 1.001f

        }else if(oC.type==2){
            hp-=1
            oC.exists= false
        }

    }
}
