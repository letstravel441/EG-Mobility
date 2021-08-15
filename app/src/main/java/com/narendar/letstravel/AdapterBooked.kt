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
//This adapter is used for displaying booked rides in yourrides fragment.
class AdapterBooked (val context: Context, private  val BookedRidesList: ArrayList<BookedRides>) :
    RecyclerView.Adapter<AdapterBooked.BookedRidesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBooked.BookedRidesViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.booked_rides_item,parent,false)
        return BookedRidesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterBooked.BookedRidesViewHolder, position: Int) {
        val bookedRides : BookedRides = BookedRidesList[position]
        holder.Name.text = bookedRides.publisherName
        holder.sharePickuplocation.text = bookedRides.sharePickuplocation
        holder.shareDroplocation.text = bookedRides.shareDroplocation
        holder.shareDate.text = bookedRides.bookDate
        holder.shareFare.text = bookedRides.Fare
        holder.message.text = bookedRides.message
        holder.ratingofpublisher.text=bookedRides.ratingofpublisher
        holder.llContent.setOnClickListener {
            if(bookedRides.message=="") {
                val intent = Intent(context, BookedRideDetails::class.java)
                intent.putExtra("publishername", bookedRides.publisherName)
                intent.putExtra("pickup", bookedRides.sharePickuplocation)
                intent.putExtra("destination", bookedRides.shareDroplocation)
                intent.putExtra("date", bookedRides.shareDate)
                intent.putExtra("rideId", bookedRides.rideId)
                intent.putExtra("bookfare", bookedRides.bookFare)
                intent.putExtra("bookpassengers", bookedRides.bookPassengers)
                intent.putExtra("publishedRideId", bookedRides.publishedRideId)
                intent.putExtra("bookpickup", bookedRides.bookPickuplocation)
                intent.putExtra("bookdrop", bookedRides.bookDroplocation)
                intent.putExtra("totalseats", bookedRides.totalseats)
                intent.putExtra("publisherId", bookedRides.publisherId)
                intent.putExtra("bookerId", bookedRides.bookerId)
                intent.putExtra("bookername", bookedRides.BookerName)
                intent.putExtra("ratingofpublisher",bookedRides.ratingofpublisher)
                context.startActivity(intent)
            }

        }
        holder.publisherImage.setOnClickListener {
            val intent = Intent(context,AboutPublisher::class.java)
            intent.putExtra("publisherid",bookedRides.publisherId)
            context.startActivity(intent)
        }

        if(bookedRides.ridestarted=="Yes"||bookedRides.ridestarted=="Completed"){
            holder.delete.visibility=View.GONE
        }
        //Code for cancelling the ride by the passenger
        holder.delete.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(holder.Name.getContext())

        //When cancelling the ride, this code helps to create a dialog box to confirm the cancellation.

            builder.setTitle("Cancel Ride")
            builder.setMessage("Do you want to cancel Ride?")
            builder.setPositiveButton(
                "Cancel"
            ) { dialogInterface, i ->
            //If passenger confirms the cancellation, corresponding seats of the ride in the database are updated.
                var totalseats = bookedRides.totalseats
                val passengersbooked = bookedRides.passengersBooked
                val bookpassengers = bookedRides.bookPassengers

                var new : Int
                var old : Any?

                val db = FirebaseFirestore.getInstance()
                db.collection("users").document(bookedRides.publishedRideId.toString()).get().addOnCompleteListener {
                        result -> if(result.isSuccessful){ old = result.result.get("passengersBooked")
                    new = old.toString().toInt()-bookpassengers!!.toInt()
                    FirebaseFirestore.getInstance().collection("users")
                        .document(bookedRides.publishedRideId.toString()).update("passengersBooked",new.toString())

                    if(new <= 0)
                    {
                        FirebaseFirestore.getInstance().collection("users")
                            .document(bookedRides.publishedRideId.toString()).update("Status","Not Booked")
                    }}

                }


                FirebaseFirestore.getInstance().collection("booked")
                    .document(bookedRides.rideId.toString()).delete()

                removeRide(position)
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        })
    }

    override fun getItemCount(): Int {
        return BookedRidesList.size
    }
    //This BookedRidesViewHolder helps in holding the views of the item.
    public class BookedRidesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val Name: TextView = itemView.findViewById(R.id.name_booked)
        val sharePickuplocation: TextView = itemView.findViewById(R.id.source_booked)
        val shareDroplocation: TextView = itemView.findViewById(R.id.destination_booked)
        val shareDate: TextView = itemView.findViewById(R.id.date_booked)
        val shareFare: TextView = itemView.findViewById(R.id.bookedamount_details)
        val delete: Button = itemView.findViewById(R.id.cancelride_booked)
        val message : TextView = itemView.findViewById(R.id.removing_booked)
        val publisherImage : ImageView = itemView.findViewById(R.id.userimg_booked)
        val llContent : LinearLayout=itemView.findViewById(R.id.llContent_bookedridedetails)
        val ratingofpublisher: TextView = itemView.findViewById(R.id.ratings_booked)
    }
    //This function is used for removing a item from recyclerview.
    fun removeRide(p : Int){
        BookedRidesList.removeAt(p)
        notifyDataSetChanged()
    }
}

