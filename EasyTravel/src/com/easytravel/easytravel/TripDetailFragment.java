package com.easytravel.easytravel;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TripDetailFragment extends Fragment {

public TripDetailFragment(){}
	
	TextView tv_TripDetail;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_trip_detail, container, false);
         
        tv_TripDetail = (TextView) rootView.findViewById(R.id.tv_tripDetail);
        
        Bundle bundle = this.getArguments();
		String tripInfo = bundle.getString("tripInfo");
		
		tv_TripDetail.setText(tripInfo);
        return rootView;
    }
}
