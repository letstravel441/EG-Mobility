package com.narendar.letstravel.mybike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.recent_chat_row.view.*

class MBChatActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatInbox"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.narendar.letstravel.R.layout.activity_mbchat)

        //back button
     /*   val actionBar = getSupportActionBar()
        // showing the back button in action bar
        actionBar!!.title = "Chat"
        actionBar?.setHomeAsUpIndicator(com.narendar.letstravel.R.drawable.ic_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)*/

        var toolbarr = findViewById<Toolbar>(com.narendar.letstravel.R.id.toolbar_mybike)

        setSupportActionBar(toolbarr)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title="chat"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //showing recent chats in recyclerview
        val adapter = GroupAdapter<GroupieViewHolder>()
        val recentChats = findViewById<RecyclerView>(com.narendar.letstravel.R.id.recent_chats)
        recentChats.adapter = adapter
        //recentChats.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))


        //mapping recent chats
        val recentChatsMap = HashMap<String, MBChatMessage>()

        //refresh recyclerview messages
        fun refreshRecyclerViewChats() {
            adapter.clear()
            recentChatsMap.values.forEach {
                adapter.add(RecentChatRow(it))
            }
        }

        val ref = FirebaseDatabase.getInstance()
            .getReference("/recent-messages/${FirebaseAuth.getInstance().currentUser?.uid}")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(MBChatMessage::class.java) ?: return
                recentChatsMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewChats()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(MBChatMessage::class.java) ?: return
                recentChatsMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewChats()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })

        //opening chat inbox
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "Trying to open chat inbox...")
            val intent = Intent(this@MBChatActivity, MBChatInboxActivity::class.java)
            val row = item as RecentChatRow

            if (row.chatMessage.toId == FirebaseAuth.getInstance().uid) {
                intent.putExtra("fromId", row.chatMessage.fromId)
                intent.putExtra("fromUsername", row.chatMessage.fromUsername)
            } else {
                intent.putExtra("fromId", row.chatMessage.toId)
                intent.putExtra("fromUsername", row.chatMessage.toUsername)
            }


            startActivity(intent)
        }

    }

    //recent chat viewholder
    class RecentChatRow(val chatMessage: MBChatMessage) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.recent_message.text = chatMessage.text

            val chatPartnerId: String
            val chatPartnerUsername: String
            if (chatMessage.toId == FirebaseAuth.getInstance().uid) {
                chatPartnerId = chatMessage.fromId.toString()
                chatPartnerUsername = chatMessage.fromUsername.toString()
            } else {
                chatPartnerId = chatMessage.toId.toString()
                chatPartnerUsername = chatMessage.toUsername.toString()
            }

            val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    viewHolder.itemView.username.text = chatPartnerUsername
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        override fun getLayout(): Int {
            return com.narendar.letstravel.R.layout.recent_chat_row
        }
    }

}
