package com.easytravel.easytravel;

import org.apache.http.client.methods.HttpPut;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("NewApi")
public class JoinTrip extends Fragment implements OnClickListener{

	private String bearer;
	private String tripId;
	private TextView et_TripData;
	private Button btn_JoinTrip;
	
	public JoinTrip() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_join_trip,
				container, false);

		tripId = "ss";
		Log.d("D1", tripId);
		Log.d("D1", "after");

		et_TripData = (TextView) rootView.findViewById(R.id.et_trip_data);
		et_TripData.setVisibility(View.INVISIBLE);
		
		btn_JoinTrip = (Button) rootView.findViewById(R.id.btn_join_trip);

		bearer = "Bearer OkA1C0wIaxfSpJH5gu6UNNRT3zOGw6E1kADrFFnqoqAj_7MlNF_rP1x3LTwqIsYqUcJhQySrk07lFrPcLr8TUuDuruwagmYhGT_zVGe1_2dA8c-rK0IONI8g3AecGzzMBC1lZYtwD73obsAcJiXu1_u7z9BcTEsMyuBvgdNnapHTg30RJ8NPGC2-qQXKMfKW6i5brjxvTWcwdYzP06HPEUZJkPCFUO82i9uRWo3iV-lApjTXDHGmrD3xlVyjKkX6HX89Wxe_Hp0D8aqtyWnPa88u85xDVZVhSlVqEXrxCQJH2h7geAxhQbghMkTxd6O6KjwIFdgZPKQX7FAycL8wuDGRnNDyKWvrX9Ps7kCX1jaX2q3tlyhD78BEYAFcka4lpE9WuLfF48-pxtSy2qnm9j5-O9RvYRjc3GeD3S3Uqh2z3c1LHoFooKY_N2p5dDvB31waSqxCIU671fF7IbKSzZrxE0ZDJK138e0qtPBocMw";

		btn_JoinTrip.setOnClickListener(this);
		

		return rootView;
	}

	@Override
	public void onClick(View v) {
		HttpPut httpput = new HttpPut(
				"http://spa2014.bgcoder.com/api/trips/" + tripId);
		
		TripDetail tripDetail = new TripDetail(bearer, tripId,btn_JoinTrip,et_TripData, httpput, getActivity());
		tripDetail.execute();
	}
}
