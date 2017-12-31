package com.location.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.location.R;
import com.location.models.LocHistModel;

import java.util.ArrayList;

public class LocationHistoryAdapter extends RecyclerView.Adapter<LocationHistoryAdapter.ViewHolder> {
    private ArrayList<LocHistModel> mDataset;
    private Activity activity;

    public LocationHistoryAdapter(Activity activity, ArrayList<LocHistModel> myDataset) {
        this.activity = activity;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LocationHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_history, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tvLocDay.setText(activity.getString(R.string.day) + mDataset.get(position).getDay());
        holder.tvLocDate.setText(activity.getString(R.string.date_str) + mDataset.get(position).getDateStr());
        holder.tvLocTime.setText(activity.getString(R.string.time) + mDataset.get(position).getTime());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocDay = null;
        private TextView tvLocDate = null;
        private TextView tvLocTime = null;

        // each data item is just a string in this case
        ViewHolder(View v) {
            super(v);
            tvLocDay = v.findViewById(R.id.tvLocDay);
            tvLocDate = v.findViewById(R.id.tvLocDate);
            tvLocTime = v.findViewById(R.id.tvLocTime);
        }
    }
}