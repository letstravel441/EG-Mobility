package com.narendar.letstravel.mybike

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.narendar.letstravel.R
import com.smarteist.autoimageslider.SliderViewAdapter

class ImagesAdapter(context: Context, private val images: ArrayList<ImagesList> = ArrayList<ImagesList>()) :
    SliderViewAdapter<ImagesAdapter.SliderAdapterVH>() {

    //fun deleteItem(position: Int) {
    //    images.removeAt(position)
    //    notifyDataSetChanged()
    //}

    //fun addItem(sliderItem: ImagesList) {
    //    images.add(sliderItem)
    //    notifyDataSetChanged()
    //}

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.image_row, parent, false)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {

        Glide.with(viewHolder.itemView)
            .load(images[position].imageURL)
            .fitCenter()
            .into(viewHolder.image)

    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return images.size
    }

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.product_photo_2)
    }

}
