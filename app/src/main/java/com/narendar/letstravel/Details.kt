package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*

var o: Int = 1

class Details : AppCompatActivity() {
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details1)
        var firstname: EditText = findViewById(R.id.firstNameforMobileAuth)
        var lastname: EditText = findViewById(R.id.lastNameforMobileAuth)
        var age: EditText = findViewById(R.id.ageforMobileAuth)
        var gender: EditText = findViewById(R.id.genderforMobileAuth)
        var email: EditText = findViewById(R.id.EmailforMobileAuth)
        var btn: Button = findViewById(R.id.createMobileAuth)
        val storedVerificationId=intent.getStringExtra("stored1")
        val otp =intent.getStringExtra("stored2")
        btn.setOnClickListener {
            if(firstname.text.toString().isEmpty() || lastname.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches() ||
                email.text.toString().isEmpty() || age.text.toString().isEmpty() || gender.text.toString() !in arrayOf("Male", "Female", "Others")) {
                if (firstname.text.toString().isEmpty()) {
                    firstname.error = "Please enter your Name"
                    firstname.requestFocus()
                }
                if (email.text.toString().isEmpty()) {
                    email.error = "Please enter your Email address"
                    email.requestFocus()
                }
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                        email.error = "Please enter a valid Email address"
                        email.requestFocus()
                    }
                }
                if (lastname.text.toString().isEmpty()) {
                    lastname.error = "Please enter the password"
                    lastname.requestFocus()
                }
                if (age.text.toString().isEmpty()) {
                    age.error = "Please enter the password"
                    age.requestFocus()
                }
                if(gender.text.toString() !in arrayOf("Male", "Female", "Others")){
                    gender.error = "Type one among the following: Male, Female, Others (Case-Sensitive)"
                    gender.requestFocus()
                }
            } else{
                if (storedVerificationId != null) {
                    verifyCode(storedVerificationId, otp!!)
                }
            }
        }
    }

    private fun verifyCode(authCode: String, enteredCode:String){
        val credential= PhoneAuthProvider.getCredential(authCode,enteredCode)
        signInWithCredentials(credential)
    }

    private fun signInWithCredentials(credential: PhoneAuthCredential) {
        var firstname: EditText = findViewById(R.id.firstNameforMobileAuth)
        var lastname: EditText = findViewById(R.id.lastNameforMobileAuth)
        var age: EditText = findViewById(R.id.ageforMobileAuth)
        var email: EditText = findViewById(R.id.EmailforMobileAuth)
        val phoneNumber = intent.getStringExtra("stored3")
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    database= FirebaseDatabase.getInstance().getReference("profile")
                    val id: String = FirebaseAuth.getInstance().currentUser!!.uid
                    val user = PhoneUser(age.text.toString(), phoneNumber!!, email.text.toString(), firstname.text.toString(), lastname.text.toString(),id, firstname.text.toString())
                    database.child(id).setValue(user).addOnSuccessListener {
                        finish()
                    }
                }
                else Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
            }
    }
}