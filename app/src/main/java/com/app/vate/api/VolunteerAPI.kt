package com.app.vate.api

import com.app.vate.api.model.ServerResponse
import com.app.vate.model.*
import retrofit2.Call
import retrofit2.http.*
import java.time.LocalDate

interface VolunteerAPI {

    @GET("/vol/activities")
    fun searchActivity(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("category") category: String?,
        @Query("startDate") startDate: LocalDate?,
        @Query("endDate") endDate: LocalDate?
    ): Call<ServerResponse<List<ActivitySession>>>

    @GET("/vol/organizations/{organizationId}")
    fun getDetailOrganizationInfo(
        @Path("organizationId") organizationId: Long
    ): Call<VolOrgan>

    @GET("/vol/activities/{activityId}")
    fun getDetailActivityInfo(
        @Path("activityId") activityId: Long
    ) : Call<VolActivity>

    @POST("/vol/sessions/{sessionId}")
    fun applySession(
        @Path("sessionId") sessionId: Long,
        @Body json: ApplicationForm
    ) : Call<ServerResponse<AppHistory>>

    @GET("/members/application")
    fun getApplicationList(
        @Query("id") memberId: Long,
    ) : Call<ServerResponse<List<AppHistory>>>
}