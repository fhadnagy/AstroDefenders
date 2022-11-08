package com.fonagyma.astro


import android.app.Activity
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : Activity() {

    private lateinit var liveDrawingView: LiveDrawingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        liveDrawingView = LiveDrawingView(this, size.x, size.y)

        setContentView(liveDrawingView)
    }

    override fun onResume() {
        super.onResume()

        liveDrawingView.resume()
    }

    override fun onPause() {
        super.onPause()

        liveDrawingView.pause()
    }
}