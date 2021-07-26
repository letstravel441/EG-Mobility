package com.narendar.letstravel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class review_publisher : AppCompatActivity() {
    lateinit var reviewPublisherRecyclerview: RecyclerView
    private lateinit var coPassengersReviewList: ArrayList<CoPassengerRides>
    private lateinit var adapterCoPassengerReview: ReviewPublisherAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_publisher)
        reviewPublisherRecyclerview = findViewById(R.id.Recyclerview_review_publisher)
        reviewPublisherRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        coPassengersReviewList = arrayListOf()
        adapterCoPassengerReview= ReviewPublisherAdapter(this@review_publisher as Context,coPassengersReviewList)
        reviewPublisherRecyclerview.adapter = adapterCoPassengerReview
        val s= intent.getStringExtra("publishedrideid")
        CopassengerReview(s.toString())
    }

    fun CopassengerReview(s: String){
        val db = FirebaseFirestore.getInstance()
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

                            coPassengersReviewList.add(dc.document.toObject(CoPassengerRides::class.java))
                        }

                    }
                    adapterCoPassengerReview.notifyDataSetChanged()
                }
            })
    }
}