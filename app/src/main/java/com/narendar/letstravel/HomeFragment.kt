package com.narendar.letstravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.narendar.letstravel.mybike.MBMainActivity
import com.narendar.letstravel.serviceprovider.ServiceproviderFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        var letstravel = view. findViewById<ImageView>(R.id.letstravel)
        val  mybike           =  view.findViewById<ImageView>(R.id.mybike)
        val   mycar          =  view.findViewById<ImageView>(R.id.mycar)
        val   accessories          =  view.findViewById<ImageView>(R.id.accessories)
        val    more         =  view.findViewById<ImageView>(R.id.more)
        val    community       =  view.findViewById<ImageView>(R.id.community)
        val    roadside         =  view.findViewById<ImageView>(R.id.roadside)
        val serviceprovider         =  view.findViewById<ImageView>(R.id.serviceprovider)




        letstravel.setOnClickListener {

            val fragment= LetstravelFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, LetstravelFragment())
            transaction?.commit()



        }

        mybike.setOnClickListener {

          //  Toast.makeText(context,"clicked on  My Bike", Toast.LENGTH_SHORT).show()
            val intent = Intent(context,MBMainActivity::class.java)
            startActivity(intent)
        }

        mycar.setOnClickListener {
            Toast.makeText(context,"clicked on  My Car", Toast.LENGTH_SHORT).show()
        }

        more.setOnClickListener {
            Toast.makeText(context,"clicked on  More", Toast.LENGTH_SHORT).show()
        }

        community.setOnClickListener {
            Toast.makeText(context,"clicked on  Community", Toast.LENGTH_SHORT).show()
        }

        accessories.setOnClickListener {
            Toast.makeText(context,"clicked on  Accessories", Toast.LENGTH_SHORT).show()
        }

        roadside.setOnClickListener {
            Toast.makeText(context,"clicked on  Road side / Local Assistance", Toast.LENGTH_SHORT).show()
        }
        serviceprovider.setOnClickListener {

          //  Toast.makeText(context,"clicked on  Service Provider", Toast.LENGTH_SHORT).show()
            if(FirebaseAuth.getInstance().currentUser != null){
            val fragment= ServiceproviderFragment()

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame, fragment)?.addToBackStack("kavya")
            transaction?.commit()
            } else startActivity(Intent(context, MobileNumber::class.java))
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}