package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
//This activity is used for Logging into the app.
class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
//If user is already logged in, then that user navigates to Home screen directly.
        val currentuser = auth.currentUser
        if(currentuser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        login()

    }
//This function will be called when the person clicks on the login button.
    private fun login() {

        var loggedin = findViewById<Button>(R.id.loggedin)
        var usernameInput = findViewById<EditText>(R.id.usernameInput)
        var passwordInput = findViewById<EditText>(R.id.passwordInput)
        loggedin.setOnClickListener {

            if(TextUtils.isEmpty(usernameInput.text.toString())){
                usernameInput.setError("Please enter username")
                return@setOnClickListener
            }
            else if(TextUtils.isEmpty(passwordInput.text.toString())){
                usernameInput.setError("Please enter password")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(usernameInput.text.toString(), passwordInput.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed, please try again! ", Toast.LENGTH_LONG).show()
                    }
                }

        }
//Following 3 lines of code will make us move to Registration page if he has not registered.
        var registerText = findViewById<TextView>(R.id.registerText)
        registerText.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))

        }
    }
}
