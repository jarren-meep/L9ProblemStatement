package com.myapplicationdev.android.l9problemstatement;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileWriter;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

public class MyService extends Service {

    boolean started;
    private FusedLocationProviderClient client;
    String folderLocation;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("Service", "Service created");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final LocationRequest mLocationRequest= new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);

        client = LocationServices.getFusedLocationProviderClient(MyService.this);
            folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
            File folder = new File(folderLocation);
            if (folder.exists() == false){
                boolean result = folder.mkdir();
                if (result == true){
                    Log.d("File Read/Write", "Folder created");
                } else {
                    Log.d("File Read/Write", "Folder creation failed");
                }
            }
        Log.d("yes", "folder checkl");
                final LocationCallback mlocationcallBack = new LocationCallback() {
                    String msg = "";
                    @Override
                    public void onLocationResult(LocationResult locationResult) {

                        Log.d("yes", "onLocation");
                        double lat = 0;
                        double lng = 0;
                        if (locationResult != null) {
                            Location data = locationResult.getLastLocation();
                            lat = data.getLatitude();
                            lng = data.getLongitude();
                            msg = "Lat : " + lat + "Long : " + lng;
                        }
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        File targetFile = new File(folderLocation, "data2.txt");
                        try {
                            FileWriter writer = new FileWriter(targetFile, true);
                            writer.write("" + lat + "\n");
                            writer.write("" + lng + "\n");
                            writer.flush();
                            writer.close();
                        } catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Failed to write!",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                };
                if (checkPermission() == true) {
                    Log.d("yes", "onLocation2");
                client.requestLocationUpdates(mLocationRequest, mlocationcallBack, null);
            }

        return super.onStartCommand(intent, flags, startId);
    }

        @Override
        public void onDestroy () {
            Log.d("Service", "Service exited");
            super.onDestroy();
        }


    private boolean checkPermission(){
        Log.d("yes", "Permission checkl");
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MyService.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MyService.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    }
