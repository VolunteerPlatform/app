package com.app.vate.api

import com.app.vate.model.FindPasswordForm
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FindPassword {

    @POST("/members/password-inquiry")
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    fun findPassword(
        @Body jsonparams: FindPasswordForm
    ): Call<PostResult>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        fun create(): FindPassword {
            return ApiClient
                .getClient()
                .create(FindPassword::class.java)
        }
    }
}
