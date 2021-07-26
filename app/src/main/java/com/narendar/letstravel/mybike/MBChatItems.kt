package com.narendar.letstravel.mybike

import com.narendar.letstravel.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatFromItem(val text: String): Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.from_message.text = text
    }
}

//to message
class ChatToItem(val text: String): Item<GroupieViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.to_message.text = text
    }
}