package com.narendar.letstravel.serviceprovider

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.narendar.letstravel.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.item_display.view.*

class break_downFragment : Fragment() {
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_break_down, container, false)
        var recyclerView = view.findViewById(R.id.stations) as RecyclerView
        var adapter= GroupAdapter<GroupieViewHolder>()
        val ref= FirebaseDatabase.getInstance().getReference("Business")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    var f=it
                    if(f.child("non-ev-car").exists()) {
                        var b=f.child("non-ev-car").child("non-ev-car-services0").value.toString()
                        if(b=="Breakdown") {
                            val u = it.getValue(Busi_user::class.java)
                            if (u != null) {
                                adapter.add(UserItem(u))
                            }
                        }
                    }
                }


                recyclerView.adapter=adapter
                //adapter.setOnItemClickListener{item,view->
                //  val intent=Intent(this@break_downFragment.context,stationdetails::class.java)
                //   val i=item as Busi_user
                //   intent.putExtra("stationname",i.station)
                //   intent.putExtra("location",i.location)
                //  intent.putExtra("mobile",i.mobile)
                //intent.putExtra("email",i.email)
                //  startActivity(intent)
                //val fragment=stationdetailsFragment()
                //val transaction=activity?.supportFragmentManager?.beginTransaction()
                //transaction?.replace(R.id.frame, stationdetailsFragment())
                //transaction?.commit()
                //}
            }


            override fun onCancelled(error: DatabaseError) {
            }

        })
        //adapter.add(UserItem())
        //adapter.add(UserItem())
        //adapter.add(UserItem())
        //recyclerView.adapter=adapter
        return view
    }}




class UserItem(val u:Busi_user): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.station1.text=u.station
        viewHolder.itemView.mobilenumber.text=u.mobile
        viewHolder.itemView.location.text=u.location
        val e=u.email
        viewHolder.itemView.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(v.context, stationdetails::class.java)
            intent.putExtra("name_service",viewHolder.itemView.station1.text)
            intent.putExtra("location_service",u.location)
            intent.putExtra("mobile_service",u.mobile)
            intent.putExtra("email_service",e)
            intent.putExtra("type_service","non-ev-car")
            intent.putExtra("linkservice","Break_down")
            intent.putExtra("service_uid",u.uid)
            v.context.startActivity(intent)
        })
        //var pid=u.uid
        //val ref=FirebaseDatabase.getInstance().getReference("links")
        //val userMap = HashMap<String, String>()
        //userMap["providerid"]=u.uid
        //lateinit var auth: FirebaseAuth
        //val currentUser = auth.currentUser
        //var databaseReference :  DatabaseReference? = null
        //val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
        //val userid=currentUser?.uid
        //userMap["userid"]=userid.toString()
        //userMap["service"]="non-ev-car-Breakdown"
        //   ref.setValue(userMap)


    }

    override fun getLayout(): Int {
        return R.layout.item_display
    }
}
