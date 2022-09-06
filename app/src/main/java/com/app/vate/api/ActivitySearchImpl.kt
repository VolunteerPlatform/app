package com.app.vate.api

import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.model.ActivitySession
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ActivitySearchImpl : ActivitySearch {
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

    companion object {
        const val SERVER_URL = "http://15.164.213.232:8080/"
    }
}