package com.narendar.letstravel.mybike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.narendar.letstravel.LoginActivity
import com.narendar.letstravel.R

class MBMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.narendar.letstravel.R.layout.activity_mbmain)

        val navController = findNavController(R.id.container_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val actionBar = findViewById<Toolbar>(R.id.mybike_toolbar)
        setSupportActionBar(actionBar)
        supportActionBar?.title ="My Bike"
        actionBar.setNavigationOnClickListener { finish() }
        bottomNavigationView.setupWithNavController(navController)

        if(FirebaseAuth.getInstance().currentUser == null){

        val p = bottomNavigationView.menu.size()
        for(i in 1 until p) bottomNavigationView.menu.getItem(i).isEnabled = false

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //sign out
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                overridePendingTransition(0, 0)
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onRestart() {
        super.onRestart()
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}