package ksnu.dsem.realtimeactivityrecognition;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import android.hardware.SensorManager;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import ksnu.dsem.structure.LocationInformation;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

public class GPS{
    public LocationRequest locationRequest;
    private Location location;
    private LatLng currentPosition;
    private Location mCurrentLocation;
//    private SensorManager sManager;
    private LocationInformation locationInfo;
//    private FusedLocationProviderClient mFusedLocationClient;
//    private MainActivity mainActivity;
//    private GoogleMap mMap;
    boolean needRequest = false;
    private static final int UPDATE_INTERVAL_MS = 3000;
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 위치 획득 후 update 되는 주기
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
//    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//
//    public final LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            location = locationResult.getLastLocation();
//            if (location != null) {
//                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
//                locationInfo.setLocationInformation(location.getLatitude(), location.getLongitude(), location.getSpeed());
//                String markerTitle = getCurrentAddress(currentPosition);
//                setCurrentLocation(location);
//                mCurrentLocation = location;
//            }
//        }
//    };

    public GPS() {
        this.locationInfo = new LocationInformation();
//        this.mainActivity = mainActivity;
//        this.sManager = (SensorManager)mainActivity.getSystemService(SENSOR_SERVICE);
        this.locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)//배터리소모보다 정확도 우선
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

    }


//
//
//    public void showDialogForLocationServiceSetting() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
//        builder.setTitle("위치 서비스 비활성화");
//        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
//                + "위치 설정을 수정하시겠습니까?");
//        builder.setCancelable(true);
//        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                Intent callGPSSettingIntent
//                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
//            }
//        });
//        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.cancel();
//            }
//        });
//        builder.create().show();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case GPS_ENABLE_REQUEST_CODE:
//                //사용자가 GPS 활성 시켰는지 검사
//                if (mainActivity.checkLocationServicesStatus()) {
//                    if (mainActivity.checkLocationServicesStatus()) {
//                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
//                        needRequest = true;
//
//                        return;
//                    }
//                }
//                break;
//        }
//    }
}
