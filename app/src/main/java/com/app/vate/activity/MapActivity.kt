package com.app.vate.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
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

        initCondition()
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

    private fun initCondition() {
        val userLocation = getUserCurrentLocation()
        searchCondition = SearchCondition(
            userLocation.longitude,
            userLocation.latitude,
            null,
            LocalDate.now(),
            LocalDate.now().plusDays(30)
        )
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
                    fragmentView(1)
                }

                R.id.volHistoryButton -> {
                    fragmentView(2)
                }

                R.id.myPageButton -> {
                    fragmentView(3)
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

                val fragment = WishListFragment.newInstance()
                currentFragment = fragment
                transaction.add(R.id.main_frame, fragment)
                transaction.commit()
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

                val fragment = MypageFragment.newInstance()
                currentFragment = fragment
                transaction.add(R.id.main_frame, fragment)
                transaction.commit()
            }
            5 -> {
                currentLocalFragment?.let {
                    transaction.remove(currentLocalFragment).commit()
                    currentFragment = null
                }

                searchActivity()
            }
        }
    }

    private fun restartMap() {
        mapView = MapView(this)
        binding.mapView.addView(mapView)
        mapView?.setMapViewEventListener(this)

        // 현재 위치 버튼을 맨 앞으로 가져오기 위해서
        binding.moveToCurrentLocationButton.bringToFront()
        val uCurrentPosition =
            MapPoint.mapPointWithGeoCoord(searchCondition.latitude, searchCondition.longitude)
        MapReverseGeoCoder(getKakaoApiKey(), uCurrentPosition, this, this).startFindingAddress()
        mapView?.setMapCenterPointAndZoomLevel(uCurrentPosition, 3, true)

        binding.sessionListRecyclerView.bringToFront()
    }

    /**
     * 검색하기
     */
    private fun searchActivity() {
        searchCondition.longitude = mapView?.mapCenterPoint?.mapPointGeoCoord?.longitude!!
        searchCondition.latitude = mapView?.mapCenterPoint?.mapPointGeoCoord?.latitude!!

        val uCurrentPosition =
            MapPoint.mapPointWithGeoCoord(searchCondition.latitude, searchCondition.longitude)
        MapReverseGeoCoder(getKakaoApiKey(), uCurrentPosition, this, this).startFindingAddress()

        mapView?.removeAllPOIItems()
        initRecycler(mutableListOf())

        binding.searchOnCurrentLocation.visibility = View.GONE

        val callSearchActivity = serverRequest.searchActivity(searchCondition)

        callSearchActivity.enqueue(object : Callback<ServerResponse<List<ActivitySession>>> {
            override fun onResponse(
                call: Call<ServerResponse<List<ActivitySession>>>,
                response: Response<ServerResponse<List<ActivitySession>>>
            ) {
                val result = response.body()?.result?.toMutableList() ?: mutableListOf()
                if (result.isEmpty()) {
                    Toast.makeText(applicationContext, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    initRecycler(result)
                    addMarker(result)
                }
            }

            override fun onFailure(
                call: Call<ServerResponse<List<ActivitySession>>>,
                t: Throwable
            ) {
                Toast.makeText(applicationContext, "네트워크 통신이 실패하였습니다.", Toast.LENGTH_SHORT).show()
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
            marker.itemName = session?.organization
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
        val userCurrentLocation = getUserCurrentLocation()
        val uCurrentPosition = MapPoint.mapPointWithGeoCoord(
            userCurrentLocation.latitude,
            userCurrentLocation.longitude
        )

        MapReverseGeoCoder(getKakaoApiKey(), uCurrentPosition, this, this).startFindingAddress()

        mapView?.setMapCenterPointAndZoomLevel(uCurrentPosition, 3, true)
        binding.searchOnCurrentLocation.visibility = View.VISIBLE;
    }

    private fun getUserCurrentLocation(): Location {
        checkRuntimePermission()

        var userCurrentLocation: Location? = null
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            userCurrentLocation =
                lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?: lm.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER
                )
        } catch (e: RuntimeException) {
            Toast.makeText(this, "위치 사용이 불가능합니다. 설정을 확인해주세요.", Toast.LENGTH_SHORT).show()
        }

        if (userCurrentLocation == null) {
            userCurrentLocation = Location("")
            userCurrentLocation.latitude = DEFAULT_LATITUDE
            userCurrentLocation.longitude = DEFAULT_LONGITUDE
        }

        return userCurrentLocation
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
            showPermissionContextPopup()
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("위치 권한이 필요합니다.")
            .setMessage("주변 봉사활동을 확인하기 위해 사용자의 위치를 확인해야 합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1000
                )
            }
            .setNegativeButton("취소하기") { dialog, which ->
                Toast.makeText(this, "위치 사용 거부시 위치 기반 추천이 불가능합니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1000 && grantResults.isNotEmpty()) {
            val userLocation = getUserCurrentLocation()
            searchCondition.latitude = userLocation.latitude
            searchCondition.longitude = userLocation.longitude

            moveCenterToUserCurrentLocation()
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    /*
    카테고리 설정 이후에 onResume 호출 -> 위치가 기존 위치가 아닌 현재 위치 기준으로 처리됨 ->
     */
    override fun onResume() {
        if (binding.mapView.isEmpty()) {
            restartMap()
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

    companion object {
        const val DEFAULT_LONGITUDE: Double = 126.9779451
        const val DEFAULT_LATITUDE: Double = 37.5662952
    }
}