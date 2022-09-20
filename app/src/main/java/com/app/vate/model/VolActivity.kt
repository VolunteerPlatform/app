package com.app.vate.model

import java.io.Serializable

data class VolActivity(
    val id : Long,
    val activityMethod: ActivityMethod,
    val activityName : String,
    val category: Category,
    val activitySummary : String,
    val activityContent : String
) : Serializable