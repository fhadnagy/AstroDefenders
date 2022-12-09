package com.fonagyma.astro


import android.app.Activity
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : Activity() {

    ///implementing my own custom view
    private lateinit var liveDrawingView: LiveDrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getting the display size in pixels
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)


        ///passing on the screen ratio
        liveDrawingView = LiveDrawingView(this, size.x, size.y)

        setContentView(liveDrawingView)
    }

    //implementing resume functionality
    override fun onResume() {
        super.onResume()

        liveDrawingView.resume()
    }


    //implementing pause functionality
    override fun onPause() {
        super.onPause()

        liveDrawingView.pause()
    }
}