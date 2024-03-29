package com.app.vate.adapter.viewholder

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.activity.ApplicationDetailActivity
import com.app.vate.model.AppHistory
import com.app.vate.util.ActivityTimeFormatter

class AppHistoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val activityNameTextView: TextView = view.findViewById(R.id.activityName)
    private val organizationNameTextView: TextView = view.findViewById(R.id.organizationName)
    private val sessionDateTimeTextView: TextView = view.findViewById(R.id.sessionDateTime)
    private val authorizedStatus: TextView = view.findViewById(R.id.authorizedStatus)
    private val categoryTextView: TextView = view.findViewById(R.id.category)
    private val activityMethodTextView: TextView = view.findViewById(R.id.activity_method)

    fun bind(item: AppHistory) {
        activityNameTextView.text = item.activityName
        organizationNameTextView.text = item.organization
        sessionDateTimeTextView.text = ActivityTimeFormatter.convertDateAndTime(
            item.activityDate,
            item.startTime,
            item.endTime
        )

        val background =
            view.findViewById<TextView>(R.id.authorizedStatus).background as GradientDrawable
        when (item.isAuthorized) {
            "APPROVAL" -> {
                background.color = ColorStateList.valueOf(Color.parseColor("#FFE7DA"))
                authorizedStatus.text = "승인\n완료"
            }
            "DISAPPROVAL" -> {
                background.color = ColorStateList.valueOf(Color.parseColor("#E5E5E5"))
                authorizedStatus.text = "승인\n거절"
            }
            "WAITING" -> {
                background.color = ColorStateList.valueOf(Color.parseColor("#E5E5E5"))
                authorizedStatus.text = "승인\n대기"
            }
            "COMPLETE" -> {
                background.color = ColorStateList.valueOf(Color.parseColor("#E5E5E5"))
                authorizedStatus.text = "활동\n완료"
            }
            "CANCELED" -> {
                background.color = ColorStateList.valueOf(Color.parseColor("#E5E5E5"))
                authorizedStatus.text = "취소\n완료"
            }
        }

        categoryTextView.text = item.category?.description ?: "카테고리"
        activityMethodTextView.text = item.activityMethod?.description ?: "온라인"
        addOnClickListener(item)
    }

    private fun addOnClickListener(item: AppHistory) {
        val activityNameTextView = view.findViewById<TextView>(R.id.activityName)
        activityNameTextView.setOnClickListener {
            if (item.isAuthorized == "APPROVAL") {
                val intent = Intent(view.context, ApplicationDetailActivity::class.java)
                intent.putExtra("appHistory", item)
                view.context.startActivity(intent)
            }
        }
    }
}