package com.example.finalpro

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalpro.adapter.FreeCourseAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FreeCoursesActivity : AppCompatActivity(), FreeCourseAdapter.OnItemClickListener {

    private lateinit var dbref: DatabaseReference
    private lateinit var freeCourseRecyclerView: RecyclerView
    private lateinit var freeCourseArrayList: ArrayList<FreeCourse>
    private lateinit var sharedPreferences: SharedPreferences  // SharedPreferences instance for storing registration status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_courses)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("freecourse_prefs", Context.MODE_PRIVATE)

        freeCourseRecyclerView = findViewById(R.id.freeCourseList)
        freeCourseRecyclerView.layoutManager = LinearLayoutManager(this)
        freeCourseRecyclerView.setHasFixedSize(true)

        freeCourseArrayList = arrayListOf()
        getFreeCourseData()
    }

    private fun getFreeCourseData() {
        Log.d("FreeCoursesActivity", "Fetching data from Firebase")
        dbref = FirebaseDatabase.getInstance().getReference("FreeCourses")

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    freeCourseArrayList.clear()
                    for (courseSnapshot in snapshot.children) {
                        val course = courseSnapshot.getValue(FreeCourse::class.java)
                        if (course != null && !course.courseName.isNullOrEmpty() && !course.courseDuration.isNullOrEmpty() && !course.courseProvider.isNullOrEmpty() && !course.courseURL.isNullOrEmpty()) {
                            // Retrieve registration status from SharedPreferences
                            val registered = sharedPreferences.getBoolean(course.courseName, false)
                            course.registered = registered
                            freeCourseArrayList.add(course)
                            Log.d("FreeCoursesActivity", "Free course data added: $course")
                        } else {
                            Log.e("FreeCoursesActivity", "Invalid free course data or empty fields found: $course")
                        }
                    }
                    freeCourseRecyclerView.adapter = FreeCourseAdapter(freeCourseArrayList, this@FreeCoursesActivity, sharedPreferences)
                } else {
                    Log.d("FreeCoursesActivity", "No data found in Firebase")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FreeCoursesActivity", "Failed to read data from Firebase: ${error.message}")
            }
        })
    }

    override fun onItemClick(freeCourse: FreeCourse) {
        val url = freeCourse.courseURL
        if (!url.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } else {
            Log.e("FreeCoursesActivity", "Empty URL for free course: $freeCourse")
            // Handle error or notify user
        }
    }
}
