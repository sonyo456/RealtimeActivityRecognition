//package ksnu.dsem.realtimeactivityrecognition;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.hardware.SensorManager;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Looper;
//import android.util.Log;
//import android.view.View;
//
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.material.snackbar.Snackbar;
//
//public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,  ActivityCompat.OnRequestPermissionsResultCallback{
//    static final String TAG = "realtime_activity_recog";
//    private Marker currentMarker = null;
//    private static final int PERMISSIONS_REQUEST_CODE = 100;
//    MainActivity mainActivity;
//    View mLayout;
//
//    GoogleMap mMap;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//
//        mLayout = (View) findViewById(R.id.map);
//    mainActivity = new MainActivity();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(final GoogleMap googleMap) {
//        Log.d(mainActivity.TAG, "[RAR] onMapReady :");
//        mMap = googleMap;
//
//        setDefaultLocation();   //초기위치 군산대
//
//        //런타임 퍼미션 처리
//        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
//        // 위치 퍼미션 체크
//        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//
//        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
//                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
//            mainActivity.startLocationUpdates(); //위치 업데이트
//        } else {  //퍼미션
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, mainActivity.REQUIRED_PERMISSIONS[0])) {
//                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
//                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        ActivityCompat.requestPermissions(this, mainActivity.REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
//                    }
//                }).show();
//            } else {
//                ActivityCompat.requestPermissions(this, mainActivity.REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
//            }
//        }
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                Log.d(mainActivity.TAG, "onMapClick :");
//            }
//        });
//    }
//
//    public void startLocationUpdates() {
//        Log.d(TAG, "[RAR] call startLocationUpdates");
//        if (!this.checkLocationServicesStatus()) {
//            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
//            this.showDialogForLocationServiceSetting();
//        } else {
//            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//
//            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
//                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
//                return;
//            }
//
//            Log.d(TAG, "[RAR] startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
//            mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());
//
//            if (this.checkPermission())
//                mMap.setMyLocationEnabled(true);
//        }
//    }
//    //OnCreate나 onRestart메소드 호출 후 바로 호출
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        Log.d(TAG, "[RAR] onStart");
//
//        if (checkPermission()) {
//            Log.d(TAG, "[RAR] onStart : call mFusedLocationClient.requestLocationUpdates");
////            mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, null);
//
//            if (mMap != null)
//                mMap.setMyLocationEnabled(true);
//        }
////
////        sManager.registerListener(accelerometer, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
////        sManager.registerListener(stepcounter, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
////        sManager.registerListener(accelerometer, linearSensor, SensorManager.SENSOR_DELAY_NORMAL);
//
//
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mFusedLocationClient != null) {
//            Log.d(TAG, "[RAR] onStop : call stopLocationUpdates");
//            mFusedLocationClient.removeLocationUpdates(this.locationCallback);
//        }
////        sManager.unregisterListener(accelerometer);
////        sManager.unregisterListener(stepcounter);
//    }
//
//    final LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            location = locationResult.getLastLocation();
//            if (location != null) {
//                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
//                li.setLocationInformation(location.getLatitude(), location.getLongitude(), location.getSpeed());
//                String markerTitle = getCurrentAddress(currentPosition);
//                setCurrentLocation(location);
//                mCurrentLocation = location;
//            }
//        }
//    };
//    public void setCurrentLocation(Location location) {
////    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
//        if (currentMarker != null) currentMarker.remove();
//
//        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(currentLatLng);
////        markerOptions.title(markerTitle);
////        markerOptions.snippet(markerSnippet);
//        markerOptions.draggable(true);
//
//        currentMarker = mMap.addMarker(markerOptions);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
//        mMap.moveCamera(cameraUpdate);
//
//    }
//
//    public void setDefaultLocation() {
//
//
//        //디폴트 위치 군산대학교
//        LatLng DEFAULT_LOCATION = new LatLng(35.945287, 126.682163);
//        String markerTitle = "위치정보 가져올 수 없음";
//        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";
//
//
//        if (currentMarker != null) currentMarker.remove();
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(DEFAULT_LOCATION);
//        markerOptions.title(markerTitle);
//        markerOptions.snippet(markerSnippet);
//        markerOptions.draggable(true);
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        currentMarker = mMap.addMarker(markerOptions);
//
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
//        mMap.moveCamera(cameraUpdate);
//
//    }
//}