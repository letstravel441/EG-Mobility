package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.narendar.letstravel.serviceprovider.BusinessRegistrationActivity
import com.narendar.letstravel.serviceprovider.BusinessaccRegistration
import com.narendar.letstravel.serviceprovider.your_servicebookings
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    lateinit var drawerLayout : DrawerLayout
    lateinit var coordinatorLayout : CoordinatorLayout
    lateinit var toolbar : Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView : NavigationView
    var previousMenuItem : MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        openDashboard()

        val g = intent.getStringExtra("ridebooked")
        if(g=="yes")
        {
            intent.putExtra("ridebooked","no" )
            openletsTravel()

        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity ,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()




        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId)
            {
                R.id.home -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())

                        .commit()
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.about_app -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())

                        .commit()
                    supportActionBar?.title="About App"
                    drawerLayout.closeDrawers()
                }
                R.id.business -> {
                    val auth = FirebaseAuth.getInstance()
                    val database = FirebaseDatabase.getInstance()
                    val databaseReference = database?.reference!!.child("profile")
                    val user = auth.currentUser
                    val userreference = databaseReference?.child(user?.uid!!)
                    userreference?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.child("businessAccount").value.toString()=="No"){
                                val intent = Intent(this@MainActivity,BusinessRegistrationActivity::class.java)
                                startActivity(intent)
                            }else{
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.frame, BusinessaccRegistration())

                                    .commit()
                                supportActionBar?.title="Business profile"
                                drawerLayout.closeDrawers()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }
                R.id.your_servicebookings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, your_servicebookings())

                        .commit()
                    supportActionBar?.title="Your Service Bookings"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    auth.signOut()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()

                }

            }


            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title ="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard()
    {
        val fragment= HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, HomeFragment())
        transaction.commit()
        supportActionBar?.title="EG MOBILITY"
        navigationView.setCheckedItem(R.id.home)
    }

    fun openletsTravel()
    {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, LetstravelFragment())
        transaction.commit()
        supportActionBar?.title="EG MOBILITY"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag) {
            !is HomeFragment ->openDashboard()
            else ->super.onBackPressed()

        }


   // }

       // loadProfile()

    }




  /*  private fun loadProfile() {

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        var emailText = findViewById<TextView>(R.id.emailText)
        var firstnameText = findViewById<TextView>(R.id.firstnameText)
        var lastnameText = findViewById<TextView>(R.id.lastnameText)
        var mobileText= findViewById<TextView>(R.id.mobileText)

        emailText.text = "Email ID : "+user?.email

        userreference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                firstnameText.text = "Firstname :"+snapshot.child("firstname").value.toString()
                lastnameText.text = "Last name : "+snapshot.child("lastname").value.toString()
                mobileText.text = "Mobile No : "+snapshot.child("mobileno").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        var logoutButton = findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    } */
}