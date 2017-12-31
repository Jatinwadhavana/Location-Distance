package com.location;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;



public class BaseActivity extends AppCompatActivity {

    public boolean checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) == 0) {
            return true;
        }
        return false;
    }

    @TargetApi(16)
    public ArrayList<String> getLocationPermission() {
        ArrayList<String> perArray = new ArrayList();
        if (Build.VERSION.SDK_INT > 22) {
            if (!checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                perArray.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }
           /* if (!checkPermission("android.permission.ACCESS_COARSE_LOCATION")) {
                perArray.add("android.permission.ACCESS_COARSE_LOCATION");
            }*/
        }
        return perArray;
    }
}
