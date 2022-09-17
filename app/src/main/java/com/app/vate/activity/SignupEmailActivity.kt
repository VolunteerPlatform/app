package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupActivityBinding
import com.app.vate.databinding.SignupEmailActivityBinding

class SignupEmailActivity : AppCompatActivity() {

    private lateinit var binding: SignupEmailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupEmailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.signup)
        button.setOnClickListener {
            val passwordInfo = Intent(this, SignupActivity::class.java)

            val email = binding.editTextEmail.text.toString()

            when {
                email.isEmpty() -> {
                    Toast.makeText(applicationContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                !(email.contains('@')) || !(email.contains('.')) -> {
                    Toast.makeText(applicationContext, "이메일 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    passwordInfo.putExtra("email", email)
                    startActivity(passwordInfo)
                }
            }
        }

        binding.startSignupButton.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }
}
