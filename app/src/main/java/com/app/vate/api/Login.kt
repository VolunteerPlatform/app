package com.app.vate.api

import com.app.vate.model.LoginForm
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Login {

    @POST("/members/login")
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    fun loginInfo(
        @Body jsonparams: LoginForm
    ): Call<PostResult>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://10.0.2.2:8080"
        fun create(): Login {

            val gson: Gson = GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(Login::class.java)
        }
    }
}