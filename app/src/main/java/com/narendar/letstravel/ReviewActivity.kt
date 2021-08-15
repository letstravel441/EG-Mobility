package com.narendar.letstravel

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.system.exitProcess
//this is the feedback page
//opens when clicked on finish button co passengers
class ReviewActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    lateinit var current1 :String
    lateinit var current2 :String
    lateinit var current3 :String
    lateinit var current4 :String
    lateinit var current5 :String
    lateinit var totalreviews: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        var ratingdialog = findViewById<TextView>(R.id.rate_review)
        var review = findViewById<EditText>(R.id.review_review)

        var bookername = intent.getStringExtra("bookername")
        var bookerId = intent.getStringExtra("bookerId")
        var publisherId = intent.getStringExtra("publisherId")
        var rideId = intent.getStringExtra("rideId")
        var publishedRideId = intent.getStringExtra("publishedRideId")
        var rb_ratingBar = findViewById<RatingBar>(R.id.rb_ratingBar)


// code for rating bar
        rb_ratingBar.rating=0f
        rb_ratingBar.stepSize=1f
        rb_ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            ratingdialog.text = rating.toString()
        }

        //horizontal progress bar

        /*var progressbar =  findViewById<ProgressBar>(R.id.progressBar)
        progressbar.max=24

        val current =12
        ObjectAnimator.ofInt(progressbar,"progress",current).setDuration(500).start()*/


        var submit = findViewById<Button>(R.id.submitreview)
// submit button functionality
        submit.setOnClickListener {

            databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(publisherId!!)

            val hashMap:HashMap<String,String> = HashMap()
            hashMap.put("reviewerid",bookerId!!)
            hashMap.put("reviewername",bookername!!)
            hashMap.put("ratingofreviewer",ratingdialog.text.toString())
            hashMap.put("review",review.text.toString())
            hashMap.put("publishedRideId",publishedRideId!!)

// updating ratings and reviews in data base

            databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalreviews =snapshot.child("totalreviews").value.toString().toInt()
                    var current1 =
                        snapshot.child("noof1starratings").value.toString().toInt()
                    var current2 =
                        snapshot.child("noof2starratings").value.toString().toInt()
                    var current3 =
                        snapshot.child("noof3starratings").value.toString().toInt()
                    var current4 =
                        snapshot.child("noof4starratings").value.toString().toInt()
                    var current5 =
                        snapshot.child("noof5starratings").value.toString().toInt()


                    val currenrtreviews = mapOf<String, String>( "totalreviews" to (totalreviews + 1).toString())
                    databaseReference!!.updateChildren(currenrtreviews)

                    if(ratingdialog.text.toString()=="1.0") {

                        val rating1 =
                            mapOf<String, String>("noof1starratings" to (current1 + 1).toString())
                        databaseReference!!.updateChildren(rating1)

                        var current11 = (current1+1).toFloat()
                        var totalrating=(current11 * 1).toFloat() + (current2 * 2 ).toFloat()+( current3 * 3).toFloat() + (current4 * 4).toFloat() + (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalrating" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)

                    }

                    if(ratingdialog.text.toString()=="2.0") {

                        val rating2 =
                            mapOf<String, String>("noof2starratings" to (current2 + 1).toString())
                        databaseReference!!.updateChildren(rating2)

                        var current22 = (current2+1).toFloat()
                        var totalrating=(current1 * 1).toFloat() + (current22 * 2 ).toFloat()+( current3 * 3).toFloat() + (current4 * 4).toFloat()+ (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalrating" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }
                    if(ratingdialog.text.toString()=="3.0") {

                        val rating3 =
                            mapOf<String, String>("noof3starratings" to (current3 + 1).toString())
                        databaseReference!!.updateChildren(rating3)

                        var current33 = (current3+1).toFloat()
                        var totalrating=(current1 * 1).toFloat() + (current2 * 2 ).toFloat()+( current33 * 3).toFloat() + (current4 * 4).toFloat() + (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalrating" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }
                    if(ratingdialog.text.toString()=="4.0") {

                        val rating4 =
                            mapOf<String, String>("noof4starratings" to (current4 + 1).toString())
                        databaseReference!!.updateChildren(rating4)

                        var current44 = (current4+1).toFloat()
                        var totalrating=(current1 * 1).toFloat() + (current2 * 2 ).toFloat()+( current3 * 3).toFloat() + (current44 * 4).toFloat() + (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalrating" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }
                    if(ratingdialog.text.toString()=="5.0") {

                        val rating5 =
                            mapOf<String, String>("noof5starratings" to (current5 + 1).toString())
                        databaseReference!!.updateChildren(rating5)

                        var current55 = (current5+1)
                        var totalrating=(current1 * 1).toFloat() + (current2 * 2 ).toFloat()+( current3 * 3).toFloat() + (current4 * 4).toFloat() + (current55 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalrating" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }





                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


            databaseReference!!.child("reviews").child(rideId!!).setValue(hashMap).addOnCompleteListener(this){
                if (it.isSuccessful){
                    //open home activity


                    Toast.makeText(this@ReviewActivity, "Feedback submitted ", Toast.LENGTH_SHORT ).show()
                    onBackPressed()



                }
                else{
                    Toast.makeText(this@ReviewActivity, "Feedback not submitted ", Toast.LENGTH_SHORT ).show()

                }
            }
        }

    }

    fun calculation ( stars1 : String ,stars2 : String,stars3 : String,stars4 : String,stars5 : String, totalreviewss : String,ratingdialog : TextView )
    {
        current1 = stars1
        current2 = stars2
        current3 = stars3
        current4 = stars4
        current5 = stars5
        totalreviews = totalreviewss


        val currenrtreviews = mapOf<String, String>( "totalreviews" to (totalreviews + 1).toString())
        databaseReference!!.updateChildren(currenrtreviews)

        if(ratingdialog.text.toString()=="1.0") {

            val rating1 =
                mapOf<String, String>("noof1starratings" to (current1 + 1).toString())
            databaseReference!!.updateChildren(rating1)

        }

        if(ratingdialog.text.toString()=="2.0") {

            val rating2 =
                mapOf<String, String>("noof2starratings" to (current2 + 1).toString())
            databaseReference!!.updateChildren(rating2)
        }
        if(ratingdialog.text.toString()=="3.0") {

            val rating3 =
                mapOf<String, String>("noof3starratings" to (current3 + 1).toString())
            databaseReference!!.updateChildren(rating3)
        }
        if(ratingdialog.text.toString()=="4.0") {

            val rating4 =
                mapOf<String, String>("noof4starratings" to (current4 + 1).toString())
            databaseReference!!.updateChildren(rating4)
        }
        if(ratingdialog.text.toString()=="5.0") {

            val rating5 =
                mapOf<String, String>("noof5starratings" to (current5 + 1).toString())
            databaseReference!!.updateChildren(rating5)
        }
    }
}