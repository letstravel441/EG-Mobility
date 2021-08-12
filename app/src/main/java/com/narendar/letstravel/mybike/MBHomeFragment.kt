package com.narendar.letstravel.mybike

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.narendar.letstravel.R

class MBHomeFragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(com.narendar.letstravel.R.layout.fragment_m_b_home, container, false)


        //display products in recyclerview
        val productDisplay = view?.findViewById<RecyclerView>(com.narendar.letstravel.R.id.product_display)
        productDisplay?.setHasFixedSize(true)
        productDisplay?.layoutManager = LinearLayoutManager(context)

        val products = arrayListOf<ProductsList>()

        database = FirebaseDatabase.getInstance().getReference("Products")
        database.keepSynced(true)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {

                    for (h in snapshot.children) {

                        val product = h.getValue(ProductsList::class.java)
                        products.add(product!!)
                    }
                    val adapter = ProductsAdapter(context!!, products)
                    productDisplay?.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })

        val searchText = view.findViewById<EditText>(com.narendar.letstravel.R.id.search_bar)

        searchText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                val queryProducts = database.orderByChild("titleSort").startAt(s.toString().lowercase()).endAt(s.toString().lowercase() + "\uf8ff")

                queryProducts.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        products.clear()
                        if (snapshot.exists()) {
                            for (h in snapshot.children) {
                                val product = h.getValue(ProductsList::class.java)
                                products.add(product!!)
                            }
                            val adapter = ProductsAdapter(context!!, products)
                            productDisplay?.adapter = adapter
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }

            override fun afterTextChanged(s: Editable?) {
                //TODO("Not yet implemented")
            }

        })
        val filter = view.findViewById<ImageView>(R.id.filter)
        filter.setOnClickListener {

            val intent = Intent(context, FilterActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}