package ksnu.dsem.realtimeactivityrecognition;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;


import ksnu.dsem.structure.*;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    GoogleMap mMap;
    private Marker currentMarker = null;

    static final String TAG = "realtime_activity_recog";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 3000;  // 위치 update 주기
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 위치 획득 후 update 되는 주기
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    Location mCurrentLocation;
    LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    SensorManager sManager;
    Sensor accSensor, stepSensor, linearSensor;
    View mLayout;
    private Button btnmap, btnloadmodel;
    private EditText etID;
    private TextView tvLat, tvLon, tvSpeed, tvActtype, tvSvm, tvStep;
    private RadioButton rb1sec, rb5sec, rb60sec, rbwalking, rbrunning, rbstationary, rbincar, rbinvehicle, rbunknown;
    private RadioGroup radioGroup, radioGroup2;


    private GPS gps;
    private ModelClient mc;
    private RFModel model;
    private Accelerometer accelerometer;
    private StepCounter stepcounter;
    private LocationInformation li;
    private DataStructure ds;
    private NetworkService networkService;
    ProgressDialog dialog;

    ValueHandler handler = new ValueHandler();
    private int cycle = 5;
    String collectActtype = " ";
    String currtime;
    private String path = "/data/data/ksnu.dsem.realtimeactivityrecognition/files/";

    //액티비티 인스턴스가 최초로 생성될 때 호출
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        btnmap = (Button)findViewById(R.id.bntmap);
        btnloadmodel = (Button)findViewById(R.id.btnloadmd);

        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLon = (TextView) findViewById(R.id.tvLon);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvSvm = (TextView) findViewById(R.id.tvSvm);
        tvStep = (TextView) findViewById(R.id.tvStep);
        tvActtype = (TextView) findViewById(R.id.tvActtype);
        rb1sec = (RadioButton) findViewById(R.id.rb1sec);
        rb5sec = (RadioButton) findViewById(R.id.rb5sec);
        rb60sec = (RadioButton) findViewById(R.id.rb60sec);
        rbwalking = (RadioButton) findViewById(R.id.rbwalking);
        rbrunning = (RadioButton) findViewById(R.id.rbrunning);
        rbincar = (RadioButton) findViewById(R.id.rbincar);
        rbinvehicle = (RadioButton) findViewById(R.id.rbinvehicle);
        rbstationary = (RadioButton) findViewById(R.id.rbstationary);
        rbunknown = (RadioButton) findViewById(R.id.rbunknown);
        mLayout = (View) findViewById(R.id.map);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        radioGroup2.setOnCheckedChangeListener(radioGroupButtonChangeListener2);

        model = new RFModel();//클래스 생성
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //default 센서로 가속도 센서 선택
        accSensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        linearSensor = sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        accelerometer = new Accelerometer();
        stepcounter = new StepCounter();
        li = new LocationInformation();
        ds = new DataStructure();
        currtime = ds.getCurrtime();
        Log.d("ATAR_ex", currtime + "1");
        //thread 생성 및 시작
        BackgroundThread thread = new BackgroundThread();
        thread.start();
        gps = new GPS();


//        locationRequest = gps.locationRequest;
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)//배터리소모보다 정확도 우선
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
//                startActivity(intent);
            }
        });

        btnloadmodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etID = (EditText)findViewById(R.id.etID);
                String userId = etID.getText().toString();
                Log.d(TAG, userId);
                ModelClient mc = new ModelClient();
                SendFile sf = new SendFile();
                CheckTypesTask task = new CheckTypesTask();
                task.execute();
                //userid와 파일 경로 전달
                mc.sendId(userId, path);

            }
        });
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "[RAR] onMapReady :");
        mMap = googleMap;

        setDefaultLocation();   //초기위치 군산대

        //런타임 퍼미션 처리
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // 위치 퍼미션 체크
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            this.startLocationUpdates(); //위치 업데이트
        } else {  //퍼미션
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick :");
            }
        });
    }

    //프로그레스바
    private class CheckTypesTask extends AsyncTask<String, String, String> {

        ProgressDialog asyncDialog = new ProgressDialog(
                MainActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    publishProgress(""+(int)(i * 30));
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC",progress[0]);
            asyncDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
    //주기 라디오 그룹
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

            if (i == R.id.rb1sec) {
                cycle = 1;
                Toast.makeText(MainActivity.this, "데이터 수집 주기는 1초 입니다.", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rb5sec) {
                cycle = 5;
                Toast.makeText(MainActivity.this, "데이터 수집 주기는 5초 입니다.", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rb60sec) {
                cycle = 60;
                Toast.makeText(MainActivity.this, "데이터 수집 주기는 60초 입니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //행동 유형 라디오 그룹
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup2, @IdRes int i) {

            if (i == R.id.rbwalking) {
                collectActtype = "walking";
                Toast.makeText(MainActivity.this, "현재 행동유형은 걷기입니다", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rbrunning) {
                collectActtype = "running";
                Toast.makeText(MainActivity.this, "현재 행동유형은 뛰기입니다", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rbincar) {
                collectActtype = "in_car";
                Toast.makeText(MainActivity.this, "현재 행동유형은 차타기입니다", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rbinvehicle) {
                collectActtype = "in_vehicle";
                Toast.makeText(MainActivity.this, "현재 행동유형은 자전거타기입니다", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rbstationary) {
                collectActtype = "stationary";
                Toast.makeText(MainActivity.this, "현재 행동유형은 멈춤입니다", Toast.LENGTH_SHORT).show();
            } else if (i == R.id.rbunknown) {
                collectActtype = "unknown";
                Toast.makeText(MainActivity.this, "현재 행동유형은 unknown입니다", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void startLocationUpdates() {
        Log.d(TAG, "[RAR] call startLocationUpdates");
        if (!this.checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            this.showDialogForLocationServiceSetting();
        } else {
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }

            Log.d(TAG, "[RAR] startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.myLooper());

            if (this.checkPermission())
                mMap.setMyLocationEnabled(true);
        }
    }

    //OnCreate나 onRestart메소드 호출 후 바로 호출
    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "[RAR] onStart");

        if (checkPermission()) {
            Log.d(TAG, "[RAR] onStart : call mFusedLocationClient.requestLocationUpdates");
//            mFusedLocationClient.requestLocationUpdates(locationRequest, this.locationCallback, null);

            if (mMap != null)
                mMap.setMyLocationEnabled(true);
        }

        sManager.registerListener(accelerometer, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(stepcounter, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(accelerometer, linearSensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    //액티비티 사용자에게 보이지 않음, 포그라운드로 액티비티가 들어가면 onRestart 호출/ 종료시 onDestory 호출
    @Override
    protected void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            Log.d(TAG, "[RAR] onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(this.locationCallback);
        }
        sManager.unregisterListener(accelerometer);
        sManager.unregisterListener(stepcounter);
    }

    public void stopNetwork(){
        if(networkService == null){
            Intent intent = new Intent(this,NetworkService.class);
            startService(intent);
        }
    }
    //데이터 업데이트
    public void updateData() {
        Acceleration acc = accelerometer.getAcc();
        StepCount scount = stepcounter.getStep();
        String acttypeStr = model.classifyActtype(li.getLatitude(), li.getLongitude(), li.getSpeed(), acc.getSvm(), (int)ds.getStep_dev());
        long startTime = System.currentTimeMillis();
        ds.setCurrentData(li.getLatitude(), li.getLongitude(), li.getSpeed(), acc.getSvm(), (int) scount.getStep(), acttypeStr);
        long endTime = System.currentTimeMillis();
        long diffTime = endTime - startTime;
        System.out.println("걸린시간:\t" + diffTime + "\tms");
//        Log.d(TAG, "acttype: " + acttypeStr);
//        for (int i = 0; i < acc.getAccArray().size(); i++) {
//            Log.d(TAG, "arraylist: " + acc.getAccArray().get(i));
//        }

    }

    //데이터 수집
    public void correctData() {
        Acceleration acc = accelerometer.getAcc();
        StepCount scount = stepcounter.getStep();

        ds.setCorrectData(li.getLatitude(), li.getLongitude(), acc.getAccx(), acc.getAccy(), acc.getAccz(), li.getSpeed(), acc.getSvm(), (int) scount.getStep(), collectActtype);
    }
    //xyz데이터 수집
    public void XYZData() {
        Acceleration acc = accelerometer.getAcc();
        StepCount scount = stepcounter.getStep();
        ds.setXYZtData(acc.getAccArray());
    }

    //textView에 값 넣기
    public void setViews() {
        tvLat.setText(String.valueOf(ds.getLat()) + "\t ");
        tvLon.setText(String.valueOf(ds.getLon()) + "\t ");
        tvSpeed.setText(String.valueOf(ds.getSpeed()) + "\t ");
        tvSvm.setText(String.valueOf(ds.getSvm()) + "\t ");
        tvStep.setText(String.valueOf((int) ds.getStep()));
        tvActtype.setText(ds.getCurracttype());
        Log.d(TAG, "acttype: " + ds.getCurracttype());

    }

    //분류데이터 저장
    public void saveLog() {
        String contents = ds.getContent();
        String date = ds.getDate();

        try {
//            Log.d("ATAR_ex", date + "2");
            String fileName = "Log_"+date + ".txt";
            String filePath = path +fileName;
            Log.d("ATAR_ex", filePath);
            if (new File(filePath).exists()) {
                FileOutputStream fos = openFileOutput(fileName, MODE_APPEND);
                //이어붙이기로
//                Toast.makeText(this, "saved2", Toast.LENGTH_SHORT).show();
                PrintWriter writer = new PrintWriter(fos);
                writer.println(contents);
                writer.flush();
                writer.close();
            } else {
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);

                PrintWriter writer = new PrintWriter(fos);
                writer.println(contents);
                writer.flush();
                writer.close();
            }
//            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //데이터 수집
    public void corrLog() {
        String corrData = ds.getCorrdata();
        String date = ds.getDate();
        try {

            String fileName = "Collect_"+date + ".txt";
            String filePath = path+fileName;

            if (new File(filePath).exists()) {
                FileOutputStream fos = openFileOutput(fileName, MODE_APPEND);
                //이어붙이기로
//                Toast.makeText(this, "saved2", Toast.LENGTH_SHORT).show();
                PrintWriter writer = new PrintWriter(fos);
                writer.println(corrData);
                writer.flush();
                writer.close();
            } else {
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);

                PrintWriter writer = new PrintWriter(fos);
                writer.println(corrData);
                writer.flush();
                writer.close();
            }
//            Toast.makeText(this, "corrected", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //XYZ 값 arrayList로 수집 후 txt
    public void corrXYZ() {
        String corrXYZ = ds.getXYZdata();
        String date = ds.getDate();
        try {

            String fileName = "SVM"+date + ".txt";
            String filePath = "/data/data/ksnu.dsem.realtimeactivityrecognition/files/"+fileName;

            if (new File(filePath).exists()) {
                FileOutputStream fos = openFileOutput(fileName, MODE_APPEND);
                //이어붙이기로
//                Toast.makeText(this, "saved2", Toast.LENGTH_SHORT).show();
                PrintWriter writer = new PrintWriter(fos);
                writer.println(corrXYZ);
                writer.flush();
                writer.close();
            } else {
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);

                PrintWriter writer = new PrintWriter(fos);
                writer.println(corrXYZ);
                writer.flush();
                writer.close();
            }
//            Toast.makeText(this, "corrected_XYZ", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    class BackgroundThread extends Thread {
        boolean running = false;

        public void run() {
            int value = 1;
            running = true;
            currtime = ds.getCurrtime();
            while (running) {
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                bundle.putString("collActtype", collectActtype);
                message.setData(bundle);
                handler.sendMessage(message);

                try {
                    Thread.sleep(cycle * 1000);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int value = bundle.getInt("value");
            String data = bundle.getString("collActtype");

            if (value == 1) {
                stopNetwork();
                if (!data.equals(" ")) {
                    correctData();
                    corrLog();
                }
                    updateData();
                    setViews();
                    saveLog();
                    corrXYZ();



            }
        }
    }
    // 1. callback 시 gps의 locationInformation을 사용해서 현재 위치 정보를 lat, lon, speed를 넣을 것 - 수정 완료
    // 2. android의 스레드를 열어서 textview 및 지도에서 위치 갱신을 위한 메소드를 생성 - 수정 완료
    // 3. map 표시를 제외한 GPS 설정과 값 받아오는 메소드들은 GPS 클래스로 이전할 것

    final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            location = locationResult.getLastLocation();
            if (location != null) {
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                li.setLocationInformation(location.getLatitude(), location.getLongitude(), location.getSpeed());
                String markerTitle = getCurrentAddress(currentPosition);
                setCurrentLocation(location);
                mCurrentLocation = location;
            }
        }
    };


    public String getCurrentAddress(LatLng latlng) {

        //GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location) {
//    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.draggable(true);

        currentMarker = mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }


    public void setDefaultLocation() {

        //디폴트 위치 군산대학교
        LatLng DEFAULT_LOCATION = new LatLng(35.945287, 126.682163);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    //    런타임 퍼미션 처리
    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 퍼미션을 허용했다면 위치 업데이트를 시작
            if (check_result) {
                startLocationUpdates();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();

                } else {
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
                }
            }
        }
    }

    //gps활성화
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}