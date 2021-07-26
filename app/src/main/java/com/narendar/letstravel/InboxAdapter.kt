package com.narendar.letstravel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class InboxAdapter(private val context: Context, private val userList: ArrayList<User>) :
    RecyclerView.Adapter<InboxAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.inbox_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.txtUserName.text = user.userName
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.profile_image).into(holder.imgUser)

        holder.layoutUser.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("userId",user.userId)
            intent.putExtra("userName",user.userName)

            context.startActivity(intent)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtUserName: TextView = view.findViewById(R.id.userName_inbox)

        val imgUser: CircleImageView = view.findViewById(R.id.userImage_inbox)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutUser_inbox)
    }
}