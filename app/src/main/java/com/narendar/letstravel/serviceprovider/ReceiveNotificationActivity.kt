package com.narendar.letstravel.serviceprovider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.narendar.letstravel.R

class ReceiveNotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_notification)
        val serviceTV = findViewById<TextView>(R.id.service)
        val user_nameTV = findViewById<TextView>(R.id.user_name)
        val pn_numTV = findViewById<TextView>(R.id.pn_num)
        if (intent.hasExtra("service")) {
            val service = intent.getStringExtra("service")
            val user_name = intent.getStringExtra("user_name")
            val pn_num=intent.getStringExtra("pn_num")
            serviceTV.text = service
            user_nameTV.text = user_name
            pn_numTV.text=pn_num
        }
    }
}