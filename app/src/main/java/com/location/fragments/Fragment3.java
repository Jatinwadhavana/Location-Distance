package com.location.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.location.BaseActivity;
import com.location.R;
import com.location.adapters.LocationHistoryAdapter;
import com.location.databinding.FragmentTab3Binding;
import com.location.globals.UtilPref;
import com.location.models.LocHistModel;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class Fragment3 extends BaseFragment {

    private FragmentTab3Binding binding = null;
    private LocationHistoryAdapter adapter = null;
    private BaseActivity activity = null;
    private ArrayList<LocHistModel> list = null;

    public Fragment3() {
        // Required empty public constructor
    }

    public Fragment3(BaseActivity baseActivity) {
        // Required empty public constructor
        activity = baseActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab3, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        binding.recHistory.setLayoutManager(layoutManager);
        list = new Gson().fromJson(UtilPref.getHistory(activity), new TypeToken<ArrayList<LocHistModel>>() {
        }.getType());


        adapter = new LocationHistoryAdapter(activity, list);
        binding.recHistory.setAdapter(adapter);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        binding.recHistory.addItemDecoration(itemDecorator);
        checkHist();
        return binding.getRoot();
    }

    public void addHistory(LocHistModel locHistModel) {
        try {
            list.add(0, locHistModel);
            refreshHist();
            checkHist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshHist() {
        try {
            adapter.notifyDataSetChanged();
            checkHist();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkHist() {
        try {
            binding.tvLocHistory.setVisibility(list.size()==0 ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}