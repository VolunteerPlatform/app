package com.app.vate.model

import java.io.Serializable
import java.time.LocalDate

data class ActivitySession(
    var activityId: Long = 0,
    var sessionId: Long = 0,
    var organizationId: Long = 0,
    var activityName: String = "",
    var organization: String = "",
    var activityDate: LocalDate,
    var startTime: Int,
    var endTime: Int,
    var category: String,
    var activityMethod: String,
    var longitude: Double,
    var latitude: Double,
    var isWished: Boolean
) : Serializable
