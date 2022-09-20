package com.app.vate.activity


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupActivityBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: SignupActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val button: Button = findViewById(R.id.continueSignUp)
        button.setOnClickListener {
            val nameInfo = Intent(this, SignupNameActivity::class.java)

            val password = binding.password.text.toString()
            val passwordValidation = binding.passwordValidation.text.toString()

            when {
                password.isEmpty() -> {
                    Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                passwordValidation.isEmpty() -> {
                    Toast.makeText(applicationContext, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                password != passwordValidation -> {
                    Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                password.length > 15 || password.length < 8 || passwordValidation.length < 8 || passwordValidation.length > 15 -> {
                    Toast.makeText(applicationContext, "비밀번호는 8글자 이상 15글자 이내로 입력해주새요", Toast.LENGTH_SHORT).show()
                }
                password != passwordValidation -> {
                    Toast.makeText(applicationContext, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val email = intent.getStringExtra("email").toString()
                    nameInfo.putExtra("password", password)
                    nameInfo.putExtra("email", email)

                    startActivity(nameInfo)
                }
            }
        }

        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupEmailActivity::class.java)
            startActivity(back)
        }
    }
}