package com.app.vate.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class ActivityTimeFormatter {
    companion object {
        fun convertDateAndTime(activityDate : LocalDate, startTime: Int, endTime: Int) : String {
            val stringBuilder = StringBuilder()

            stringBuilder.append(activityDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")).toString())
            stringBuilder.append(String.format("(%s)", activityDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN)))
            stringBuilder.append(" ")

            if (startTime in 0..11) {
                stringBuilder.append("오전 ".plus(startTime) + "시")
            } else {
                var startTimeMinus = startTime.minus(12)
                if (startTimeMinus == 0) {
                    startTimeMinus = 12;
                }

                stringBuilder.append("오후 ".plus(startTimeMinus) + "시")
            }

            stringBuilder.append(" - ")

            if (endTime in 0..11) {
                stringBuilder.append("오전 ".plus(endTime) + "시")
            } else {
                var endTimeMinus = endTime.minus(12)
                if (endTimeMinus == 0) {
                    endTimeMinus = 12;
                }

                stringBuilder.append("오후 ".plus(endTimeMinus) + "시")
            }

            return stringBuilder.toString()
        }
    }
}