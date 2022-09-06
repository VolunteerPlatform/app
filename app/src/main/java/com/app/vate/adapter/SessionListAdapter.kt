package com.app.vate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.adapter.viewholder.SessionViewHolder
import com.app.vate.model.ActivitySession

class SessionListAdapter(private val context: Context) : RecyclerView.Adapter<SessionViewHolder>() {

    var sessionList = mutableListOf<ActivitySession>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_session_item, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(sessionList[position])
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }
}