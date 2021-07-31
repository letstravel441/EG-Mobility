package com.narendar.letstravel.serviceprovider

import android.app.*
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.NotificationCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.narendar.letstravel.R
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class stationdetails : AppCompatActivity() {
    private var mRequestQue: RequestQueue? = null
    private val URL = "https://fcm.googleapis.com/fcm/send"
    var shareday = 0
    var sharemonth = 0
    var shareyear = 0
    var sharehour = 0
    var sharemin = 0
    var formate = SimpleDateFormat("EEE ddMMM,yyyy hh:mm a", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stationdetails)
        val b = intent.extras
        val stationnamedetail=findViewById<TextView>(R.id.stationnamedetail)
        val location=findViewById<TextView>(R.id.location)
        val mobile=findViewById<TextView>(R.id.mobile)
        val btndate = findViewById<Button>(R.id.btndate)
        val emaildetail=findViewById<TextView>(R.id.emaildetail)
        val confirm=findViewById<Button>(R.id.btn_confirm)
        //val cancel=findViewById<Button>(R.id.cancel)
        val cal: Calendar = Calendar.getInstance()
        val date = formate.format(cal.time)
        btndate.text=date
        btndate.setOnClickListener {
            TimePickerDialog(
                this ,
                TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay, minute ->
                    sharehour=hourOfDay
                    sharemin=minute
                    cal.set(Calendar.HOUR,hourOfDay)
                    cal.set(Calendar.MINUTE,minute)
                    val update = formate.format(cal.time)
                    btndate.text=update
                },
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                false
            ).show()
            DatePickerDialog(
                this  ,
                DatePickerDialog.OnDateSetListener { view: DatePicker?, myear: Int, mmonth: Int, mday: Int ->
                    shareday=mday
                    sharemonth=mmonth
                    shareyear=myear
                    cal.set(Calendar.YEAR,myear)
                    cal.set(Calendar.MONTH,mmonth)
                    cal.set(Calendar.DAY_OF_MONTH,mday)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_WEEK)
            ).show()


        }
        stationnamedetail.text= b?.get("name_service") as CharSequence?
        location.text=b?.get("location_service") as CharSequence?
        mobile.text=b?.get("mobile_service") as CharSequence?
        emaildetail.text=b?.get("email_service") as CharSequence?
        val nameservice=b?.get("linkservice") as CharSequence?
        val serviceuid=b?.get("service_uid") as CharSequence?
        val ty=b?.get("type_service") as CharSequence?
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database?.reference!!.child("Business")
        val user = auth.currentUser
        val usid = databaseReference?.child(user?.uid!!)
        val userid=user?.uid
        lateinit var user_name: String
        lateinit var user_email: String
        lateinit var user_mobile: String
        usid?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user_name =snapshot.child("username").value.toString()
                user_email=snapshot.child("email").value.toString()
                user_mobile=snapshot.child("mobile").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        if (intent.hasExtra("service_noti")) {
            val i = Intent(this@stationdetails, ReceiveNotificationActivity::class.java)
            i.putExtra("service_noti", getIntent().getStringExtra("service_noti"))
            i.putExtra("user_name_noti", getIntent().getStringExtra("user_name_noti"))
            i.putExtra("pn_num",getIntent().getStringExtra("pn_num"))
            startActivity(i)
        }
//        else {
//            val i1 = Intent(this, NotifyActivity::class.java)
//        }
        mRequestQue = Volley.newRequestQueue(this)
        FirebaseMessaging.getInstance().subscribeToTopic(serviceuid as String)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "New Notification",
                "Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }


        confirm.setOnClickListener{

            saveFireStore(stationnamedetail.text.toString(),btndate.text.toString(),emaildetail.text.toString(),mobile.text.toString(),location.text.toString(),serviceuid,userid.toString(),ty.toString(),nameservice.toString(),user_name.toString(),user_email.toString(),user_mobile.toString())
            //startActivity(Intent(this@stationdetails,confirmbtn::class.java))
            //finish()
            val notificationManager = applicationContext.getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager
            val intent = Intent(this@stationdetails, stationdetails::class.java)
            //   intent.putExtra("CANCEL BOOKING", true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this@stationdetails, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val builder = NotificationCompat.Builder(
                this@stationdetails, "New Notification"
            )
            builder.setContentTitle("New Notification")
            builder.setContentText("Waiting for the service provider to accept.")
            //builder.setSmallIcon(android.R.drawable.ic_notificationbell)
            builder.setSmallIcon(R.drawable.ic_notificationbell)
            builder.setSound(uri)
            builder.setAutoCancel(true)
            builder.priority = NotificationCompat.PRIORITY_HIGH
            //   builder.addAction(R.drawable.ic_launcher_foreground, "CANCEL BOOKING", pendingIntent)
            notificationManager.notify(1, builder.build())


            // sendNotification();
            // sendNotification
            val mainObj = JSONObject()
            try {
                mainObj.put("to", "/topics/"+serviceuid)
                val notificationObj = JSONObject()
                notificationObj.put("title", "Service Provider")
                notificationObj.put("body", "New service booking request")
                //   val extraData = JSONObject()
                //  extraData.put("user_name", "adcd")
                //  extraData.put("pn_num", "xxxxxx")
                //  extraData.put("service","car_breakdown")
                //  extraData.put("booking_time","1200")
                mainObj.put("notification", notificationObj)
                //  mainObj.put("data", extraData)
                val request: JsonObjectRequest =
                    object : JsonObjectRequest(Request.Method.POST, URL,
                        mainObj,

                        Response.Listener<JSONObject?>() {
                            fun onResponse(response: JSONObject?) {
                                //code here will run on success
                            }
                        }, Response.ErrorListener() {
                            fun onErrorResponse(error: VolleyError?) {
                                //code here will run on error
                            }
                        }
                    ) {
                        override fun getHeaders(): MutableMap<String, String>? {
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

        }
    }

    private fun saveFireStore(stationnamedetail : String,btndate : String,emaildetail:String,mobile:String,location:String,serviceuid:String,userid:String,ty:String,nameservice:String,user_name:String,user_email:String,user_mobile:String) {
        val db= FirebaseFirestore.getInstance()
        val ref= db.collection("service_user").document()
        val refid=ref.id
        val linkservice: MutableMap<String, Any> = HashMap()
        //station_name,status,service_email,service_mobile,service_location,serviceid,userid,type ,linkservice ,serviceDate ,linkservice_id ,message
        linkservice["stationname"]=stationnamedetail
        linkservice["serviceemail"]=emaildetail
        linkservice["servicemobile"]=mobile
        linkservice["servicelocation"]=location
        linkservice["status"]="Pending"
        linkservice["serviceproviderid"]= serviceuid
        linkservice["userid"]= userid
        linkservice["nameservice"]= nameservice
        linkservice["type"]=ty
        linkservice["Datetime"]= btndate as String
        linkservice["bookedserviceid"]=refid
        linkservice["message"]=""
        db.collection("service_user").document(refid)
            .set(linkservice)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Waiting for the servie provider acceptance", Toast.LENGTH_SHORT ).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Failed to send your request please try again",
                    Toast.LENGTH_SHORT).show()
            }
        val ref1= FirebaseFirestore.getInstance().collection("service_provider").document()
        val ref1id=ref1.id
        val linkservice1: MutableMap<String, Any> = HashMap()

        linkservice1["status"]="booked by user"
        linkservice1["username"]=user_name
        linkservice1["usermobile"]=user_mobile
        linkservice1["useremail"]=user_email
        linkservice1["serviceproviderid"]= serviceuid
        linkservice1["userid"]=userid
        linkservice1["nameservice"]= nameservice
        linkservice1["type"]=ty
        linkservice1["Datetime"]= btndate as String
        linkservice1["bookedserviceid"]=ref1id
        linkservice1["message"]=""
       FirebaseFirestore.getInstance().collection("service_provider").document(ref1id)
          .set(linkservice1)
         //   .addOnSuccessListener {
      //          Toast.makeText(applicationContext, "Waiting for the servie provider acceptance", Toast.LENGTH_SHORT ).show()
        //    }
        //    .addOnFailureListener {
        //       Toast.makeText(applicationContext,"Failed to send your request please try again",
         //           Toast.LENGTH_SHORT).show()
         //   }

    }
}