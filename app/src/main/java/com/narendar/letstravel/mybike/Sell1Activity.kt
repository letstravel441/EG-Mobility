package com.narendar.letstravel.mybike

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.narendar.letstravel.R
import kotlinx.android.synthetic.main.activity_sell1.*

class Sell1Activity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val brandName :Array<String> = arrayOf("Selected","BMW","Hero","Honda","yamaha","bajaj","Kawasaki","TVS","KTM","Mahindra","Suzuki","Roil Enfield","Vespa","Jawa","Husqvarna","Others")
    val fuelType :Array<String> = arrayOf("Selected","Petrol","electric","Diesel","Others")
    val ownershipStatus :Array<String> = arrayOf("Selected","1st Hand","2nd Hand","3rd Hand","Others")
    val bodyType :Array<String> = arrayOf("Selected","Commuter","Cruiser","Moped","Naked","Offroad","Scooter","Sports","Others")
    val colour :Array<String> = arrayOf("Selected","White","Red","Pink","Purple","Blue","Yellow","Green","Orange","Silver","Black","Others")

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

        val spinner1  =findViewById<Spinner>(R.id.spinnerbrand)
        val spinner2 = findViewById<Spinner>(R.id.spinnerfuelType)
        val spinner3 = findViewById<Spinner>(R.id.spinnerstatus)
        val spinner4 = findViewById<Spinner>(R.id.spinnerbodyType)
        val spinner5 = findViewById<Spinner>(R.id.spinnercolour)




        spinner1.onItemSelectedListener = this
        val ad1: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,brandName)

        ad1.setDropDownViewResource(

            android.R.layout.simple_spinner_dropdown_item)

        spinner1.adapter = ad1


        spinner2.onItemSelectedListener = this
        val ad2: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,fuelType)

        ad2.setDropDownViewResource(

            android.R.layout.simple_spinner_dropdown_item)

        spinner2.adapter = ad2



        spinner3.onItemSelectedListener = this
        val ad3: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,ownershipStatus)

        ad3.setDropDownViewResource(

            android.R.layout.simple_spinner_dropdown_item)

        spinner3.adapter = ad3


        spinner4.onItemSelectedListener = this
        val ad4: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,bodyType)

        ad4.setDropDownViewResource(

            android.R.layout.simple_spinner_dropdown_item)

        spinner4.adapter = ad4


        spinner5.onItemSelectedListener = this
        val ad5: ArrayAdapter<*> = ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item,colour)

        ad5.setDropDownViewResource(

            android.R.layout.simple_spinner_dropdown_item)

        spinner5.adapter = ad5

    }


    private fun productDetails() {

        //val brandName = findViewById<EditText>(R.id.brand)
        val bikeModel = findViewById<EditText>(R.id.model)
        val price = findViewById<EditText>(R.id.Price)
        //var fuelType = findViewById<EditText>(R.id.fuelType)
        //var ownershipStatus = findViewById<EditText>(R.id.status)
        //var bodyType = findViewById<EditText>(R.id.bodyType)
        //var colour = findViewById<EditText>(R.id.colour)
        val sellerLocation = findViewById<EditText>(R.id.sellerLocation)

        val saveButton = findViewById<Button>(R.id.saveButton)


        saveButton.setOnClickListener {
            if ((spinnerbrand.selectedItem == "Selected")) {
                (spinnerbrand.selectedView as TextView).error = "Select a Brand"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(bikeModel.text.toString())) {
                bikeModel.error = "Please enter Bike Model "
                return@setOnClickListener
            } else if ((spinnerfuelType.selectedItem == "Selected")) {
                (spinnerfuelType.selectedView as TextView).error = "Select FuelType"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(price.text.toString())) {
                price.error = "Please enter pice "
                return@setOnClickListener
            } else if ((spinnerstatus.selectedItem == "Selected")) {
                (spinnerstatus.selectedView as TextView).error = "Select Ownership Status"
                return@setOnClickListener
            } else if ((spinnerbodyType.selectedItem == "Selected")) {
                (spinnerbodyType.selectedView as TextView).error = "Select BodyType"
                return@setOnClickListener
            } else if ((spinnercolour.selectedItem == "Selected")) {
                (spinnercolour.selectedView as TextView).error = "Select BodyType"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(sellerLocation.text.toString())) {
                sellerLocation.setError("Please enter the location where bike is being sold.")
                return@setOnClickListener
            }else {

                //me?.child("User uid")?.setValue(currentUser?.uid!!)

                val intent = Intent(this, Sell2::class.java)
                intent.putExtra("brandName", spinnerbrand.selectedItem.toString())
                intent.putExtra("bikeModel", bikeModel.text.toString())
                intent.putExtra("price", price.text.toString())
                intent.putExtra("fuelType", spinnerfuelType.selectedItem.toString())
                intent.putExtra("colour", spinnercolour.selectedItem.toString())
                intent.putExtra("ownershipStatus", spinnerstatus.selectedItem.toString())
                intent.putExtra("bodyType", spinnerbodyType.selectedItem.toString())
                intent.putExtra("sellerLocation", sellerLocation.text.toString())
                startActivity(intent)
                finish()

            }

            bikeModel!!.text.clear()
            // brandName!!.text.clear()
            //  colour!!.text.clear()
            price!!.text.clear()
            // fuelType!!.text.clear()
            // ownershipStatus!!.text.clear()
            //  bodyType!!.text.clear()

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        Toast.makeText(applicationContext,
            brandName[position],
            Toast.LENGTH_LONG)
            .show()
        Toast.makeText(applicationContext,
            fuelType[position],
            Toast.LENGTH_LONG)
            .show()
        Toast.makeText(applicationContext,
            ownershipStatus[position],
            Toast.LENGTH_LONG)
            .show()
        Toast.makeText(applicationContext,
            bodyType[position],
            Toast.LENGTH_LONG)
            .show()

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {}
}