package com.narendar.letstravel.serviceprovider

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.narendar.letstravel.R

class Bookingdetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookingdetails)
        val stationname22 = findViewById<TextView>(R.id.stationname22)
        val servicelocation22 = findViewById<TextView>(R.id.servicelocation22)
        val servicemobile22 = findViewById<TextView>(R.id.servicemobile22)
        val serviceemail22 = findViewById<TextView>(R.id.serviceemail22)
        val typeofservice22=findViewById<TextView>(R.id.typeofservice22)
        val Datetime22=findViewById<TextView>(R.id.Datetime22)
        val cancelservice22=findViewById<Button>(R.id.cancelservice22)
        val status22=findViewById<TextView>(R.id.status22)
        stationname22.text=intent.getStringExtra("stationname")
        serviceemail22.text=intent.getStringExtra("serviceemail")
        servicelocation22.text=intent.getStringExtra("servicelocation")
        servicemobile22.text=intent.getStringExtra("servicemobile")
        val stringb=StringBuilder()
        stringb.append(intent.getStringExtra("type"))
        stringb.append(" ")
        stringb.append(intent.getStringExtra("nameservice"))
        typeofservice22.text=stringb
        Datetime22.text=intent.getStringExtra("Datetime")
        status22.text=intent.getStringExtra("status")
        val serviceproviderid=intent.getStringExtra("serviceproviderid")
        val userid=intent.getStringExtra("userid")
        val bookedserviceid=intent.getStringExtra("bookedserviceid")

        cancelservice22.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(stationname22.context)
            builder.setTitle("Cancel Service")
            builder.setMessage("Do you want to cancel Service request?")
            builder.setPositiveButton(
                "Cancel"
            ) { dialogInterface, i ->
                var db = FirebaseFirestore.getInstance()
                db.collection("service_provider").whereEqualTo("serviceproviderid", serviceproviderid).whereEqualTo("userid",userid).whereEqualTo("type",intent.getStringExtra("type")).whereEqualTo("nameservice",intent.getStringExtra("nameservice"))
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
                    .document(bookedserviceid!!).delete()
                onBackPressed()
                finish()
            }
            builder.setNegativeButton(
                "No"
            ) { dialogInterface, i -> }
            builder.show()
        })
    }
}