package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupActivityBinding
import com.app.vate.databinding.SignupEmailActivityBinding

class SignupEmailActivity : AppCompatActivity() {

    private lateinit var binding: SignupEmailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupEmailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.signup)
        button.setOnClickListener{
            val passwordInfo = Intent(this, SignupActivity::class.java)

            val editText: EditText = findViewById(R.id.editTextEmail)
            passwordInfo.putExtra("email", editText.text.toString())

            startActivity(passwordInfo)
        }
    }
}
