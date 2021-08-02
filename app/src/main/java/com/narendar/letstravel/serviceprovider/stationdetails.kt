package com.narendar.letstravel.serviceprovider

import android.app.*
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.app.NotificationCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.narendar.letstravel.NotificationData
import com.narendar.letstravel.PushNotification
import com.narendar.letstravel.R
import com.narendar.letstravel.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class stationdetails : AppCompatActivity() {
    var shareday = 0
    var sharemonth = 0
    var shareyear = 0
    var sharehour = 0
    var sharemin = 0
    var formate = SimpleDateFormat("EEE ddMMM,yyyy hh:mm a", Locale.US)
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var topic = ""
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
        val databaseReference = database?.reference!!.child("profile")
        val user = auth.currentUser
        val usid = databaseReference?.child(user?.uid!!)
        val userid=user?.uid
        lateinit var user_name: String
        lateinit var user_email: String
        lateinit var user_mobile: String
        usid?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                user_name =snapshot.child("userName").value.toString()
                user_email=snapshot.child("email").value.toString()
                user_mobile=snapshot.child("mobileno").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



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

            saveFireStore(stationnamedetail.text.toString(),btndate.text.toString(),emaildetail.text.toString(),mobile.text.toString(),location.text.toString(),
                serviceuid as String,userid.toString(),ty.toString(),nameservice.toString(),user_name.toString(),user_email.toString(),user_mobile.toString())
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
            //send notification
            var message: String = "New service booking request"


            var   topic = "/topics/$serviceuid"


            var  auth = FirebaseAuth.getInstance()
            var database = FirebaseDatabase.getInstance()
            var  databaseReference = database?.reference!!.child("profile")

            val user = auth.currentUser
            val userreference = databaseReference?.child(user?.uid!!)


            var firebaseUser = FirebaseAuth.getInstance().currentUser!!

            databaseReference =
                FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser!!.uid)

            var nameofuser : String


            userreference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    nameofuser=   snapshot.child("firstname").value.toString()
                    PushNotification(
                        NotificationData( nameofuser,message),
                        topic).also {
                        sendNotification(it)
                    }

                    //context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



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
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody()!!.string())
            }
        } catch(e: Exception) {

            Log.e("TAG", e.toString())
        }

    }
}