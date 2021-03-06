package com.narendar.letstravel.travelfragments
//This fragment is used when user wants to search for rides.
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.narendar.letstravel.DetailsActivity
import com.narendar.letstravel.MapsActivity
import com.narendar.letstravel.R
import com.narendar.letstravel.SearchActivity
import java.text.SimpleDateFormat
import java.util.*

;
//Declaration of views
lateinit var bookpickuplocation: EditText
lateinit var bookdroplocation: EditText
lateinit var btnbookdate: Button // 1
lateinit var bookpassengers: EditText

lateinit var textView1: TextView  //2
lateinit var textView11:TextView


lateinit var textView2:TextView  //3
lateinit var textView22:TextView

class FindFragment : Fragment() {



    lateinit var bookerimage : String
    lateinit var startPos : String
    lateinit var endPos: String
// Variables declared are used for storing date and time.
    var uday = 0
    var umonth = 0
    var uyear = 0
    var uhour = 0
    var umin = 0
    var formate = SimpleDateFormat("EEE ddMMM,yyyy hh:mm a",Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find, container, false)
//Initialisation of views
        bookpickuplocation = view.findViewById<EditText>(R.id.bookpickuplocation)
        bookdroplocation = view.findViewById<EditText>(R.id.bookdroplocation)
        val search = view.findViewById<Button>(R.id.search)
        btnbookdate = view.findViewById<Button>(R.id.btnbookdate)
        bookpassengers = view.findViewById<EditText>(R.id.bookpassengers)

        val sharedPrefernces = activity?.getSharedPreferences("production", Context.MODE_PRIVATE)





        textView1 = view.findViewById<TextView>(R.id.text_view1_find)
        textView11 = view.findViewById<TextView>(R.id.text_view11_find)


//Following Code is for AutoComplete feature where place suggestions are shown when user clicks on pick up and drop locations and store corresponding location in sharedPreference file.
        Places.initialize(requireActivity().getApplicationContext(), "AIzaSyAG5Oh9bHDBoqM3BU1S2V0f-8uuo3ZHliw")
        bookpickuplocation.isFocusable = false

        bookpickuplocation.setOnClickListener {
            val fieldList = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
            val intent = this@FindFragment.context?.let { it1 ->
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(it1)
            }
            startActivityForResult(intent, 100)
        }

        textView2 = view.findViewById<TextView>(R.id.text_view2_find)
        textView22 = view.findViewById<TextView>(R.id.text_view22_find)

//        sharedPrefernces?.edit()?.apply{
//            putString("bp", bookpassengers.text.toString())
//            putString("bbd", btnbookdate.text.toString())
//            putString("t1", textView1.text.toString())
//            putString("t2", textView2.text.toString())
//        }?.apply()

        Places.initialize(requireActivity().getApplicationContext(), "AIzaSyAG5Oh9bHDBoqM3BU1S2V0f-8uuo3ZHliw")
        bookdroplocation.isFocusable = false

        bookdroplocation.setOnClickListener {
            val fieldList = Arrays.asList(
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
                Place.Field.NAME
            )
            val intent = this@FindFragment.context?.let { it1 ->
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(it1)
            }
            startActivityForResult(intent, 50)
        }
        //Following code is used for selecting data and time by the user in Find fragment and store those values in corresponding variables
        val cal: Calendar = Calendar.getInstance()
        val date = formate.format(cal.time)
        btnbookdate.text=date
        btnbookdate.setOnClickListener {
            TimePickerDialog(
                view.context,
                TimePickerDialog.OnTimeSetListener { view: TimePicker?, hourOfDay, minute ->
                    uhour=hourOfDay
                    umin=minute
                    cal.set(Calendar.HOUR,hourOfDay)
                    cal.set(Calendar.MINUTE,minute)
                    val update = formate.format(cal.time)
                    btnbookdate.text=update
                },
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                false
            ).show()
            DatePickerDialog(
                view.context  ,
                DatePickerDialog.OnDateSetListener { view: DatePicker?, myear: Int, mmonth: Int, mday: Int ->
                    uday=mday
                    umonth=mmonth
                    uyear=myear
                    cal.set(Calendar.YEAR,myear)
                    cal.set(Calendar.MONTH,mmonth)
                    cal.set(Calendar.DAY_OF_MONTH,mday)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_WEEK)
            ).show()


        }




        search.setOnClickListener {
            if(TextUtils.isEmpty(bookpickuplocation.text.toString())) {
                bookpickuplocation.setError("Please enter Pickup Location ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(bookdroplocation.text.toString())) {
                bookdroplocation.setError("Please enter Drop Location")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(bookpassengers.text.toString())) {
                bookpassengers.setError("Please enter No of Passengers ")
                return@setOnClickListener
            }


   //         Toast.makeText(context, "-${bookpassengers.text.toString()}-${btnbookdate.text.toString()}-${textView1.text.toString()}-${textView2.text.toString()}", Toast.LENGTH_LONG).show()

            sharedPrefernces?.edit()?.apply{
                putString("bp", bookpassengers.text.toString())
                putString("bbd", btnbookdate.text.toString())
                putString("t1", textView1.text.toString())
                putString("t2", textView2.text.toString())
            }?.apply()
            val intent = Intent(context, SearchActivity ::class.java)
            intent.putExtra("start", startPos)
            intent.putExtra("end", endPos)
         //   intent.putExtra("bp", bookpassengers.text.toString())
            startActivity(intent)

            bookpickuplocation.text.clear()
            bookdroplocation.text.clear()


        }
        return view



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(
                data!!
            )
            bookpickuplocation.setText(place.address)
            textView1.text = String.format("%s", place.name)
            textView11.text = place.latLng.toString()
            startPos = place.latLng.toString()
        }else if (requestCode == 50 && resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(
                data!!
            )
            bookdroplocation.setText(place.address)
            textView2.text = String.format("%s", place.name)
            textView22.text = place.latLng.toString()
            endPos= place.latLng.toString()

        }else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(data!!)
            Toast.makeText(requireActivity().getApplicationContext(), status.statusMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
