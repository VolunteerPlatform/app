package com.app.vate.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.vate.R
import com.app.vate.adapter.AppHistoryListAdapter
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.AppHistory
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.stream.Collectors

class ActivityHistoryFragment : Fragment() {

    private var serverRequest = ServerRequestImpl()
    private lateinit var currentContext: Context
    private var appHistory : MutableList<AppHistory> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.context.let {
            currentContext = container!!.context
        }

        return inflater.inflate(R.layout.fragment_activity_history, container, false)
    }

    override fun onResume() {
        super.onResume()
        getApplicationList()
        initTabMenu()
    }

    private fun initTabMenu() {
        view?.findViewById<TabLayout>(R.id.optionTabMenu)
            ?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position == 0) {
                        initAdapter(historyFilter(appHistory, "APPROVAL"))
                    }

                    if (tab?.position == 1) {
                        initAdapter(historyFilter(appHistory, "COMPLETE"))
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
    }

    fun getApplicationList() {
        serverRequest.getApplicationList()
            .enqueue(object : Callback<ServerResponse<List<AppHistory>>> {
                override fun onResponse(
                    call: Call<ServerResponse<List<AppHistory>>>,
                    response: Response<ServerResponse<List<AppHistory>>>
                ) {
                    appHistory = response.body()?.result?.toMutableList()?: mutableListOf()
                    initAdapter(historyFilter(appHistory, "APPROVAL"))
                }

                override fun onFailure(call: Call<ServerResponse<List<AppHistory>>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun initAdapter(applicationList: MutableList<AppHistory>) {
        val appHistoryListAdapter = AppHistoryListAdapter(currentContext)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.appHistoryRecycler)
        recyclerView?.adapter = appHistoryListAdapter

        appHistoryListAdapter.applicationList = applicationList
        appHistoryListAdapter.notifyDataSetChanged()
    }

    fun historyFilter(applicationList: MutableList<AppHistory>, isAuthorized : String) : MutableList<AppHistory> {
        return applicationList
            .stream()
            .filter {
                it.isAuthorized == isAuthorized
            }.collect(Collectors.toList())
    }

    companion object {
        @JvmStatic
        fun newInstance(): ActivityHistoryFragment {
            return ActivityHistoryFragment()
        }
    }
}