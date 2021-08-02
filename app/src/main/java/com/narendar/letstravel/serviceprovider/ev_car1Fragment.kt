package com.narendar.letstravel.serviceprovider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.narendar.letstravel.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ev_car1.newInstance] factory method to
 * create an instance of this fragment.
 */
class ev_car1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ev_car1, container, false)

        val car1_breakdown = view.findViewById<ImageView>(R.id.car1_break_down)
        val car1_servicing = view.findViewById<ImageView>(R.id.car1_servicing)
        val car1_cleaning = view.findViewById<ImageView>(R.id.car1_cleaning)
        val car1_tyre_puncture = view.findViewById<ImageView>(R.id.car1_tyre_puncture)
        val car1_towing = view.findViewById<ImageView>(R.id.car1_towing)
        val car1_batterycharge = view.findViewById<ImageView>(R.id.car1_batterycharge)


        car1_breakdown.setOnClickListener {
            Toast.makeText(context, "clicked on  Break Down", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, car1_breakdownFragment())?.addToBackStack("kavya")
            transaction?.commit()


        }

        car1_servicing.setOnClickListener {
            Toast.makeText(context, "clicked on  Servicing", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, car1_servicingFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        car1_cleaning.setOnClickListener {
            Toast.makeText(context, "clicked on  Cleaning", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, car1_cleaningFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        car1_tyre_puncture.setOnClickListener {
            Toast.makeText(context, "clicked on Tyre puncture", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, car1_tyrepunctureFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

        car1_towing.setOnClickListener {
            Toast.makeText(context, "clicked on  Towing", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, car1_towingFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }

       car1_batterycharge.setOnClickListener {
            Toast.makeText(context, "clicked on battery charge", Toast.LENGTH_SHORT).show()
            val fragment= bike_2Fragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, car1_batterychargeFragment())?.addToBackStack("kavya")
            transaction?.commit()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ev_car1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ev_car1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}