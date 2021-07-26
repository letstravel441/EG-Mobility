package com.narendar.letstravel

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AboutBooker : AppCompatActivity() {
    var reviewsList = ArrayList<review>()
    lateinit var  reviewRecyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_booker)


        var toolbar = findViewById<Toolbar>(R.id.toolbar_about_booker)

        setSupportActionBar(toolbar)
        supportActionBar?.title="About CoPassenger"
        reviewRecyclerView=findViewById(R.id.reviewrecyclerview_booker)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        var firstnamebooker = findViewById<TextView>(R.id.bookername_about_booker)
        var bookerage = findViewById<TextView>(R.id.bookerage)
        var gender = findViewById<TextView>(R.id.bookergender_about_booker)
        var userid = intent.getStringExtra("bookerId")

       var auth = FirebaseAuth.getInstance()
       var database = FirebaseDatabase.getInstance()
       var databaseReference = database?.reference!!.child("profile")

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)
       var storage = FirebaseStorage.getInstance()
     var   storageRef = storage.reference

       var firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference =
            FirebaseDatabase.getInstance().getReference("profile").child(userid!!)




        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                firstnamebooker.text = snapshot.child("firstname").value.toString()
                gender.text = snapshot.child("gender").value.toString()
                bookerage.text = snapshot.child("age").value.toString()

              //  context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        var totalrides = findViewById<TextView>(R.id.noofrides_about_booker)
        var totalreviews=findViewById<TextView>(R.id.totalreviews_about_booker)
        var totalfivestars = findViewById<TextView>(R.id.noof5star_booker)
        var totalthreestars = findViewById<TextView>(R.id.noof3star_booker)
        var totalfourstars = findViewById<TextView>(R.id.noof4star_booker)
        var totaltwostars = findViewById<TextView>(R.id.noof2star_booker)
        var totalonestars = findViewById<TextView>(R.id.noof1star_booker)
        var rating = findViewById<TextView>(R.id.rating_about_booker)








        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                totalrides.text = snapshot.child("noofridestaken").value.toString()
                totalreviews.text = snapshot.child("totalreviewsasuser").value.toString()
                totalfivestars.text = snapshot.child("noof5starratingsasuser").value.toString()
                totalthreestars.text = snapshot.child("noof3starratingsasuser").value.toString()
                totalfourstars.text = snapshot.child("noof4starratingsasuser").value.toString()
                totaltwostars.text = snapshot.child("noof2starratingsasuser").value.toString()
                totalonestars.text = snapshot.child("noof1starratingsasuser").value.toString()
                val rating_float = snapshot.child("totalratingasuser").value.toString().toFloat()
                val rating_float_onedecimal = String.format("%.1f",rating_float)
                rating.text= rating_float_onedecimal

                var progressbar1 =  findViewById<ProgressBar>(R.id.progressBar1_booker)
                progressbar1.max=totalreviews.text.toString().toInt()
                val current1 = totalonestars.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar1,"progress",current1).setDuration(500).start()

                var progressbar2 =  findViewById<ProgressBar>(R.id.progressBar2_booker)
                progressbar2.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar2,"progress",totaltwostars.text.toString().toInt()).setDuration(500).start()

                var progressbar3 =  findViewById<ProgressBar>(R.id.progressBar3_booker)
                progressbar3.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar3,"progress",totalthreestars.text.toString().toInt()).setDuration(500).start()

                var progressbar4 =  findViewById<ProgressBar>(R.id.progressBar4_booker)
                progressbar4.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar4,"progress",totalfourstars.text.toString().toInt()).setDuration(500).start()

                var progressbar5 =  findViewById<ProgressBar>(R.id.progressBar5_booker)
                progressbar5.max=totalreviews.text.toString().toInt()
                ObjectAnimator.ofInt(progressbar5,"progress",totalfivestars.text.toString().toInt()).setDuration(500).start()

                //  context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        val data = databaseReference.child("reviewsasuser")


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

                val reviewAdapter = AdapterBookerReviews(this,reviewsList)

                reviewRecyclerView.adapter = reviewAdapter
            }

        })


    }
}