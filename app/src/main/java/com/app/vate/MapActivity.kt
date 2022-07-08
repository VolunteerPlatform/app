package com.app.vate

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.vate.databinding.MapActivityBinding
import net.daum.mf.map.api.MapView

class MapActivity : AppCompatActivity() {

    private lateinit var binding: MapActivityBinding
    private var mapView : MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
    }

    private fun initMap() {
        mapView = MapView(this)
        binding.mapView.addView(mapView)
        checkRuntimePermission()
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