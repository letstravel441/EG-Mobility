package com.narendar.letstravel

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class Profile : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var storage: FirebaseStorage
    lateinit var storageRef: StorageReference
    private var filePath: Uri? = null
    lateinit var btnSave : Button
    lateinit  var progressBar : ProgressBar
    lateinit var imgProfilePic: ImageView
    lateinit var imgProfilePlus: ImageView


    private val PICK_IMAGE_REQUEST: Int = 2020
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile2)

        val actionBar = findViewById<Toolbar>(R.id.filter_tttoolbar)
        setSupportActionBar(actionBar)
        supportActionBar?.title ="Profile Details"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar.setNavigationOnClickListener { finish() }

        auth = FirebaseAuth.getInstance()
        val currentUser= auth.currentUser
        val user= auth.currentUser
       // database= FirebaseDatabase.getInstance().getReference("Users").child("Mobile Auth")
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")
        if(currentUser == null){
            startActivity(Intent(this,MobileNumber::class.java))
            finish()
        }
        val firstnameText = findViewById<TextView>(R.id.firstnameText)
        val lastnameText = findViewById<TextView>(R.id.lastnameText)
        val mobile = findViewById<TextView>(R.id.mobile)
        val email = findViewById<TextView>(R.id.email)
        imgProfilePic = findViewById(R.id.circular_profile_image)
        imgProfilePlus = findViewById(R.id.circular_profile_plus)
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        if (user == null) {
            startActivity(Intent(this, MobileNumber::class.java))
            finish()
            // activity?.finish()
           // activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit();
        }
        // databaseReference = FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser.uid)
        if(user != null ) {
            val firebaseUser = FirebaseAuth.getInstance().currentUser!!
            val userreference = databaseReference?.child(user.uid)
            databaseReference =
                FirebaseDatabase.getInstance().getReference("profile").child(firebaseUser.uid)
            userreference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    firstnameText.text = snapshot.child("firstname").value.toString()
                    lastnameText.text = snapshot.child("lastname").value.toString()
                    email.text = snapshot.child("email").value.toString()
                    mobile.text = user.phoneNumber

                    Glide.with(this@Profile).load(snapshot.child("profileImage").value.toString()).placeholder(R.drawable.profile_image).into(imgProfilePic)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


            btnSave = findViewById(R.id.btnSave)
            progressBar = findViewById(R.id.progressBar)


            imgProfilePic.setOnClickListener {
                chooseImage()
            }
            imgProfilePlus.setOnClickListener {
                chooseImage()
            }

            btnSave.setOnClickListener {
                uploadImage()
                progressBar.visibility = View.VISIBLE
            }
        }

    }

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
//            try {
//                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(applicationContext!!.contentResolver, filePath)
//                val imgProfilePic: ImageView = findViewById(R.id.circular_profile_image)
//                imgProfilePic .setImageBitmap(bitmap)
//                btnSave.visibility = View.VISIBLE
//            } catch (e: IOException) {
//               // e.printStackTrace()
//              //  Toast.makeText(this, "The problem is " +e.message, Toast.LENGTH_LONG).show()
//                Log.d("Exit", "The problem is " +e.message)
//            }
            if(filePath != null){
                imgProfilePic = findViewById(R.id.circular_profile_image)
                imgProfilePic.setImageURI(filePath)
                btnSave.visibility = View.VISIBLE
            }
        }
    }

    private fun uploadImage() {

        if (filePath != null) {


            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashMap: HashMap<String, String> = HashMap()

                    hashMap.put("profileImage",filePath.toString())
                    databaseReference?.updateChildren(hashMap as Map<String, Any>)
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
                    btnSave.visibility = View.GONE
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed" + it.message, Toast.LENGTH_SHORT)
                        .show()

                }

        }
    }

    override fun onRestart(){
        super.onRestart()
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}