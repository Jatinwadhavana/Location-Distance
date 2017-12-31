package com.location.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.location.BaseActivity;
import com.location.R;
import com.location.databinding.FragmentTab2Binding;
import com.location.globals.UtilPref;

@SuppressLint("ValidFragment")
public class Fragment2 extends BaseFragment {

    private FragmentTab2Binding binding = null;
    private BaseActivity activity = null;

    public Fragment2() {
        // Required empty public constructor
    }

    public Fragment2(BaseActivity mainActivity) {
        this.activity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab2, container, false);
        setDistanceLbl();
        return binding.getRoot();
    }

    public void setDistanceLbl() {
        try {
            binding.tvDist.setText(getString(R.string.distance) + UtilPref.getDIST(activity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}