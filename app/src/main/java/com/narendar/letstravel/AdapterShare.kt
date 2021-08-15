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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
//This adapter is used for displaying published rides of a publisher in yourrides fragment.
class AdapterShare(val context:Context,private  val sharedRidesList: ArrayList<SharedRides>) :RecyclerView.Adapter<AdapterShare.SharedRidesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterShare.SharedRidesViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.shared_rides_item,parent,false)
        return SharedRidesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterShare.SharedRidesViewHolder, position: Int) {
        val sharedRides : SharedRides = sharedRidesList[position]
        holder.Name.text = sharedRides.Name
        holder.sharePickuplocation.text = sharedRides.sharePickuplocation
        holder.shareDroplocation.text = sharedRides.shareDroplocation
        holder.shareDate.text = sharedRides.shareDate
        holder.status.text=sharedRides.Status
        holder.shareFare.text = sharedRides.shareFare
//When publisher clicks on the layout of any particular published ride, it will lead to an SharedRideDetails where all the details of that ride will be shown.
        holder.llContent.setOnClickListener {
            val intent = Intent(context, SharedRidesDetails::class.java)
            intent.putExtra("pickup", sharedRides.sharePickuplocation)
            intent.putExtra("destination", sharedRides.shareDroplocation)
            intent.putExtra("date",  sharedRides.shareDate)
            intent.putExtra("rideId",  sharedRides.rideId)
            intent.putExtra("passengers",  sharedRides.sharePassengers)
            intent.putExtra("bookfare", sharedRides.shareFare)
            intent.putExtra("Status",sharedRides.Status)
            intent.putExtra("publisherId", sharedRides.publisherId)
            intent.putExtra("passengersBooked", sharedRides.passengersBooked)
            intent.putExtra("ridestarted", sharedRides.ridestarted)
            context.startActivity(intent)

        }


        context.let { Glide.with(it).load(sharedRides.publisherimage).placeholder(R.drawable.profile_image).into(holder.userImage) }

        if(sharedRides.ridestarted=="Yes"||sharedRides.ridestarted=="Completed"){
            holder.delete.visibility=View.GONE
        }
        // If publisher wants to cancel the ride, following code helps in this regard and delete that particular ride from database.
        holder.delete.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(holder.Name.getContext())



            builder.setTitle("Cancel Ride")
            builder.setMessage("Do you want to cancel Ride?")
            builder.setPositiveButton(
                "Cancel"
            ) { dialogInterface, i ->


               var db = FirebaseFirestore.getInstance()
                db.collection("booked").whereEqualTo("publishedRideId", sharedRides.rideId)
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if (value != null) {
                                for (i in value) {
                                    FirebaseFirestore.getInstance().collection("booked")
                                        .document(i.id)
                                        .update("message", "Ride Cancelled By Publisher")
                                }

                            }
                        }
                    })


                FirebaseFirestore.getInstance().collection("users")
                    .document(sharedRides.rideId.toString()).delete()

                removeRide(position)
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        })
    }

    override fun getItemCount(): Int {
        return sharedRidesList.size
    }

    public class SharedRidesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val Name: TextView = itemView.findViewById(R.id.name)
        val sharePickuplocation: TextView = itemView.findViewById(R.id.source)
        val shareDroplocation: TextView = itemView.findViewById(R.id.destination)
        val shareDate: TextView = itemView.findViewById(R.id.time)
        val shareFare: TextView = itemView.findViewById(R.id.sharefare)
        val userImage : ImageView = itemView.findViewById(R.id.userimg)
        val status : TextView = itemView.findViewById(R.id.status)
        val delete: Button = itemView.findViewById(R.id.cancelride)
        val llContent : LinearLayout = itemView.findViewById(R.id.llContent)
    }
    fun removeRide(p : Int){
        sharedRidesList.removeAt(p)
        notifyDataSetChanged()
    }
}

