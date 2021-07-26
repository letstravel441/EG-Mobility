package com.narendar.letstravel.mybike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.narendar.letstravel.R
import com.smarteist.autoimageslider.SliderView

class ProductDetails : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        val title = intent.getStringExtra("title")
        val productTitle = findViewById<TextView>(R.id.product_title_2)
        productTitle.text = title
        val price = intent.getStringExtra("price")
        val productPrice = findViewById<TextView>(R.id.product_price_2)
        productPrice.text = price
        //val imagePreview = intent.getStringExtra("imagePreview")
        //val productImageURL = findViewById<ImageView>(R.id.product_photo_2)
        //Glide.with(this).load(imagePreview).into(productImageURL)
        val fromId = intent.getStringExtra("fromId")
        val fromUsername = intent.getStringExtra("fromUsername")


        val brand = intent.getStringExtra("brand")
        val productBrand = findViewById<TextView>(R.id.brand_2)
        productBrand.text = brand
        val fuelType = intent.getStringExtra("fuelType")
        val productFuelType = findViewById<TextView>(R.id.fuelType_2)
        productFuelType.text = fuelType
        val colour = intent.getStringExtra("colour")
        val productColour = findViewById<TextView>(R.id.product_colour)
        productColour.text = colour
        val ownershipStatus = intent.getStringExtra("ownershipStatus")
        val productOwnershipStatus = findViewById<TextView>(R.id.ownershipStatus_2)
        productOwnershipStatus.text = ownershipStatus
        val bodyType = intent.getStringExtra("bodyType")
        val productBodyType = findViewById<TextView>(R.id.bodyType_2)
        productBodyType.text = bodyType
        val sellerLocation = intent.getStringExtra("sellerLocation")
        val productSellerLocation = findViewById<TextView>(R.id.sellerLocation_2)
        productSellerLocation.text = sellerLocation
        val odometerStatus = intent.getStringExtra("odometerStatus")
        val productOdometerStatus = findViewById<TextView>(R.id.odometerStatus_2)
        productOdometerStatus.text = odometerStatus
        val insuranceStatus = intent.getStringExtra("insuranceStatus")
        val productInsuranceStatus = findViewById<TextView>(R.id.insuranceStatus_2)
        productInsuranceStatus.text = insuranceStatus
        val comment = intent.getStringExtra("comment")
        val productComment = findViewById<TextView>(R.id.addAComment_2)
        productComment.text = comment
        val yearOfBuy = intent.getStringExtra("yearOfBuy")
        val productYearOfBuy = findViewById<TextView>(R.id.yearOfBuy_2)
        productYearOfBuy.text = yearOfBuy
        val mileage = intent.getStringExtra("mileage")
        val productMileage = findViewById<TextView>(R.id.mileage_2)
        productMileage.text = mileage


        //image slider

        val productID = intent.getStringExtra("productID")
        val sliderView: SliderView = findViewById(R.id.product_slider)
        val database = FirebaseDatabase.getInstance().reference.child("Products/$productID/images")
        val images = arrayListOf<ImagesList>()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (h in snapshot.children) {
                        val image = h.getValue(ImagesList::class.java)
                        images.add(image!!)
                    }
                    val adapter = ImagesAdapter(this@ProductDetails, images)
                    sliderView.setSliderAdapter(adapter)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        //back button
        val actionBar = getSupportActionBar()
        // showing the back button in action bar
        actionBar!!.title = title
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)


        //message seller
        val messageSeller = findViewById<Button>(R.id.message_seller)
        messageSeller.setOnClickListener {
            if (fromId == FirebaseAuth.getInstance().currentUser?.uid) {
                Toast.makeText(this,"Can't open chat.", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this@ProductDetails, MBChatInboxActivity::class.java)
                intent.putExtra("fromId", fromId)
                intent.putExtra("fromUsername", fromUsername)
                startActivity(intent)
            }
        }

        //share button
        val btnshare = findViewById<Button>(R.id.product_share)
        btnshare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Insert Subject here")
            val productUrl = " https://play.google.com/store/apps/details?id=com.sql.commands.app"
            shareIntent.putExtra(Intent.EXTRA_TEXT, productUrl)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}
