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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class car1_servicingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_car1_servicing, container, false)
        var recyclerView = view.findViewById(R.id.stations21) as RecyclerView
        var adapter= GroupAdapter<GroupieViewHolder>()

        val ref= FirebaseDatabase.getInstance().getReference("Business")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach{
                    var f=it
                    if(f.child("ev-car").exists()) {
                        var k=0;
                        while(f.child("ev-car").child("ev-car-services$k").exists()) {
                            var b =
                                f.child("ev-car").child("ev-car-services$k").value.toString()
                            if (b == "Servicing") {
                                val u = it.getValue(Busi_user::class.java)
                                if (u != null) {
                                    adapter.add(UserItem21(u))
                                }
                            }
                            k++
                        }
                    }
                }


                recyclerView.adapter=adapter
            }


            override fun onCancelled(error: DatabaseError) {
            }

        })
        return view
    }
}

class UserItem21(val u:Busi_user): Item<GroupieViewHolder>(){
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
            intent.putExtra("type_service","ev-car")
            intent.putExtra("linkservice","Servicing")
            intent.putExtra("service_uid",u.uid)
            v.context.startActivity(intent)
        })
    }

    override fun getLayout(): Int {
        return R.layout.item_display
    }
}

