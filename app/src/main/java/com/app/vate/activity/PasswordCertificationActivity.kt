package com.app.vate.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.databinding.PasswordCertificationActivityBinding
import com.app.vate.model.CertificationForm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordCertificationActivity : AppCompatActivity() {

    private lateinit var binding: PasswordCertificationActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PasswordCertificationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backPage.setOnClickListener {
            onBackPressed()
        }


        binding.continueSignUp.setOnClickListener {
            val editPasswordActivity = Intent(this, EditPasswordActivity::class.java)
            val redirect = Intent(this, PasswordCertificationActivity::class.java)
            val password = binding.password.text.toString()

            val data = CertificationForm(
                password = password
            )

            when {
                password.isEmpty() -> {
                    Toast.makeText(applicationContext, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    ServerRequestImpl().certificationPassword(data)
                        .enqueue(object : Callback<ServerResponse<CertificationForm>> {
                            override fun onResponse(
                                call: Call<ServerResponse<CertificationForm>>,
                                response: Response<ServerResponse<CertificationForm>>
                            ) {
                                response.body()?.message?.let {
                                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                                }
                                if (response.body()?.statusCode == 200) {
                                    Toast.makeText(applicationContext, "확인 되었습니다.", Toast.LENGTH_SHORT).show()
                                    startActivity(editPasswordActivity)
                                } else {
                                    Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                                    startActivity(redirect)
                                }
                            }

                            override fun onFailure(
                                call: Call<ServerResponse<CertificationForm>>,
                                t: Throwable
                            ) {
                                Toast.makeText(applicationContext, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }
        }
    }
}
