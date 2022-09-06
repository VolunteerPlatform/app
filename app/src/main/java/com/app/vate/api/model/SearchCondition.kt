package com.app.vate.api.model

import java.io.Serializable
import java.time.LocalDate

data class SearchCondition(
    var longitude: Double,
    var latitude: Double,
    var category: String? = null,
    var startDate: LocalDate? = null,
    var endDate: LocalDate? = null
) : Serializable
