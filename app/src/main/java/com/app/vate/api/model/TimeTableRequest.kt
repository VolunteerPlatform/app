package com.app.vate.api.model

import com.app.vate.model.TimeTableElement

data class TimeTableRequest(
    val elements: List<TimeTableElement>
)
