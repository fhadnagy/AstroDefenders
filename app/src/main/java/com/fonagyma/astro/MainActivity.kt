package com.fonagyma.astro

//mit tud a játék?
/**
 * a gombok többnyire dinamikusan resizeolnak
 * az aszteroidák realisztikusan tökéletesen rugalmasan utkoznek
 * dinamikusan állíthatóak az ütközöképes elemek paraméterei, ezáltal upgradelhetőek
 * a különböző paraméterek ingame currencyvel
 * fokozatosan egyre több hp ja van a meteoroknak
 *
 *
 * made by FÓNAGY MÁRTON ÁDÁM
 * DEBRECENI FAZEKAS MIHÁLY GIMNÁZIUM 12A
 * */

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