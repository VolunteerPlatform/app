package com.app.vate.model

import java.time.DayOfWeek

data class TimeTableElement(
    val dayOfWeek: DayOfWeek,
    var startTime: Int,
    var endTime: Int
)
