package com.narendar.letstravel.serviceprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.narendar.letstravel.HomeFragment
import com.narendar.letstravel.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class bike_2Fragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bike_2, container, false)

        val bikebreak_down = view.findViewById<ImageView>(R.id.bikebreak_down)
        val bike_servicing = view.findViewById<ImageView>(R.id.bike_servicing)
        val bike_cleaning = view.findViewById<ImageView>(R.id.bike_cleaning)
        val bike_tyre_puncture = view.findViewById<ImageView>(R.id.bike_tyre_puncture)
        val bike_towing = view.findViewById<ImageView>(R.id.bike_towing)
        val bike_oilchange = view.findViewById<ImageView>(R.id.bike_oilchange)


        bikebreak_down.setOnClickListener {
            Toast.makeText(context, "clicked on  Break Down", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_breakdownFragment())?.addToBackStack("kavya")
            transaction?.commit()


        }

        bike_servicing.setOnClickListener {
            Toast.makeText(context, "clicked on  Servicing", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_servicingFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        bike_cleaning.setOnClickListener {
            Toast.makeText(context, "clicked on  Cleaning", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_cleaningFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        bike_tyre_puncture.setOnClickListener {
            Toast.makeText(context, "clicked on Tyre puncture", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_tyre_punctureFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        bike_towing.setOnClickListener {
            Toast.makeText(context, "clicked on  Towing", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_towingFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        bike_oilchange.setOnClickListener {
            Toast.makeText(context, "clicked on Oil and filter change", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, bike_oilchangeFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }






        return view
    }

}