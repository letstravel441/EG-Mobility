package com.narendar.letstravel.mybike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.narendar.letstravel.R

class Sell2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell2)

        //val odometerStatus = findViewById<EditText>(R.id.odometerStatus)
        val odometerStatus = findViewById<EditText>(R.id.odometerStatus)
        val insuranceStatus = findViewById<EditText>(R.id.insuranceStatus)
        val addAComment = findViewById<EditText>(R.id.addAComment)
        val yearOfBuy = findViewById<EditText>(R.id.year_of_buy)
        val mileage = findViewById<EditText>(R.id.mileage)

        val brandName = intent.getStringExtra("brandName")
        val bikeModel = intent.getStringExtra("bikeModel")
        val price = intent.getStringExtra("price")
        val fuelType = intent.getStringExtra("fuelType")
        val colour= intent.getStringExtra("colour")
        val ownershipStatus = intent.getStringExtra("ownershipStatus")
        val bodyType= intent.getStringExtra("bodyType")
        val sellerLocation = intent.getStringExtra("sellerLocation")

        val saveButton = findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {

            if (TextUtils.isEmpty(odometerStatus.text.toString())) {
                odometerStatus.setError("Please enter Odometer Status ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(insuranceStatus.text.toString())) {
                insuranceStatus.error = "Please enter Insurance Status "
                return@setOnClickListener
            } else if (TextUtils.isEmpty(addAComment.text.toString())) {
                addAComment.error = "Please add any comments. In case of no comments, add NA."
                return@setOnClickListener
            } else if (TextUtils.isEmpty(yearOfBuy.text.toString())) {
                yearOfBuy.error = "Please enter year of purchase of bike."
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mileage.text.toString())) {
                mileage.error = "Please enter average mileage of bike."
                return@setOnClickListener
            } else {


                val intent = Intent(this, Sell3Activity::class.java)

                intent.putExtra("odometerStatus", odometerStatus.text.toString())
                intent.putExtra("insuranceStatus", insuranceStatus.text.toString())
                intent.putExtra("addAComment", addAComment.text.toString())
                intent.putExtra("yearOfBuy", yearOfBuy.text.toString())
                intent.putExtra("mileage", mileage.text.toString())

                intent.putExtra("brandName", brandName)
                intent.putExtra("bikeModel", bikeModel)
                intent.putExtra("price", price)
                intent.putExtra("fuelType", fuelType)
                intent.putExtra("colour", colour)
                intent.putExtra("ownershipStatus", ownershipStatus)
                intent.putExtra("bodyType", bodyType)
                intent.putExtra("sellerLocation", sellerLocation)

                startActivity(intent)
            }
        }
    }
}