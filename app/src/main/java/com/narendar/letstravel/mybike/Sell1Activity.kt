package com.narendar.letstravel.mybike

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.narendar.letstravel.R

class Sell1Activity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell1)

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        // if(currentuser != null) {
        //   startActivity(Intent(this@Sell1, MainActivity::class.java))
        // finish()
        //}

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Product Details")


        productDetails()
    }


    @SuppressLint("WrongViewCast")
    private fun productDetails() {

        var brandName = findViewById<EditText>(R.id.brand)
        var bikeModel = findViewById<EditText>(R.id.model)
        var price = findViewById<EditText>(R.id.Price)
        var fuelType = findViewById<EditText>(R.id.fuelType)
        var ownershipStatus = findViewById<EditText>(R.id.status)
        var bodyType = findViewById<EditText>(R.id.bodyType)
        var colour = findViewById<EditText>(R.id.colour)
        var sellerLocation = findViewById<EditText>(R.id.sellerLocation)

        var saveButton = findViewById<Button>(R.id.saveButton)


        saveButton.setOnClickListener {
            if (TextUtils.isEmpty(brandName.text.toString())) {
                brandName.setError("Please enter Brand Name ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(bikeModel.text.toString())) {
                bikeModel.error = "Please enter Bike Model "
                return@setOnClickListener
            } else if (TextUtils.isEmpty(bodyType.text.toString())) {
                bodyType.error = "Please enter body type "
                return@setOnClickListener
            } else if (TextUtils.isEmpty(price.text.toString())) {
                price.error = "Please enter pice "
                return@setOnClickListener
            } else if (TextUtils.isEmpty(fuelType.text.toString())) {
                fuelType.setError("Please enter the fuel type")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(ownershipStatus.text.toString())) {
                ownershipStatus.setError("Please enter the ownership status.")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(colour.text.toString())) {
                colour.setError("Please enter the bike colour.")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(sellerLocation.text.toString())) {
                sellerLocation.setError("Please enter the location where bike is being sold.")
                return@setOnClickListener
            }else {

                //me?.child("User uid")?.setValue(currentUser?.uid!!)

                val intent = Intent(this, Sell2Activity::class.java)
                intent.putExtra("brandName", brandName.text.toString())
                intent.putExtra("bikeModel", bikeModel.text.toString())
                intent.putExtra("price", price.text.toString())
                intent.putExtra("fuelType", fuelType.text.toString())
                intent.putExtra("colour", colour.text.toString())
                intent.putExtra("ownershipStatus", ownershipStatus.text.toString())
                intent.putExtra("bodyType", bodyType.text.toString())
                intent.putExtra("sellerLocation", sellerLocation.text.toString())
                startActivity(intent)

            }

            brandName!!.text.clear()
            bikeModel!!.text.clear()
            colour!!.text.clear()
            price!!.text.clear()
            fuelType!!.text.clear()
            ownershipStatus!!.text.clear()
            bodyType!!.text.clear()

        }
    }
}