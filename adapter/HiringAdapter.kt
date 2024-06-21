package com.example.finalpro.adapter

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalpro.HiringChallenge
import com.example.finalpro.R

class HiringAdapter(
    private val hiringList: ArrayList<HiringChallenge>,
    private val listener: OnItemClickListener,
    private val sharedPreferences: SharedPreferences // SharedPreferences instance for storing registration status
) : RecyclerView.Adapter<HiringAdapter.HiringViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(hiringChallenge: HiringChallenge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiringViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_hiringchallenges, parent, false)
        return HiringViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HiringViewHolder, position: Int) {
        val currentItem = hiringList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return hiringList.size
    }

    inner class HiringViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val challengeName: TextView = itemView.findViewById(R.id.tvChallengeName)
        private val eligibility: TextView = itemView.findViewById(R.id.tvEligibility)
        private val registerDeadline: TextView = itemView.findViewById(R.id.tvRegisterDeadline)
        private val firstPhaseDate: TextView = itemView.findViewById(R.id.tvFirstPhaseDate)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(hiringChallenge: HiringChallenge) {
            challengeName.text = "Challenge Name: ${hiringChallenge.challengeName}"
            eligibility.text = "Eligibility: ${hiringChallenge.eligibility}"
            registerDeadline.text = "Register Deadline: ${hiringChallenge.registerDeadline}"
            firstPhaseDate.text = "1st Phase Date: ${hiringChallenge.firstPhaseDate}"

            // Update UI based on registration status
            if (hiringChallenge.registered) {
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
                hiringList[position].registered = !hiringList[position].registered
                listener.onItemClick(hiringList[position])

                // Save registration status in SharedPreferences
                saveRegistrationStatus(hiringList[position])

                // Update UI after registration status change
                notifyDataSetChanged()
            }
        }

        private fun saveRegistrationStatus(hiringChallenge: HiringChallenge) {
            // Update SharedPreferences with registration status
            val editor = sharedPreferences.edit()
            editor.putBoolean(hiringChallenge.challengeName, hiringChallenge.registered)
            editor.apply()
        }
    }
}
