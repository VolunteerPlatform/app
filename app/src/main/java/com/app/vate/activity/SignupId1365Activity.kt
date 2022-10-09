package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupId1365ActivityBinding

class SignupId1365Activity : AppCompatActivity() {

    private lateinit var binding: SignupId1365ActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupId1365ActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUp)
        button.setOnClickListener{
            val centerNameInfo = Intent(this, SignupCenterNameActivity::class.java)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()
            val gender = intent.getStringExtra("gender").toString()

            val idOf1365 = binding.editTextUserId1365.text.toString()

            when {
                idOf1365.isEmpty() -> {
                    Toast.makeText(applicationContext, "1365 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    centerNameInfo.putExtra("memberName", memberName)
                    centerNameInfo.putExtra("email", email)
                    centerNameInfo.putExtra("password", password)
                    centerNameInfo.putExtra("birthday", birthday)
                    centerNameInfo.putExtra("gender", gender)
                    centerNameInfo.putExtra("idOf1365", idOf1365)

                    startActivity(centerNameInfo)
                }
            }
        }
    }
}
