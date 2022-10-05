package com.app.vate.adapter.viewholder

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.activity.VolunteerDetailActivity
import com.app.vate.adapter.WishListAdapter
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.ActivityMethod
import com.app.vate.model.ActivitySession
import com.app.vate.model.Category
import com.app.vate.util.ActivityTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val activityNameTextView: TextView = view.findViewById(R.id.activityName)
    private val organizationNameTextView: TextView = view.findViewById(R.id.organizationName)
    private val sessionDateTimeTextView: TextView = view.findViewById(R.id.sessionDateTime)
    private val categoryTextView: TextView = view.findViewById(R.id.category)
    private val activityMethodTextView: TextView = view.findViewById(R.id.activity_method)
    private val cancelButton: Button = view.findViewById(R.id.wishCancelButton)

    fun bind(item: ActivitySession) {
        activityNameTextView.text = item.activityName
        organizationNameTextView.text = item.organization
        sessionDateTimeTextView.text = ActivityTimeFormatter.convertDateAndTime(
            item.activityDate,
            item.startTime,
            item.endTime
        )

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

        cancelButton.setOnClickListener {
            ServerRequestImpl().callWishList(item.sessionId).enqueue(object :
                Callback<ServerResponse<String>> {
                override fun onResponse(
                    call: Call<ServerResponse<String>>,
                    response: Response<ServerResponse<String>>
                ) {
                    var adapter = bindingAdapter as WishListAdapter
                    adapter.sessionList.removeAt(bindingAdapterPosition)
                    adapter.notifyDataSetChanged()

                    Toast.makeText(view.context, "정상 처리 되었습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ServerResponse<String>>, t: Throwable) {
                    Toast.makeText(view.context, "찜하기에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}