package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.api.LoginIdValidation
import com.app.vate.api.PostResult
import com.app.vate.api.Signup
import com.app.vate.databinding.SignupActivityBinding
import com.app.vate.databinding.SignupEmailActivityBinding
import com.app.vate.model.LoginForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupEmailActivity : AppCompatActivity() {

    private lateinit var binding: SignupEmailActivityBinding
    val api = LoginIdValidation.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupEmailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isValidation = false

        binding.loginIdValidation.setOnClickListener {

            val email = binding.editTextEmail.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()

            } else {

                val data = LoginForm(
                    userName = email,
                    password = ""
                )

                api.loginValidation(data).enqueue(object : Callback<PostResult> {
                    override fun onResponse(
                        call: Call<PostResult>,
                        response: Response<PostResult>
                    ) {
                        if (response.isSuccessful) {

                            Log.d("tlqkf", response.body()?.statusCode.toString())

                            isValidation = if (response.body()?.statusCode == 200) {
                                Toast.makeText(applicationContext, "회원 가입 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show()
                                true
                            } else {
                                Toast.makeText(applicationContext, "이미 가입된 회원입니다.", Toast.LENGTH_SHORT).show()
                                false
                            }
                        }
                    }

                    override fun onFailure(call: Call<PostResult>, t: Throwable) {
                        Log.d("test signup", t.message.toString())
                        Log.d("test signup", "fail login")
                    }
                })
            }
        }

        val button: Button = findViewById(R.id.signup)
        button.setOnClickListener {
            val passwordInfo = Intent(this, SignupActivity::class.java)

            val email = binding.editTextEmail.text.toString()

            when {
                email.isEmpty() -> {
                    Toast.makeText(applicationContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                !(email.contains('@')) || !(email.contains('.')) -> {
                    Toast.makeText(applicationContext, "이메일 형식이 잘못 되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    passwordInfo.putExtra("email", email)

                    if (isValidation) {
                        startActivity(passwordInfo)
                    } else {
                        Toast.makeText(applicationContext, "아이디 중복 체크를 진행 후 회원가입 해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.startSignupButton.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }
}
