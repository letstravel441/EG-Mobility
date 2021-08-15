package com.narendar.letstravel
//This activity is opened when a passenger clicks on publisher profile.This shows details of publisher like rating , reviews etc.,.
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class AboutPublisher : AppCompatActivity() {
    //Declaration of arrayList of reviews and recyclerview
    var reviewsList = ArrayList<review>()
    lateinit var  reviewRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_publisher)
        //Initialisation of toolbar and assigning title to it
        var toolbar = findViewById<Toolbar>(R.id.toolbar_about_publisher)

        setSupportActionBar(toolbar)
        supportActionBar?.title="About Publisher"
        //Initialisation of recycler view.
        reviewRecyclerView=findViewById(R.id.reviewrecyclerview)
           reviewRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        //Declaration and initialisation of other views through intent from AdapterCoPassengers.
        var publishername = findViewById<TextView>(R.id.publishername_about_publisher)
        var publisherage = findViewById<TextView>(R.id.publisherage)
        var gender = findViewById<TextView>(R.id.publishergender_about_publisher)
        var totalrides = findViewById<TextView>(R.id.noofrides_about_publisher)
        var totalreviews=findViewById<TextView>(R.id.totalreviews_about_publisher)
        var totalfivestars = findViewById<TextView>(R.id.noof5star)
        var totalthreestars = findViewById<TextView>(R.id.noof3star)
        var totalfourstars = findViewById<TextView>(R.id.noof4star)
        var totaltwostars = findViewById<TextView>(R.id.noof2star)
        var totalonestars = findViewById<TextView>(R.id.noof1star)
        var rating = findViewById<TextView>(R.id.rating_about_publisher)



        var userid = intent.getStringExtra("publisherid")
        //Declaration of database and assigning values to the above declared variables
        var auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance()
        var databaseReference = database?.reference!!.child("profile")

        val user = auth.currentUser
     //   val userreference = databaseReference?.child(user?.uid!!)
        var storage = FirebaseStorage.getInstance()
        var   storageRef = storage.reference

 //       var firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference =
            FirebaseDatabase.getInstance().getReference("profile").child(userid!!)




        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                publishername.text = snapshot.child("firstname").value.toString()
                gender.text = snapshot.child("gender").value.toString()
                publisherage.text = snapshot.child("age").value.toString()
                totalrides.text = snapshot.child("noofridespublished").value.toString()
                totalreviews.text = snapshot.child("totalreviews").value.toString()
                 totalfivestars.text = snapshot.child("noof5starratings").value.toString()
                 totalthreestars.text = snapshot.child("noof3starratings").value.toString()
                 totalfourstars.text = snapshot.child("noof4starratings").value.toString()
                 totaltwostars.text = snapshot.child("noof2starratings").value.toString()
                 totalonestars.text = snapshot.child("noof1starratings").value.toString()
                 val rating_float = snapshot.child("totalrating").value.toString().toFloat()
                val rating_float_onedecimal = String.format("%.1f",rating_float)
                rating.text= rating_float_onedecimal

                var progressbar1 =  findViewById<ProgressBar>(R.id.progressBar1)
                progressbar1.max=totalreviews.text.toString().toInt()
                val current1 = totalonestars.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar1,"progress",current1).setDuration(500).start()

                var progressbar2 =  findViewById<ProgressBar>(R.id.progressBar2)
                progressbar2.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar2,"progress",totaltwostars.text.toString().toInt()).setDuration(500).start()

                var progressbar3 =  findViewById<ProgressBar>(R.id.progressBar3)
                progressbar3.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar3,"progress",totalthreestars.text.toString().toInt()).setDuration(500).start()

                var progressbar4 =  findViewById<ProgressBar>(R.id.progressBar4)
                progressbar4.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar4,"progress",totalfourstars.text.toString().toInt()).setDuration(500).start()

                var progressbar5 =  findViewById<ProgressBar>(R.id.progressBar5)
                progressbar5.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar5,"progress",totalfivestars.text.toString().toInt()).setDuration(500).start()

            //  context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
//Here reviews of a publisher are fetched from database and stored in a reviewsList and passed to AdapterBookerReviews for displaying on the mobile.

        val data = databaseReference.child("reviews")


        data.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                // Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                reviewsList.clear()
                //val currentUser = snapshot.getValue(User::class.java)



                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(review::class.java)

                    reviewsList.add(user!!)

                }

                val reviewAdapter = ReviewAdapter(this,reviewsList)

                reviewRecyclerView.adapter = reviewAdapter
            }

        })
    }

}