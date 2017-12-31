package com.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.location.databinding.ActivityMainBinding;
import com.location.fragments.Fragment1;
import com.location.fragments.Fragment2;
import com.location.fragments.Fragment3;
import com.location.globals.UtilPref;
import com.location.models.LocHistModel;
import com.location.views.MainView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.location.globals.Constants.DATE_FORMAT;
import static com.location.globals.Constants.DAY_FORMAT;
import static com.location.globals.Constants.DISTANCE_LIMIT;
import static com.location.globals.Constants.LOC_REQ_CODE;
import static com.location.globals.Constants.TIME_FORMAT;

public class MainActivity extends BaseActivity implements MainView{

    private ActivityMainBinding binding = null;
    private LocationManager locationManager = null;
    private ViewPagerAdapter adapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilPref.setStartLoc(this, "");
        UtilPref.setCurrentLoc(this, "");
        UtilPref.setDIST(this, "0");
        UtilPref.setSession(this, false);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        createViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
        createTab();
        if (getLocationPermission().size() > 0) {
            ActivityCompat.requestPermissions(this, getLocationPermission().toArray(new String[0]), LOC_REQ_CODE);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    requestLocation(true);
                } else if (position == 1) {
                    ((Fragment2) adapter.getItem(1)).setDistanceLbl();
                } else if (position == 2) {
                    ((Fragment3) adapter.getItem(2)).refreshHist();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.viewpager.setCurrentItem(1);
        requestLocation(true);
        checkGPS();
    }

    private void requestLocation(boolean isForSingle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation == null) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (lastLocation == null) {
            showMessageIfGPSOff();
        } else {
            ((Fragment1) adapter.getItem(0)).setCurrentAddress(lastLocation);
            setStartLoc(lastLocation);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, locationListener);
    }

    private void setStartLoc(Location startLoc) {
        if (UtilPref.getStartLoc(MainActivity.this).length() == 0) {
            UtilPref.setStartLoc(MainActivity.this, startLoc.getLatitude() + ":" + startLoc.getLongitude());
        }
    }

    private void setDistance(Location location) {
        try {
            UtilPref.setCurrentLoc(MainActivity.this, location.getLatitude() + ":" + location.getLongitude());
            String startDist = UtilPref.getStartLoc(this);
            String[] lt = startDist.split(":");
            Location startLoc = new Location("sLoc");

            startLoc.setLatitude(Double.parseDouble(lt[0]));
            startLoc.setLongitude(Double.parseDouble(lt[1]));

            float distance = startLoc.distanceTo(location);
            UtilPref.setDIST(this, String.valueOf(distance));
            Log.d("distance= ", "=" + distance);

            ((Fragment2) adapter.getItem(1)).setDistanceLbl();
            if (distance > DISTANCE_LIMIT) {
                if (!UtilPref.getSession(this)) {
                    UtilPref.setSession(this, true);
                    setSessionData();
                    sendNotification();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSessionData() {
        try {
            JSONObject jsonObject = new JSONObject();
            LocHistModel locHistMode = new LocHistModel();
            SimpleDateFormat sdf = new SimpleDateFormat(DAY_FORMAT);
            Date d = new Date();
            locHistMode.setDay(sdf.format(d));
            jsonObject.put("day", locHistMode.getDay());

            sdf = new SimpleDateFormat(DATE_FORMAT);
            locHistMode.setDateStr(sdf.format(d));
            jsonObject.put("dateStr", locHistMode.getDateStr());

            sdf = new SimpleDateFormat(TIME_FORMAT);
            locHistMode.setTime(sdf.format(d));
            jsonObject.put("time", locHistMode.getTime());


            JSONArray jsonArray = new JSONArray(UtilPref.getHistory(this));
            jsonArray.put(jsonObject);
            UtilPref.setHistory(this, jsonArray.toString());
            Log.d("getHistory", "" + UtilPref.getHistory(this));
            ((Fragment3) adapter.getItem(2)).addHistory(locHistMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOC_REQ_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int reqCode = grantResults[i];
                if (permission.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, (reqCode == 0) ? R.string.loc_permission_msg : R.string.loc_permission_rejected_msg, Toast.LENGTH_SHORT).show();
                    if (reqCode == 0) {
                        requestLocation(true);
                    }
                }
            }
        }
    }

    private void createViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Fragment1(this), getString(R.string.tab_1));
        adapter.addFrag(new Fragment2(this), getString(R.string.tab_2));
        adapter.addFrag(new Fragment3(this), getString(R.string.tab_3));
        viewPager.setAdapter(adapter);
    }

    private void createTab() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.iten_tab, null);
        tabOne.setText(R.string.tab_1);
        binding.tabs.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.iten_tab, null);
        tabTwo.setText(R.string.tab_2);
        binding.tabs.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.iten_tab, null);
        tabThree.setText(R.string.tab_3);
        binding.tabs.getTabAt(2).setCustomView(tabThree);
    }

    @Override
    public void showMessageIfGPSOff() {
        Toast.makeText(this, R.string.gps_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean checkGPS() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!statusOfGPS){
            showMessageIfGPSOff();
        }
        return statusOfGPS;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(getBaseContext(), ">> " + location.getLatitude() + ":" + location.getLongitude(), Toast.LENGTH_LONG).show();

            setStartLoc(location);
            setDistance(location);

            ((Fragment1) adapter.getItem(0)).setCurrentAddress(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void sendNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("You covered " + UtilPref.getDIST(this) + " meters");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setContentIntent(contentIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        notificationManager.notify(0, notification);
    }
}