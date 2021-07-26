package com.narendar.letstravel.mybike

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_mbchat_inbox.*

class MBChatInboxActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatInbox"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.narendar.letstravel.R.layout.activity_mbchat_inbox)

        //getting users ID's
        val fromId = intent.getStringExtra("fromId")
        val fromUsername = intent.getStringExtra("fromUsername")
        val toId = FirebaseAuth.getInstance().currentUser?.uid
        val toUsername = FirebaseAuth.getInstance().currentUser?.email


        //actionbar
      /*  val actionBar = supportActionBar
        //displaying username
        actionBar!!.title = fromUsername
        // showing the back button in action bar
        actionBar?.setHomeAsUpIndicator(com.narendar.letstravel.R.drawable.ic_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)*/

        var toolbarr = findViewById<Toolbar>(com.narendar.letstravel.R.id.toolbar_mybikechat)

        setSupportActionBar(toolbarr)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title=fromUsername
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //recyclerview using groupie library
        val adapter = GroupAdapter<GroupieViewHolder>()
        val newMessage = findViewById<RecyclerView>(com.narendar.letstravel.R.id.new_message)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        newMessage.layoutManager = linearLayoutManager
        newMessage.adapter = adapter


        //send message
        val send_message = findViewById<ImageView>(com.narendar.letstravel.R.id.send_message)
        send_message.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")

            val enterMessage = findViewById<EditText>(com.narendar.letstravel.R.id.enter_message)
            val text = enterMessage.text.toString()

            val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
            val fromReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

            val chatMessage =
                MBChatMessage(reference.key!!, text, fromId!!, fromUsername!!, toId!!, toUsername!!, System.currentTimeMillis())
            reference.setValue(chatMessage).addOnSuccessListener {
                enter_message.text.clear()
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                new_message.scrollToPosition(adapter.itemCount - 1)
            }
            fromReference.setValue(chatMessage)

            //references for recent chats
            val recentMessageRef = FirebaseDatabase.getInstance().getReference("/recent-messages/$fromId/$toId")
            recentMessageRef.setValue(chatMessage)

            val recentMessageFromRef = FirebaseDatabase.getInstance().getReference("/recent-messages/$toId/$fromId")
            recentMessageFromRef.setValue(chatMessage)

        }

        //Displaying messages in chat/ listen messages
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(MBChatMessage::class.java)

                if (chatMessage != null) {
                    chatMessage?.text?.let { Log.d(TAG, it) }

                    if (chatMessage?.fromId == FirebaseAuth.getInstance().uid) {
                        chatMessage?.text?.let { ChatFromItem(it) }?.let { adapter.add(it) }
                    } else {
                        chatMessage?.text?.let { ChatToItem(it) }?.let { adapter.add(it) }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}