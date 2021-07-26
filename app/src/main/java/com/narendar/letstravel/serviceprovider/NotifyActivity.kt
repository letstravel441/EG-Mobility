package com.narendar.letstravel.serviceprovider

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessaging
import com.narendar.letstravel.MainActivity
import com.narendar.letstravel.R
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class NotifyActivity : AppCompatActivity() {
    private var mRequestQue: RequestQueue? = null
    private val URL = "https://fcm.googleapis.com/fcm/send"

    //  TextView textView;
    override fun onCreate(savedInstanceState: Bundle?) {
        val btnyes: Button
        mRequestQue = Volley.newRequestQueue(this)
        FirebaseMessaging.getInstance().subscribeToTopic("news")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        btnyes = findViewById(R.id.btn_yes)
        val b = intent.extras
        val serviceuid=b?.get("suid") as CharSequence?
        //val ref= FirebaseDatabase.getInstance().getReference("links")
        //ref.push().setValue(serviceuid)
        btnyes.setOnClickListener { //sending notification
            val mainObj = JSONObject()
            try {
                mainObj.put("to", "/topics/$serviceuid")
                val notificationObj = JSONObject()
                notificationObj.put("title", "Service Provider")
                notificationObj.put("body", "Requested service has been cancelled")
                mainObj.put("notification", notificationObj)
                val request: JsonObjectRequest =
                    object : JsonObjectRequest(
                        Method.POST, URL,
                        mainObj,
                        Response.Listener {
                            //code here will run on success
                        },
                        Response.ErrorListener {
                            //code here will run on error
                        }
                    ) {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val header: MutableMap<String, String> =
                                HashMap()
                            header["content-type"] = "application/json"
                            header["authorization"] =
                                "key=AAAAqTgcW8Y:APA91bGaeM8hM5D4UL_wqQIhKQt2cB9dk6R7C_Ban_ATf_rribh3EmkPZ3moLtMmJ7NYzAGlhFJv0FeCHPTahuaLi9rrhUfs3GiD9hKj_p7P3fL_sAfgQ9TAcGwDmO4gqdpn-7-Pym7e"
                            return header
                        }
                    }
                mRequestQue?.add(request)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            //alertdialogbox
            val builder = AlertDialog.Builder(this@NotifyActivity)
            builder.setMessage("Your service booking has been cancelled")
            builder.setPositiveButton(
                "ok"
            ) { dialog, which ->
                Toast.makeText(this@NotifyActivity, "Done", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@NotifyActivity, MainActivity::class.java)
                startActivity(intent)
            }
            builder.create()
            builder.show()
        }
    }
}