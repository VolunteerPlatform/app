package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
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

            val editText: EditText = findViewById(R.id.editTextUserId1365)
            centerNameInfo.putExtra("memberName", memberName)
            centerNameInfo.putExtra("email", email)
            centerNameInfo.putExtra("password", password)
            centerNameInfo.putExtra("birthday", birthday)
            centerNameInfo.putExtra("gender", gender)
            centerNameInfo.putExtra("idOf1365", editText.text.toString())


            startActivity(centerNameInfo)
        }


        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupGenderActivity::class.java)
            startActivity(back)
        }
    }
}
