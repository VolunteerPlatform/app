package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
        button.setOnClickListener{
            val genderInfo = Intent(this, SignupGenderActivity::class.java)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()

            val editText: EditText = findViewById(R.id.editTextUserBirthDay)
            val birthday = editText.text.toString()
            genderInfo.putExtra("memberName", memberName)
            genderInfo.putExtra("email", email)
            genderInfo.putExtra("password", password)
            genderInfo.putExtra("birthday", birthday)

            startActivity(genderInfo)
        }

        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupNameActivity::class.java)
            startActivity(back)
        }
    }
}