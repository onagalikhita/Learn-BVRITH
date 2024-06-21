package com.example.finalpro.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalpro.FreeCourse
import com.example.finalpro.R

class FreeCourseAdapter(
    private val freeCourseList: ArrayList<FreeCourse>,
    private val listener: OnItemClickListener,
    private val sharedPreferences: SharedPreferences  // SharedPreferences instance for storing registration status
) : RecyclerView.Adapter<FreeCourseAdapter.FreeCourseViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(freeCourse: FreeCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeCourseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_free_course, parent, false)
        return FreeCourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FreeCourseViewHolder, position: Int) {
        val currentItem = freeCourseList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return freeCourseList.size
    }

    inner class FreeCourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val courseName: TextView = itemView.findViewById(R.id.tvCourseName)
        private val courseDuration: TextView = itemView.findViewById(R.id.tvCourseDuration)
        private val courseProvider: TextView = itemView.findViewById(R.id.tvCourseProvider)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(freeCourse: FreeCourse) {
            courseName.text = "Course Name: ${freeCourse.courseName}"
            courseDuration.text = "Duration: ${freeCourse.courseDuration}"
            courseProvider.text = "Provided by: ${freeCourse.courseProvider}"

            // Update UI based on registration status
            if (freeCourse.registered) {
                // Handle UI for registered state
                // Example: Change background color or text to indicate registration
                itemView.setBackgroundResource(R.color.grey)
            } else {
                // Handle UI for unregistered state
                itemView.setBackgroundResource(android.R.color.white)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Toggle registration status
                freeCourseList[position].registered = !freeCourseList[position].registered
                listener.onItemClick(freeCourseList[position])

                // Save registration status in SharedPreferences
                saveRegistrationStatus(freeCourseList[position])

                // Update UI after registration status change
                notifyItemChanged(position)
            }
        }

        private fun saveRegistrationStatus(freeCourse: FreeCourse) {
            // Update SharedPreferences with registration status
            val editor = sharedPreferences.edit()
            editor.putBoolean(freeCourse.courseName, freeCourse.registered)
            editor.apply()
        }
    }
}
