package com.narendar.letstravel.serviceprovider

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
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import com.narendar.letstravel.R
import org.json.JSONException
import org.json.JSONObject

class Adaptermybookings (val context: Context, private  val MybookingsList: ArrayList<mybookings>) :
    RecyclerView.Adapter<Adaptermybookings.mybookingsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adaptermybookings.mybookingsViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_mybookings,parent,false)
        return mybookingsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Adaptermybookings.mybookingsViewHolder, position: Int) {
        val bookingservice : mybookings = MybookingsList[position]
        holder.stationname.text = bookingservice.stationname
        holder.stationlocation.text = bookingservice.servicelocation
        val stringBuilder = StringBuilder()
        stringBuilder.append(bookingservice.type)
        stringBuilder.append(" ")
        stringBuilder.append(bookingservice.nameservice)
        holder.typeofservice.text = stringBuilder
        holder.message.text = bookingservice.message
        holder.status2.text=bookingservice.status
        holder.llContent.setOnClickListener {
            if(bookingservice.message=="") {
                val intent = Intent(context, Bookingdetails::class.java)
                intent.putExtra("staionname",bookingservice.stationname )
                intent.putExtra("servicelocation", bookingservice.servicelocation)
                intent.putExtra("serviceemail", bookingservice.serviceemail)
                intent.putExtra("servicemobile",bookingservice.servicemobile)
                intent.putExtra("serviceproviderid", bookingservice.serviceproviderid)
                intent.putExtra("userid",bookingservice.userid )
                intent.putExtra("nameservice", bookingservice.nameservice)
                intent.putExtra("type", bookingservice.type)
                intent.putExtra("Datetime", bookingservice.Datetime)
                intent.putExtra("bookedserviceid", bookingservice.bookedserviceid)
                intent.putExtra("status",bookingservice.status)
                context.startActivity(intent)
            }

        }


        holder.delete.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(holder.stationname.getContext())
            builder.setTitle("Cancel Service")
            builder.setMessage("Do you want to cancel Service request?")
            builder.setPositiveButton(
                "Cancel"
            ) { dialogInterface, i ->
                var db = FirebaseFirestore.getInstance()
                db.collection("service_provider").whereEqualTo("serviceproviderid", bookingservice.serviceproviderid).whereEqualTo("userid",bookingservice.userid).whereEqualTo("type",bookingservice.type).whereEqualTo("nameservice",bookingservice.nameservice)
                    .addSnapshotListener(object : EventListener<QuerySnapshot> {
                        override fun onEvent(
                            value: QuerySnapshot?,
                            error: FirebaseFirestoreException?
                        ) {
                            if (value != null) {
                                for (i in value) {
                                    FirebaseFirestore.getInstance().collection("service_provider")
                                        .document(i.id)
                                        .update("message", "Service request is Cancelled By User")
                                }

                            }
                        }
                    })
                FirebaseFirestore.getInstance().collection("service_user")
                    .document(bookingservice.bookedserviceid.toString()).delete()

                removeRide(position)
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        })
    }

    override fun getItemCount(): Int {
        return MybookingsList.size
    }

    public class mybookingsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val stationname: TextView = itemView.findViewById(R.id.station2)
        val stationlocation: TextView = itemView.findViewById(R.id.location2)
        val typeofservice: TextView = itemView.findViewById(R.id.typeservice2)
        val delete: Button = itemView.findViewById(R.id.cancelservice2)
        val message : TextView = itemView.findViewById(R.id.messagenoti)
        val status2: TextView =itemView.findViewById(R.id.status2)

        val llContent : LinearLayout =itemView.findViewById(R.id.llContent_bookingsdetail)
    }
    fun removeRide(p : Int){
        MybookingsList.removeAt(p)
        notifyDataSetChanged()
    }
}