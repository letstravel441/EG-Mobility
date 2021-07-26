package com.narendar.letstravel.serviceprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.narendar.letstravel.ProfileFragment
import com.narendar.letstravel.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class stationdetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_stationdetails, container, false)
        //val b = intent.extras
        val stationname=view.findViewById<TextView>(R.id.stationname)
        val location=view.findViewById<TextView>(R.id.location)
        val mobile=view.findViewById<TextView>(R.id.mobile)
        return view
    }
}