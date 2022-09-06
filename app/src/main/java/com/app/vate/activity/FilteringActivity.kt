package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.model.SearchCondition
import com.app.vate.databinding.FilterActivityBinding

class FilteringActivity : AppCompatActivity() {

    private lateinit var binding: FilterActivityBinding
    private lateinit var searchCondition : SearchCondition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FilterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchCondition = intent.getSerializableExtra("searchCondition") as SearchCondition
        initButton()
        preserveSelectedCategory()
    }

    private fun preserveSelectedCategory() {
        if (searchCondition.category.isNullOrBlank()) {
            return;
        }

        val pressedRadioButton = binding.categoryRadioButtonGroup.findViewWithTag<RadioButton>(searchCondition.category)
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

    // 뒤로가기 시에 searchCondition 이 넘어가지 않아서 NullPointer 발생
    override fun onBackPressed() {
        Toast.makeText(this, "선택 완료 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
    }
}