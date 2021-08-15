package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//This activity is not used
class InvisibleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invisible)

        val intent = Intent(this@InvisibleActivity,SharedRidesDetails::class.java)
        startActivity(intent)
    }
}