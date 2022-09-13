package com.app.vate.api

import com.app.vate.api.model.ServerResponse
import com.app.vate.model.ActivitySession
import com.app.vate.model.VolActivity
import com.app.vate.model.VolOrgan
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
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
}