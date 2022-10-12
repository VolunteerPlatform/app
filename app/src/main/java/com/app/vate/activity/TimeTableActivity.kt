package com.app.vate.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatToggleButton
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.databinding.TimetableActivityBinding
import com.app.vate.model.TimeTableElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.util.*

class TimeTableActivity : AppCompatActivity() {

    private lateinit var binding : TimetableActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TimetableActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getTimetable()

        binding.submitButton.setOnClickListener {
            submitTimetable(parseTable())
            onBackPressed()
        }
    }

    private fun parseTable() : List<TimeTableElement> {
        val elementList = ArrayDeque<TimeTableElement>()

        for (dayOfWeekIndex in 0 until dayOfWeekList.size) {
            var previousChecked = false
            for (timeIndex in 0 until timeList.size) {
                val id = resources.getIdentifier(dayOfWeekList[dayOfWeekIndex].plus(timeList[timeIndex]), "id", packageName)
                if (findViewById<AppCompatToggleButton>(id).isChecked) {
                    if (previousChecked && elementList.isNotEmpty()) {
                        val previousElement = elementList.pollLast()
                        previousElement.endTime = timeIndex + 10;
                        elementList.addLast(previousElement)
                    } else {
                        elementList.addLast(TimeTableElement(DayOfWeek.valueOf(dayOfWeekList[dayOfWeekIndex].uppercase()), timeIndex + 9, timeIndex + 10))
                    }

                    previousChecked = true
                } else {
                    previousChecked = false
                }
            }
        }

        return elementList.toList()
    }

    private fun fillTable(timeTableElements : List<TimeTableElement>) {
        for (element in timeTableElements) {
            for (timeIndex in element.startTime until element.endTime) {
                val id = resources.getIdentifier(dayOfWeekList[element.dayOfWeek.value - 1].plus(timeList[timeIndex - 9]), "id", packageName)
                findViewById<AppCompatToggleButton>(id).isChecked = true
            }
        }
    }

    private fun getTimetable() {
        ServerRequestImpl().getTimetable()
            .enqueue(object : Callback<ServerResponse<List<TimeTableElement>>> {
                override fun onResponse(
                    call: Call<ServerResponse<List<TimeTableElement>>>,
                    response: Response<ServerResponse<List<TimeTableElement>>>
                ) {
                    response.body()?.message?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    }

                    response.body()?.result?.let {
                        fillTable(it)
                    }

                    if (!response.isSuccessful) {
                        val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        Toast.makeText(applicationContext, "${jsonObject?.get("message")}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(
                    call: Call<ServerResponse<List<TimeTableElement>>>,
                    t: Throwable
                ) {
                    Toast.makeText(applicationContext, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun submitTimetable(timeTableElements : List<TimeTableElement>) {
        ServerRequestImpl().registerTimetable(timeTableElements)
            .enqueue(object : Callback<ServerResponse<String>> {
                override fun onResponse(
                    call: Call<ServerResponse<String>>,
                    response: Response<ServerResponse<String>>
                ) {
                    if (!response.isSuccessful) {
                        val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        Toast.makeText(applicationContext, "${jsonObject?.get("message")}", Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(applicationContext, "정상 처리되었습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ServerResponse<String>>, t: Throwable) {
                    Toast.makeText(applicationContext, "서버와 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    companion object {
        val dayOfWeekList = mutableListOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
        val timeList = mutableListOf("09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21")
    }
}