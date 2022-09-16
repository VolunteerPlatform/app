package com.app.vate.activity


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupActivityBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: SignupActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.continueSignUp)
        button.setOnClickListener {
            val nameInfo = Intent(this, SignupNameActivity::class.java)

            val email = intent.getStringExtra("email").toString()

            val editText: EditText = findViewById(R.id.editTextPassword)
            nameInfo.putExtra("password", editText.text.toString())
            nameInfo.putExtra("email", email)

            startActivity(nameInfo)
        }

        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupEmailActivity::class.java)
            startActivity(back)
        }
    }
}