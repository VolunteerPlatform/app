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
import com.app.vate.databinding.SignupCenterNameActivityBinding
import com.app.vate.databinding.SignupNameActivityBinding

class SignupCenterNameActivity : AppCompatActivity() {

    private lateinit var binding: SignupCenterNameActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupCenterNameActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUp)
        button.setOnClickListener {
            val tendencyActivity = Intent(this, TendencyActivity::class.java)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()
            val idOf1365 = intent.getStringExtra("idOf1365").toString()
            val gender = intent.getStringExtra("gender").toString()


            val centerName = binding.editTextCenterName.text.toString()

            when {
                centerName.isEmpty() -> {
                    Toast.makeText(applicationContext, "센터 이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    tendencyActivity.putExtra("memberName", memberName)
                    tendencyActivity.putExtra("email", email)
                    tendencyActivity.putExtra("password", password)
                    tendencyActivity.putExtra("birthday", birthday)
                    tendencyActivity.putExtra("idOf1365", idOf1365)
                    tendencyActivity.putExtra("gender", gender)
                    tendencyActivity.putExtra("centerName", centerName)

                    startActivity(tendencyActivity)
                }
            }
        }
    }
}
