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
import com.app.vate.api.PostResult
import com.app.vate.api.Signup
import com.app.vate.databinding.SignupPhoneNumberActivityBinding
import com.app.vate.model.Member
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPhoneNumberActivity : AppCompatActivity() {

    private lateinit var binding: SignupPhoneNumberActivityBinding
    val api = Signup.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupPhoneNumberActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUpName)
        button.setOnClickListener {
            val loginActivity = Intent(this, LoginActivity::class.java)

            val editText: EditText = findViewById(R.id.editTextPhoneNumber)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()
            val idOf1365 = intent.getStringExtra("idOf1365").toString()
            val centerName = intent.getStringExtra("centerName").toString()
            val gender = intent.getStringExtra("gender").toString()
            val phoneNumber = editText.text.toString()

            when {
                phoneNumber.isEmpty() -> {
                    Toast.makeText(applicationContext, "휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                phoneNumber.contains('-') -> {
                    Toast.makeText(
                        applicationContext,
                        "지원하지 않는 양식입니다. '-' 를 제거해 주세요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val data = Member(
                        userName = email,
                        password = password,
                        userRealName = memberName,
                        birthday = birthday,
                        idOf1365 = idOf1365,
                        centerName = centerName,
                        gender = gender,
                        phoneNumber = phoneNumber
                    )

                    api.post_users(data).enqueue(object : Callback<PostResult> {
                        override fun onResponse(
                            call: Call<PostResult>,
                            response: Response<PostResult>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("test signup", response.toString())
                                Log.d("test signup", response.body().toString())
                            }
                        }

                        override fun onFailure(call: Call<PostResult>, t: Throwable) {
                            Log.d("test signup", t.message.toString())
                            Log.d("test signup", "fail login")
                        }
                    })
                    startActivity(loginActivity)
                }
            }
        }
    }
}
