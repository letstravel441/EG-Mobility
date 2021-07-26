package com.narendar.letstravel.mybike

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.slider.RangeSlider
import com.narendar.letstravel.R
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class FilterActivity : AppCompatActivity() {

    var bodyTypes: ArrayList<String> = ArrayList()
    var brands: ArrayList<String> = ArrayList()
    var colours: ArrayList<String> = ArrayList()
    var ageOfBike: ArrayList<Int> = ArrayList()
    var minPrice : String ?= null
    var maxPrice : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        //back button
        val actionBar = supportActionBar
        // showing the back button in action bar
        actionBar!!.title = "Filter"
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)


        //price filter
        val rangeSlider = findViewById<RangeSlider>(R.id.slider_price_range)
        rangeSlider.setLabelFormatter {
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("INR")
            format.format(it.toDouble())
        }

        rangeSlider.addOnChangeListener { slider, value, fromUser ->
            val vals = slider.values
            minPrice = vals[0].toString()
            maxPrice = vals[1].toString()
        }


        //brand filter
        val bmwCheckBox = findViewById<CheckBox>(R.id.bmw_checkbox)
        bmwCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("bmw")
            }
        }
        val heroCheckBox = findViewById<CheckBox>(R.id.hero_checkbox)
        heroCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("hero")
            }
        }
        val hondaCheckBox = findViewById<CheckBox>(R.id.honda_checkbox)
        hondaCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("honda")
            }
        }
        val yamahaCheckBox = findViewById<CheckBox>(R.id.yamaha_checkbox)
        yamahaCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("yamaha")
            }
        }
        val bajajCheckBox = findViewById<CheckBox>(R.id.bajaj_checkbox)
        bajajCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("bajaj")
            }
        }
        val kawasakiCheckBox = findViewById<CheckBox>(R.id.kawasaki_checkbox)
        kawasakiCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("kawasaki")
            }
        }
        val tvsCheckBox = findViewById<CheckBox>(R.id.tvs_checkbox)
        tvsCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("tvs")
            }
        }
        val ktmCheckBox = findViewById<CheckBox>(R.id.ktm_checkbox)
        ktmCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("ktm")
            }
        }
        val mahindraCheckBox = findViewById<CheckBox>(R.id.mahindra_checkbox)
        mahindraCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("mahindra")
            }
        }
        val suzukiCheckBox = findViewById<CheckBox>(R.id.suzuki_checkbox)
        suzukiCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("suzuki")
            }
        }
        val royalEnfieldCheckBox = findViewById<CheckBox>(R.id.royal_enfield_checkbox)
        royalEnfieldCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("royal enfield")
            }
        }
        val vespaCheckBox = findViewById<CheckBox>(R.id.vespa_checkbox)
        vespaCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("vespa")
            }
        }
        val jawaCheckBox = findViewById<CheckBox>(R.id.jawa_checkbox)
        jawaCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("jawa")
            }
        }
        val husqvarnaCheckBox = findViewById<CheckBox>(R.id.husqvarna_checkbox)
        husqvarnaCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                brands.add("husqvarna")
            }
        }


        //body type filter
        val commuterCheckBox = findViewById<CheckBox>(R.id.commuter_checkbox)
        commuterCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("commuter")
            }
        }
        val cruiserCheckBox = findViewById<CheckBox>(R.id.cruiser_checkbox)
        cruiserCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("cruiser")
            }
        }
        val mopedCheckBox = findViewById<CheckBox>(R.id.moped_checkbox)
        mopedCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("moped")
            }
        }
        val nakedCheckBox = findViewById<CheckBox>(R.id.naked_checkbox)
        nakedCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("naked")
            }
        }
        val offroadCheckBox = findViewById<CheckBox>(R.id.offroad_checkbox)
        offroadCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("offroad")
            }
        }
        val scooterCheckBox = findViewById<CheckBox>(R.id.scooter_checkbox)
        scooterCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("scooter")
            }
        }
        val sportsCheckBox = findViewById<CheckBox>(R.id.sports_checkbox)
        sportsCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                bodyTypes.add("sports")
            }
        }


        //colour filter
        val whiteCheckBox = findViewById<CheckBox>(R.id.white_checkbox)
        whiteCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("white")
            }
        }
        val redCheckBox = findViewById<CheckBox>(R.id.red_checkbox)
        redCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("red")
            }
        }
        val pinkCheckBox = findViewById<CheckBox>(R.id.pink_checkbox)
        pinkCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("pink")
            }
        }
        val purpleCheckBox = findViewById<CheckBox>(R.id.purple_checkbox)
        purpleCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("purple")
            }
        }
        val blueCheckBox = findViewById<CheckBox>(R.id.blue_checkbox)
        blueCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("blue")
            }
        }
        val yellowCheckBox = findViewById<CheckBox>(R.id.yellow_checkbox)
        yellowCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("yellow")
            }
        }
        val greenCheckBox = findViewById<CheckBox>(R.id.green_checkbox)
        greenCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("green")
            }
        }
        val orangeCheckBox = findViewById<CheckBox>(R.id.orange_checkbox)
        orangeCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("orange")
            }
        }
        val silverCheckBox = findViewById<CheckBox>(R.id.silver_checkbox)
        silverCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("silver")
            }
        }
        val blackCheckBox = findViewById<CheckBox>(R.id.black_checkbox)
        blackCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("black")
            }
        }
        val brownCheckBox = findViewById<CheckBox>(R.id.brown_checkbox)
        brownCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                colours.add("brown")
            }
        }


        //age of bike filter
        val oneYearCheckBox = findViewById<CheckBox>(R.id.last_1_year_checkbox)
        oneYearCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                ageOfBike.add(1)
            }
        }
        val twoYearsCheckBox = findViewById<CheckBox>(R.id.last_2_years_checkbox)
        twoYearsCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                ageOfBike.add(2)
            }
        }
        val threeYearsCheckBox = findViewById<CheckBox>(R.id.last_3_years_checkbox)
        threeYearsCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                ageOfBike.add(3)
            }
        }
        val fiveYearsCheckBox = findViewById<CheckBox>(R.id.last_5_years_checkbox)
        fiveYearsCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                ageOfBike.add(5)
            }
        }
        val tenYearsCheckBox = findViewById<CheckBox>(R.id.last_10_years_checkbox)
        tenYearsCheckBox.setOnCheckedChangeListener { checkBox, isChecked ->
            if (isChecked) {
                ageOfBike.add(10)
            }
        }


        //applying filters
        val apply = findViewById<TextView>(R.id.apply)
        apply.setOnClickListener {

            if ((minPrice == 0.toString() || minPrice.isNullOrEmpty()) && (maxPrice == 200000.toString() || maxPrice.isNullOrEmpty()) && brands.isEmpty() && bodyTypes.isEmpty() && colours.isEmpty() && ageOfBike.isEmpty()) {

                Toast.makeText(this, "No Filters Selected", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MBMainActivity::class.java)
                startActivity(intent)

            } else {

                val intent = Intent(this, FilteredProducts::class.java)

                if (minPrice.isNullOrEmpty()) {
                    minPrice = 0.toString()
                }
                if (maxPrice.isNullOrEmpty()) {
                    maxPrice = 200000.toString()
                }

                intent.putExtra("minPrice", minPrice)
                intent.putExtra("maxPrice", maxPrice)
                intent.putExtra("brands", brands)
                intent.putExtra("bodyTypes", bodyTypes)
                intent.putExtra("colours", colours)
                intent.putExtra("ageOfBike", ageOfBike)

                startActivity(intent)
            }

        }

    }
}