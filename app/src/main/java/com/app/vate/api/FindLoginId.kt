package com.app.vate.api

import com.app.vate.model.FindLoginIdForm
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FindLoginId {

    @POST("/members/id-inquiry")
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    fun findLoginId(
        @Body jsonparams: FindLoginIdForm
    ): Call<PostResult>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        fun create(): FindLoginId {
            return ApiClient
                .getClient()
                .create(FindLoginId::class.java)
        }
    }
}
