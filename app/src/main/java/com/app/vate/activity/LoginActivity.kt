package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
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
                        Log.d("test login",response.toString())
                        Log.d("test login", response.body().toString())
                        Log.d("test login", response.headers().toString())

                        Log.d("tlqkd",
                            response.headers().get("accessToken").toString().split(" ")[1]
                        )
                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("test login",t.message.toString())
                    Log.d("test login","fail")
                }
            })

            startActivity(mapActivity)
        }

        binding.backPage.setOnClickListener {
            val emailActivity = Intent(this, SignupEmailActivity::class.java)
            startActivity(emailActivity)
        }
    }

}