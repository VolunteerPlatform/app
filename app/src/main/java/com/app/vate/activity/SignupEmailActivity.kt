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

        val button: Button = findViewById(R.id.signup)
        button.setOnClickListener {
            val passwordInfo = Intent(this, SignupActivity::class.java)
            val emailInfo = Intent(this, SignupEmailActivity::class.java)

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
                                if (response.body()?.statusCode == 200) {
                                    startActivity(passwordInfo)
                                } else {
                                    Toast.makeText(applicationContext, "이미 가입된 회원입니다.", Toast.LENGTH_SHORT).show()
                                    startActivity(emailInfo)
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
        }

        binding.startSignupButton.setOnClickListener {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }
}
