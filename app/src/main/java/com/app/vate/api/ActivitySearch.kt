package com.app.vate.api

import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.ActivitySession
import retrofit2.Call

interface ActivitySearch {

    fun searchActivity(searchCondition: SearchCondition) : Call<ServerResponse<List<ActivitySession>>>

}