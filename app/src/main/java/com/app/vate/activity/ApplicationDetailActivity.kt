package com.app.vate.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.app.vate.api.ServerRequest
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.ServerResponse
import com.app.vate.databinding.ApplicationDetailActivityBinding
import com.app.vate.model.AppHistory
import com.app.vate.model.VolActivity
import com.app.vate.model.VolOrgan
import com.app.vate.util.ActivityTimeFormatter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplicationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ApplicationDetailActivityBinding
    private lateinit var appHistory : AppHistory
    private lateinit var organization: VolOrgan
    private lateinit var volActivity: VolActivity
    private var serverRequest: ServerRequest = ServerRequestImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ApplicationDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()

        val applicationData = intent.getSerializableExtra("appHistory")
        applicationData?.let {
            appHistory = applicationData as AppHistory
            fetchData()
            bindContent()
        }
    }

    private fun fetchData() {
        serverRequest.getDetailActivityInfo(appHistory.activityId)
            .enqueue(object : Callback<ServerResponse<VolActivity>> {
                override fun onResponse(
                    call: Call<ServerResponse<VolActivity>>,
                    response: Response<ServerResponse<VolActivity>>
                ) {
                    val result = response.body()?.result
                    result?.let {
                        volActivity = it
                        binding.category.text = volActivity.category.description
                        binding.activityMethod.text = volActivity.activityMethod.description
                        binding.content.text = volActivity.activityContent
                        fetchOrgan()
                    }
                }

                override fun onFailure(call: Call<ServerResponse<VolActivity>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


    }

    private fun fetchOrgan() {
        serverRequest.getDetailOrganizationInfo(volActivity.organizationId)
            .enqueue(object : Callback<ServerResponse<VolOrgan>> {
                override fun onResponse(
                    call: Call<ServerResponse<VolOrgan>>,
                    response: Response<ServerResponse<VolOrgan>>
                ) {
                    val result = response.body()?.result
                    result?.let {
                        organization = it
                        binding.locationText.text = organization.address.detailAddress
                        bindMap()
                    }
                }

                override fun onFailure(call: Call<ServerResponse<VolOrgan>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun bindContent() {
        binding.organizationName.text = appHistory.organization
        binding.activityName.text = appHistory.activityName
        binding.activityTime.text = ActivityTimeFormatter.convertDateAndTime(appHistory.activityDate, appHistory.startTime, appHistory.endTime)
    }

    private fun bindMap() {
        val mapView = MapView(this)
        binding.locationMap.addView(mapView)

        val uCurrentPosition = MapPoint.mapPointWithGeoCoord(
            organization.address.coordinate.latitude,
            organization.address.coordinate.longitude)
        mapView.setMapCenterPointAndZoomLevel(uCurrentPosition, 2, true)

        val marker = MapPOIItem()
        marker.itemName = appHistory.organization
        marker.markerType = MapPOIItem.MarkerType.RedPin
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(
            organization.address.coordinate.latitude,
            organization.address.coordinate.longitude)
        marker.isShowCalloutBalloonOnTouch = false
        mapView.addPOIItem(marker)
    }

    private fun initButton() {
        binding.backwardButton.setOnClickListener {
            finish()
        }

        binding.cancelButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("취소하시겠습니까?")
                .setMessage("취소 이후에는 되돌릴 수 없습니다. \n그래도 취소하시겠습니까?")
                .setPositiveButton("네") { _, _ ->
                    ServerRequestImpl().cancelApplication(appHistory.applicationId)
                        .enqueue(object : Callback<ServerResponse<String>> {
                            override fun onResponse(call: Call<ServerResponse<String>>, response: Response<ServerResponse<String>>) {
                                if (response.isSuccessful) {
                                    val intent = Intent(applicationContext, ApplicationCancelCompleteActivity::class.java)
                                    intent.putExtra("appHistory", appHistory)
                                    startActivity(intent)
                                } else {
                                    val jsonObject = response.errorBody()?.string()?.let { JSONObject(it) }
                                    Toast.makeText(applicationContext, "${jsonObject?.get("message")}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(
                                call: Call<ServerResponse<String>>,
                                t: Throwable
                            ) {
                                Toast.makeText(applicationContext, "오류가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .setNegativeButton("아니요", null)
                .create()
                .show()
        }
    }

    override fun onPause() {
        binding.locationMap.removeAllViews()
        Thread.sleep(300)
        super.onPause()
    }

    override fun onResume() {
        if (binding.locationMap.isEmpty() && this::organization.isInitialized) {
            bindMap()
        }
        super.onResume()
    }

}