package com.app.vate.model

import java.io.Serializable
import java.time.LocalDate

data class AppHistory(
    val applicationId : Long,
    val isAuthorized : String,
    val activityDate : LocalDate,
    val startTime : Int,
    val endTime : Int,
    val activityId : Long,
    val activityName : String,
    val activitySummary: String,
    val organization : String,
    var category: Category?,
    var activityMethod : ActivityMethod?
    ) : Serializable
