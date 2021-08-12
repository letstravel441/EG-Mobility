package com.narendar.letstravel.mybike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.narendar.letstravel.R
import kotlinx.android.synthetic.main.activity_sell1.*
import kotlinx.android.synthetic.main.activity_sell2.*

class Sell2Activity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    val InsuranceStatus: Array<String> = arrayOf("Selected", "Present", "Absent", "Others")

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell2)

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Product Details")

        productDetails()

        val spinner6 = findViewById<Spinner>(R.id.spinnerInsurance)

        spinner6.onItemSelectedListener = this
        val ad6: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, InsuranceStatus)

        ad6.setDropDownViewResource(

            R.layout.spinner_list)


        spinner6.adapter = ad6
    }


    private fun productDetails(){
        //val odometerStatus = findViewById<EditText>(R.id.odometerStatus)
        val odometerStatus = findViewById<EditText>(R.id.odometerStatus)
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
            } else if ((spinnerInsurance.selectedItem == "Selected")) {
                (spinnerInsurance.selectedView as TextView).error = "Select Insurance Status"
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
                intent.putExtra("insuranceStatus", spinnerInsurance.selectedItem.toString())
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
//        Toast.makeText(applicationContext,
//            InsuranceStatus[position],
//            Toast.LENGTH_LONG)
//            .show()
//
//        (spinnerInsurance.selectedView as TextView).setTextColor(0x00000000)

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}

}