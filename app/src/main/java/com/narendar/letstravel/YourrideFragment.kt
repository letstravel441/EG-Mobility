package com.narendar.letstravel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class YourrideFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null


    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedRidesList: ArrayList<SharedRides>
    private lateinit var adapterShare: AdapterShare

    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerView_booked: RecyclerView
    private lateinit var bookedRidesList: ArrayList<BookedRides>
    private lateinit var adapterBooked: AdapterBooked

    private lateinit var recyclerView_completed_booked: RecyclerView
    private lateinit var completedBookedRidesList: ArrayList<BookedRides>
    private lateinit var adapterCompletedBooked: AdapterCompletedBookedRides

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_yourride, container, false)

        recyclerView = view.findViewById(R.id.sharerecyclerview)
        recyclerView.layoutManager=LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        sharedRidesList = arrayListOf()
        adapterShare = context?.let { AdapterShare(it,sharedRidesList) }!!
        recyclerView.adapter = adapterShare

        recyclerView_booked = view.findViewById(R.id.bookedRides_recyclerview)
        recyclerView_booked.layoutManager=LinearLayoutManager(context)
        recyclerView_booked.setHasFixedSize(true)
        bookedRidesList = arrayListOf()
        adapterBooked = context?.let { AdapterBooked(it,bookedRidesList) }!!
        recyclerView_booked.adapter = adapterBooked

        recyclerView_completed_booked = view.findViewById(R.id.bookedRides_completed_recyclerview)
        recyclerView_completed_booked.layoutManager=LinearLayoutManager(context)
        recyclerView_completed_booked.setHasFixedSize(true)
        completedBookedRidesList = arrayListOf()
        adapterCompletedBooked = context?.let { AdapterCompletedBookedRides(it,completedBookedRidesList) }!!
        recyclerView_completed_booked.adapter = adapterCompletedBooked


        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("profile")
        val user = auth.currentUser


       var  emailofuser = view.findViewById<TextView>(R.id.emailofuserid)


        if(user != null){

        val userreference = databaseReference?.child(user?.uid!!)
        userreference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                emailofuser.text =  user?.email
                EventChangeListner(emailofuser.text.toString())
                EventChangeListner_booked(emailofuser.text.toString())
                EventChangeListner_completed_booked(emailofuser.text.toString())
                //   mobile.text = "Mobile Number - > " + snapshot.child("mobileno").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        }

        val s = emailofuser.text.toString()


        return view
    }

    private fun getUserData() {
        TODO("Not yet implemented")
    }

    private fun EventChangeListner(s : String) {


                db = FirebaseFirestore.getInstance()
                db.collection("users").whereEqualTo("UserID", s).whereNotEqualTo("ridestarted","Completed")
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
                            adapterShare.notifyDataSetChanged()
                        }
                    })

    }
    private fun EventChangeListner_booked(s : String) {


        db = FirebaseFirestore.getInstance()
        db.collection("booked").whereEqualTo("BookerUserID", s).whereNotEqualTo("ridestarted","Completed")
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

                            bookedRidesList.add(dc.document.toObject(BookedRides::class.java))
                        }

                    }
                    adapterBooked.notifyDataSetChanged()
                }
            })

    }

    private fun EventChangeListner_completed_booked(s: String) {


        db = FirebaseFirestore.getInstance()
        db.collection("booked").whereEqualTo("ridestarted", "Completed").whereEqualTo("BookerUserID",s)
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

                            completedBookedRidesList.add(dc.document.toObject(BookedRides::class.java))
                        }

                    }
                    adapterCompletedBooked.notifyDataSetChanged()
                }
            })

    }


    companion object {

        fun newInstance(param1: String, param2: String) =
            YourrideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}


