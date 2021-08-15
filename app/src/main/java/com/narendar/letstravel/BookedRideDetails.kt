package com.narendar.letstravel

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_find.*
//This activity is opened when user clicks on the layout of bookedrides in yourrides fragment.
class BookedRideDetails : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_ride_details)
        //Declaration and Initialisation of views through intent from AdapterBooked.
        val publishername = findViewById<TextView>(R.id.name_bookedRideDetails)
        val pickupBookedRideDetails = findViewById<TextView>(R.id.pickuplocation_bookedRideDetails)
        val dropBookedRideDetails = findViewById<TextView>(R.id.droplocation_bookedRideDetails)
        val offeredBookedRideDetails = findViewById<TextView>(R.id.seatsOffered_bookedRideDetails)
        val bookedseatsBookedRideDetails =
            findViewById<TextView>(R.id.seatsBooked_bookedRideDetails)
        val availableBookedRideDetails =
            findViewById<TextView>(R.id.seatsAvailable_bookedRideDetails)
        val fareBookedRideDetails = findViewById<TextView>(R.id.fareamount_bookedRideDetails)
        val bookedPickup = findViewById<TextView>(R.id.bookedpickuplocation_bookedRideDetails)
        val bookedDrop = findViewById<TextView>(R.id.bookeddroplocation_bookedRideDetails)
        val bookerSeatsBooked = findViewById<TextView>(R.id.seatsBookedbyBooker)
        val cancelBookedRideDetails = findViewById<Button>(R.id.cancelride_bookedRideDetails)
        val chatBookedRideDetails = findViewById<Button>(R.id.chat_bookedRideDetails)
        val datepublishedride = findViewById<TextView>(R.id.date_bookedRideDetails)
        val ratingofpublisher = findViewById<TextView>(R.id.ratings_bookedRideDetails)
        datepublishedride.text = intent.getStringExtra("date")
        ratingofpublisher.text=intent.getStringExtra("ratingofpublisher")
        publishername.text = intent.getStringExtra("publishername")
        pickupBookedRideDetails.text = intent.getStringExtra("pickup")
        dropBookedRideDetails.text = intent.getStringExtra("destination")
        offeredBookedRideDetails.text = intent.getStringExtra("totalseats")
        fareBookedRideDetails.text = intent.getStringExtra("bookfare")
        bookedPickup.text = intent.getStringExtra("bookpickup")
        bookedDrop.text = intent.getStringExtra("bookdrop")
        bookerSeatsBooked.text = intent.getStringExtra("bookpassengers")

        val publishedRideId = intent.getStringExtra("publishedRideId")
        val bookedRideId = intent.getStringExtra("rideId")

        val publisherId =  intent.getStringExtra("publisherId")
        //Code for moving to Chat Activity through intent when passenger wants to communicate with publisher.
        chatBookedRideDetails.setOnClickListener {
            val intent = Intent(this@BookedRideDetails, ChatActivity ::class.java)

            intent.putExtra("userId",publisherId)
            intent.putExtra("userName",publishername.toString())
            startActivity(intent)
        }



        //Code for assigning and updating seats availabity in this page.
        var available : Int
        var booked : Any?
        var total:Any?

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(publishedRideId!!).get().addOnCompleteListener {
                result -> if(result.isSuccessful){ booked = result.result.get("passengersBooked")
            total = result.result.get("sharePassengers")
            available = total.toString().toInt()-booked.toString().toInt()

                bookedseatsBookedRideDetails.text=booked.toString()
                availableBookedRideDetails.text= available.toString()
            }
        }
        //If passenger wants to cancel the ride, following code is helpful.
        cancelBookedRideDetails.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cancel Ride")
            builder.setMessage("Do you want to cancel Ride?")
            builder.setPositiveButton(
                "Cancel"
            ) { dialogInterface, i ->


                var new : Int
                var old : Any?

                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(publishedRideId!!).get().addOnCompleteListener {
                        result -> if(result.isSuccessful){ old = result.result.get("passengersBooked")
                    new = old.toString().toInt()-bookerSeatsBooked.text.toString().toInt()
                    FirebaseFirestore.getInstance().collection("users")
                        .document(publishedRideId).update("passengersBooked",new.toString())

                    if(new <= 0)
                    {
                        FirebaseFirestore.getInstance().collection("users")
                            .document(publishedRideId).update("Status","Not Booked")
                    }}

                }
                FirebaseFirestore.getInstance().collection("booked")
                    .document(bookedRideId!!).delete()
                onBackPressed()
                finish()
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        })

    //Code for visibility of finish button
       var a :Any?
        db.collection("users").document(publishedRideId!!).get().addOnCompleteListener {
                result -> if(result.isSuccessful){ a = result.result.get("ridestarted")
            if(a=="Yes"||a=="Completed") {
                var completeride = findViewById<Button>(R.id.completedRide)
                completeride.visibility = View.VISIBLE

            }

        }
        }

        var bookername = intent.getStringExtra("bookername")
        var bookerId = intent.getStringExtra("bookerId")
        var rideId = intent.getStringExtra("rideId")

        var completeride = findViewById<Button>(R.id.completedRide)
//Following code will leads to review page where passenger wants to rate and review the ride when passenger clicks on finish button.
        completeride.setOnClickListener {

            val intent = Intent(this@BookedRideDetails, ReviewActivity::class.java)

            intent.putExtra("publisherId",publisherId)
            intent.putExtra("bookerId", bookerId)
            intent.putExtra("bookername", bookername)
            intent.putExtra("rideId",rideId)
            intent.putExtra("publishedRideId", publishedRideId)
             startActivity(intent)


                }

            }

        }

