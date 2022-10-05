package com.app.vate.api

import com.app.vate.model.Member
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Signup {

    @POST("/members/signup")
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    fun post_users(
        @Body jsonparams: Member
    ): Call<PostResult>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        fun create(): Signup {
            return ApiClient
                .getClient()
                .create(Signup::class.java)
        }
    }
}