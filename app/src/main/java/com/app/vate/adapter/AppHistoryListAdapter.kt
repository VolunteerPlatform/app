package com.app.vate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.adapter.viewholder.AppHistoryViewHolder
import com.app.vate.model.AppHistory

class AppHistoryListAdapter(private val context: Context) : RecyclerView.Adapter<AppHistoryViewHolder>() {

    var applicationList = mutableListOf<AppHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHistoryViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_history_item, parent, false)
        return AppHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppHistoryViewHolder, position: Int) {
        holder.bind(applicationList[position])
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }
}