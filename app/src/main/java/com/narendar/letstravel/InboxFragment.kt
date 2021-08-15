package com.narendar.letstravel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import de.hdodenhof.circleimageview.CircleImageView
//Actually this fragment is used for displaying all chats of that person where atleast one message in between those two persons.
//But at present we are showing all the users of app
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InboxFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InboxFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var userList = ArrayList<User>()
    lateinit var  userRecyclerView : RecyclerView
    lateinit var imgProfile : CircleImageView
    lateinit var imgBack : ImageView

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
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        userRecyclerView = view.findViewById(com.narendar.letstravel.R.id.inboxrecyclerview)



        userRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)





        getUsersList()



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InboxFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InboxFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//This function will store all users of app into userList and then that list is passed to InboxAdapter to display them on the screen.
    fun getUsersList() {
        val firebase: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(firebase != null ){

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("profile")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
               // Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)



                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)) {

                        userList.add(user)
                    }
                }

                val userAdapter = context?.let { InboxAdapter(it, userList) }

                userRecyclerView.adapter = userAdapter
            }

        })
        }
//        else {
//            startActivity(Intent(context, MobileNumber::class.java))
//        }
    }
}