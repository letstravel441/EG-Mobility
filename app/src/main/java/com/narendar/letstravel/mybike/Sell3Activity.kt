package com.narendar.letstravel.mybike

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.narendar.letstravel.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Sell3Activity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null
    private var storageReference: StorageReference? = null

    //store uri of picked images
    private var images: ArrayList<Uri>? = null

    private var downloadImages: ArrayList<Uri>? = null

    private var position = 0
    private var PICK_IMAGES_CODE = 0
    private lateinit var urlStrings:ArrayList<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell3)

        val choose = findViewById<Button>(R.id.choose)
        val prev = findViewById<Button>(R.id.prev)
        val next = findViewById<Button>(R.id.next)
        val save = findViewById<Button>(R.id.save)
        val imageSwitcher = findViewById<ImageSwitcher>(R.id.imageSwitcher)
        images = ArrayList()

        val brandName = intent.getStringExtra("brandName")
        val bikeModel = intent.getStringExtra("bikeModel")
        val price = intent.getStringExtra("price")
        val fuelType = intent.getStringExtra("fuelType")
        val colour= intent.getStringExtra("colour")
        val ownershipStatus = intent.getStringExtra("ownershipStatus")
        val bodyType= intent.getStringExtra("bodyType")
        val sellerLocation = intent.getStringExtra("sellerLocation")
        val odometerStatus = intent.getStringExtra("odometerStatus")
        val insuranceStatus = intent.getStringExtra("insuranceStatus")
        val addAComment = intent.getStringExtra("addAComment")
        val yearOfBuy = intent.getStringExtra("yearOfBuy")
        val mileage = intent.getStringExtra("mileage")


        val product = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().reference!!.child("Products/$product")
        val currentUser = FirebaseAuth.getInstance().currentUser

        ref?.child("userID")?.setValue(currentUser?.uid!!)
        ref?.child("title")?.setValue(bikeModel.toString())

        ref?.child("titleSort")?.setValue(bikeModel.toString().lowercase())

        ref?.child("price")?.setValue(price.toString())
        ref?.child("username")?.setValue(currentUser?.email)

        ref?.child("productID")?.setValue(product)

        ref?.child("brand")?.setValue(brandName.toString())
        ref?.child("fuelType")?.setValue(fuelType.toString())
        ref?.child("colour")?.setValue(colour.toString())
        ref?.child("ownershipStatus")?.setValue(ownershipStatus.toString())
        ref?.child("bodyType")?.setValue(bodyType.toString())
        ref?.child("sellerLocation")?.setValue(sellerLocation.toString())
        ref?.child("odometerStatus")?.setValue(odometerStatus.toString())
        ref?.child("insuranceStatus")?.setValue(insuranceStatus.toString())
        ref?.child("comment")?.setValue(addAComment.toString())
        ref?.child("yearOfBuy")?.setValue(yearOfBuy.toString())
        ref?.child("mileage")?.setValue(mileage.toString())


        imageSwitcher.setFactory { ImageView(applicationContext) }

        choose.setOnClickListener {
            pickImagesIntent()
        }
        prev.setOnClickListener {
            if (position > 0) {
                position--
                imageSwitcher.setImageURI(images!![position])
            } else {
                Toast.makeText(this, "No More Images...", Toast.LENGTH_SHORT).show()
            }
        }
        next.setOnClickListener {
            if (position < images!!.size - 1) {
                position++
                imageSwitcher.setImageURI(images!![position])
            } else {
                Toast.makeText(this, "No More Images...", Toast.LENGTH_SHORT).show()
            }
        }
        save.setOnClickListener {
            val intent = Intent(this, MBMainActivity::class.java)
            // start your next activity
            startActivity(intent)

            val fileName = UUID.randomUUID().toString()
            val imageFolder: StorageReference =
                FirebaseStorage.getInstance().reference.child("imageFolder/$fileName")


            for (i in 0 until images!!.size) {
                val individualImage: Uri = images!![i]
                val imageName: StorageReference? = individualImage.lastPathSegment?.let { it1 ->
                    imageFolder.child(
                        it1
                    )
                }

                val refImages = ref.child("images")

                imageName?.putFile(individualImage)?.addOnSuccessListener() {

                    Log.d("Sell3", "Image location: ${it.metadata?.path}")
                    imageName?.downloadUrl.addOnSuccessListener {
                        Log.d("Sell3", "File location: $it")
                        val imagesMap = HashMap<String, String>()
                        imagesMap["imageURL"] = it.toString()
                        refImages.push().setValue(imagesMap)

                        if (i == 0) {
                            ref.child("imagePreview").setValue(it.toString())
                        }

                    }
                }
            }
        }
    }


    private fun pickImagesIntent() {

        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select"), PICK_IMAGES_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageSwitcher = findViewById<ImageSwitcher>(R.id.imageSwitcher)
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data?.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data?.clipData!!.getItemAt(i).uri
                        images!!.add(imageUri)
                    }
                    imageSwitcher.setImageURI(images!![0])
                    position = 0
                } else {
                    val imageUri = data?.data
                    imageSwitcher.setImageURI(imageUri)
                    position = 0
                }
            }
        }

    }
}