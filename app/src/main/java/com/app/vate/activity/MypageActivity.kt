package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.R
import com.app.vate.api.PostResult
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.UpdateMemberProfile
import com.app.vate.api.model.ServerResponse
import com.app.vate.databinding.MypageActivityBinding
import com.app.vate.databinding.SignupActivityBinding
import com.app.vate.model.MemberProfile
import com.app.vate.model.TimeTableElement
import com.app.vate.model.UpdateMemberProfileForm
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageActivity : AppCompatActivity() {

    private lateinit var binding: MypageActivityBinding
    val api = UpdateMemberProfile.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MypageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMemberProfile()

        binding.backPage.setOnClickListener {
            onBackPressed()
        }

        binding.mypageEditButton.setOnClickListener {
            val userRealName = binding.memberNameEdit.text.toString()
            val userBirthday = binding.memberBirthdayEdit.text.toString()
            val userId_1365 = binding.memberId1365Edit.text.toString()
            val userPhoneNumber = binding.memberPhoneNumberEdit.text.toString()
            val userCenterName = binding.memberCenterNameEdit.text.toString()

            when {
                userRealName.isEmpty() -> {
                    Toast.makeText(applicationContext, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                }

                userBirthday.isEmpty() -> {
                    Toast.makeText(applicationContext, "생년월일을 입력해주세요", Toast.LENGTH_SHORT).show()
                }

                userBirthday.length != 8 || userBirthday.contains('/') -> {
                    Toast.makeText(applicationContext, "지원하지 않는 생년월일 양식입니다.", Toast.LENGTH_SHORT)
                        .show()
                }

                userId_1365.isEmpty() -> {
                    Toast.makeText(applicationContext, "1365 아이디를 입력해주세요", Toast.LENGTH_SHORT)
                        .show()
                }

                userPhoneNumber.isEmpty() -> {
                    Toast.makeText(applicationContext, "휴대폰 번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }

                userCenterName.isEmpty() -> {
                    Toast.makeText(applicationContext, "센터 이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val data = UpdateMemberProfileForm(
                        userRealName = userRealName,
                        birthday = userBirthday,
                        phoneNumber = userPhoneNumber,
                        idOf1365 = userId_1365,
                        centerName = userCenterName,
                    )

                    updateMemberProfile(data)
                }
            }
        }
    }

    private fun fillMemberProfileInfo(memberProfile: MemberProfile) {
        binding.memberIdEdit.text = memberProfile.userName
        binding.memberNameEdit.setText(memberProfile.userRealName)
        binding.memberBirthdayEdit.setText(memberProfile.birthday)
        binding.memberId1365Edit.setText(memberProfile.idOf1365)
        binding.memberPhoneNumberEdit.setText(memberProfile.phoneNumber)
        binding.memberCenterNameEdit.setText(memberProfile.centerName)

    }

    private fun getMemberProfile() {
        ServerRequestImpl().getMemberProfile()
            .enqueue(object : Callback<ServerResponse<MemberProfile>> {
                override fun onResponse(
                    call: Call<ServerResponse<MemberProfile>>,
                    response: Response<ServerResponse<MemberProfile>>
                ) {
                    response.body()?.message?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    }
                    response.body()?.result?.let { fillMemberProfileInfo(it) }
                }

                override fun onFailure(
                    call: Call<ServerResponse<MemberProfile>>,
                    t: Throwable
                ) {
                    Toast.makeText(applicationContext, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun updateMemberProfile(updateMemberProfileForm: UpdateMemberProfileForm) {
        ServerRequestImpl().updateMemberProfile(updateMemberProfileForm)
            .enqueue(object : Callback<ServerResponse<UpdateMemberProfileForm>> {
                override fun onResponse(
                    call: Call<ServerResponse<UpdateMemberProfileForm>>,
                    response: Response<ServerResponse<UpdateMemberProfileForm>>
                ) {
                    response.body()?.message?.let {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                    }
                    if (response.body()?.statusCode == 200) {
                        Toast.makeText(
                            applicationContext,
                            "성공적으로 업데이트 되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                }

                override fun onFailure(
                    call: Call<ServerResponse<UpdateMemberProfileForm>>,
                    t: Throwable
                ) {
                    Toast.makeText(applicationContext, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}