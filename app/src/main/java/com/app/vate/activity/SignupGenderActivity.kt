package com.app.vate.activity


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.databinding.SignupGenderActivityBinding

class SignupGenderActivity : AppCompatActivity() {

    private lateinit var binding: SignupGenderActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupGenderActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var gender = ""

        val maleButton: Button = findViewById(R.id.signupGenderMale)
        maleButton.setOnClickListener {
            val idOf1365Info = Intent(this, SignupId1365Activity::class.java)

            gender = "MALE"

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()

            idOf1365Info.putExtra("memberName", memberName)
            idOf1365Info.putExtra("email", email)
            idOf1365Info.putExtra("password", password)
            idOf1365Info.putExtra("birthday", birthday)
            idOf1365Info.putExtra("gender", gender)

            startActivity(idOf1365Info)
        }


        val femaleButton: Button = findViewById(R.id.signupGenderFemale)
        femaleButton.setOnClickListener {
            val idOf1365Info = Intent(this, SignupId1365Activity::class.java)

            gender = "FEMALE"

            val email = intent.getStringExtra("email").toString()
            val password = intent.getStringExtra("password").toString()
            val memberName = intent.getStringExtra("memberName").toString()
            val birthday = intent.getStringExtra("birthday").toString()

            idOf1365Info.putExtra("memberName", memberName)
            idOf1365Info.putExtra("email", email)
            idOf1365Info.putExtra("password", password)
            idOf1365Info.putExtra("birthday", birthday)
            idOf1365Info.putExtra("gender", gender)


            startActivity(idOf1365Info)
        }


//        val button: Button = findViewById(R.id.continueSignUp)
//        button.setOnClickListener {
//            val idOf1365Info = Intent(this, SignupId1365Activity::class.java)
//
//            val email = intent.getStringExtra("email").toString()
//            val password = intent.getStringExtra("password").toString()
//            val memberName = intent.getStringExtra("memberName").toString()
//
//            var gender = ""
//
//            startActivity(idOf1365Info)
//
//            val editText: EditText = findViewById(R.id.editTextEmail)
//            idOf1365Info.putExtra("gender", editText.text.toString())
//        }

        val backButton: ImageButton = findViewById(R.id.backPage)
        backButton.setOnClickListener {
            val back = Intent(this, SignupBirthdayActivity::class.java)
            startActivity(back)
        }
    }
}
