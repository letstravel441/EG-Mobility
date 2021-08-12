package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.chaos.view.PinView
import com.google.firebase.auth.*
import com.google.firebase.database.*

class VerificationLayout : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_layout)
        val storedVerificationId=intent.getStringExtra("storedVerificationId")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val btn: Button = findViewById(R.id.buttonVerification)
        val otpp: EditText = findViewById(R.id.otp)
        btn.setOnClickListener {
            if(otpp.text.toString().isEmpty()){
                otpp.setError("Please enter OTP")
                otpp.requestFocus()
            }
            else{
                database= FirebaseDatabase.getInstance().getReference("profile")
                database.keepSynced(true)
                database.orderByChild("mobileno").equalTo(phoneNumber!!).addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.value != null){
                            verifyCode(storedVerificationId!!, otpp.text.toString())
                        }
                        else {
                            val intent = Intent(applicationContext, Details::class.java)
                            intent.putExtra("stored1", storedVerificationId )
                            intent.putExtra("stored2", otpp.text.toString())
                            intent.putExtra("stored3", phoneNumber)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        }
    }

    private fun verifyCode(authCode: String, enteredCode:String){
        val credential= PhoneAuthProvider.getCredential(authCode,enteredCode)
        signInWithCredentials(credential)
    }

    private fun signInWithCredentials(credential: PhoneAuthCredential) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
    }

    override fun onRestart() {
        super.onRestart()
        overridePendingTransition(0, 0)
    }


    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}