package com.narendar.letstravel

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*

class SharedRidesDetails : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var coPassengersRidesList: ArrayList<CoPassengerRides>
    private lateinit var adapterCoPassengers: AdapterCoPassengers
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_rides_details)
        val dateSharedRideDetails = findViewById<TextView>(R.id.date_shared_ride_details)
        val pickupSharedRideDetails = findViewById<TextView>(R.id.pickuplocation_shared_ride_details)
        val dropSharedRideDetails = findViewById<TextView>(R.id.droplocation_shared_ride_details)
        val offeredSeatsSharedRideDetails = findViewById<TextView>(R.id.seatsOffered_shared_ride_details)
        val bookedseatsSharedRideDetails = findViewById<TextView>(R.id.seatsBooked_shared_ride_details)
        val availableSeatsSharedRideDetails = findViewById<TextView>(R.id.seatsAvailable_shared_ride_details)
        val fareSharedRideDetails = findViewById<TextView>(R.id.fareamount_shared_ride_details)
        val statusSharedRideDetails = findViewById<TextView>(R.id.status_shared_ride_details)
        val ridestart = findViewById<Button>(R.id.ridestart)

        dateSharedRideDetails.text = intent.getStringExtra("date")
        pickupSharedRideDetails.text = intent.getStringExtra("pickup")
        dropSharedRideDetails.text = intent.getStringExtra("destination")
        offeredSeatsSharedRideDetails.text = intent.getStringExtra("passengers")
        bookedseatsSharedRideDetails.text = intent.getStringExtra("passengersBooked")
        fareSharedRideDetails.text = intent.getStringExtra("bookfare")
        statusSharedRideDetails.text = intent.getStringExtra("Status")
        val publishedRideId = intent.getStringExtra("rideId")
        val publisherId = intent.getStringExtra("publisherId")

        val a = offeredSeatsSharedRideDetails.text.toString().toInt()-bookedseatsSharedRideDetails.text.toString().toInt()
        availableSeatsSharedRideDetails.text = a.toString()


        recyclerView = findViewById(R.id.bookerdetails_recyclerview)
        recyclerView.layoutManager= LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        coPassengersRidesList = arrayListOf()
        adapterCoPassengers= AdapterCoPassengers(this@SharedRidesDetails as Context,coPassengersRidesList)
        recyclerView.adapter = adapterCoPassengers


        EventChangeListnerCoPassengers(publishedRideId!!)

        var ridecompleted = findViewById<Button>(R.id.ridecompleted)

        ridestart.setOnClickListener {



                db = FirebaseFirestore.getInstance()
                db.collection("users").document(publishedRideId).update("ridestarted","Yes")
            db.collection("booked").whereEqualTo("publishedRideId", publishedRideId).whereEqualTo("message","")
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(
                        value: QuerySnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if(value!=null){
                            for(dc in value){
                                var docid = dc.id
                                db.collection("booked").document(docid).update("ridestarted","Yes")
                            }
                        }
                    }

                })
                ridestart.visibility = View.GONE
                ridecompleted.visibility= View.VISIBLE

        }

        var isRideStarted = intent.getStringExtra("ridestarted")
        if(isRideStarted=="Yes"||isRideStarted=="Completed")
        {
            ridestart.visibility = View.GONE
            ridecompleted.visibility= View.VISIBLE

        }
        if(isRideStarted=="Completed"){
            ridecompleted.visibility= View.GONE
            ridestart.visibility = View.GONE
        }
        ridecompleted.setOnClickListener {

            db = FirebaseFirestore.getInstance()
            db.collection("users").document(publishedRideId).update("ridestarted","Completed")

            if(statusSharedRideDetails.text=="Booked"){
                var databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(publisherId!!)
                databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val ridespublished = snapshot.child("noofridespublished").value.toString().toInt()
                        val updatedrides = ridespublished+1
                        val rides =
                            mapOf<String, String>("noofridespublished" to updatedrides.toString())
                        databaseReference!!.updateChildren(rides)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
                db.collection("booked").whereEqualTo("publishedRideId", publishedRideId).whereEqualTo("message","")
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if(value!=null){
                                for(dc in value){
                                    var docid = dc.id
                                    db.collection("booked").document(docid).update("ridestarted","Completed")
                                }
                            }
                        }

                    })
                ridecompleted.visibility=View.GONE
                val intent= Intent(this@SharedRidesDetails,review_publisher::class.java)
                intent.putExtra("publishedrideid",publishedRideId)
                startActivity(intent)

            }
            ridecompleted.visibility=View.GONE
        }


    }
    private fun EventChangeListnerCoPassengers(s : String) {


        db = FirebaseFirestore.getInstance()
        db.collection("booked").whereEqualTo("publishedRideId", s).whereEqualTo("message","")
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

                            coPassengersRidesList.add(dc.document.toObject(CoPassengerRides::class.java))
                        }

                    }
                    adapterCoPassengers.notifyDataSetChanged()
                }
            })

    }

}