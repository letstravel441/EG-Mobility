package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
// this is the registration page
// opens when clicked on register button

class RegistrationActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
// declaring data base variables
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()
    }
// function for registering the details
    private fun register() {

        var registerButton= findViewById<Button>(R.id.registerButton)
        var firstnameInput = findViewById<EditText>(R.id.firstnameInput)
        var lastnameInput = findViewById<EditText>(R.id.lastnameInput)
        var usernameInput = findViewById<EditText>(R.id.usernameInput)
        var mobileInput = findViewById<EditText>(R.id.mobileInput)
        var passwordInput = findViewById<EditText>(R.id.passwordInput)
        var confirmPassword = findViewById<EditText>(R.id.confirmPasswordInput)
        var gender = findViewById<EditText>(R.id.gender)
        var age = findViewById<EditText>(R.id.editTextAge)
// functionality for register button
        registerButton.setOnClickListener {

           if(TextUtils.isEmpty(firstnameInput.text.toString())) {
                firstnameInput.setError("Please enter first name ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(lastnameInput.text.toString())) {
                lastnameInput.setError("Please enter last name ")
                return@setOnClickListener
            }else   if(TextUtils.isEmpty(gender.text.toString())) {
               gender.setError("Please enter gender ")
               return@setOnClickListener
           } else if(TextUtils.isEmpty(age.text.toString())) {
                age.setError("Please enter Age ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(usernameInput.text.toString())) {
                usernameInput.setError("Please enter user name ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(passwordInput.text.toString())) {
                passwordInput.setError("Please enter password ")
                return@setOnClickListener
            }else  if (!passwordInput.text.toString().equals(confirmPassword.text.toString())){
                Toast.makeText(applicationContext,"password not match",Toast.LENGTH_SHORT).show()
                passwordInput.setError("Please enter password ")
                return@setOnClickListener
            }

// pre defining the branches which are required in the other files
            auth.createUserWithEmailAndPassword(usernameInput.text.toString(), passwordInput.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))

                        val user: FirebaseUser? = auth.currentUser
                        val userId:String = currentUser!!.uid
                        databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(userId)

                        val hashMap:HashMap<String,String> = HashMap()
                        hashMap.put("userId",userId)
                        hashMap.put("profileImage","")
                        hashMap.put("noofridespublished","0")
                        hashMap.put("noof5starratings","0")
                        hashMap.put("noof4starratings","0")
                        hashMap.put("noof3starratings","0")
                        hashMap.put("noof2starratings","0")
                        hashMap.put("noof1starratings","0")
                        hashMap.put("totalrating","0")
                        hashMap.put("totalreviews","0")

                        hashMap.put("noofridestaken","0")
                        hashMap.put("noof5starratingsasuser","0")
                        hashMap.put("noof4starratingsasuser","0")
                        hashMap.put("noof3starratingsasuser","0")
                        hashMap.put("noof2starratingsasuser","0")
                        hashMap.put("noof1starratingsasuser","0")
                        hashMap.put("totalratingasuser","0")
                        hashMap.put("totalreviewsasuser","0")

                        hashMap.put("firstname",firstnameInput.text.toString())
                        hashMap.put("lastname",lastnameInput.text.toString())
                        hashMap.put("userName",firstnameInput.text.toString())
                        hashMap.put("gender",gender.text.toString())
                        hashMap.put("mobileno",mobileInput.text.toString())
                        hashMap.put("email",usernameInput.text.toString())
                        hashMap.put("age",age.text.toString())
                        hashMap.put("businessAccount","No")



                        databaseReference!!.setValue(hashMap).addOnCompleteListener(this){
                            if (it.isSuccessful){
                                //open home activity
                            // clearing the textfiels after clicking register button
                                firstnameInput.setText("")
                                usernameInput.setText("")
                                lastnameInput.setText("")
                                age.setText("")
                                gender.setText("")
                                mobileInput.setText("")



                            }
                        }


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