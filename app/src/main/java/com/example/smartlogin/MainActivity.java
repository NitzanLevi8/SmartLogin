package com.example.smartlogin;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import java.util.Calendar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;

import android.location.Location;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText inputBattery;
    Button checkBattery;
    ImageView statusBattery;
    EditText inputCharging;
    Button checkCharging;
    ImageView statusCharging;
    EditText inputWifi;
    Button checkWifi;
    ImageView statusWifi;
    EditText inputHour;
    Button checkHour;
    ImageView statusHour;
    EditText inputLocation;
    Button checkLocation;
    ImageView statusLocation;
    Button btnLogin;

    boolean isBatteryCorrect = false;
    boolean isChargingCorrect = false;
    boolean isWifiCorrect = false;
    boolean isHourCorrect = false;
    boolean isLocationCorrect = false;
    FusedLocationProviderClient fusedLocationClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBattery = findViewById(R.id.input_battery);
        checkBattery = findViewById(R.id.check_battery);
        statusBattery = findViewById(R.id.status_battery);
        inputCharging = findViewById(R.id.input_charging);
        checkCharging = findViewById(R.id.check_charging);
        statusCharging = findViewById(R.id.status_charging);
        inputWifi = findViewById(R.id.input_wifi);
        checkWifi = findViewById(R.id.check_wifi);
        statusWifi = findViewById(R.id.status_wifi);
        inputHour = findViewById(R.id.input_hour);
        checkHour = findViewById(R.id.check_hour);
        statusHour = findViewById(R.id.status_hour);
        inputLocation = findViewById(R.id.input_location);
        checkLocation = findViewById(R.id.check_location);
        statusLocation = findViewById(R.id.status_location);
        btnLogin = findViewById(R.id.btn_login);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermissionIfNeeded();

        checkBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBatteryLock();
            }
        });

        checkCharging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkChargingLock();
            }
        });

        checkWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWifiLock();
            }
        });

        checkHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkHourLock();
            }
        });

        checkLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationLock();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSuccessToast();
            }
        });

    }

    private void checkBatteryLock() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) ((level / (float) scale) * 100);
        String userInput = inputBattery.getText().toString().trim();

        if (userInput.equals(String.valueOf(batteryPct))) {
            statusBattery.setImageResource(android.R.drawable.presence_online);
            isBatteryCorrect = true;
        } else {
            statusBattery.setImageResource(android.R.drawable.presence_busy);
            isBatteryCorrect = false;
        }
        updateLoginButton();
    }

    private void checkChargingLock() {

        String userInput = inputCharging.getText().toString().trim().toLowerCase();
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        if ((userInput.equals("yes") && isCharging) || (userInput.equals("no") && !isCharging)) {
            statusCharging.setImageResource(android.R.drawable.presence_online);
            isChargingCorrect = true;
        } else {
            statusCharging.setImageResource(android.R.drawable.presence_busy);
            isChargingCorrect = false;
        }
        updateLoginButton();
    }

    private void checkWifiLock() {
        String userInput = inputWifi.getText().toString().trim().toLowerCase();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
        boolean isConnectedToWifi = capabilities != null &&
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        if ((userInput.equals("yes") && isConnectedToWifi) || (userInput.equals("no") && !isConnectedToWifi)) {
            statusWifi.setImageResource(android.R.drawable.presence_online);
            isWifiCorrect = true;
        } else {
            statusWifi.setImageResource(android.R.drawable.presence_busy);
            isWifiCorrect = false;
        }
        updateLoginButton();
    }

    private void checkHourLock() {
        String userInput = inputHour.getText().toString().trim();
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        if (userInput.equals(String.valueOf(currentHour))) {
            statusHour.setImageResource(android.R.drawable.presence_online);
            isHourCorrect = true;
        } else {
            statusHour.setImageResource(android.R.drawable.presence_busy);
            isHourCorrect = false;
        }
        updateLoginButton();
    }

    private void requestLocationPermissionIfNeeded() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationLock();
            } else {
                statusLocation.setImageResource(android.R.drawable.presence_busy);
                isLocationCorrect = false;
                updateLoginButton();
            }
        }
    }

    private void checkLocationLock() {
        String userInput = inputLocation.getText().toString().trim().toLowerCase();
        double targetLat = 32.0853;
        double targetLng = 34.7818;
        double radiusInMeters = 10000;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).setMaxUpdates(1).build();

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location newLocation = locationResult.getLastLocation();
                if (newLocation == null) {
                    Toast.makeText(MainActivity.this, "Location is null!", Toast.LENGTH_LONG).show();
                    statusLocation.setImageResource(android.R.drawable.presence_busy);
                    isLocationCorrect = false;
                    updateLoginButton();
                    return;
                }
                float[] distance = new float[1];
                Location.distanceBetween(newLocation.getLatitude(), newLocation.getLongitude(), targetLat, targetLng, distance);

                Toast.makeText(MainActivity.this, "Lat: " + newLocation.getLatitude() + ", Lng: " + newLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                if (userInput.contains("tel") && distance[0] <= radiusInMeters) {
                    statusLocation.setImageResource(android.R.drawable.presence_online);
                    isLocationCorrect = true;
                } else {
                    statusLocation.setImageResource(android.R.drawable.presence_busy);
                    isLocationCorrect = false;
                }
                updateLoginButton();
            }
        }, null);
    }



    private void updateLoginButton() {
        if (isBatteryCorrect && isChargingCorrect && isWifiCorrect && isHourCorrect && isLocationCorrect) {
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundTintList(getColorStateList(android.R.color.holo_green_dark));
        } else {
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        }
    }


    private void showSuccessToast() {
        Toast toast = Toast.makeText(this, "Access Granted! Welcome :)", Toast.LENGTH_LONG);
        toast.setGravity(android.view.Gravity.CENTER, 0, 0);
        toast.show();
    }


}
