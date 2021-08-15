package com.narendar.letstravel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.narendar.letstravel.travelfragments.FindFragment
import com.narendar.letstravel.travelfragments.ShareFragment
//This fragment gets opened when user clicks on the Lets travel option in Home fragment.
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LetstravelFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LetstravelFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_letstravel, container, false)

        var viewPager = view.findViewById<ViewPager>(R.id.viewpager) as ViewPager
        var tablayout = view.findViewById<TabLayout>(R.id.tablayout) as TabLayout
        var t2 = view.findViewById<TabItem>(R.id.secondItem)
        var t3 = view.findViewById<TabItem>(R.id.thirditem)
        var t4 = view.findViewById<TabItem>(R.id.fourthitem)
//This fragment consists of a tabLayout below toolbar which consists of four options "FIND" , "OFFER" , "INBOX" and "YOUR RIDES"

        val tabLayout = view.findViewById<TabLayout>(R.id.tablayout) as TabLayout



//When clicked on any particular option , corresponding fragment will be opened with the help of FragmentAdapter.
        val fragmentAdapter = FragmentAdapter(requireActivity()!!.supportFragmentManager)
        fragmentAdapter.addFragment(FindFragment(),"FIND")
        fragmentAdapter.addFragment(ShareFragment(),"OFFER")
        fragmentAdapter.addFragment(InboxFragment(),"INBOX")
        fragmentAdapter.addFragment(YourrideFragment(),"YOUR RIDES")


        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

        if(FirebaseAuth.getInstance().currentUser == null){
            disableTab(tabLayout, 1)
            disableTab(tabLayout, 2)
            disableTab(tabLayout, 3)
//            t2.visibility = View.INVISIBLE
//            t3.visibility = View.INVISIBLE
//            t4.visibility = View.INVISIBLE

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
         * @return A new instance of fragment LetstravelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LetstravelFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun disableTab(tabLayout: TabLayout, index: Int){
        (tabLayout.getChildAt(0) as ViewGroup ).getChildAt(index).setEnabled(false)

    }
}