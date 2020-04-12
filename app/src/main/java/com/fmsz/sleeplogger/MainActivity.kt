package com.fmsz.sleeplogger


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    /**
     * The MainActivity is only responsible for setting the content view that contains the
     * Navigation Host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // force night mode for now xD
//        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
        setContentView(R.layout.activity_main)
    }
}
