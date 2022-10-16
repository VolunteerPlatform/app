package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupCenterNameActivityBinding
import com.app.vate.databinding.SignupNameActivityBinding
import com.app.vate.databinding.TendencyActivityBinding

class TendencyActivity : AppCompatActivity() {

    private lateinit var binding: TendencyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TendencyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.homeEnvironmentButton.setOnClickListener {
            setting()
        }

        binding.livingSupportButton.setOnClickListener {
            setting()
        }

        binding.counselingButton.setOnClickListener {
            setting()
        }

        binding.educationButton.setOnClickListener {
            setting()
        }

        binding.medicalButton.setOnClickListener {
            setting()
        }

        binding.ruralButton.setOnClickListener {
            setting()
        }

        binding.cultureButton.setOnClickListener {
            setting()
        }

        binding.environmentButton.setOnClickListener {
            setting()
        }

        binding.adminAssistButton.setOnClickListener {
            setting()
        }

        binding.safetyButton.setOnClickListener {
            setting()
        }

        binding.publicButton.setOnClickListener {
            setting()
        }

        binding.disasterButton.setOnClickListener {
            setting()
        }

        binding.globalButton.setOnClickListener {
            setting()
        }

        binding.mentorButton.setOnClickListener {
            setting()
        }

        binding.etcButton.setOnClickListener {
            setting()
        }
    }

    private fun setting() {
        val phoneNumberActivity = Intent(this, SignupPhoneNumberActivity::class.java)
        val email = intent.getStringExtra("email").toString()
        val password = intent.getStringExtra("password").toString()
        val memberName = intent.getStringExtra("memberName").toString()
        val birthday = intent.getStringExtra("birthday").toString()
        val idOf1365 = intent.getStringExtra("idOf1365").toString()
        val gender = intent.getStringExtra("gender").toString()
        val centerName = intent.getStringExtra("centerName").toString()

        phoneNumberActivity.putExtra("memberName", memberName)
        phoneNumberActivity.putExtra("email", email)
        phoneNumberActivity.putExtra("password", password)
        phoneNumberActivity.putExtra("birthday", birthday)
        phoneNumberActivity.putExtra("idOf1365", idOf1365)
        phoneNumberActivity.putExtra("gender", gender)
        phoneNumberActivity.putExtra("centerName", centerName)

        startActivity(phoneNumberActivity)
    }
}
