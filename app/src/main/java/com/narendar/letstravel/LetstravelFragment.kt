package com.narendar.letstravel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

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


        val fragmentAdapter = FragmentAdapter(requireActivity()!!.supportFragmentManager)
        fragmentAdapter.addFragment(FindFragment(),"FIND")
        fragmentAdapter.addFragment(ShareFragment(),"OFFER")
        fragmentAdapter.addFragment(InboxFragment(),"INBOX")
        fragmentAdapter.addFragment(YourrideFragment(),"YOUR RIDE")


        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

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
}