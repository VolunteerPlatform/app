package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.EditPassword
import com.app.vate.api.FindLoginId
import com.app.vate.api.PostResult
import com.app.vate.databinding.EditPasswordActivityBinding
import com.app.vate.databinding.FindLoginIdActivityBinding
import com.app.vate.model.EditPasswordForm
import com.app.vate.model.FindLoginIdForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: EditPasswordActivityBinding
    val api = EditPassword.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditPasswordActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backPage.setOnClickListener {
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }

        binding.editPasswordButton.setOnClickListener {
            val loginActivity = Intent(this, LoginActivity::class.java)
            val redirect = Intent(this, EditPasswordActivity::class.java)

            val userName = binding.enterUserName.text.toString()
            val password = binding.password.text.toString()
            val passwordValidation = binding.passwordValidation.text.toString()

            val editPasswordForm = EditPasswordForm(userName, password)

            when {
                password.isEmpty() -> {
                    Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                passwordValidation.isEmpty() -> {
                    Toast.makeText(applicationContext, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                password != passwordValidation -> {
                    Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                password.length > 15 || password.length < 8 || passwordValidation.length < 8 || passwordValidation.length > 15 -> {
                    Toast.makeText(
                        applicationContext,
                        "비밀번호는 8글자 이상 15글자 이내로 입력해주새요",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                password != passwordValidation -> {
                    Toast.makeText(applicationContext, "비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                userName.isEmpty() -> {
                    Toast.makeText(applicationContext, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    api.editPassword(editPasswordForm).enqueue(object : Callback<PostResult> {
                        override fun onResponse(
                            call: Call<PostResult>,
                            response: Response<PostResult>
                        ) {
                            if (response.isSuccessful) {
                                if (response.body()?.statusCode == 200) {
                                    val result = response.body()?.result
                                    AlertDialog.Builder(this@EditPasswordActivity)
                                        .setTitle("비밀번호 변경 완료.")
                                        .setMessage(result)
                                        .setPositiveButton("로그인 하기") { _, _ ->
                                            startActivity(loginActivity)
                                        }
                                        .create()
                                        .show()
                                } else {
                                    Toast.makeText(applicationContext, "변경 실패", Toast.LENGTH_SHORT)
                                        .show()
                                    startActivity(redirect)
                                }
                            }
                        }

                        override fun onFailure(call: Call<PostResult>, t: Throwable) {
                            Log.d("test login", t.message.toString())
                            Log.d("test login", "fail")
                        }
                    })
                }
            }
        }
    }
}