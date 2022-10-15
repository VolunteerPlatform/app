package com.app.vate.api

import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.*
import retrofit2.Call

interface ServerRequest {

    fun searchActivity(searchCondition: SearchCondition): Call<ServerResponse<List<ActivitySession>>>

    fun getDetailOrganizationInfo(organizationId: Long): Call<ServerResponse<VolOrgan>>

    fun getDetailActivityInfo(activityId: Long): Call<ServerResponse<VolActivity>>

    fun applySession(
        sessionId: Long,
        comment: String,
        privacyApproval: String
    ): Call<ServerResponse<AppHistory>>

    fun getApplicationList(): Call<ServerResponse<List<AppHistory>>>

    fun callWishList(sessionId: Long): Call<ServerResponse<String>>

    fun getWishList(): Call<ServerResponse<List<ActivitySession>>>

    fun cancelApplication(applicationId: Long): Call<ServerResponse<String>>

    fun registerTimetable(timeTableElements: List<TimeTableElement>): Call<ServerResponse<String>>

    fun getTimetable(): Call<ServerResponse<List<TimeTableElement>>>

    fun getMemberProfile(): Call<ServerResponse<MemberProfile>>

    fun updateMemberProfile(updateMemberProfileForm: UpdateMemberProfileForm): Call<ServerResponse<UpdateMemberProfileForm>>
}
