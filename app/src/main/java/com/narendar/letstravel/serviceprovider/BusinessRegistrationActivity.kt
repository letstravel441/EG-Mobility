package com.narendar.letstravel.serviceprovider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.narendar.letstravel.LoginActivity
import com.narendar.letstravel.R
import java.util.*

class BusinessRegistrationActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    var EV_car_services: TextView? = null
    var EV_bike_services: TextView? = null
    var Non_EV_car_services: TextView? = null
    var Non_EV_bike_services: TextView? = null
    lateinit var selectedservice1: BooleanArray

    var servicelist1 = ArrayList<Int>()
    var servicearray1 = arrayOf(
        "Breakdown",
        "cleaning",
        "Battery charging",
        "Servicing",
        "Towing",
        "Tyre puncture"
    )
    lateinit var selectedservice2: BooleanArray

    var servicelist2 = ArrayList<Int>()
    var servicearray2= arrayOf(
        "Breakdown",
        "cleaning",
        "Battery charging",
        "servicing",
        "towing",
        "tyre puncture"
    )
    lateinit var selectedservice3: BooleanArray

    var servicelist3 = ArrayList<Int>()
    var servicearray3= arrayOf(
        "Breakdown",
        "cleaning",
        "oil and filter change",
        "servicing",
        "towing",
        "tyre puncture"
    )
    lateinit var selectedservice4: BooleanArray

    var servicelist4 = ArrayList<Int>()
    var servicearray4 = arrayOf(
        "Breakdown",
        "cleaning",
        "oil and filter change",
        "servicing",
        "towing",
        "tyre puncture"
    )
    private var reg_station: TextInputLayout? = null
    private  var reg_location: TextInputLayout? = null
    private  var reg_username: TextInputLayout? = null
    private  var reg_email: TextInputLayout? = null
    private  var reg_mobile: TextInputLayout? = null
    private  var reg_password: TextInputLayout? = null
    private var button: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business_registration)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Business")
        reg_station = findViewById(R.id.stationInput)
        reg_location = findViewById(R.id.locationInput)
        reg_username = findViewById(R.id.nameInput)
        reg_email = findViewById(R.id.usernameInput)
        reg_mobile = findViewById(R.id.mobileInput)
        reg_password = findViewById(R.id.passwordInput)
        button = findViewById(R.id.registerButton)

        //EV car
        EV_car_services = findViewById(R.id.EV_car_services)
        selectedservice1 = BooleanArray(servicearray1.size)
        EV_car_services?.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(
                this@BusinessRegistrationActivity
            )
            builder.setTitle("select service")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                servicearray1, selectedservice1
            ) { dialog, which, isChecked ->
                if (isChecked) {
                    servicelist1.add(which)
                    Collections.sort(servicelist1)
                } else {
                    servicelist1.removeAt(which)
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val stringBuilder = StringBuilder()
                for (j in servicelist1.indices) {
                    stringBuilder.append(servicearray1[servicelist1[j]])
                    if (j != servicelist1.size - 1) {
                        stringBuilder.append(",")
                    }
                }
                EV_car_services?.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            builder.setNeutralButton(
                "Clear all"
            ) { dialog, which ->
                for (j in selectedservice1.indices) {
                    selectedservice1[j] = false
                    servicelist1.clear()
                    EV_car_services?.setText("")
                }
            }
            builder.show()
        })
        //EV_ bike
        EV_bike_services = findViewById(R.id.EV_bike_services)
        selectedservice2 = BooleanArray(servicearray2.size)
        EV_bike_services?.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(
                this@BusinessRegistrationActivity
            )
            builder.setTitle("select service")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                servicearray2, selectedservice2
            ) { dialog, which, isChecked ->
                if (isChecked) {
                    servicelist2.add(which)
                    Collections.sort(servicelist2)
                } else {
                    servicelist2.removeAt(which)
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val stringBuilder = StringBuilder()
                for (j in servicelist2.indices) {
                    stringBuilder.append(servicearray2[servicelist2[j]])
                    if (j != servicelist2.size - 1) {
                        stringBuilder.append(",")
                    }
                }
                EV_bike_services?.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            builder.setNeutralButton(
                "Clear all"
            ) { dialog, which ->
                for (j in selectedservice2.indices) {
                    selectedservice2[j] = false
                    servicelist2.clear()
                    EV_bike_services?.setText("")
                }
            }
            builder.show()
        })
        //non EV car
        Non_EV_car_services = findViewById(R.id.NonEV_car_services)
        selectedservice3 = BooleanArray(servicearray3.size)
        Non_EV_car_services?.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(
                this@BusinessRegistrationActivity
            )
            builder.setTitle("select service")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                servicearray3, selectedservice3
            ) { dialog, which, isChecked ->
                if (isChecked) {
                    servicelist3.add(which)
                    Collections.sort(servicelist3)
                } else {
                    servicelist3.removeAt(which)
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val stringBuilder = StringBuilder()
                for (j in servicelist3.indices) {
                    stringBuilder.append(servicearray3[servicelist3[j]])
                    if (j != servicelist3.size - 1) {
                        stringBuilder.append(",")
                    }
                }
                Non_EV_car_services?.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            builder.setNeutralButton(
                "Clear all"
            ) { dialog, which ->
                for (j in selectedservice3.indices) {
                    selectedservice3[j] = false
                    servicelist3.clear()
                    Non_EV_car_services?.setText("")
                }
            }
            builder.show()
        })
        //non EV bike
        Non_EV_bike_services = findViewById(R.id.NonEV_bike_services)
        selectedservice4 = BooleanArray(servicearray4.size)
        Non_EV_bike_services?.setOnClickListener(View.OnClickListener {
            val builder = AlertDialog.Builder(
                this@BusinessRegistrationActivity
            )
            builder.setTitle("select service")
            builder.setCancelable(false)
            builder.setMultiChoiceItems(
                servicearray4, selectedservice4
            ) { dialog, which, isChecked ->
                if (isChecked) {
                    servicelist4.add(which)
                    Collections.sort(servicelist4)
                } else {
                    servicelist4.removeAt(which)
                }
            }
            builder.setPositiveButton(
                "OK"
            ) { dialog, which ->
                val stringBuilder = StringBuilder()
                for (j in servicelist4.indices) {
                    stringBuilder.append(servicearray4[servicelist4[j]])
                    if (j != servicelist4.size - 1) {
                        stringBuilder.append(",")
                    }
                }
                Non_EV_bike_services?.setText(stringBuilder.toString())
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }
            builder.setNeutralButton(
                "Clear all"
            ) { dialog, which ->
                for (j in selectedservice4.indices) {
                    selectedservice4[j] = false
                    servicelist4.clear()
                    Non_EV_bike_services?.setText("")
                }
            }
            builder.show()
        })
        button?.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database?.reference!!.child("profile")
            val user = auth.currentUser
            val userreference = databaseReference?.child(user?.uid!!)
            userreference?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val yes = "Yes"
                    val business =
                        mapOf<String, String>("businessAccount" to yes)
                    userreference!!.updateChildren(business)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            database?.reference!!.child("Business").child(user?.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentUser = auth.currentUser
                    val currentUSerDb = database?.reference!!.child("Business").child(user?.uid!!)
                    val uid=currentUser?.uid
                    currentUSerDb?.child("station")?.setValue(reg_station?.getEditText()!!.text.toString())
                    currentUSerDb?.child("location")?.setValue(reg_location?.getEditText()?.getText().toString())
                    currentUSerDb?.child("username")?.setValue(reg_username?.getEditText()?.getText().toString())
                    currentUSerDb?.child("mobile")?.setValue(reg_mobile?.getEditText()?.getText().toString())
                    currentUSerDb?.child("uid")?.setValue(uid)
                    currentUSerDb?.child("email")?.setValue(reg_email?.getEditText()?.getText().toString())
                    currentUSerDb?.child("password")?.setValue(reg_password?.getEditText()?.getText().toString())
                    //FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                    //   OnCompleteListener<Any?> { task ->
                    //        if (task.isSuccessful) {
                    //            val token: string = task.result.Token
                    //        } else {
                    //        }
                    //   })

                    val userMap1 = HashMap<String, String>()
                    var k=0;
                    for (j in servicelist1.indices)
                    {
                        userMap1["ev-car-services"+k]=servicearray1[servicelist1[j]]
                        k++;
                    }
                    currentUSerDb?.child("ev-car")?.setValue(userMap1)
                    val userMap2 = HashMap<String, String>()
                    k=0;
                    for (j in servicelist2.indices)
                    {
                        userMap2["ev-bike-services"+k]=servicearray2[servicelist2[j]]
                        k++;
                    }
                    currentUSerDb?.child("ev-bike")?.setValue(userMap2)
                    val userMap3 = HashMap<String, String>()
                    k=0;
                    for (j in servicelist3.indices)
                    {
                        userMap3["non-ev-car-services"+k]=servicearray3[servicelist3[j]]
                        k++;
                    }
                    currentUSerDb?.child("non-ev-car")?.setValue(userMap3)
                    val userMap4 = HashMap<String, String>()
                    k=0;
                    for (j in servicelist4.indices)
                    {
                        userMap4["non-ev-bike-services"+k]=servicearray4[servicelist4[j]]
                        k++;
                    }
                    currentUSerDb?.child("non-ev-bike")?.setValue(userMap4)
                    Toast.makeText(this@BusinessRegistrationActivity , "Registration Success. ", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@BusinessRegistrationActivity , LoginActivity::class.java))
                    finish()



                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

                }
        }

    }
