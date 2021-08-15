package com.narendar.letstravel

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
//This adapter is used for displaying completed booked rides in yourrides fragment.
class AdapterCompletedBookedRides (val context: Context, private  val BookedRidesList: ArrayList<BookedRides>) :
    RecyclerView.Adapter<AdapterCompletedBookedRides.CompletedBookedRidesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCompletedBookedRides.CompletedBookedRidesViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.completed_booked_item,parent,false)
        return CompletedBookedRidesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterCompletedBookedRides.CompletedBookedRidesViewHolder, position: Int) {
        val bookedRides: BookedRides = BookedRidesList[position]
        holder.Name.text = bookedRides.publisherName
        holder.sharePickuplocation.text = bookedRides.sharePickuplocation
        holder.shareDroplocation.text = bookedRides.shareDroplocation
        holder.shareDate.text = bookedRides.bookDate
        holder.shareFare.text = bookedRides.Fare
        holder.seats.text = bookedRides.bookPassengers

        holder.publisherImage.setOnClickListener {
            val intent = Intent(context, AboutPublisher::class.java)
            intent.putExtra("publisherid", bookedRides.publisherId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return BookedRidesList.size
    }

    public class CompletedBookedRidesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val Name: TextView = itemView.findViewById(R.id.name_completed_booked)
        val sharePickuplocation: TextView = itemView.findViewById(R.id.source_completed_booked)
        val shareDroplocation: TextView = itemView.findViewById(R.id.destination_completed_booked)
        val shareDate: TextView = itemView.findViewById(R.id.time_completed_booked)
        val shareFare: TextView = itemView.findViewById(R.id.fare_completed_booked)
        val chat: Button = itemView.findViewById(R.id.chat_completed_booked)
        val seats: TextView = itemView.findViewById(R.id.seats_completed_booked)
        val publisherImage : ImageView = itemView.findViewById(R.id.userimg_completed_booked)
        val llContent : LinearLayout=itemView.findViewById(R.id.llContent_completed_booked)
    }

}

