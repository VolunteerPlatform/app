package com.app.vate.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.databinding.ApplicationCompleteActivityBinding
import com.app.vate.model.AppHistory
import com.app.vate.util.ActivityTimeFormatter

class ApplicationCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ApplicationCompleteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationCompleteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindContent(intent.getSerializableExtra("appHistory") as AppHistory)
        initButton()
    }

    private fun initButton() {
        binding.completeButton.setOnClickListener {
            finishHandler()
        }

        binding.bottomCompleteButton.setOnClickListener {
            finishHandler()
        }
    }

    override fun onBackPressed() {
        finishHandler()
    }

    private fun finishHandler() {
        val intent = Intent(this, MapActivity::class.java)
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun bindContent(appHistory: AppHistory) {
        binding.applicationActivityName.text = appHistory.activityName
        binding.applicationOrganizationName.text = appHistory.organization
        binding.applicationActivityTime.text = ActivityTimeFormatter.convertDateAndTime(
            appHistory.activityDate,
            appHistory.startTime,
            appHistory.endTime
        )
    }
}