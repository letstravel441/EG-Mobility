package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class MobileNumber : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_number)
        auth = FirebaseAuth.getInstance()
        val currentUser= auth.currentUser
        if(currentUser !=null) finish()
        val btn: Button= findViewById(R.id.buttonMobileAuth)
        val et: EditText= findViewById(R.id.editTextMobileNumberAuth)
        val ccp: CountryCodePicker = findViewById(R.id.ccp)
        val num: String = "+" + ccp.selectedCountryCode.toString()
        Log.d("Cat", "1")
        btn.setOnClickListener{
            if(et.text.isEmpty()) {
                et.error = "Please enter your Mobile Number"
                et.requestFocus()
            }
            else {
                if (!mobilevalidate(et.text.toString())) {
                    et.error = "Please enter a valid Mobile Number"
                    et.requestFocus()
                }
                else{
                    sendVerificationCode(num + et.text.toString())
                   // et.setText(number+ et.text.toString())
                }
            }
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("Cat", "onVerificationCompleted")
                //finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("Cat", "onCodeSent")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext, VerificationLayout::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                intent.putExtra("phoneNumber", num + et.text.toString())
                startActivity(intent)
            }
        }
        Log.d("Cat", "2")
    }
    override fun onRestart() {
        super.onRestart()
        auth= FirebaseAuth.getInstance()
        if(auth.currentUser !=null) finish()
        overridePendingTransition(0, 0)
    }
    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }




    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun mobilevalidate(s: String): Boolean{
        var p = Pattern.compile("[1-9][0-9]{9}")
        var m = p.matcher(s)
        return m.matches()
    }
}