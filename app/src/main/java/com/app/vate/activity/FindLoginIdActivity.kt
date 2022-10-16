package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.FindLoginId
import com.app.vate.api.PostResult
import com.app.vate.databinding.FindLoginIdActivityBinding
import com.app.vate.model.FindLoginIdForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindLoginIdActivity : AppCompatActivity() {

    private lateinit var binding: FindLoginIdActivityBinding
    val api = FindLoginId.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FindLoginIdActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginActivity = Intent(this, LoginActivity::class.java)
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        binding.backPage.setOnClickListener {
            startActivity(loginActivity)
        }

        binding.findLoginIdButton.setOnClickListener {
            val editUserRealName: EditText = binding.enterUserName
            val userRealName = editUserRealName.text.toString()

            val editPhoneNumberText: EditText = binding.enterPhoneNumber
            val phoneNumber = editPhoneNumberText.text.toString()

            val findLoginIdForm = FindLoginIdForm(userRealName, phoneNumber)

            api.findLoginId(findLoginIdForm).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    if (response.isSuccessful) {
                        val result = response.body()?.result
                        AlertDialog.Builder(this@FindLoginIdActivity)
                            .setTitle("아이디 찾기.")
                            .setMessage(result)
                            .setPositiveButton("로그인 하기") { _, _ ->
                                startActivity(loginActivity)
                            }
                            .create()
                            .show()
                    } else {
                        Toast.makeText(applicationContext, "해당하는 사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
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