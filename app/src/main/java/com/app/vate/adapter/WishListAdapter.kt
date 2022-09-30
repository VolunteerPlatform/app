package com.app.vate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.adapter.viewholder.WishListViewHolder
import com.app.vate.model.ActivitySession

class WishListAdapter(private val context: Context) : RecyclerView.Adapter<WishListViewHolder>() {

    var sessionList = mutableListOf<ActivitySession>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.wishlist_item, parent, false)
        return WishListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        holder.bind(sessionList[position])
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }
}