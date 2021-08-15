package com.narendar.letstravel

import android.content.Context
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
//This activity is opened when clicked on the layout of any ride in SearchActivity to show the complete details of the ride.
class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //Declaration and Initialisation of views through intent coming from AdapterSearch.
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
        val sharedPreferences = getSharedPreferences("production", Context.MODE_PRIVATE)
       val bookPassengers = sharedPreferences.getString("bp", null)

        val rating  = findViewById<TextView>(R.id.ratings_details)

        rating.text = intent.getStringExtra("rating")
        date_details.text= intent.getStringExtra("date")
        pickuplocation_details.text= intent.getStringExtra("pickup")
        droplocation_details.text= intent.getStringExtra("destination")
        amount_details.text= intent.getStringExtra("bookfare")
        var passengersBoked = intent.getStringExtra("passengersBooked")

        noOfseatsBooked.text = passengersBoked

        noofpassengersdetails.text = intent.getStringExtra("passengers")

        val y = noofpassengersdetails.text.toString().toInt()   - passengersBoked!!.toInt()

        noOfSeatsAvailable.text = y.toString()

//Initialisation of toolbar and assigning title to it
        var toolbar = findViewById<Toolbar>(R.id.toolbar_details)

        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title="Ride Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        name_details.text = intent.getStringExtra("name")
        var userimg_string =intent.getStringExtra("publisherimage")

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("profile")
        val user = auth.currentUser


        val name1 = findViewById<TextView>(R.id.nameofuser)
        val emailofuser = findViewById<TextView>(R.id.emailofuser)

        val rideID = intent.getStringExtra("rideId")




        var publisherId = intent.getStringExtra("publisherId")
        if(FirebaseAuth.getInstance().currentUser != null){
            if(FirebaseAuth.getInstance().currentUser!!.uid == publisherId) finish()
        }
        var name = intent.getStringExtra("name")
//Code used for moving to ChatActivity through intent
        chat_details.setOnClickListener {
            if(user != null){
            val intent = Intent(this@DetailsActivity, ChatActivity ::class.java)

            intent.putExtra("userId",publisherId)
            intent.putExtra("userName", name)
            startActivity(intent)
            } else startActivity(Intent(this, MobileNumber::class.java))
        }

//Code for moving to AboutPublisher activity when user clicks on publisher profile
        userimg_details.setOnClickListener {
            val intent = Intent(this@DetailsActivity,AboutPublisher ::class.java)

            intent.putExtra("publisherid",publisherId)
            startActivity(intent)
        }



//Following code will be in action when user clicks on the Book Ride button.
        bookride_details.setOnClickListener {

        if(user != null ){
            val userreference = databaseReference?.child(user?.uid!!)
            var bookerimage : String = ""
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


            //Here all the details of person who booked the will be stored into varianles and then passed to saveFireStore function.
            val bookPickuplocation = sharedPreferences.getString("t1", null)
            val bookDroplocation = sharedPreferences.getString("t2", null)
            val btnbookDate = sharedPreferences.getString("bbd", null)

            val bookFare = amount_details.text.toString()
            val publishername = name_details.text.toString()
            val sharepickuplocation=pickuplocation_details.text.toString()
            val sharedroplocation=droplocation_details.text.toString()
            val noofpassengers_details: TextView =findViewById(R.id.noofpassengers_details)

            var x = noofpassengers_details.text.toString().toInt() -  bookPassengers!!.toInt() - passengersBoked!!.toInt()

           // Toast.makeText(this, "1. ${noofpassengers_details.text.toString().toInt()} 2. ${bookPassengers!!.toInt()} 3. ${passengersBoked.toInt()}", Toast.LENGTH_LONG).show()


//Code will check whether seats are available or not.
            //If available then saveFireStore function will be called.

           if(x>=0){
               if (rideID != null) {
                  val passengersbooked= bookPassengers!!.toInt() + passengersBoked!!.toInt()

                   saveFireStore(emailofuser.text.toString(),name1.text.toString(),
                       bookPickuplocation!!, bookDroplocation!!,btnbookDate!! ,bookPassengers!!, user?.uid!!.toString(), bookerimage,bookFare,publishername,sharepickuplocation,sharedroplocation,rideID,noofpassengers_details.text.toString(),passengersbooked.toString(),date_details.text.toString(),publisherId!!,rating.text.toString())
               }
               //Following code is used for two purposes
               //One is to the update the status of ridestatus as booked in that ride in users collection
               //Other one is to send the notification to publisher that a particular person booked your ride.
            updatestatus(rideID,bookPassengers!!.toInt(),passengersBoked.toInt())

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


               val intent = Intent(this@DetailsActivity, BookRidesActivity::class.java)
               startActivity(intent)

               }
            else{
               Toast.makeText(this@DetailsActivity, "Seats Not Available ", Toast.LENGTH_SHORT ).show()
           }


        }else {
            startActivity(Intent(this, MobileNumber::class.java))
        }
        }

        Glide.with(this@DetailsActivity).load(userimg_string).placeholder(R.drawable.profile_image).into(userimg_details)


    }
    //This function will be helpful in creating a new file for that booked ride with all the necessary details in booked collection in FireStore Database.
    fun saveFireStore(emailofuser : String ,bookerName:String, bookpickuplocation: String, bookdroplocation: String,btnbookdate:String ,bookPassengers : String , bookerId : String, bookerimage : String,bookFare: String, publisherName:String,sharePickuplocation: String, shareDroplocation: String,publishedrideId : String,totalPassengers : String,passengersBooked : String,shareDate:String,publisherId:String,ratingofpublisher:String) {
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
        user["ratingofpublisher"]=ratingofpublisher
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
    override fun onRestart(){
        super.onRestart()
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }


}