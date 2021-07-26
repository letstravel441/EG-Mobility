package com.narendar.letstravel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class ReviewAdapter(private val context: ValueEventListener, private val reviewsList: ArrayList<review>):
RecyclerView.Adapter<ReviewAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_single_row, parent, false)
        return ReviewAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        val Review : review = reviewsList[position]
        holder.reviewerName.text=Review.reviewername
        holder.rating.text=Review.ratingofreviewer
        holder.reviewText.text=Review.review
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reviewerName: TextView = view.findViewById(R.id.name_review_item)
        val rating: TextView = view.findViewById(R.id.rating_review_item)
        val reviewText: TextView = view.findViewById(R.id.review_review_item)
    }

}