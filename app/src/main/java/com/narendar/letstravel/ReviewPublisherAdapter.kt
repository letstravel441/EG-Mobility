package com.narendar.letstravel

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
//This adapter is used for holding the reviews of the publisher
class ReviewPublisherAdapter(val context: Context, private  val CoPassengersRidesList: ArrayList<CoPassengerRides>) :
    RecyclerView.Adapter<ReviewPublisherAdapter.ReviewPublisherViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewPublisherAdapter.ReviewPublisherViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.review_publisher_item,parent,false)
        return ReviewPublisherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewPublisherAdapter.ReviewPublisherViewHolder, position: Int) {
        val bookedRides : CoPassengerRides = CoPassengersRidesList[position]
        holder.nameReview.text = bookedRides.BookerName
        holder.ratingBar.rating=0f
        holder.ratingBar.stepSize=1f
        holder.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            holder.ratingReviewPublisher.text=rating.toString()
        }
        val databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(bookedRides.bookerId!!)
        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var ridestaken = snapshot.child("noofridestaken").value.toString().toInt()
                databaseReference.updateChildren(mapOf<String,String>("noofridestaken" to (ridestaken+1).toString()))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        // functionality for review submit button
        holder.submitReviewPublisher.setOnClickListener {

            val databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(bookedRides.bookerId!!)

            val hashMap:HashMap<String,String> = HashMap()
            hashMap.put("reviewerid",bookedRides.publisherId!!)
            hashMap.put("reviewername",bookedRides.publisherName!!)
            hashMap.put("ratingofreviewer",holder.ratingReviewPublisher.text.toString())
            hashMap.put("review",holder.reviewPublisher.text.toString())
            hashMap.put("publishedRideId",bookedRides.publishedRideId!!)


// fetching and updating reviews of publisher in realtime data base
            databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalreviews =snapshot.child("totalreviewsasuser").value.toString().toInt()
                    var current1 =
                        snapshot.child("noof1starratingsasuser").value.toString().toInt()
                    var current2 =
                        snapshot.child("noof2starratingsasuser").value.toString().toInt()
                    var current3 =
                        snapshot.child("noof3starratingsasuser").value.toString().toInt()
                    var current4 =
                        snapshot.child("noof4starratingsasuser").value.toString().toInt()
                    var current5 =
                        snapshot.child("noof5starratingsasuser").value.toString().toInt()


                    val currenrtreviews = mapOf<String, String>( "totalreviewsasuser" to (totalreviews + 1).toString())
                    databaseReference!!.updateChildren(currenrtreviews)

                    if(holder.ratingReviewPublisher.text.toString()=="1.0") {

                        val rating1 =
                            mapOf<String, String>("noof1starratingsasuser" to (current1 + 1).toString())
                        databaseReference!!.updateChildren(rating1)

                        var current11 = (current1+1).toFloat()
                        var totalrating=(current11 * 1).toFloat() + (current2 * 2 ).toFloat()+( current3 * 3).toFloat() + (current4 * 4).toFloat() + (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalratingasuser" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)

                    }

                    if(holder.ratingReviewPublisher.text.toString()=="2.0") {

                        val rating2 =
                            mapOf<String, String>("noof2starratingsasuser" to (current2 + 1).toString())
                        databaseReference!!.updateChildren(rating2)

                        var current22 = (current2+1).toFloat()
                        var totalrating=(current1 * 1).toFloat() + (current22 * 2 ).toFloat()+( current3 * 3).toFloat() + (current4 * 4).toFloat()+ (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalratingasuser" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }
                    if(holder.ratingReviewPublisher.text.toString()=="3.0") {

                        val rating3 =
                            mapOf<String, String>("noof3starratingsasuser" to (current3 + 1).toString())
                        databaseReference!!.updateChildren(rating3)

                        var current33 = (current3+1).toFloat()
                        var totalrating=(current1 * 1).toFloat() + (current2 * 2 ).toFloat()+( current33 * 3).toFloat() + (current4 * 4).toFloat() + (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalratingasuser" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }
                    if(holder.ratingReviewPublisher.text.toString()=="4.0") {

                        val rating4 =
                            mapOf<String, String>("noof4starratingsasuser" to (current4 + 1).toString())
                        databaseReference!!.updateChildren(rating4)

                        var current44 = (current4+1).toFloat()
                        var totalrating=(current1 * 1).toFloat() + (current2 * 2 ).toFloat()+( current3 * 3).toFloat() + (current44 * 4).toFloat() + (current5 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalratingasuser" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }
                    if(holder.ratingReviewPublisher.text.toString()=="5.0") {

                        val rating5 =
                            mapOf<String, String>("noof5starratingsasuser" to (current5 + 1).toString())
                        databaseReference!!.updateChildren(rating5)

                        var current55 = (current5+1)
                        var totalrating=(current1 * 1).toFloat() + (current2 * 2 ).toFloat()+( current3 * 3).toFloat() + (current4 * 4).toFloat() + (current55 * 5).toFloat()
                        totalrating=totalrating / (totalreviews+1)
                        val totalratings =
                            mapOf<String, String>("totalratingasuser" to totalrating.toString())
                        databaseReference!!.updateChildren(totalratings)
                    }





                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })




            holder.submitReviewPublisher.visibility = View.GONE
            holder.txtSubmitReviewPublisher.visibility=View.VISIBLE


            databaseReference!!.child("reviewsasuser").child(bookedRides.publishedRideId!!).setValue(hashMap).addOnCompleteListener(context as Activity){
                if (it.isSuccessful){
                    //open home activity

                    Toast.makeText(context, "Feedback submitted ", Toast.LENGTH_SHORT ).show()

                }
                else{
                    Toast.makeText(context, "Feedback not submitted ", Toast.LENGTH_SHORT ).show()

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return CoPassengersRidesList.size
    }

    public class ReviewPublisherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgReviewPublisher: ImageView= itemView.findViewById(R.id.imgReview_publisher_item)
        val nameReview: TextView = itemView.findViewById(R.id.name_review_publisher_item)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingbar_review_publisher_item)
        val ratingReviewPublisher: TextView = itemView.findViewById(R.id.rating_review_publisher_item)
        val reviewPublisher: EditText = itemView.findViewById(R.id.review_review_publisher_item)
        val submitReviewPublisher : Button = itemView.findViewById(R.id.btnSubmit_review_publisher_item)
        val txtSubmitReviewPublisher: TextView = itemView.findViewById(R.id.txtSubmit_review_publisher_item)
    }

}