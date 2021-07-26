package com.narendar.letstravel.mybike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.narendar.letstravel.R
import java.util.*
import kotlin.collections.ArrayList

class FilteredProducts : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    var minPrice : String ?= null
    var maxPrice : String ?= null
    var brands : ArrayList<String> ?= null
    var bodyTypes: ArrayList<String> ?= null
    var colours: ArrayList<String> ?= null
    var ageOfBike: ArrayList<Int> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_products)

        //back button
        val actionBar = supportActionBar
        // showing the back button in action bar
        actionBar!!.title = "Filtered Products"
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        //display products in recyclerview
        minPrice = intent.getStringExtra("minPrice")
        maxPrice = intent.getStringExtra("maxPrice")
        brands = intent.getStringArrayListExtra("brands")
        bodyTypes = intent.getStringArrayListExtra("bodyTypes")
        colours = intent.getStringArrayListExtra("colours")
        ageOfBike = intent.getIntegerArrayListExtra("ageOfBike")


        if (brands!!.isEmpty()) {
            brands = arrayListOf("bmw", "hero", "honda", "yamaha", "bajaj", "kawasaki", "tvs", "ktm", "mahindra", "suzuki", "royal enfield", "vespa", "jawa", "husqvarna")
        }

        if (bodyTypes!!.isEmpty()) {
            bodyTypes = arrayListOf("commuter", "cruiser", "moped", "naked", "offroad", "scooter", "sports")
        }

        if (colours!!.isEmpty()) {
            colours = arrayListOf("white", "red", "pink", "purple", "blue", "yellow", "green", "orange", "silver", "black", "brown")
        }

        if (ageOfBike!!.isEmpty()) {
            ageOfBike!!.add(2000)
        }


        val productDisplay = findViewById<RecyclerView>(R.id.filtered_product_display)
        productDisplay?.setHasFixedSize(true)
        productDisplay?.layoutManager = LinearLayoutManager(this)

        val emptyView = findViewById<TextView>(R.id.empty_view)

        val products = arrayListOf<ProductsList>()

        val adapter = ProductsAdapter(this@FilteredProducts, products)

        database = FirebaseDatabase.getInstance().getReference("Products")
        database.keepSynced(true)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    for (h in snapshot.children) {

                        val product = h.getValue(ProductsList::class.java)

                        val price = product?.price!!.toDouble()
                        val brand = product?.brand.toString().lowercase()
                        val bodyType = product?.bodyType.toString().lowercase()
                        val colour = product?.colour.toString().lowercase()
                        val yearOfBuy = product?.yearOfBuy!!.toDouble()
                        val thisYear = Calendar.getInstance().get(Calendar.YEAR)
                        val age = thisYear - yearOfBuy

                        if (price >= minPrice?.toDouble()!! && price <= maxPrice?.toDouble()!! && brand in brands!! && bodyType in bodyTypes!! && colour in colours!! && age <= Collections.max(
                                ageOfBike!!
                            )
                        ) {
                            products.add(product!!)
                            productDisplay?.adapter = adapter
                        }
                    }
                    if (adapter.itemCount == 0) {
                        emptyView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })

    }
}