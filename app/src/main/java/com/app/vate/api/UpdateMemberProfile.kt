package com.app.vate.api

import com.app.vate.model.UpdateMemberProfileForm
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.PUT

interface UpdateMemberProfile {

    @PUT("/members/profile")
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    fun updateMemberProfile(
        @Body jsonparams: UpdateMemberProfileForm
    ): Call<PostResult>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        fun create(): UpdateMemberProfile {
            return ApiClient.getClient()
                .create(UpdateMemberProfile::class.java)
        }
    }
}
