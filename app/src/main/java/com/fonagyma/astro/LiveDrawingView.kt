package com.fonagyma.astro


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

@SuppressLint("ViewConstructor")
class LiveDrawingView(context: Context, mScreenX : Int, mScreenY: Int): SurfaceView(context) , Runnable{
    private val debugging = true
    private lateinit var canvas: Canvas
    private val paint: Paint = Paint()
    private var fps: Long = 0
    private val millisInSecond: Long = 1000
    private val fontSize: Int = mScreenX / 20
    private val fontMargin: Int = mScreenX / 75
    private lateinit var thread: Thread
    @Volatile
    private var drawing: Boolean = false
    private var paused = true
    private var clickableList = ArrayList<Clickable>()

    var mP= PointF(mScreenX.toFloat()/2,mScreenY.toFloat()/2)
    init {


    }

    private fun draw(){
        if (holder.surface.isValid){
            // Lock the canvas (graphics memory) ready to draw
            canvas = holder.lockCanvas()
            // Fill the screen with a solid color
            canvas.drawColor(Color.argb(255, 25, 15, 0))
            // Choose a color to paint with
            paint.color = Color.argb(255, 25, 255, 25)
            // Choose the font size
            paint.textSize = fontSize.toFloat()
            // Draw the particle systems
            for (cl in clickableList)
            {
                cl.draw(canvas,paint)
            }


            if (debugging) {
                printDebuggingText()
            }

            // Display the drawing on screen
            // unlockCanvasAndPost is a
            // function of SurfaceHolder
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun printDebuggingText() {
        val debugSize = fontSize / 2
        val debugStart = 150
        paint.color = Color.argb(255,25,255,25)
        paint.textSize = debugSize.toFloat()
        canvas.drawText("fps: $fps",
            10f, (debugStart + debugSize).toFloat(), paint)

    }

    override fun run() {
        // The drawing Boolean gives us finer control
        // rather than just relying on the calls to run
        // drawing must be true AND
        // the thread running for the main
        // loop to execute
        while (drawing) {
            // What time is it now at the
            // start of the loop?
            val frameStartTime = System.currentTimeMillis()
            // Provided the app isn't paused
            // call the update function
            if (!paused) {
                update()
            }
            // The movement has been handled
            // we can draw the scene.
            draw()
            // How long did this frame/loop take?
            // Store the answer in timeThisFrame
            val timeThisFrame = System.currentTimeMillis() - frameStartTime
            // Make sure timeThisFrame is
            // at least 1 millisecond
            // because accidentally dividing
            // by zero crashes the app
            if (timeThisFrame > 0) {
                // Store the current frame rate in fps
                // ready to pass to the update functions of
                // of our particles in the next frame/loop
                fps = millisInSecond / timeThisFrame
            }
        }
    }

    private fun update() {
        // Update the particles
        //updates

    }

    fun pause() {
        // Set drawing to false
        // Stopping the thread isn't
        // always instant
        drawing = false
        try {
            // Stop the thread
            thread.join()
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }
    }

    fun resume() {
        drawing = true
        // Initialize the instance of Thread
        thread = Thread(this)
        // Start the thread
        thread.start()
    }

    override fun onTouchEvent(
        motionEvent: MotionEvent
    ): Boolean {
        if (motionEvent.action and MotionEvent.
            ACTION_MASK ==
            MotionEvent.ACTION_MOVE) {
            for (cl in clickableList)
            {

                    cl.onClick(PointF(motionEvent.x,motionEvent.y))

            }
        }
        // Did the user touch the screen
        if (motionEvent.action and MotionEvent.ACTION_MASK ==
            MotionEvent.ACTION_DOWN) {
            var free = true
            for (cl in clickableList) {
                var js = cl as Joystick
                val d= sqrt(abs(js.midP.x-motionEvent.x).pow(2)+ abs(js.midP.y-motionEvent.y).pow(2))
                if (d<js.padRadius*2)
                {
                    cl.onClick(PointF(motionEvent.x,motionEvent.y))
                    free = false
                    break
                }
            }
            if (free){
                clickableList.add(Joystick(PointF(motionEvent.x,motionEvent.y),RectF(motionEvent.x-150f,motionEvent.y-150f,motionEvent.x+150f,motionEvent.y+150f)))
            }

        }
        return true
    }
}