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
class Car_2Fragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_car_2, container, false)

        val break_down = view.findViewById<ImageView>(R.id.break_down)
        val servicing = view.findViewById<ImageView>(R.id.servicing)
        val cleaning = view.findViewById<ImageView>(R.id.cleaning)
        val tyre_puncture = view.findViewById<ImageView>(R.id.tyre_puncture)
        val towing = view.findViewById<ImageView>(R.id.towing)
        val oil_change = view.findViewById<ImageView>(R.id.oil_change)


        break_down.setOnClickListener {
            Toast.makeText(context, "clicked on  Break Down", Toast.LENGTH_SHORT).show()
            val fragment= break_downFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, break_downFragment())?.addToBackStack("kavya")
            transaction?.commit()


        }

        servicing.setOnClickListener {
            Toast.makeText(context, "clicked on  Servicing", Toast.LENGTH_SHORT).show()
            val fragment=servicingFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, servicingFragment())?.addToBackStack("kavya")
            transaction?.commit()



        }

        cleaning.setOnClickListener {
            Toast.makeText(context, "clicked on  Cleaning", Toast.LENGTH_SHORT).show()
            val fragment= cleaningFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, cleaningFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        tyre_puncture.setOnClickListener {
            Toast.makeText(context, "clicked on Tyre puncture", Toast.LENGTH_SHORT).show()
            val fragment= tyre_punctureFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, tyre_punctureFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        towing.setOnClickListener {
            Toast.makeText(context, "clicked on  Towing", Toast.LENGTH_SHORT).show()
            val fragment= towingFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, towingFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        oil_change.setOnClickListener {
            Toast.makeText(context, "clicked on Oil and filter change", Toast.LENGTH_SHORT).show()
            val fragment= oilchangeFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, oilchangeFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }






        return view
    }

}
