package com.narendar.letstravel.mybike

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.narendar.letstravel.R

class SellFragment : Fragment() {

    lateinit var Dbref: DatabaseReference

    lateinit var userRecyclerView: RecyclerView
    lateinit var products: ArrayList<ProductsList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.narendar.letstravel.R.layout.fragment_sell, container, false)

        //sell new bike
        val addProduct = view.findViewById<RelativeLayout>(com.narendar.letstravel.R.id.add_product)
        addProduct.setOnClickListener {
            val intent = Intent(context, Sell1Activity::class.java)
            // start your next activity
            startActivity(intent)
        }

        //show previous bikes
//        userRecyclerView = view.findViewById(R.id.my_bikes)
//        userRecyclerView.layoutManager = LinearLayoutManager(context)
//        userRecyclerView.setHasFixedSize(true)
//
//        productsList = arrayListOf<ProductsList>()
//        getProductsData()
//
//        return view
//    }
//
//    private fun getProductsData() {
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        val ref = FirebaseDatabase.getInstance().getReference("Products")
//
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if (snapshot.exists()) {
//
//                    for (userSnapshot in snapshot.children) {
//                        val product = userSnapshot.getValue(ProductsList::class.java)
//                        val user = product?.userID
//                        if (user == currentUser?.uid!!) {
//                            productsList.add(product!!)
//                        }
//                    }
//                    val adapter = MyActivityAdapter(context!!, productsList)
//                    userRecyclerView.adapter = adapter
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                //TODO("Not yet implemented")
//            }
//
//        })
//
//    }
//}

        userRecyclerView = view.findViewById(com.narendar.letstravel.R.id.my_bikes)
        userRecyclerView.layoutManager = LinearLayoutManager(activity)
        userRecyclerView.setHasFixedSize(true)

        products = arrayListOf<ProductsList>()
        getProductsData()


        return view
    }


    private fun getProductsData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        Dbref = FirebaseDatabase.getInstance().getReference("Products")



        Dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // val user_id: String = DataSnapshot.child("userID").getValue(String::class.java)
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {


                        val product = userSnapshot.getValue(ProductsList::class.java)
                        val user = product?.userID
                        if (user == currentUser?.uid!!) {
                            products.add(product!!)

                        }

                        userRecyclerView.adapter = MyAdapter(context!!, products)

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}