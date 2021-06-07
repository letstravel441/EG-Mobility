package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ProfileActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")



        loadProfile()

    }

    private fun loadProfile() {

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        var emailText = findViewById<TextView>(R.id.emailText)
        var firstnameText = findViewById<TextView>(R.id.firstnameText)
        var lastnameText = findViewById<TextView>(R.id.lastnameText)
        var mobileText= findViewById<TextView>(R.id.mobileText)

        emailText.text = "Email ID : "+user?.email

        userreference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                firstnameText.text = "Firstname :"+snapshot.child("firstname").value.toString()
                lastnameText.text = "Last name : "+snapshot.child("lastname").value.toString()
                mobileText.text = "Mobile No : "+snapshot.child("mobileno").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        var logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            finish()
        }
    }
}