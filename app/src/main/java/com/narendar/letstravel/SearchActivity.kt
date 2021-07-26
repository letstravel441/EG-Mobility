package com.narendar.letstravel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import java.util.HashMap

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRidesList: ArrayList<SharedRides>
    private lateinit var adapterSearch: AdapterSearch
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        recyclerView = findViewById(R.id.searchrecyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this )
        recyclerView.setHasFixedSize(true)
        sharedRidesList = arrayListOf()
        adapterSearch = AdapterSearch(this@SearchActivity as Context ,sharedRidesList)
        recyclerView.adapter = adapterSearch


        var toolbar = findViewById<Toolbar>(R.id.toolbar_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        EventChangeListner()


    }


    private fun EventChangeListner() {

        db = FirebaseFirestore.getInstance()
        db.collection("users")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("FireStore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {

                            sharedRidesList.add(dc.document.toObject(SharedRides::class.java))
                        }

                    }
                    adapterSearch.notifyDataSetChanged()
                }
            })

    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


}