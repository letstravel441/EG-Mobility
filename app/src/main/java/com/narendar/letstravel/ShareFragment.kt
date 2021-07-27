package com.narendar.letstravel.travelfragments

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.narendar.letstravel.MapsActivity
import com.narendar.letstravel.R
import java.text.SimpleDateFormat
import java.util.*


class ShareFragment : Fragment() {

   lateinit var editText : EditText
    lateinit var textView1: TextView
    lateinit var textView2:TextView
    lateinit var sharepickuplocation : EditText
    lateinit var textView3: TextView
    lateinit var textView4:TextView

    lateinit var sharedroplocation : EditText

   lateinit var publisherimage : String
    lateinit var rating: String
    lateinit var noofridespublished:String
    var shareday = 0
    var sharemonth = 0
    var shareyear = 0
    var sharehour = 0
    var sharemin = 0
    var formate = SimpleDateFormat("EEE ddMMM,yyyy hh:mm a", Locale.US)

    lateinit var name : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_share, container, false)
        // Inflate the layout for this fragment
        sharepickuplocation = view.findViewById<EditText>(R.id.sharepickuplocation)
         sharedroplocation = view.findViewById<EditText>(R.id.sharedroplocation)
        val btnshare = view.findViewById<Button>(R.id.btnshare)
        val btnsharedate = view.findViewById<Button>(R.id.btnsharedate)
        val sharepassengers = view.findViewById<EditText>(R.id.sharepassengers)
        val sharefare = view.findViewById<EditText>(R.id.sharefare)
        editText = view.findViewById<EditText>(R.id.edit_text)
        textView1 = view.findViewById<TextView>(R.id.text_view1)
        textView2 = view.findViewById<TextView>(R.id.text_view2)
        textView3 = view.findViewById<TextView>(R.id.text_view3)
        textView4 = view.findViewById<TextView>(R.id.text_view4)

        val cal: Calendar = Calendar.getInstance()
        val date = formate.format(cal.time)
        btnsharedate.text=date
        btnsharedate.setOnClickListener {
            TimePickerDialog(
                view.context,
                TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay, minute ->
                    sharehour=hourOfDay
                    sharemin=minute
                    cal.set(Calendar.HOUR,hourOfDay)
                    cal.set(Calendar.MINUTE,minute)
                    val update = formate.format(cal.time)
                    btnsharedate.text=update
                },
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                false
            ).show()
            DatePickerDialog(
                view.context  ,
                DatePickerDialog.OnDateSetListener { view: DatePicker?, myear: Int, mmonth: Int, mday: Int ->
                    shareday=mday
                    sharemonth=mmonth
                    shareyear=myear
                    cal.set(Calendar.YEAR,myear)
                    cal.set(Calendar.MONTH,mmonth)
                    cal.set(Calendar.DAY_OF_MONTH,mday)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_WEEK)
            ).show()


        }




        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("profile")

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        val name = view.findViewById<TextView>(R.id.nameofuser)
        val emailofuser = view.findViewById<TextView>(R.id.emailofuser)


        userreference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                name.text =snapshot.child("firstname").value.toString()

             //   lastnameText.text = "Last name - -> " + snapshot.child("lastname").value.toString()
                emailofuser.text =  user?.email
             //   mobile.text = "Mobile Number - > " + snapshot.child("mobileno").value.toString()
                publisherimage = snapshot.child("profileImage").value.toString()
                rating = snapshot.child("totalrating").value.toString()
                noofridespublished=snapshot.child("noofridespublished").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        Places.initialize(requireActivity().getApplicationContext(), "AIzaSyAG5Oh9bHDBoqM3BU1S2V0f-8uuo3ZHliw")
        sharepickuplocation.isFocusable = false

        sharepickuplocation.setOnClickListener {
            val fieldList = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
            val intent = this@ShareFragment.context?.let { it1 ->
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(it1)
            }
            startActivityForResult(intent, 100)
        }

        Places.initialize(requireActivity().getApplicationContext(), "AIzaSyAG5Oh9bHDBoqM3BU1S2V0f-8uuo3ZHliw")
        sharedroplocation.isFocusable = false

        sharedroplocation.setOnClickListener {
            val fieldList = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
            val intent = this@ShareFragment.context?.let { it1 ->
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(it1)
            }
            startActivityForResult(intent, 101)
        }



        btnshare.setOnClickListener {

            if(TextUtils.isEmpty(sharepickuplocation.text.toString())) {
                sharepickuplocation.setError("Please enter Pickup Location ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(sharedroplocation.text.toString())) {
                sharedroplocation.setError("Please enter Drop Location")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(sharepassengers.text.toString())) {
                sharepassengers.setError("Please enter No of Passengers ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(sharefare.text.toString())) {
                sharefare.setError("Please enter Fare/km")
                return@setOnClickListener
            }
            val sharePickuplocation = sharepickuplocation.text.toString()
            val shareDroplocation = sharedroplocation.text.toString()
            val btnshareDate = btnsharedate.text.toString()
            val sharePassengers = sharepassengers.text.toString()
            val shareFare = sharefare.text.toString()
           val passengersbooked : String = "0"

            saveFireStore(emailofuser.text.toString(),name.text.toString(),textView1.text.toString(), textView3.text.toString(),btnshareDate,sharePassengers,shareFare, user?.uid!!.toString(), publisherimage , textView2.text.toString(),textView4.text.toString(),passengersbooked,rating,noofridespublished)

            sharedroplocation!!.text.clear()
            sharepickuplocation!!.text.clear()
            sharefare!!.text.clear()
            sharepassengers!!.text.clear()

        }





        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(
                data!!
            )
            sharepickuplocation.setText(place.address)
            textView1.text = String.format("%s", place.name)
            textView2.text = place.latLng.toString()
        }else  if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(
                data!!
            )
            sharedroplocation.setText(place.address)
            textView3.text = String.format("%s", place.name)
            textView4.text = place.latLng.toString()
        }


        else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(requireActivity().getApplicationContext(), status.statusMessage, Toast.LENGTH_SHORT).show()
        }
    }



    fun saveFireStore(emailofuser : String ,name:String, sharepickuplocation: String, sharedroplocation: String,btnsharedate:String ,sharePassengers : String , sharefare:String,publisherId : String, publisherimage : String,share_pickup_latlng : String,share_drop_latlng : String,passengersbooked: String,rating: String,noofridespublished:String) {
        val db = FirebaseFirestore.getInstance()
        val ride = db.collection("users").document()
        val rideId = ride.id
        val user: MutableMap<String, Any> = HashMap()
        user["publisherimage"]= publisherimage
        user["publisherId"]= publisherId
        user["UserID"]=emailofuser
        user["Name"]=name
        user["sharePickuplocation"] = sharepickuplocation
        user["shareDroplocation"] = sharedroplocation
        user["shareDate"] = btnsharedate
        user["shareFare"] = sharefare
        user["sharePassengers"] = sharePassengers
        user["rideId"] = rideId
        user["totalrating"]=rating
        user["noofridespublished"]=noofridespublished
        user["Status"]="Not Booked"
        user["passengersBooked"] = passengersbooked
        user["sharePickupLatlng"] = share_pickup_latlng
        user["shareDropLatlng"]=share_drop_latlng
        user["ridestarted"] ="No"

        db.collection("users").document(rideId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Ride published", Toast.LENGTH_SHORT ).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Failed to publish ", Toast.LENGTH_SHORT ).show()
            }
    }

}
