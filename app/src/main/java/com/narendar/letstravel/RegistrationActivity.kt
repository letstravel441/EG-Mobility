package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegistrationActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()
    }

    private fun register() {

        var registerButton= findViewById<Button>(R.id.registerButton)
        var firstnameInput = findViewById<EditText>(R.id.firstnameInput)
        var lastnameInput = findViewById<EditText>(R.id.lastnameInput)
        var usernameInput = findViewById<EditText>(R.id.usernameInput)
        var mobileInput = findViewById<EditText>(R.id.mobileInput)
        var passwordInput = findViewById<EditText>(R.id.passwordInput)
        var gender = findViewById<EditText>(R.id.gender)

        registerButton.setOnClickListener {

            if(TextUtils.isEmpty(gender.text.toString())) {
                gender.setError("Please enter gender ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(firstnameInput.text.toString())) {
                firstnameInput.setError("Please enter first name ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(lastnameInput.text.toString())) {
                lastnameInput.setError("Please enter last name ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(usernameInput.text.toString())) {
                usernameInput.setError("Please enter user name ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(passwordInput.text.toString())) {
                passwordInput.setError("Please enter password ")
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(usernameInput.text.toString(), passwordInput.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                        currentUSerDb?.child("firstname")?.setValue(firstnameInput.text.toString())
                        currentUSerDb?.child("lastname")?.setValue(lastnameInput.text.toString())
                        currentUSerDb?.child("gender")?.setValue(gender.text.toString())
                        currentUSerDb?.child("mobileno")?.setValue(mobileInput.text.toString())

                        Toast.makeText(this@RegistrationActivity, "Registration Success. ", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                        finish()


                    } else {
                        Toast.makeText(this@RegistrationActivity, "Registration failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}