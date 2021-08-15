package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
//this is splash screen
//opens when we open the app and appears for 2 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val startAct = Intent(this@SplashActivity , MainActivity :: class.java)
            startActivity(startAct)
        },2000)


    }

    override fun onPause() {
        finish()
        super.onPause()
    }
}