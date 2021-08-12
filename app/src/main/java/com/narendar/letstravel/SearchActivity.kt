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
import kotlin.math.*

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRidesList: ArrayList<SharedRides>
    private lateinit var adapterSearch: AdapterSearch
    private lateinit var db: FirebaseFirestore
    lateinit var startPos : String
    lateinit var endPos: String
    lateinit var bp: String

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
        toolbar.setNavigationOnClickListener { finish() }


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
                    if(value !=null){
//                    for (dc: DocumentChange in value?.documentChanges!!) {
//                        if (dc.type == DocumentChange.Type.ADDED) {
//
//                            sharedRidesList.add(dc.document.toObject(SharedRides::class.java))
//                        }
//
//                    }
                        val snapshotList: List<DocumentSnapshot> = value.documents
                        for(snapshot in snapshotList) {
                            val laloStart:String = snapshot.get("sharePickupLatlng") as String
                            val laloEnd:String  = snapshot.get("shareDropLatlng") as String
                            val separate1 = laloStart.split("(",  ",",  ")").map{it.trim()}
                            val separate2 = laloEnd.split("(",  ",",  ")").map{it.trim()}
                            if(distancify(separate1[1].toDouble(), separate1[2].toDouble()) && snapshot.get("publisherId") != FirebaseAuth.getInstance().uid) sharedRidesList.add(snapshot.toObject(SharedRides::class.java)!!)
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

    fun distancify(a: Double, b: Double): Boolean{
        startPos = intent.getStringExtra("start").toString()
        endPos = intent.getStringExtra("end").toString()
        val separate1 = startPos.split("(",  ",",  ")").map{it.trim()}
        val separate2 = endPos.split("(",  ",",  ")").map{it.trim()}
        var c= separate1[1].toDouble()
        var d= separate1[2].toDouble()
        var a1: Double= a * Math.PI /180
        var b1: Double= b * Math.PI /180
        c = c * Math.PI / 180
        d = d * Math.PI /180
        var dlon = d-b1
        var dlat = c-a1
        var e = sin(dlat/2) * sin(dlat/2) + cos(a1)*cos(c)*sin(dlon/2)*sin(dlon/2)
        var f  = 2 * atan2(sqrt(e), sqrt(1-e))
//        Log.d("Cat", a.toString())
//        Log.d("Cat", b.toString())
//        Log.d("Cat", separate1[1])
//        Log.d("Cat", separate1[2])
//        Log.d("Cat", "-----")
//        Log.d("Cat", a1.toString())
//        Log.d("Cat", b1.toString())
//        Log.d("Cat", c.toString())
//        Log.d("Cat", d.toString())
//        Log.d("Cat", (f).toString())
//        Log.d("Cat", (f*6371).toString())
//        Log.d("Cat", "-----------------")
       return f*6371 <= 7
    }

    override fun onRestart(){
        super.onRestart()
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

}