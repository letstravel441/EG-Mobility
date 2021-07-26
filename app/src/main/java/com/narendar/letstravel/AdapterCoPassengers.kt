package com.narendar.letstravel

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdapterCoPassengers(val context: Context, private  val CoPassengersRidesList: ArrayList<CoPassengerRides>) :
    RecyclerView.Adapter<AdapterCoPassengers.CoPassengersViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCoPassengers.CoPassengersViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.co_passengers_item,parent,false)
        return CoPassengersViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterCoPassengers.CoPassengersViewHolder, position: Int) {
        val bookedRides : CoPassengerRides = CoPassengersRidesList[position]
        holder.bookerName.text = bookedRides.BookerName
        holder.bookPickuplocation.text = bookedRides.bookPickuplocation
        holder.bookDroplocation.text = bookedRides.bookDroplocation

        holder.seatsbooked.text = bookedRides.bookPassengers


        holder.llContent.setOnClickListener {
            if(bookedRides.message=="") {
                val intent = Intent(context, AboutBooker::class.java)
                intent.putExtra("bookername", bookedRides.BookerName)
                intent.putExtra("bookerId", bookedRides.bookerId)


                context.startActivity(intent)
            }

        }

        if(bookedRides.ridestarted=="Yes"||bookedRides.ridestarted=="Completed"){
            holder.delete.visibility=View.GONE
        }

        holder.chat.setOnClickListener {
            val intent = Intent(context, ChatActivity ::class.java)

            intent.putExtra("userId",bookedRides.bookerId)
            intent.putExtra("userName", bookedRides.BookerName)
            context.startActivity(intent)
        }


        holder.delete.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(holder.bookerName.getContext())



            builder.setTitle("Cancel Ride")
            builder.setMessage("Do you want to Remove this Passenger?")
            builder.setPositiveButton(
                "Cancel"
            ) { dialogInterface, i ->

                var totalseats = bookedRides.totalseats

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


               /* FirebaseFirestore.getInstance().collection("booked")
                    .document(bookedRides.rideId.toString()).delete()*/
                FirebaseFirestore.getInstance().collection("booked")
                    .document(bookedRides.rideId.toString()).update("message","Publisher Cancelled Your Ride!!")

                FirebaseFirestore.getInstance().collection("booked")
                    .document(bookedRides.rideId.toString()).update("bookPassengers","0")



                removeRide(position)

            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        } )
    }

    override fun getItemCount(): Int {
        return CoPassengersRidesList.size
    }

    public class CoPassengersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val bookerName: TextView = itemView.findViewById(R.id.name_coPassengers)
        val bookPickuplocation: TextView = itemView.findViewById(R.id.source_coPassengers)
        val bookDroplocation: TextView = itemView.findViewById(R.id.destination_coPassengers)
        val chat: Button =itemView.findViewById(R.id.chat_coPassengers)
        val delete: Button = itemView.findViewById(R.id.cancelride_coPassengers)
        val seatsbooked : TextView = itemView.findViewById(R.id.seatsbooked)
        val llContent : LinearLayout =itemView.findViewById(R.id.llContent_coPassengers)
        val review: Button = itemView.findViewById(R.id.review_coPassengers)
    }
    fun removeRide(p : Int){
        CoPassengersRidesList.removeAt(p)
        notifyDataSetChanged()
    }
}