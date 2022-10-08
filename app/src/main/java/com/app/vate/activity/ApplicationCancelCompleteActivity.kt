package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.databinding.ActivityCancelCompleteActivityBinding
import com.app.vate.model.AppHistory
import com.app.vate.util.ActivityTimeFormatter

class ApplicationCancelCompleteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCancelCompleteActivityBinding
    private lateinit var appHistory: AppHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelCompleteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getSerializableExtra("appHistory")?.let {
            appHistory = intent.getSerializableExtra("appHistory") as AppHistory
        }

        initButton()
        bindContent()
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
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun bindContent() {
        binding.applicationActivityName.text = appHistory.activityName
        binding.applicationOrganizationName.text = appHistory.organization
        binding.applicationActivityTime.text = ActivityTimeFormatter.convertDateAndTime(
            appHistory.activityDate,
            appHistory.startTime,
            appHistory.endTime
        )
    }

}