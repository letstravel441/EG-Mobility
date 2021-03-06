package com.narendar.letstravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
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
//This is the main activity where different fragments gets opened as a part of it.

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
        val headerView : View = navigationView.getHeaderView(0)
        setUpToolbar()

        openDashboard()

        val g = intent.getStringExtra("ridebooked")
        if(g=="yes")
        {
            intent.putExtra("ridebooked","no" )
            openletsTravel()

        }

        val profile: TextView = headerView.findViewById(R.id.usernamenav)
        if(auth.currentUser != null) {
             val database = FirebaseDatabase.getInstance()
             var databaseReference = database?.reference!!.child("profile")
            val userreference = databaseReference?.child(auth.currentUser!!.uid)
            userreference?.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    profile.text = "Welcome," + "\n" +snapshot.child("firstname").value.toString() +" " + snapshot.child("lastname").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

//The Code from line 86 to 91 is used for creating a hamburger icon on toolbar and giving functionality for it.
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity ,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()



// Following code is used for selecting the items of menu present in navigation drawer.
        //When clicked on any particular item, corresponding fragment will be opened.
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
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.frame, ProfileFragment())
//                        .addToBackStack(null)
//                        .commit()
                    startActivity(Intent(this ,Profile::class.java))

                  //  supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.about_app -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutAppFragment())

                        .commit()
                    supportActionBar?.title="About App"
                    drawerLayout.closeDrawers()
                }
                //In Business item, if user does not have Business Account, he goes for BusinessRegistrationActivity.
                //Else he moves to Business profile Fragment.
                R.id.business -> {
                    val auth = FirebaseAuth.getInstance()
                    val database = FirebaseDatabase.getInstance()
                    val databaseReference = database?.reference!!.child("profile")
                    val user = auth.currentUser
                    if(user != null){
                    val userreference = databaseReference?.child(user?.uid!!)
                    userreference?.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.child("businessAccount").value.toString()=="No"){
                                val intent = Intent(this@MainActivity,BusinessRegistrationActivity::class.java)
                                startActivity(intent)
                                drawerLayout.closeDrawers()
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
                    }else{
                        startActivity(Intent(this, MobileNumber::class.java))
                        drawerLayout.closeDrawers()
                    }

                }
                R.id.your_servicebookings -> {
                    if(FirebaseAuth.getInstance().currentUser != null){
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, your_servicebookings())

                        .commit()
                    supportActionBar?.title="Your Service Bookings"
                    drawerLayout.closeDrawers()
                    } else {
                        startActivity(Intent(this, MobileNumber::class.java))
                        drawerLayout.closeDrawers()
                    }
                }
                R.id.logout -> {
                    auth.signOut()
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    drawerLayout.closeDrawers()

                }

            }


            return@setNavigationItemSelectedListener true
        }


    }
//This function is used for setting the title of tool bar.
    fun setUpToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title ="Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

//When clicked on hamburger icon, navigation drawer comes from left. For this following function will be helpful.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
//By default Home fragment will be opened when main activity is opened because as it is the main screen of the app.
    fun openDashboard()
    {
        val fragment= HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, HomeFragment())
           // .addToBackStack(null)
        transaction.commit()
        supportActionBar?.title="EG MOBILITY"
        navigationView.setCheckedItem(R.id.home)
    }
//This function is used for opening Lets travel fragment.
    fun openletsTravel()
    {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, LetstravelFragment())
         //   .addToBackStack(null)
        transaction.commit()
        supportActionBar?.title="Electrogati"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frame)

//        if(supportFragmentManager.backStackEntryCount ==1 ) finish()
//        else super.onBackPressed()

        when(frag) {
            !is HomeFragment ->openDashboard()
            else ->super.onBackPressed()

       }


   //}

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
  override fun onRestart() {
      super.onRestart()
      startActivity(intent)
      overridePendingTransition(0, 0)
      finish()
  }
}