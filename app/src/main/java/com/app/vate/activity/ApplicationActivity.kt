package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.databinding.ApplicationActivityBinding
import com.app.vate.model.ActivitySession
import com.app.vate.model.AppHistory
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationActivity : AppCompatActivity() {

    private lateinit var binding : ApplicationActivityBinding
    private val serverRequest = ServerRequestImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activitySession = intent.getSerializableExtra("activitySession") as ActivitySession

        binding.organizationComment.text = "능동적으로 봉사활동을 수행할 수 있고, 봉사정신이 투철하신 분을 희망합니다. 기타 사항은 기관으로 문의해주세요. 기관 연락처) 031-123-4567"
        binding.privacyComment.text = "VATE 는 고객의 개인정보를 봉사신청/활동등록을 위해 기관에 제공합니다. 제공되는 정보 : 이름, 연락처, 1365 아이디/소속센터"

        binding.applyButton.setOnClickListener {
            if (!binding.privacyAcceptCheckbox.isChecked && !binding.organizationAcceptCheckbox.isChecked) {
                Toast.makeText(this, "개인정보 활용과 기관 요청사항에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!binding.privacyAcceptCheckbox.isChecked) {
                Toast.makeText(this, "개인정보 활용에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!binding.organizationAcceptCheckbox.isChecked) {
                Toast.makeText(this, "기관 요청사항에 동의해야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            apply(activitySession)
        }
    }

    private fun apply(activitySession: ActivitySession) {
        serverRequest.applySession(activitySession.activitySessionId, 4, "해주세요.", "AGREE")
            .enqueue(object : Callback<ServerResponse<AppHistory>> {
                override fun onResponse(
                    call: Call<ServerResponse<AppHistory>>,
                    response: Response<ServerResponse<AppHistory>>
                ) {
                    if (!response.isSuccessful) {
                        val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                        Toast.makeText(applicationContext, "${jsonObject?.get("message")}", Toast.LENGTH_SHORT).show()
                    } else {
                        val intent = Intent(applicationContext, ApplicationCompleteActivity::class.java)
                        intent.putExtra("appHistory", response.body()?.result)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<ServerResponse<AppHistory>>, t: Throwable) {
                    Toast.makeText(applicationContext, "네트워크 통신에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}