package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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
            val mapActivity = Intent(this, MapActivity::class.java)

            val editText: EditText = findViewById(R.id.editTextPhoneNumber)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()
            val idOf1365 = intent.getStringExtra("idOf1365").toString()
            val centerName = intent.getStringExtra("centerName").toString()
            val gender = intent.getStringExtra("gender").toString()
            val phoneNumber = editText.text.toString()

            val data = Member(
                loginId = email,
                password = password,
                memberName = memberName,
                birthday = birthday,
                idOf1365 = idOf1365,
                centerName = centerName,
                gender = gender,
                phoneNumber = phoneNumber
            )

            Log.d("data ", data.loginId)
            Log.d("data ", data.password)
            Log.d("data ", data.memberName)
            Log.d("data ", data.birthday)
            Log.d("data ", data.idOf1365)
            Log.d("data ", data.centerName)
            Log.d("data ", data.gender)
            Log.d("data ", data.phoneNumber)

            api.post_users(data).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")
                }

            })

            startActivity(mapActivity)
        }


        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupId1365Activity::class.java)
            startActivity(back)
        }
    }
}