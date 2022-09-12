package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.app.vate.api.model.SearchCondition
import com.app.vate.databinding.FilterActivityBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


class FilteringActivity : AppCompatActivity() {

    private lateinit var binding: FilterActivityBinding
    private lateinit var searchCondition: SearchCondition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FilterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchCondition = intent.getSerializableExtra("searchCondition") as SearchCondition
        initButton()
        preserveCategory()
        preserveDate()
    }

    private fun preserveCategory() {
        if (searchCondition.category.isNullOrBlank()) {
            return;
        }

        val pressedRadioButton =
            binding.categoryRadioButtonGroup.findViewWithTag<RadioButton>(searchCondition.category)
        binding.categoryRadioButtonGroup.activeRadioButton = pressedRadioButton
        pressedRadioButton.isChecked = true
    }

    private fun changeCategoryCondition() {
        val checkedRadioButtonId = binding.categoryRadioButtonGroup.checkedRadioButtonId
        if (checkedRadioButtonId == -1) {
            searchCondition.category = null
        } else {
            val pressedRadioButton = findViewById<RadioButton>(checkedRadioButtonId)
            searchCondition.category = pressedRadioButton.tag.toString()
        }
    }

    private fun initButton() {
        binding.filterChangeCompleteButton.setOnClickListener {
            changeCategoryCondition()

            val intent = Intent()
            intent.putExtra("searchCondition", searchCondition)
            setResult(200, intent)
            finish()
        }
    }

    private fun preserveDate() {
        binding.pickedDateView.text = pickedDateBinder(
            searchCondition.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            searchCondition.endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )

        val datePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Select date")
            .setSelection(
                Pair(
                    searchCondition.startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                    searchCondition.endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                )
            )
            .build()

        binding.pickedDateView.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag")
        }

        datePicker.addOnPositiveButtonClickListener {
            binding.pickedDateView.text = pickedDateBinder(it.first, it.second)
        }
    }

    // 뒤로가기 시에 searchCondition 이 넘어가지 않아서 NullPointer 발생
    override fun onBackPressed() {
        Toast.makeText(this, "선택 완료 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
    }

    private fun pickedDateBinder(startDate: Long, endDate: Long): String {
        val startDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate), ZoneId.of("UTC+9"))
        searchCondition.startDate = startDateTime.toLocalDate()

        val endDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endDate), ZoneId.of("UTC+9"))
        searchCondition.endDate = endDateTime.toLocalDate()

        val stringBuilder = StringBuilder()
        return stringBuilder.append(String.format("%02d", startDateTime.month.value))
            .append(".")
            .append(String.format("%02d", startDateTime.dayOfMonth))
            .append(" ~ ")
            .append(String.format("%02d", endDateTime.month.value))
            .append(".")
            .append(String.format("%02d", endDateTime.dayOfMonth))
            .toString()
    }
}