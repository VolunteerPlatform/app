package com.app.vate.adapter.viewholder

import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.model.ActivityMethod
import com.app.vate.model.ActivitySession
import com.app.vate.model.Category
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val activityNameTextView : TextView = view.findViewById(R.id.activityName)
    private val organizationNameTextView : TextView = view.findViewById(R.id.organizationName)
    private val sessionDateTimeTextView : TextView = view.findViewById(R.id.sessionDateTime)
    private val checkedSessionStar : RatingBar = view.findViewById(R.id.checkedSession)
    private val categoryTextView : TextView = view.findViewById(R.id.category)
    private val activityMethodTextView: TextView = view.findViewById(R.id.activity_method)

    fun bind(item : ActivitySession) {
        activityNameTextView.text = item.activityName
        organizationNameTextView.text = item.organizationName
        sessionDateTimeTextView.text = convertDateAndTime(item.activityDate, item.startTime, item.endTime)
        checkedSessionStar.rating = 0F
        categoryTextView.text = Category.valueOf(item.category).description
        activityMethodTextView.text = ActivityMethod.valueOf(item.activityMethod).description
    }

    private fun convertDateAndTime(activityDate : LocalDate, startTime: Int, endTime: Int) : String {
        val stringBuilder = StringBuilder()

        stringBuilder.append(activityDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")).toString()).append(" ")

        if (startTime in 0..11) {
            stringBuilder.append("오전 ".plus(startTime) + "시")
        } else {
            var startTimeMinus = startTime.minus(12)
            if (startTimeMinus == 0) {
                startTimeMinus = 12;
            }

            stringBuilder.append("오후 ".plus(startTimeMinus) + "시")
        }

        stringBuilder.append(" ~ ")

        if (endTime in 0..11) {
            stringBuilder.append("오전 ".plus(endTime) + "시")
        } else {
            var endTimeMinus = endTime.minus(12)
            if (endTimeMinus == 0) {
                endTimeMinus = 12;
            }

            stringBuilder.append("오후 ".plus(endTimeMinus) + "시")
        }

        return stringBuilder.toString()
    }
}