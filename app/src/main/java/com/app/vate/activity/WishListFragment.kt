package com.app.vate.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.adapter.WishListAdapter
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.ActivitySession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishListFragment : Fragment() {
    private lateinit var currentContext: Context
    private val serverRequest = ServerRequestImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.context?.let {
            currentContext = container.context
        }

        getWishList()
        return inflater.inflate(R.layout.fragment_wishlist, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    fun getWishList() {
        serverRequest.getWishList().enqueue(object : Callback<ServerResponse<List<ActivitySession>>> {
            override fun onResponse(
                call: Call<ServerResponse<List<ActivitySession>>>,
                response: Response<ServerResponse<List<ActivitySession>>>
            ) {
                response.body()?.result?.let {
                    initAdapter(it.toMutableList())
                }
            }

            override fun onFailure(call: Call<ServerResponse<List<ActivitySession>>>, t: Throwable) {
                Toast.makeText(currentContext, "서버와 통신중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun initAdapter(sessionList: MutableList<ActivitySession>) {
        val wishListAdapter = WishListAdapter(currentContext)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.wishListRecycler)
        recyclerView?.adapter = wishListAdapter

        wishListAdapter.sessionList = sessionList
        wishListAdapter.notifyDataSetChanged()
    }



    companion object {
        @JvmStatic
        fun newInstance(): WishListFragment {
            return WishListFragment()
        }
    }
}