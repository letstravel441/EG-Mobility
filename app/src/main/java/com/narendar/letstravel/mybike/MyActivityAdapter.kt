package com.narendar.letstravel.mybike

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.narendar.letstravel.R

class MyAdapter(val context: Context, private val products: ArrayList<ProductsList>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_product_row, parent, false)
        val holder = MyViewHolder(view)


        view.setOnClickListener {
            val intent = Intent(parent.context, ProductDetails::class.java)

            intent.putExtra("title", products[holder.bindingAdapterPosition].title)
            intent.putExtra("price", products[holder.bindingAdapterPosition].price)
            intent.putExtra("fromId", products[holder.bindingAdapterPosition].userID)
            intent.putExtra("fromUsername", products[holder.bindingAdapterPosition].username)

            intent.putExtra("brand", products[holder.bindingAdapterPosition].brand)
            intent.putExtra("fuelType", products[holder.bindingAdapterPosition].fuelType)
            intent.putExtra("colour", products[holder.bindingAdapterPosition].colour)
            intent.putExtra("ownershipStatus", products[holder.bindingAdapterPosition].ownershipStatus)
            intent.putExtra("bodyType", products[holder.bindingAdapterPosition].bodyType)
            intent.putExtra("sellerLocation", products[holder.bindingAdapterPosition].sellerLocation)
            intent.putExtra("odometerStatus", products[holder.bindingAdapterPosition].odometerStatus)
            intent.putExtra("insuranceStatus", products[holder.bindingAdapterPosition].insuranceStatus)
            intent.putExtra("comment", products[holder.bindingAdapterPosition].comment)
            intent.putExtra("yearOfBuy", products[holder.bindingAdapterPosition].yearOfBuy)
            intent.putExtra("mileage", products[holder.bindingAdapterPosition].mileage)

            intent.putExtra("productID", products[holder.bindingAdapterPosition].productID)


            parent.context.startActivity(intent)
        }
        return holder

    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = products[position].brand
        holder.price.text = products[position].price
        Glide.with(context).load(products[position].imagePreview).into(holder.image)

        // get reference to 'users' node
        val mFirebaseInstance = FirebaseDatabase.getInstance()
        val mFirebaseDatabase = mFirebaseInstance!!.getReference("Products")


        val key =  products[holder.bindingAdapterPosition].productID
        holder.delete.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(holder.title.context)


            builder.setTitle("Deleted Product")
            builder.setMessage("Do you want to Remove Product?")
            builder.setPositiveButton(
                "Delete"
            ) { dialogInterface, i ->

                //FirebaseFirestore.getInstance().collection("Products").document().delete()
                mFirebaseDatabase!!.child(key!!).removeValue()
                removeRide(position)
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> }
            builder.show()
        })


    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val image: ImageView = view.findViewById(R.id.product_photo_3)
        val title: TextView = view.findViewById(R.id.product_title_3)
        val price: TextView = view.findViewById(R.id.product_price_3)
        val delete: ImageView = view.findViewById(R.id.delete_product)

    }
    private fun removeRide(p : Int){
        products.removeAt(p)
        notifyDataSetChanged()
    }

}