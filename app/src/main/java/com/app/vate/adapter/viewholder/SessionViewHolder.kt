package com.app.vate.adapter.viewholder

import android.content.Intent
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.activity.VolunteerDetailActivity
import com.app.vate.model.ActivityMethod
import com.app.vate.model.ActivitySession
import com.app.vate.model.Category
import com.app.vate.util.ActivityTimeFormatter

class SessionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val activityNameTextView : TextView = view.findViewById(R.id.activityName)
    private val organizationNameTextView : TextView = view.findViewById(R.id.organizationName)
    private val sessionDateTimeTextView : TextView = view.findViewById(R.id.sessionDateTime)
    private val checkedSessionStar : RatingBar = view.findViewById(R.id.checkedSession)
    private val categoryTextView : TextView = view.findViewById(R.id.category)
    private val activityMethodTextView: TextView = view.findViewById(R.id.activity_method)

    fun bind(item : ActivitySession) {
        activityNameTextView.text = item.activityName
        organizationNameTextView.text = item.organizationName
        sessionDateTimeTextView.text = ActivityTimeFormatter.convertDateAndTime(item.activityDate, item.startTime, item.endTime)
        checkedSessionStar.rating = 0F
        categoryTextView.text = Category.valueOf(item.category).description
        activityMethodTextView.text = ActivityMethod.valueOf(item.activityMethod).description
        addOnClickListener(item)
    }

    private fun addOnClickListener(item: ActivitySession) {
        val activityNameTextView = view.findViewById<TextView>(R.id.activityName)
        activityNameTextView.setOnClickListener {
            val intent = Intent(view.context, VolunteerDetailActivity::class.java)
            intent.putExtra("activitySession", item)
            view.context.startActivity(intent)
        }
    }
}