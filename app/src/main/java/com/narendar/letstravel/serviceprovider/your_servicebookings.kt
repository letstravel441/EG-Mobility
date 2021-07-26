package com.narendar.letstravel.serviceprovider

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.*
import com.narendar.letstravel.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [your_servicebookings.newInstance] factory method to
 * create an instance of this fragment.
 */
class your_servicebookings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerViewuser: RecyclerView
    private lateinit var mybookingslist: ArrayList<mybookings>
    private lateinit var adaptermybookings: Adaptermybookings
    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerViewsp: RecyclerView
    private lateinit var myserviceslist: ArrayList<myservices>
    private lateinit var adaptermyservices: Adaptermyservices

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
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_your_servicebookings, container, false)
        recyclerViewuser = view.findViewById(R.id.mybookings)
        recyclerViewuser.layoutManager= LinearLayoutManager(context)
        recyclerViewuser.setHasFixedSize(true)
        mybookingslist = arrayListOf()
        adaptermybookings = context?.let { Adaptermybookings(it,mybookingslist) }!!
        recyclerViewuser.adapter = adaptermybookings

        recyclerViewsp = view.findViewById(R.id.myservices)
        recyclerViewsp.layoutManager= LinearLayoutManager(context)
        recyclerViewsp.setHasFixedSize(true)
        myserviceslist = arrayListOf()
        adaptermyservices = context?.let { Adaptermyservices(it,myserviceslist) }!!
        recyclerViewsp.adapter = adaptermyservices

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("Business")
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        var  emailofuser = view.findViewById<TextView>(R.id.emailofuserid)
        userreference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                emailofuser.text =  user?.uid
                EventChangeListner_services(emailofuser.text.toString())
                EventChangeListner_bookings(emailofuser.text.toString())
                //   mobile.text = "Mobile Number - > " + snapshot.child("mobileno").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return view
    }
    private fun EventChangeListner_bookings(s : String) {


        db = FirebaseFirestore.getInstance()
        db.collection("service_user").whereEqualTo("userid", s)
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

                            mybookingslist.add(dc.document.toObject(mybookings::class.java))
                        }

                    }
                    adaptermybookings.notifyDataSetChanged()
                }
            })

    }
    private fun EventChangeListner_services(s : String) {


        db = FirebaseFirestore.getInstance()
        db.collection("service_provider").whereEqualTo("serviceproviderid", s)
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

                            myserviceslist.add(dc.document.toObject(myservices::class.java))
                        }

                    }
                    adaptermyservices.notifyDataSetChanged()
                }
            })

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment your_servicebookings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            your_servicebookings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}