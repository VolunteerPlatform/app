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
import com.app.vate.databinding.SignupBirthdayActivityBinding

class SignupBirthdayActivity : AppCompatActivity() {

    private lateinit var binding: SignupBirthdayActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBirthdayActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUp)
        button.setOnClickListener {
            val genderInfo = Intent(this, SignupGenderActivity::class.java)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()

            val birthday = binding.editTextUserBirthDay.text.toString()

            when {
                birthday.isEmpty() -> {
                    Toast.makeText(applicationContext, "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                birthday.length != 8 || birthday.contains('/') -> {
                    Toast.makeText(applicationContext, "지원하지 않는 생년월일 양식입니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    genderInfo.putExtra("memberName", memberName)
                    genderInfo.putExtra("email", email)
                    genderInfo.putExtra("password", password)
                    genderInfo.putExtra("birthday", birthday)

                    startActivity(genderInfo)
                }
            }
        }

        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupNameActivity::class.java)
            startActivity(back)
        }
    }
}