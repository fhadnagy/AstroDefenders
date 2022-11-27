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
    private var gameTimeMillis :Long = 0
    private var msPassed= System.currentTimeMillis()
    private var prevFrameMillis: Long = 0
    private val millisInSecond: Long = 1000
    private val fontSize: Int = mScreenX /15
    private lateinit var thread: Thread
    private val walls : PointF
    @Volatile
    private var drawing: Boolean = false
    private var paused = false
    private var clickableList = ArrayList<Clickable>()
    private var drawables = ArrayList<GameObject>()
    private var js : Joystick
    private var cnn : Cannon
    private var pseRect = RectF(10f,10f,100f,100f)
    ///var mP= PointF(mScreenX.toFloat()/2,mScreenY.toFloat()/2)
    init {
        clickableList.add(Joystick(PointF(mScreenX*.6f,mScreenY*.7f),RectF(mScreenX*.6f,mScreenY*.7f,mScreenX-5f,mScreenY-5f),context))
        drawables.add(Cannon(PointF(mScreenX*.3f,mScreenY*.9f),context))
        js = clickableList[0] as Joystick
        cnn = drawables[0] as Cannon
        walls = PointF(mScreenX.toFloat(),mScreenY.toFloat())
        drawables.add(Ball(PointF(mScreenX*.5f,mScreenY*.5f),
            context, PointF(1f,0f), walls, 60f, 216f))
        drawables.add(Ball(PointF(mScreenX*.4f,mScreenY*.6f),
            context, PointF(1f,0f), walls, 40f, 64f))
        drawables.add(Ball(PointF(mScreenX*.3f,mScreenY*.7f),
            context, PointF(1f,0f), walls, 30f, 27f))
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
            for(go in drawables){
                go.draw(canvas,paint)
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
            if (prevFrameMillis>0){
                msPassed = frameStartTime-prevFrameMillis
            }else{
                msPassed = 0
            }
            prevFrameMillis = frameStartTime
            gameTimeMillis+=msPassed

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
        if(!paused){
            if (drawables.size>0){
                for(a in 1..drawables.size-2){
                    for(b in a+1..drawables.size-1){
                        (drawables[a] as Ball).collide(drawables[b] as Ball)
                    }
                }
            }
            for(a in 1..drawables.size-2)
            for (cl in clickableList){
                cl.update(msPassed)
            }
            for(go in drawables){
                go.update(msPassed,js.rotation)
            }
            if (gameTimeMillis/500 > drawables.size){
                Log.d("gtms","$gameTimeMillis")

                drawables.add(Ball(PointF(cnn.position.x+cnn.ballStartP.x, cnn.position.y+cnn.ballStartP.y),
                    context, cnn.ballStartV, walls, 20f, 8f))
            }
        }

    }

    fun pause() {
        // Set drawing to false
        // Stopping the thread isn't
        // always instant
        drawing = false
        try {
            // Stop the thread
            thread.join()
            prevFrameMillis=0
        } catch (e: InterruptedException) {
            Log.e("Error:", "joining thread")
        }
        prevFrameMillis=0

    }

    fun resume() {
        drawing = true
        // Initialize the instance of Thread
        thread = Thread(this)
        // Start the thread
        thread.start()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(
        motionEvent: MotionEvent
    ): Boolean {
        if (motionEvent.action and MotionEvent.
            ACTION_MASK ==
            MotionEvent.ACTION_MOVE) {
                if (js.hitBox.contains(motionEvent.x,motionEvent.y))
                    js.onClick(PointF(motionEvent.x,motionEvent.y))
        }
        // Did the user touch the screen
        if (motionEvent.action and MotionEvent.ACTION_MASK ==
            MotionEvent.ACTION_DOWN) {
            for (cl in clickableList) {
                if(cl.hitBox.contains(motionEvent.x,motionEvent.y))
                {
                    cl.onClick(PointF(motionEvent.x,motionEvent.y))
                }
            }
            if(pseRect.contains(motionEvent.x,motionEvent.y)){
                if (paused)
                {
                    paused=false
                    resume()
                }else{
                    paused=true
                    pause()
                }
            }
        }
        return true
    }
}