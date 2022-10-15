package com.app.vate.api

import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.api.model.TimeTableRequest
import com.app.vate.api.model.WishListForm
import com.app.vate.model.*
import retrofit2.Call

class ServerRequestImpl : ServerRequest {
    override fun searchActivity(searchCondition: SearchCondition): Call<ServerResponse<List<ActivitySession>>> {
        val api = ApiClient.getClient().create(VolunteerAPI::class.java)
        val callGetSearchActivity = api.searchActivity(
            searchCondition.longitude,
            searchCondition.latitude,
            searchCondition.category,
            searchCondition.startDate,
            searchCondition.endDate
        )

        return callGetSearchActivity;
    }

    override fun getDetailOrganizationInfo(organizationId: Long): Call<ServerResponse<VolOrgan>> {
        val api = ApiClient.getClient().create(VolunteerAPI::class.java)
        return api.getDetailOrganizationInfo(organizationId)
    }

    override fun getDetailActivityInfo(activityId: Long): Call<ServerResponse<VolActivity>> {
        val api = ApiClient.getClient().create(VolunteerAPI::class.java)
        return api.getDetailActivityInfo(activityId)
    }

    override fun applySession(sessionId: Long, comment : String, privacyApproval : String) : Call<ServerResponse<AppHistory>> {
        val api = ApiClient.getClient().create(VolunteerAPI::class.java)
        val applicationForm = ApplicationForm(comment, privacyApproval)
        return api.applySession(sessionId, applicationForm)
    }

    override fun getApplicationList(): Call<ServerResponse<List<AppHistory>>> {
        val api = ApiClient.getClient().create(VolunteerAPI::class.java)
        return api.getApplicationList()
    }

    override fun callWishList(sessionId: Long) : Call<ServerResponse<String>> {
        val wishListForm = WishListForm(sessionId)
        return ApiClient.getClient().create(VolunteerAPI::class.java).callWishList(wishListForm)
    }

    override fun getWishList(): Call<ServerResponse<List<ActivitySession>>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).getWishList()
    }

    override fun cancelApplication(applicationId: Long) : Call<ServerResponse<String>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).cancelApplication(applicationId)
    }

    override fun registerTimetable(timeTableElements: List<TimeTableElement>): Call<ServerResponse<String>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).registerTimetable(TimeTableRequest(timeTableElements))
    }

    override fun getTimetable(): Call<ServerResponse<List<TimeTableElement>>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).getTimetable()
    }

    override fun getMemberProfile(): Call<ServerResponse<MemberProfile>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).getMemberProfile()
    }

    override fun updateMemberProfile(updateMemberProfileForm: UpdateMemberProfileForm): Call<ServerResponse<UpdateMemberProfileForm>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).updateMemberProfile(updateMemberProfileForm)
    }

    override fun certificationPassword(certificationForm: CertificationForm): Call<ServerResponse<CertificationForm>> {
        return ApiClient.getClient().create(VolunteerAPI::class.java).certificationPassword(certificationForm)
    }
}