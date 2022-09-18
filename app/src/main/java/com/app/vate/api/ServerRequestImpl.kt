package com.app.vate.api

import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ServerRequestImpl : ServerRequest {
    override fun searchActivity(searchCondition: SearchCondition): Call<ServerResponse<List<ActivitySession>>> {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDate.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                })
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        val api = retrofit.create(VolunteerAPI::class.java)
        val callGetSearchActivity = api.searchActivity(
            searchCondition.longitude,
            searchCondition.latitude,
            searchCondition.category,
            searchCondition.startDate,
            searchCondition.endDate
        )

        return callGetSearchActivity;
    }

    override fun getDetailOrganizationInfo(organizationId: Long): Call<VolOrgan> {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(VolunteerAPI::class.java)
        return api.getDetailOrganizationInfo(organizationId)
    }

    override fun getDetailActivityInfo(activityId: Long): Call<VolActivity> {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(VolunteerAPI::class.java)
        return api.getDetailActivityInfo(activityId)
    }

    override fun applySession(sessionId: Long, memberId : Long, comment : String, privacyApproval : String) : Call<ServerResponse<AppHistory>> {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDate.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                })
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        val api = retrofit.create(VolunteerAPI::class.java)
        val applicationForm = ApplicationForm(memberId, comment, privacyApproval)
        return api.applySession(sessionId, applicationForm)
    }

    override fun getApplicationList(memberId: Long): Call<ServerResponse<List<AppHistory>>> {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDate::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDate.parse(
                        json.asString,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                })
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        val api = retrofit.create(VolunteerAPI::class.java)
        return api.getApplicationList(memberId)
    }

    companion object {
        const val SERVER_URL = "http://192.168.0.25:8080/"
    }
}