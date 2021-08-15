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
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
//This adapter is used for displaying available rides in SearchActivity when a passenger searches for rides
class AdapterSearch(val context:Context,private  val searchRidesList: ArrayList<SharedRides>) : RecyclerView.Adapter<AdapterSearch.SearchRidesViewHolder>(){
    class SearchRidesViewHolder( itemView: View): RecyclerView.ViewHolder(itemView) {
        val Name: TextView = itemView.findViewById(R.id.name_search)
        val searchPickuplocation: TextView = itemView.findViewById(R.id.source_search)
        val searchDroplocation: TextView = itemView.findViewById(R.id.destination_search)
        val searchDate: TextView = itemView.findViewById(R.id.time_search)
        val searchFare: TextView = itemView.findViewById(R.id.sharefare_search)
        val llContent : LinearLayout = itemView.findViewById(R.id.llContent_search)
        val book: Button = itemView.findViewById(R.id.bookride)
        val userimg_search : ImageView = itemView.findViewById(R.id.userimg_search)
        val rating: TextView = itemView.findViewById(R.id.ratings_search)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSearch.SearchRidesViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.search_rides_item,parent,false)
        return SearchRidesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterSearch.SearchRidesViewHolder, position: Int) {
        val searchRides : SharedRides = searchRidesList[position]
        holder.Name.text = searchRides.Name
        holder.searchPickuplocation.text = searchRides.sharePickuplocation
        holder.searchDroplocation.text = searchRides.shareDroplocation
        holder.searchDate.text = searchRides.shareDate
        holder.searchFare.text = searchRides.shareFare
        //When publisher is publishing the ride for first time then his rating is shown as 4.5
        if(searchRides.noofridespublished=="0"){
            holder.rating.text="4.5"
        }else {
            holder.rating.text = searchRides.totalrating
        }

        context.let { Glide.with(it).load(searchRides.publisherimage).placeholder(R.drawable.profile_image).into(holder.userimg_search) }
        //It will lead to complete details of a ride in DetailsActivity when passenger clicks on a particular ride
        holder.llContent.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("name", searchRides.Name)
            intent.putExtra("pickup", searchRides.sharePickuplocation)
            intent.putExtra("destination", searchRides.shareDroplocation)
            intent.putExtra("date",  searchRides.shareDate)
            intent.putExtra("rideId",  searchRides.rideId)
            intent.putExtra("passengers",  searchRides.sharePassengers)
            intent.putExtra("bookfare", searchRides.shareFare)
            intent.putExtra("publisherId", searchRides.publisherId)
            intent.putExtra("publisherimage", searchRides.publisherimage)
            intent.putExtra("passengersBooked", searchRides.passengersBooked)
            intent.putExtra("rating",holder.rating.text.toString())
            context.startActivity(intent)

        }

        holder.book.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(holder.Name.getContext())

            builder.setTitle("Confirm Ride")
            builder.setMessage("Do you want to Book Ride?")
            builder.setPositiveButton(
                "Ok"
            ) { dialogInterface, i ->
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        })
    }

    override fun getItemCount(): Int {
        return searchRidesList.size
    }


}