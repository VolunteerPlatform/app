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
import com.app.vate.databinding.SignupNameActivityBinding

class SignupNameActivity : AppCompatActivity() {

    private lateinit var binding: SignupNameActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupNameActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUpName)
        button.setOnClickListener {
            val birthdayInfo = Intent(this, SignupBirthdayActivity::class.java)

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()

            val editText: EditText = findViewById(R.id.editTextUserName)
            birthdayInfo.putExtra("memberName", editText.text.toString())
            birthdayInfo.putExtra("email", email)
            birthdayInfo.putExtra("password", password)

            startActivity(birthdayInfo)
        }


        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupActivity::class.java)
            startActivity(back)
        }
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show()
        super.onBackPressed()
    }
}