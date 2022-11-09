package com.fonagyma.astro


import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView

class LiveDrawingView(context: Context, mScreenX : Int, mScreenY: Int): SurfaceView(context) , Runnable{

    // Are we debugging?
    private val debugging = true
    // These objects are needed to do the drawing
    private lateinit var canvas: Canvas
    private val paint: Paint = Paint()
    // How many frames per second did we get?
    private var fps: Long = 0
    // The number of milliseconds in a second
    private val millisInSecond: Long = 1000
    // How big will the text be?
    // Font is 5% (1/20th) of screen width
    // Margin is 1.5% (1/75th) of screen width
    private val fontSize: Int = mScreenX / 20
    private val fontMargin: Int = mScreenX / 75
    // The particle systems will be declared here later

    // Here is the Thread and two control variables
    private lateinit var thread: Thread
    // This volatile variable can be accessed
    // from inside and outside the thread
    @Volatile
    private var drawing: Boolean = false
    private var paused = true

    // These will be used to make simple buttons
    private var resetButton: RectF
    private var togglePauseButton: RectF

    private var system : ObjectSystem
    var mP= PointF(mScreenX.toFloat()/2,mScreenY.toFloat()/2)

    init {
        // Initialize the two buttons
        resetButton = RectF(mScreenX.toFloat()-150f, 50f, mScreenX.toFloat()-150f+100f, 50+100f)
        togglePauseButton = RectF(mScreenX.toFloat()-150f, 200f, mScreenX.toFloat()-150f+100f, 200+100f)
        system = ObjectSystem(mP)

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
            system.draw(canvas,paint)

            // Draw the buttons
            paint.color = Color.argb(255, 25, 255, 25)
            canvas.drawRect(resetButton, paint)
            canvas.drawRect(togglePauseButton, paint)
            paint.color = Color.argb(255, 255, 10, 25)
            canvas.drawRect(mP.x-10f,mP.y-10f,mP.x+10f,mP.y+10f,paint)
            paint.color = Color.argb(255, 25, 255, 25)
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
        canvas.drawText("No. of objects: ${system.gameObjects.size}",
            10f, (fontMargin + debugStart +
                    debugSize * 2).toFloat(), paint)
        /*canvas.drawText("Particles: ${nextSystem *
                particlesPerSystem}",
            10f, (fontMargin + debugStart
                    + debugSize * 3).toFloat(), paint)*/
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

            system.update(fps)


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
            if (resetButton.contains(motionEvent.x,
                    motionEvent.y)) {
                // Clear the screen of all particles

            } else if (togglePauseButton.contains(motionEvent.x,
                    motionEvent.y)) {

            }else if (!paused){
                system.addObject(
                    PointF(motionEvent.x,
                        motionEvent.y)
                )
            }
        }
        // Did the user touch the screen
        if (motionEvent.action and MotionEvent.ACTION_MASK ==
            MotionEvent.ACTION_DOWN) {
            // User pressed the screen so let's
            // see if it was in the reset button
            if (resetButton.contains(motionEvent.x,
                    motionEvent.y)) {
                // Clear the screen of all particles
                system.clear= true
            } else if (togglePauseButton.contains(motionEvent.x,
                    motionEvent.y)) {
                paused = !paused
            }else{
                system.addObject(
                    PointF(motionEvent.x,
                        motionEvent.y)
                )
            }

        }
        return true
    }
}