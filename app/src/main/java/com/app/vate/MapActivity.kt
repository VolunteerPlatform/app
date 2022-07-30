package com.app.vate

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.vate.databinding.MapActivityBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {

    private lateinit var binding: MapActivityBinding
    private var mapView : MapView? = null
    private var currentLocationPOI: MapPOIItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
        initButton()
    }

    private fun initButton() {
        binding.moveToCurrentLocationButton.setOnClickListener {
            moveCenterToUserCurrentLocation()
        }
    }

    private fun initMap() {
        mapView = MapView(this)
        binding.mapView.addView(mapView)
        checkRuntimePermission()

        // 현재 위치 버튼을 맨 앞으로 가져오기 위해서
        binding.moveToCurrentLocationButton.bringToFront()
        moveCenterToUserCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun moveCenterToUserCurrentLocation() {
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userCurrentLocation: Location? = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val uLatitude = userCurrentLocation?.latitude
        val uLongitude = userCurrentLocation?.longitude
        val uCurrentPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)

        val marker = MapPOIItem()
        marker.itemName = "Current Location"
        marker.tag = 1
        marker.mapPoint = uCurrentPosition
        marker.markerType = MapPOIItem.MarkerType.CustomImage

        marker.customImageResourceId = R.drawable.location
        marker.isCustomImageAutoscale = false

        // 현 위치가 옮겨질 경우 마커가 여러개 찍힐 수 있으므로
        if (currentLocationPOI != null) {
            mapView?.removePOIItem(currentLocationPOI)
        }

        mapView?.addPOIItem(marker)
        currentLocationPOI = marker

        mapView?.setMapCenterPointAndZoomLevel(uCurrentPosition, 2,true)
    }

    private fun checkRuntimePermission() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return
        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showPermissionContextPopup()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("위치 권한이 필요합니다.")
            .setMessage("주변 봉사활동을 확인하기 위해 사용자의 위치를 확인해야 합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.removeAllViews()
    }
}