package com.narendar.letstravel.serviceprovider

import android.app.AlertDialog
import android.content.Context
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

class Adaptermyservices (val context: Context, private  val MyservicesList: ArrayList<myservices>) :
    RecyclerView.Adapter<Adaptermyservices.myservicesViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adaptermyservices.myservicesViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_myservices,parent,false)
        return myservicesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Adaptermyservices.myservicesViewHolder, position: Int) {
        val sp_services: myservices = MyservicesList[position]
        holder.username3.text = sp_services.username
        holder.usermobile.text = sp_services.usermobile
        val stringBuilder = StringBuilder()
        stringBuilder.append(sp_services.type)
        stringBuilder.append(" ")
        stringBuilder.append(sp_services.nameservice)
        holder.typeofservice3.text = stringBuilder
        holder.messagesp.text = sp_services.message
        holder.datetime.text=sp_services.Datetime
        //holder.llContent.setOnClickListener {

        //}

            holder.deny.setOnClickListener(View.OnClickListener {
                val builder = AlertDialog.Builder(holder.username3.getContext())
                builder.setTitle("Deny Service")
                builder.setMessage("Do you want to deny the request?")
                builder.setPositiveButton(
                    "DENY"
                ) { dialogInterface, i ->

                    var db = FirebaseFirestore.getInstance()
                    db.collection("service_user")
                        .whereEqualTo("serviceproviderid", sp_services.serviceproviderid)
                        .whereEqualTo("userid", sp_services.userid)
                        .whereEqualTo("type", sp_services.type)
                        .whereEqualTo("nameservice", sp_services.nameservice)
                        .addSnapshotListener(object : EventListener<QuerySnapshot> {
                            override fun onEvent(
                                value: QuerySnapshot?,
                                error: FirebaseFirestoreException?
                            ) {
                                if (value != null) {
                                    for (i in value) {
                                        FirebaseFirestore.getInstance().collection("service_user")
                                            .document(i.id)
                                            .update(
                                                "message",
                                                "Service request is Cancelled By Service Provider"
                                            )
                                        FirebaseFirestore.getInstance().collection("service_user")
                                            .document(i.id)
                                            .update("status", "Denied")
                                    }

                                }
                            }
                        })
                    FirebaseFirestore.getInstance().collection("service_provider")
                        .document(sp_services.bookedserviceid.toString()).delete()

                    removeRide(position)
                }
                builder.setNegativeButton(
                    "No"
                ) { dialogInterface, i -> }
                builder.show()
            })
            holder.accept.setOnClickListener(View.OnClickListener {
                val builder = AlertDialog.Builder(holder.username3.getContext())
                builder.setTitle("Accept Service")
                builder.setMessage("Do you want to accept the request?")
                builder.setPositiveButton(
                    "ACCEPT"
                ) { dialogInterface, i ->
                    var db = FirebaseFirestore.getInstance()
                    db.collection("service_user")
                        .whereEqualTo("serviceproviderid", sp_services.serviceproviderid)
                        .whereEqualTo("userid", sp_services.userid)
                        .whereEqualTo("type", sp_services.type)
                        .whereEqualTo("nameservice", sp_services.nameservice)
                        .addSnapshotListener(object : EventListener<QuerySnapshot> {
                            override fun onEvent(
                                value: QuerySnapshot?,
                                error: FirebaseFirestoreException?
                            ) {
                                if (value != null) {
                                    for (i in value) {
                                        FirebaseFirestore.getInstance().collection("service_user")
                                            .document(i.id)
                                            .update("status", "Accepted")
                                    }

                                }
                            }
                        })
                    db.collection("service_provider")
                        .whereEqualTo("serviceproviderid", sp_services.serviceproviderid)
                        .whereEqualTo("userid", sp_services.userid)
                        .whereEqualTo("type", sp_services.type)
                        .whereEqualTo("nameservice", sp_services.nameservice)
                        .addSnapshotListener(object : EventListener<QuerySnapshot> {
                            override fun onEvent(
                                value: QuerySnapshot?,
                                error: FirebaseFirestoreException?
                            ) {
                                if (value != null) {
                                    for (i in value) {
                                        FirebaseFirestore.getInstance()
                                            .collection("service_provider")
                                            .document(i.id)
                                            .update("message", "You have accepted this service")
                                    }

                                }
                            }
                        })
                }
                builder.setNegativeButton(
                    "No"
                ) { dialogInterface, i -> }
                builder.show()
            })

    }

    override fun getItemCount(): Int {
        return MyservicesList.size
    }

    public class myservicesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val username3: TextView = itemView.findViewById(R.id.username3)
        val usermobile: TextView = itemView.findViewById(R.id.usermobile3)
        val typeofservice3: TextView = itemView.findViewById(R.id.typeservice3)
        val accept: Button = itemView.findViewById(R.id.accept3)
        val deny: Button =itemView.findViewById(R.id.deny3)
        val messagesp : TextView = itemView.findViewById(R.id.messagesp)
        val datetime:TextView=itemView.findViewById(R.id.datetime3)

        val llContent : LinearLayout =itemView.findViewById(R.id.llContent_bookingsdetail)
    }
    fun removeRide(p : Int){
        MyservicesList.removeAt(p)
        notifyDataSetChanged()
    }
}