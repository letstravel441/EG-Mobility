package com.narendar.letstravel

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.contentValuesOf
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*
// used for showing profile of user in navigation drawer
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null


     lateinit var btnSave : Button
    lateinit  var progressBar : ProgressBar
    lateinit var imgProfilePic : ImageView
    lateinit var userreference: DatabaseReference



//
    private var filePath: Uri? = null

    private val PICK_IMAGE_REQUEST: Int = 2020

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

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
        val view =inflater.inflate(R.layout.fragment_profile, container, false)
        val firstnameText = view.findViewById<TextView>(R.id.firstnameText)
        val lastnameText = view.findViewById<TextView>(R.id.lastnameText)
        val mobile = view.findViewById<TextView>(R.id.mobile)
        val email = view.findViewById<TextView>(R.id.email)
        imgProfilePic = view.findViewById(R.id. imgProfilePic)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        val user = auth.currentUser

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference




// fetching user data from realtime data base

        if (user == null) {
            startActivity(Intent(activity,MobileNumber::class.java))
           // activity?.finish()
           activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
        }
       // databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser.uid)
        if(user != null ) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val userreference = databaseReference?.child(user.uid)
        databaseReference =
            FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser.uid)
        userreference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                firstnameText.text = "Firstname - - > " + snapshot.child("firstname").value.toString()
                lastnameText.text = "Last name - -> " + snapshot.child("lastname").value.toString()
                email.text = "Email  -- > " + user?.email
                mobile.text = "Mobile Number - > " + snapshot.child("mobileno").value.toString()

                context?.let { Glide.with(it).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic) }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        btnSave = view.findViewById(R.id.btnSave)
         progressBar = view.findViewById(R.id.progressBar)


        imgProfilePic.setOnClickListener {
            chooseImage()
        }

        btnSave.setOnClickListener {
            uploadImage()
            progressBar.visibility = View.VISIBLE
        }
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    fun onBackPressed() {
//        activity?.supportFragmentManager?.popBackStack()
//        return
//    }
//function not working properly

    private fun chooseImage() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            filePath = data!!.data
            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity()!!.contentResolver, filePath)
                imgProfilePic .setImageBitmap(bitmap)
                btnSave.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


// not working properly

    private fun uploadImage() {


        if (filePath != null) {


            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashMap: HashMap<String, String> = HashMap()

                    hashMap.put("profileImage",filePath.toString())
                    databaseReference?.updateChildren(hashMap as Map<String, Any>)
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show()
                    btnSave.visibility = View.GONE
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(context, "Failed" + it.message, Toast.LENGTH_SHORT)
                        .show()

                }

        }
    }

}