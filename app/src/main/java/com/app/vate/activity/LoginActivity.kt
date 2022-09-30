package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.Login
import com.app.vate.api.PostResult
import com.app.vate.databinding.LoginActivityBinding
import com.app.vate.model.LoginForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    val api = Login.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.login.setOnClickListener {
            val mapActivity = Intent(this, MapActivity::class.java)
            val redirect = Intent(this, LoginActivity::class.java)

            val editLoginIdText: EditText = binding.enterLoginId
            val loginId = editLoginIdText.text.toString()

            val editPasswordText: EditText = binding.enterPassword
            val password = editPasswordText.text.toString()

            val loginForm = LoginForm(
                userName = loginId,
                password = password
            )

            api.loginInfo(loginForm).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    if (response.isSuccessful) {
                        response.headers().get("accessToken")?.let {
                            LoginContext.accessToken = it
                        }

                        response.headers().get("refreshToken")?.let {
                            LoginContext.refreshToken = it
                        }

                        if (response.body()?.statusCode == 200) {
                            startActivity(mapActivity)
                        } else {
                            Toast.makeText(applicationContext, "등록되지 않은 회원 정보입니다.", Toast.LENGTH_SHORT).show()
                            startActivity(redirect)
                        }
                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("test login",t.message.toString())
                    Log.d("test login","fail")
                }
            })
        }

        binding.backPage.setOnClickListener {
            val emailActivity = Intent(this, SignupEmailActivity::class.java)
            startActivity(emailActivity)
        }
    }

}