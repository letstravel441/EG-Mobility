package com.narendar.letstravel.serviceprovider

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.narendar.letstravel.HomeFragment
import com.narendar.letstravel.R
import kotlinx.android.synthetic.main.fragment_serviceprovider.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ServiceproviderFragment : Fragment(){
    lateinit var textView1: TextView
    lateinit var textView11: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_serviceprovider, container, false)

        var bike_1= view. findViewById<ImageView>(R.id.bike_1)
        val  bike_2         =  view.findViewById<ImageView>(R.id.bike_2)
        val   car_1         =  view.findViewById<ImageView>(R.id.car_1)
        var   car_2         =  view.findViewById<ImageView>(R.id.car_2)
        var  userlocation   = view.findViewById<EditText>(R.id.userlocation)

        textView1 = view.findViewById<TextView>(R.id.text_view1_find)
        textView11 = view.findViewById<TextView>(R.id.text_view11_find)
        Places.initialize(requireActivity().getApplicationContext(), "AIzaSyAG5Oh9bHDBoqM3BU1S2V0f-8uuo3ZHliw")
        userlocation.isFocusable = false

        userlocation.setOnClickListener {
            val fieldList = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
            val intent = this@ServiceproviderFragment.context?.let { it1 ->
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(it1)
            }
            startActivityForResult(intent, 100)
        }
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("profile")
        val user = auth.currentUser
       val usid = databaseReference?.child(user?.uid!!)
        var userid = user?.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")

        bike_1.setOnClickListener {
            Toast.makeText(context,"clicked on  EV BIKE", Toast.LENGTH_SHORT).show()
            val fragment= ev_bike_1Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, ev_bike_1Fragment())?.addToBackStack("k")
            transaction?.commit()


        }

        bike_2.setOnClickListener {
            Toast.makeText(context,"clicked on  NON EV BIKE", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_2Fragment())?.addToBackStack("k")
            transaction?.commit()
        }

        car_1.setOnClickListener {
            Toast.makeText(context,"clicked on  EV Car ", Toast.LENGTH_SHORT).show()
            val fragment=ev_car1Fragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, ev_car1Fragment())?.addToBackStack("k")
            transaction?.commit()
        }

        car_2.setOnClickListener {
            Toast.makeText(context,"clicked on  NON EV Car", Toast.LENGTH_SHORT).show()
            val fragment= Car_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, Car_2Fragment())?.addToBackStack("k")
            transaction?.commit()
        }





        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(
                data!!
            )
            userlocation.setText(place.address)
            textView1.text = String.format("%s", place.name)
            textView11.text = place.latLng.toString()
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database?.reference!!.child("profile")
            val user = auth.currentUser
            val usid = databaseReference?.child(user?.uid!!)
            usid?.child("local").setValue(place.address)
            usid?.child("latlng").setValue(place.latLng)
        }


        else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(requireActivity().getApplicationContext(), status.statusMessage, Toast.LENGTH_SHORT).show()
        }
    }
}