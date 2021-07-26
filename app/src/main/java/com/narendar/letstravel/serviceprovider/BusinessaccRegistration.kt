package com.narendar.letstravel.serviceprovider

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.narendar.letstravel.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BusinessaccRegistration : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

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
        val view =inflater.inflate(R.layout.fragment_businessprofile, container, false)
        val stationname = view.findViewById<TextView>(R.id.stationname)
        val username = view.findViewById<TextView>(R.id.nameInput)
        val email = view.findViewById<TextView>(R.id.email)
        val location = view.findViewById<TextView>(R.id.location)
        val mobile= view.findViewById<TextView>(R.id.mobile)
        val servicesprovided= view.findViewById<TextView>(R.id.yourservices)
        var updateaccount = view.findViewById<Button>(R.id.update_account)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Business")

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)



        userreference?.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {

                stationname.text = "Station Name - " + snapshot.child("station").value.toString()
                username.text = "User Name - " + snapshot.child("username").value.toString()
                email.text = "E-mail -  " + user?.email
                location.text = "Location - " + snapshot.child("location").value.toString()
                mobile.text = "Mobile Number - " + snapshot.child("mobile").value.toString()
                var stringbuilder= StringBuilder();
                lateinit var selectedservice: BooleanArray
                selectedservice=BooleanArray(6)
                stringbuilder.append("Your services: ")
                stringbuilder.append("\n")

                var k=0; var c=0;
                while(k<6) {
                    selectedservice[k]=snapshot.child("ev-car").child("ev-car-services" + k).exists()
                    if( selectedservice[k])
                        c++;
                    k++;
                }
                if(c>0) {
                    stringbuilder.append("EV CAR SERVICES")
                    stringbuilder.append("\n")
                }
                for (j in selectedservice.indices)
                {
                    if(selectedservice[j])
                    {
                        stringbuilder.append(snapshot.child("ev-car").child("ev-car-services"+j).value.toString())
                        stringbuilder.append(",")
                    }
                }
                stringbuilder.append("\n")
                k=0; c=0;
                while(k<6) {
                    selectedservice[k]=snapshot.child("ev-bike").child("ev-bike-services" + k).exists()
                    if( selectedservice[k])
                        c++;
                    k++;
                }
                if(c>0) {
                    stringbuilder.append("EV BIKE SERVICES")
                    stringbuilder.append("\n")
                }
                for (j in selectedservice.indices)
                {
                    if(selectedservice[j])
                    {
                        stringbuilder.append(snapshot.child("ev-bike").child("ev-bike-services"+j).value.toString())
                        stringbuilder.append(",")
                    }
                }
                stringbuilder.append("\n")
                k=0; c=0;
                while(k<6) {
                    selectedservice[k]=snapshot.child("non-ev-car").child("non-ev-car-services" + k).exists()
                    if( selectedservice[k])
                        c++;
                    k++;
                }
                if(c>0) {
                    stringbuilder.append("NON EV CAR SERVICES")
                    stringbuilder.append("\n")
                }
                for (j in selectedservice.indices)
                {
                    if(selectedservice[j])
                    {
                        stringbuilder.append(snapshot.child("non-ev-car").child("non-ev-car-services"+j).value.toString())
                        stringbuilder.append(",")
                    }
                }
                stringbuilder.append("\n")
                k=0; c=0;
                while(k<6) {
                    selectedservice[k]=snapshot.child("non-ev-bike").child("non-ev-bike-services" + k).exists()
                    if( selectedservice[k])
                        c++;
                    k++;
                }
                if(c>0) {
                    stringbuilder.append("NON EV BIKE SERVICES")
                    stringbuilder.append("\n")
                }
                for (j in selectedservice.indices)
                {
                    if(selectedservice[j])
                    {
                        stringbuilder.append(snapshot.child("non-ev-bike").child("non-ev-bike-services"+j).value.toString())
                        stringbuilder.append(",")
                    }
                }
                servicesprovided.setText(stringbuilder.toString())
            }



            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
        updateaccount.setOnClickListener {
            Toast.makeText(context,"clicked on  Update Account", Toast.LENGTH_SHORT).show()
            val fragment= updateaccountFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, updateaccountFragment())
            transaction?.commit()}

        return view
    }
}


