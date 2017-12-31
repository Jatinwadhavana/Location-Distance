package com.location.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.location.BaseActivity;
import com.location.R;
import com.location.databinding.FragmentTab1Binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class Fragment1 extends BaseFragment {

    private FragmentTab1Binding binding = null;
    private BaseActivity activity = null;

    public Fragment1() {

    }

    public Fragment1(BaseActivity baseActivity) {
        // Required empty public constructor
        activity = baseActivity;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = (FragmentTab1Binding) DataBindingUtil.inflate(inflater, R.layout.fragment_tab1, container, false);
        return binding.getRoot();
    }

    public void setCurrentAddress(Location location) {
        List<Address> addresses = null;
        if (location == null) {
            return;
        }
        try {
            Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(), 1);
        } catch (Exception ioException) {
            ioException.printStackTrace();
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {

        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            try {
                String finalAddress = "";
                for (String addressLine : addressFragments) {
                    finalAddress += "\n" + addressLine;
                }
                Log.d("NULL", "finalAddress= " + finalAddress);
                if (binding != null)
                    binding.tvUCurrentAdd.setText(getString(R.string.you_are_here) + "\n" + finalAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}