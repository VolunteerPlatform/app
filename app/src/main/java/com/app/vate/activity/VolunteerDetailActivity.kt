package com.app.vate.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import com.app.vate.api.ServerRequest
import com.app.vate.api.ServerRequestImpl
import com.app.vate.databinding.VolunteerDetailActivityBinding
import com.app.vate.model.*
import com.app.vate.util.ActivityTimeFormatter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VolunteerDetailActivity : AppCompatActivity() {

    private lateinit var binding: VolunteerDetailActivityBinding
    private lateinit var activitySession: ActivitySession
    private var serverRequest: ServerRequest = ServerRequestImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VolunteerDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()

        val activityData = intent.getSerializableExtra("activitySession")
        activityData?.let {
            activitySession = activityData as ActivitySession
            bindContent()
        }
    }

    private fun bindContent() {
        binding.organizationName.text = activitySession.organizationName
        binding.activityName.text = activitySession.activityName
        binding.category.text = Category.valueOf(activitySession.category).description
        binding.activityMethod.text = ActivityMethod.valueOf(activitySession.activityMethod).description
        binding.activityTime.text = ActivityTimeFormatter.convertDateAndTime(activitySession.activityDate, activitySession.startTime, activitySession.endTime)

        bindOrganAddress()
        bindActivityContent()
    }

    private fun bindMap() {
        val mapView = MapView(this)
        binding.locationMap.addView(mapView)

        val uCurrentPosition = MapPoint.mapPointWithGeoCoord(activitySession.latitude, activitySession.longitude)
        mapView.setMapCenterPointAndZoomLevel(uCurrentPosition, 2, true)

        val marker = MapPOIItem()
        marker.itemName = activitySession.organizationName
        marker.markerType = MapPOIItem.MarkerType.RedPin
        marker.mapPoint = MapPoint.mapPointWithGeoCoord(activitySession.latitude, activitySession.longitude)
        marker.isShowCalloutBalloonOnTouch = false
        mapView.addPOIItem(marker)
    }

    private fun bindOrganAddress() {
        serverRequest.getDetailOrganizationInfo(activitySession.organizationId)
            .enqueue(object : Callback<VolOrgan> {
                override fun onResponse(
                    call: Call<VolOrgan>,
                    response: Response<VolOrgan>
                ) {
                    val result = response.body()
                    binding.locationText.text = result?.address?.detailAddress
                }

                override fun onFailure(call: Call<VolOrgan>, t: Throwable) {
                    binding.locationText.text = "상세 위치 검색에 실패하였습니다."
                }
            })
    }

    private fun bindActivityContent() {
        serverRequest.getDetailActivityInfo(activitySession.activityId)
            .enqueue(object: Callback<VolActivity> {
                override fun onResponse(call: Call<VolActivity>, response: Response<VolActivity>) {
                    binding.content.text = response.body()?.activityContent
                }

                override fun onFailure(call: Call<VolActivity>, t: Throwable) {
                    binding.content.text = "내용을 불러오는데 실패하였습니다."
                }
            })
    }

    private fun initButton() {
        binding.backwardButton.setOnClickListener {
            finish()
        }

        binding.applyButton.setOnClickListener {
            val intent = Intent(this, ApplicationActivity::class.java)
            intent.putExtra("activitySession", activitySession)
            intent.flags = FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onPause() {
        binding.locationMap.removeAllViews()
        super.onPause()
    }

    override fun onResume() {
        if (binding.locationMap.isEmpty()) {
            bindMap()
        }
        super.onResume()
    }
}