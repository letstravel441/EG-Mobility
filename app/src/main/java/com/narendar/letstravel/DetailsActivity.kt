package com.narendar.letstravel

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.narendar.letstravel.travelfragments.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailsActivity : AppCompatActivity() {
    lateinit var bookerimage : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val name_details: TextView = findViewById(R.id.name_details)
        val date_details: TextView = findViewById(R.id.date_details)
        val pickuplocation_details: TextView = findViewById(R.id.pickuplocation_details)
        val droplocation_details: TextView = findViewById(R.id.droplocation_details)
        val noofpassengersdetails: TextView =findViewById(R.id.noofpassengers_details)
        val amount_details: TextView = findViewById(R.id.amount_details)
        val bookride_details: Button = findViewById(R.id.bookride_details)
        val chat_details: Button = findViewById(R.id.chat_details)
        val userimg_details : ImageView = findViewById(R.id.userimg_details)
        var noOfSeatsAvailable : TextView = findViewById(R.id.noofseatsAvailable)
        var noOfseatsBooked : TextView = findViewById(R.id.noofseatsbooked_details)
       var bookPassengers = bookpassengers.text.toString()



        date_details.text= intent.getStringExtra("date")
        pickuplocation_details.text= intent.getStringExtra("pickup")
        droplocation_details.text= intent.getStringExtra("destination")
        amount_details.text= intent.getStringExtra("bookfare")
        var passengersBoked = intent.getStringExtra("passengersBooked")

        noOfseatsBooked.text = passengersBoked

        noofpassengersdetails.text = intent.getStringExtra("passengers")

        val y = noofpassengersdetails.text.toString().toInt()   - passengersBoked!!.toInt()

        noOfSeatsAvailable.text = y.toString()


        var toolbar = findViewById<Toolbar>(R.id.toolbar_details)




        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title="Ride Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        name_details.text = intent.getStringExtra("name")
        var userimg_string =intent.getStringExtra("publisherimage")

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("profile")
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        val name1 = findViewById<TextView>(R.id.nameofuser)
        val emailofuser = findViewById<TextView>(R.id.emailofuser)

        val rideID = intent.getStringExtra("rideId")


        userreference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                name1.text =snapshot.child("firstname").value.toString()

                //   lastnameText.text = "Last name - -> " + snapshot.child("lastname").value.toString()
                emailofuser.text =  user?.email
                //   mobile.text = "Mobile Number - > " + snapshot.child("mobileno").value.toString()
                bookerimage = snapshot.child("profileImage").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        var publisherId = intent.getStringExtra("publisherId")
        var name = intent.getStringExtra("name")

        chat_details.setOnClickListener {
            val intent = Intent(this@DetailsActivity, ChatActivity ::class.java)

            intent.putExtra("userId",publisherId)
            intent.putExtra("userName", name)
            startActivity(intent)
        }


        userimg_details.setOnClickListener {
            val intent = Intent(this@DetailsActivity,AboutPublisher ::class.java)

            intent.putExtra("publisherid",publisherId)
            startActivity(intent)
        }


        bookride_details.setOnClickListener {



            val bookPickuplocation = textView1.text.toString()
            val bookDroplocation = textView2.text.toString()
            val btnbookDate = btnbookdate.text.toString()

            val bookFare = amount_details.text.toString()
            val publishername = name_details.text.toString()
            val sharepickuplocation=pickuplocation_details.text.toString()
            val sharedroplocation=droplocation_details.text.toString()
            val noofpassengers_details: TextView =findViewById(R.id.noofpassengers_details)


            var x = noofpassengers_details.text.toString().toInt() -  bookPassengers.toInt() - passengersBoked!!.toInt()




           if(x>=0){
               if (rideID != null) {
                  val passengersbooked=bookPassengers.toInt() + passengersBoked!!.toInt()

                   saveFireStore(emailofuser.text.toString(),name1.text.toString(),
                       bookPickuplocation, bookDroplocation,btnbookDate,bookPassengers, user?.uid!!.toString(), bookerimage,bookFare,publishername,sharepickuplocation,sharedroplocation,rideID,noofpassengers_details.text.toString(),passengersbooked.toString(),date_details.text.toString(),publisherId!!)
               }
            updatestatus(rideID,bookPassengers.toInt(),passengersBoked.toInt())

             // notification


                   var message: String = "Ride booked"






                    var   topic = "/topics/$publisherId"


                       var  auth = FirebaseAuth.getInstance()
                       var database = FirebaseDatabase.getInstance()
                       var  databaseReference = database?.reference!!.child("profile")

                       val user = auth.currentUser
                       val userreference = databaseReference?.child(user?.uid!!)


                      var firebaseUser = FirebaseAuth.getInstance().currentUser!!

                       databaseReference =
                           FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser!!.uid)

                       var nameofuser : String


                       userreference?.addListenerForSingleValueEvent(object : ValueEventListener {
                           override fun onDataChange(snapshot: DataSnapshot) {

                               nameofuser=   snapshot.child("firstname").value.toString()
                               PushNotification(NotificationData( nameofuser,message),
                                   topic).also {
                                   sendNotification(it)
                               }

                               //context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
                           }

                           override fun onCancelled(error: DatabaseError) {
                               TODO("Not yet implemented")
                           }
                       })




                       /*    PushNotification(NotificationData( userName!!,message),
                                 topic).also {
                                 sendNotification(it)
                             }*/










             // end

               val intent = Intent(this@DetailsActivity, BookRidesActivity::class.java)
               startActivity(intent)

               }
            else{
               Toast.makeText(this@DetailsActivity, "Seats Not Available ", Toast.LENGTH_SHORT ).show()
           }


        }

        Glide.with(this@DetailsActivity).load(userimg_string).placeholder(R.drawable.profile_image).into(userimg_details)







    }
    fun saveFireStore(emailofuser : String ,bookerName:String, bookpickuplocation: String, bookdroplocation: String,btnbookdate:String ,bookPassengers : String , bookerId : String, bookerimage : String,bookFare: String, publisherName:String,sharePickuplocation: String, shareDroplocation: String,publishedrideId : String,totalPassengers : String,passengersBooked : String,shareDate:String,publisherId:String) {
        val db = FirebaseFirestore.getInstance()
        val ride = db.collection("booked").document()
        val rideId = ride.id
        val user: MutableMap<String, Any> = HashMap()
        user["bookerImage"]= bookerimage
        user["bookerId"]= bookerId
        user["BookerUserID"]=emailofuser
        user["BookerName"]=bookerName
        user["bookPickuplocation"] = bookpickuplocation
        user["bookDroplocation"] = bookdroplocation
        user["bookDate"] = btnbookdate
        user["bookPassengers"] = bookPassengers
        user["rideId"] = rideId
        user["Fare"]= bookFare
        user["publisherName"]=publisherName
        user["sharePickuplocation"]=sharePickuplocation
        user["shareDroplocation"]=shareDroplocation
        user["publishedRideId"]=publishedrideId
        user["totalseats"]=totalPassengers
        user["passengersBooked"]=passengersBooked
        user["message"]=""
        user["shareDate"]=shareDate
        user["publisherId"]=publisherId
        user["ridestarted"] = ""

        db.collection("booked").document(rideId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this@DetailsActivity, "Ride booked", Toast.LENGTH_SHORT ).show()
            }
            .addOnFailureListener{
                Toast.makeText(this@DetailsActivity, "Booking Failed ", Toast.LENGTH_SHORT ).show()
            }
    }
    fun updatestatus(rideId : String?,bookpassengers : Int,passengersbooked : Int){

        val c = bookpassengers + passengersbooked

        if (rideId != null) {

            FirebaseFirestore.getInstance().collection("users")
                .document(rideId).update("Status","Booked","passengersBooked","$c")

        }
    }


    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {

            Log.e("TAG", e.toString())
        }

    }


}