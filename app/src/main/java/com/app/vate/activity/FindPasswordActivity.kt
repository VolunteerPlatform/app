package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.FindPassword
import com.app.vate.api.PostResult
import com.app.vate.databinding.FindPasswordActivityBinding
import com.app.vate.model.FindPasswordForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindPasswordActivity : AppCompatActivity() {

    private lateinit var binding: FindPasswordActivityBinding
    val api = FindPassword.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backPage.setOnClickListener {
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }

        binding.findPasswordButton.setOnClickListener {
            val editPasswordActivity = Intent(this, EditPasswordActivity::class.java)
            val redirect = Intent(this, FindPasswordActivity::class.java)

            val editUserRealName: EditText = binding.enterUserId
            val userName = editUserRealName.text.toString()

            val findPasswordForm = FindPasswordForm(userName)

            api.findPassword(findPasswordForm).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "비밀번호를 변경해 주세요.", Toast.LENGTH_SHORT).show()
                        startActivity(editPasswordActivity)
                    } else {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_SHORT).show()
                        startActivity(redirect)
                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    Log.d("test login",t.message.toString())
                    Log.d("test login","fail")
                }
            })
        }
    }
}