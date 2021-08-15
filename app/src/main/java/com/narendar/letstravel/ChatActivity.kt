package com.narendar.letstravel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList
//This activity is opened when chat button is clicked which shows the messages between those particular users.
class ChatActivity : AppCompatActivity() {


    lateinit var imgProfile : CircleImageView
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    var topic = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.narendar.letstravel.R.layout.activity_chat)
        //Declaration and Initialisation of recycler view which is helpful in displaying the messages between those users.
        var chatRecyclerView = findViewById<RecyclerView>(com.narendar.letstravel.R.id.chatRecyclerView)

        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)


        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")


        var imgBack = findViewById<ImageView>(com.narendar.letstravel.R.id.imgBack)
        var tvUserName = findViewById<TextView>(com.narendar.letstravel.R.id.tvUserName)
        imgProfile = findViewById(com.narendar.letstravel.R.id.imgProfile)
        var btnSendMessage = findViewById<ImageButton>(com.narendar.letstravel.R.id.btnSendMessage)
        var etMessage = findViewById<EditText>(com.narendar.letstravel.R.id.etMessage)



        imgBack.setOnClickListener {
            onBackPressed()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("profile").child(userId!!)




        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {


                val user = snapshot.getValue(User::class.java)
                tvUserName.text = user!!.userName

                if (user.profileImage == "") {
                    imgProfile.setImageResource(com.narendar.letstravel.R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity).load(user.profileImage).into(imgProfile)
                }
            }
        })
        //Following code will be in action when user clicks on send button
        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)


               etMessage.setText("")
                topic = "/topics/$userId"


              var  auth = FirebaseAuth.getInstance()
               var database = FirebaseDatabase.getInstance()
              var  databaseReference = database?.reference!!.child("profile")

                val user = auth.currentUser
                val userreference = databaseReference?.child(user?.uid!!)


                firebaseUser = FirebaseAuth.getInstance().currentUser!!

                databaseReference =
                    FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser!!.uid)

                var nameofuser : String


                userreference?.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        nameofuser=   snapshot.child("firstname").value.toString()
                        PushNotification(NotificationData( nameofuser,message), topic).also {
                            sendNotification(it)
                        }

                        //context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })




          /*    PushNotification(NotificationData( userName!!,message),
                    topic).also {
                    sendNotification(it)
                }*/

            }
        }

        readMessage(firebaseUser!!.uid, userId)
    }
// This function is used for storing new message in database which will be called when user clicks on send button.
    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }
//This function will be in action for displaying the older messages.
    fun readMessage(senderId: String, receiverId: String) {

        var chatRecyclerView = findViewById<RecyclerView>(com.narendar.letstravel.R.id.chatRecyclerView)

        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }
//This function is used for sending notifications when  there is a new message
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