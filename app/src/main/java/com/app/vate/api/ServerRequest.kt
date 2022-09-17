package com.app.vate.api

import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.ActivitySession
import com.app.vate.model.AppHistory
import com.app.vate.model.VolActivity
import com.app.vate.model.VolOrgan
import retrofit2.Call

interface ServerRequest {

    fun searchActivity(searchCondition: SearchCondition) : Call<ServerResponse<List<ActivitySession>>>

    fun getDetailOrganizationInfo(organizationId : Long) : Call<VolOrgan>

    fun getDetailActivityInfo(activityId: Long) : Call<VolActivity>

    fun applySession(sessionId: Long, memberId : Long, comment : String, privacyApproval : String) : Call<ServerResponse<AppHistory>>
}