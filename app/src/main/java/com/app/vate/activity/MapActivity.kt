package com.app.vate.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import com.app.vate.R
import com.app.vate.adapter.SessionListAdapter
import com.app.vate.api.ServerRequest
import com.app.vate.api.ServerRequestImpl
import com.app.vate.api.model.SearchCondition
import com.app.vate.api.model.ServerResponse
import com.app.vate.databinding.MapActivityBinding
import com.app.vate.model.ActivitySession
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.stream.Collectors

class MapActivity : AppCompatActivity(), MapReverseGeoCoder.ReverseGeoCodingResultListener,
    MapView.MapViewEventListener {

    private lateinit var binding: MapActivityBinding
    private var mapView: MapView? = null
    private var serverRequest: ServerRequest = ServerRequestImpl()
    private lateinit var searchCondition: SearchCondition
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initMap()
        initButton()
        initBottomNavbar()
    }

    /**
     * 초기화
     */
    private fun initButton() {
        binding.moveToCurrentLocationButton.setOnClickListener {
            moveCenterToUserCurrentLocation()
        }

        binding.searchOnCurrentLocation.setOnClickListener {
            searchActivity()
        }

        binding.changeConditionButton.setOnClickListener {
            val intent = Intent(this, FilteringActivity::class.java)
            intent.putExtra("searchCondition", searchCondition)

            startActivityForResult(intent, 200);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        searchCondition = data?.getSerializableExtra("searchCondition") as SearchCondition
        searchActivity()
    }

    private fun initMap() {
        mapView = MapView(this)
        binding.mapView.addView(mapView)
        checkRuntimePermission()
        mapView?.setMapViewEventListener(this)

        // 현재 위치 버튼을 맨 앞으로 가져오기 위해서
        binding.moveToCurrentLocationButton.bringToFront()
        moveCenterToUserCurrentLocation()

        if (!this::searchCondition.isInitialized) {
            searchCondition = SearchCondition(
                mapView?.mapCenterPoint?.mapPointGeoCoord?.longitude!!,
                mapView?.mapCenterPoint?.mapPointGeoCoord?.latitude!!,
                null,
                LocalDate.now(),
                LocalDate.now().plusDays(30)
            )
        }

        binding.sessionListRecyclerView.bringToFront()
    }

    private fun initRecycler(sessionList: MutableList<ActivitySession>) {
        val sessionListAdapter = SessionListAdapter(this)
        binding.sessionListRecyclerView.adapter = sessionListAdapter

        sessionListAdapter.sessionList = sessionList
        sessionListAdapter.notifyDataSetChanged()
    }

    private fun initBottomNavbar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeButton -> {
                    fragmentView(5)
                }

                R.id.LikedVolButton -> {

                }

                R.id.volHistoryButton -> {
                    fragmentView(2)
                }

                R.id.myPageButton -> {

                }

            }
            true
        }
    }

    private fun fragmentView(option: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        val currentLocalFragment = currentFragment

        when (option) {
            1 -> { // 찜한봉사 선택시
                if (currentLocalFragment != null) {
                    transaction.remove(currentLocalFragment)
                    currentFragment = null
                }
            }
            2 -> { // 활동내역 선택시
                if (currentLocalFragment != null) {
                    transaction.remove(currentLocalFragment)
                    currentFragment = null
                }

                val fragment = ActivityHistoryFragment.newInstance()
                currentFragment = fragment
                transaction.add(R.id.main_frame, fragment)
                transaction.commit()
            }

            3 -> { // 마이페이지 선택시
                if (currentLocalFragment != null) {
                    transaction.remove(currentLocalFragment)
                    currentFragment = null
                }
            }
            5 -> {
                currentLocalFragment?.let {
                    transaction.remove(currentLocalFragment).commit()
                    currentFragment = null
                }
            }
        }
    }

    /**
     * 검색하기
     */
    private fun searchActivity() {
        searchCondition.longitude = mapView?.mapCenterPoint?.mapPointGeoCoord?.longitude!!
        searchCondition.latitude = mapView?.mapCenterPoint?.mapPointGeoCoord?.latitude!!
        mapView?.removeAllPOIItems()
        binding.searchOnCurrentLocation.visibility = View.GONE

        val callSearchActivity = serverRequest.searchActivity(searchCondition)
        callSearchActivity.enqueue(object : Callback<ServerResponse<List<ActivitySession>>> {
            override fun onResponse(
                call: Call<ServerResponse<List<ActivitySession>>>,
                response: Response<ServerResponse<List<ActivitySession>>>
            ) {
                // draw 함수 추가
                val result = response.body()?.result
                result?.toMutableList()?.let {
                    initRecycler(it)
                    addMarker(it)
                }
            }

            override fun onFailure(
                call: Call<ServerResponse<List<ActivitySession>>>,
                t: Throwable
            ) {
                // 네트워크 통신 불가 오류 출력
            }
        })
    }

    private fun addMarker(list: MutableList<ActivitySession>) {
        val sessionMap = list.stream().collect(Collectors.groupingBy { it ->
            it.organizationId
        })

        for (key in sessionMap.keys) {
            val session = sessionMap[key]?.get(0)
            val marker = MapPOIItem()
            marker.itemName = session?.organizationName
            marker.markerType = MapPOIItem.MarkerType.RedPin
            marker.mapPoint = MapPoint.mapPointWithGeoCoord(session!!.latitude, session.longitude)
            mapView?.addPOIItem(marker)
        }
    }

    /**
     * 권한 설정 관련 부분
     */
    @SuppressLint("MissingPermission")
    private fun moveCenterToUserCurrentLocation() {
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val userCurrentLocation: Location? =
            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            
        val uLatitude = userCurrentLocation?.latitude ?: 37.5662952
        val uLongitude = userCurrentLocation?.longitude ?: 126.9779451
        val uCurrentPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)

        MapReverseGeoCoder(getKakaoApiKey(), uCurrentPosition, this, this).startFindingAddress()

        mapView?.setMapCenterPointAndZoomLevel(uCurrentPosition, 2, true)
    }

    private fun checkRuntimePermission() {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return
        } else {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
                || shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                showPermissionContextPopup()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1000)
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("위치 권한이 필요합니다.")
            .setMessage("주변 봉사활동을 확인하기 위해 사용자의 위치를 확인해야 합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 1000)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    /**
     * 카카오맵 API 를 이용해서 좌표를 이용해 주소를 받아오기
     */
    override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, addressResult: String?) {
        binding.topToolbarLocationKeyword.text = addressResult;
    }

    override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
        binding.topToolbarLocationKeyword.text = "위치 변환 실패";
    }

    private fun getKakaoApiKey(): String {
        val applicationInfo =
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val appKey = applicationInfo.metaData.get("com.kakao.sdk.AppKey").toString();
        return appKey;
    }

    /**
     * 지도 이동에 따른 이벤트 추가
     */
    override fun onPause() {
        binding.mapView.removeAllViews()
        super.onPause()
    }

    override fun onResume() {
        if (!binding.mapView.contains(mapView!!)) {
            initMap()
        }
        super.onResume()
    }

    override fun onMapViewInitialized(p0: MapView?) {
        searchActivity()
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        // 이제 보이고 나서 다시 검색버튼이 눌리면, 그때 gone 으로 변경
        binding.searchOnCurrentLocation.visibility = View.VISIBLE;
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
        return;
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        return;
    }

    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {
        return;
    }

    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {
        return;
    }

    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {
        return;
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        return;
    }

    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        return;
    }
}