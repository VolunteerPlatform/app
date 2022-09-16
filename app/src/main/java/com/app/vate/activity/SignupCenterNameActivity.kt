package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupCenterNameActivityBinding
import com.app.vate.databinding.SignupNameActivityBinding

class SignupCenterNameActivity: AppCompatActivity() {

    private lateinit var binding: SignupCenterNameActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupCenterNameActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUp)
        button.setOnClickListener {
            val phoneNumberActivity = Intent(this, SignupPhoneNumberActivity::class.java)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()
            val idOf1365 = intent.getStringExtra("idOf1365").toString()
            val gender = intent.getStringExtra("gender").toString()
            val centerName = intent.getStringExtra("centerName").toString()

            val editText: EditText = findViewById(R.id.editTextCenterName)
            phoneNumberActivity.putExtra("memberName", memberName)
            phoneNumberActivity.putExtra("email", email)
            phoneNumberActivity.putExtra("password", password)
            phoneNumberActivity.putExtra("birthday", birthday)
            phoneNumberActivity.putExtra("idOf1365", idOf1365)
            phoneNumberActivity.putExtra("gender", gender)
            phoneNumberActivity.putExtra("centerName", centerName)
            phoneNumberActivity.putExtra("centerName", editText.text.toString())

            startActivity(phoneNumberActivity)
        }

        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupId1365Activity::class.java)
            startActivity(back)
        }
    }
}