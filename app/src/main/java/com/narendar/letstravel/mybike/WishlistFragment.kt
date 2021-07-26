package com.narendar.letstravel.mybike

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.narendar.letstravel.R

class WishlistFragment : Fragment() {

    //lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<User , ProductsAdapter.ViewHolder>

    private var recyclerView: RecyclerView?= null
    private var searchText : EditText?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        return view
    }

}