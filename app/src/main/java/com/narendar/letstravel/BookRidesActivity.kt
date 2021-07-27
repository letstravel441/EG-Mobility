package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.RelativeLayout

class BookRidesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_rides)
        val relay = findViewById<RelativeLayout>(R.id.RelLay)
        Handler().postDelayed({
            val intent = Intent(this@BookRidesActivity, MainActivity::class.java)
            intent.putExtra("ridebooked","yes" )
            startActivity(intent)

        },2000)

    }

}