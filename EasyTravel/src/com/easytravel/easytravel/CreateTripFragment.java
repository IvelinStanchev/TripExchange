package com.easytravel.easytravel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class CreateTripFragment extends Fragment implements OnClickListener {

	private Spinner spinnerFrom;
	private Spinner spinnerEnd;
	private EditText etAvailbleSeats;
	private Button btnMakeTrip;
	private EditText etGetDate;
	private String bearer;

	public CreateTripFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_create_trip,
				container, false);

		spinnerFrom = (Spinner) rootView.findViewById(R.id.spinner_create_town_startTown);
		spinnerEnd = (Spinner) rootView.findViewById(R.id.spinner_create_town_endTown);
		/*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.array_names, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);*/
		
		etAvailbleSeats = (EditText) rootView
				.findViewById(R.id.et_available_seats);
		btnMakeTrip = (Button) rootView.findViewById(R.id.btn_make_trip);
		etGetDate = (EditText) rootView.findViewById(R.id.et_get_date);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());

		bearer = preferences.getString("access_token", "");

		btnMakeTrip.setOnClickListener(this);

		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CreateTripPost login = new CreateTripPost();

		String fromCity = spinnerFrom.getSelectedItem().toString();
		String toCity = spinnerEnd.getSelectedItem().toString();
		Integer availableSeats = Integer.valueOf(String.valueOf(etAvailbleSeats
				.getText()));
		String dateOfDeparture = String.valueOf(etGetDate.getText());

		if (availableSeats < 1 || availableSeats > 254) {
			Toast.makeText(
					getActivity(),
					"Invalid number of available seats. Must be between 1 and 254",
					Toast.LENGTH_SHORT).show();
		} else {
			login.execute(fromCity, toCity, String.valueOf(availableSeats),
					dateOfDeparture);
		}
	}

	private class CreateTripPost extends AsyncTask<String, Void, HttpResponse> {

		@Override
		protected void onPostExecute(HttpResponse response) {
			// TODO Auto-generated method stub
			super.onPostExecute(response);

			StringBuilder str = new StringBuilder();
			try {
				InputStream is = response.getEntity().getContent();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);

				String chunk = null;

				while ((chunk = br.readLine()) != null) {
					str.append(chunk);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				JSONObject jsnobject = new JSONObject(str.toString());

				if (response.getStatusLine().getStatusCode() == 200) {
					Toast.makeText(getActivity(), "Trip created successfully!",
							Toast.LENGTH_SHORT).show();
					
					StringBuilder tripData = new StringBuilder();

					String driverName = jsnobject.getString("driverName");
					tripData.append("Driver name: " + driverName + "\n\n");

					String fromCity = jsnobject.getString("from");
					tripData.append("From city: " + fromCity + "\n\n");

					String toCity = jsnobject.getString("to");
					tripData.append("To city: " + toCity + "\n\n");

					String departureDate = jsnobject.getString("departureDate");
					tripData.append("Departure date: " + departureDate + "\n\n");

					String freeSeats = jsnobject.getString("numberOfFreeSeats");
					tripData.append("Free seats:  " + freeSeats + "\n\n");
					
					Fragment fragment = new TripDetailFragment();

					Bundle bundle = new Bundle();
					bundle.putString("tripInfo", tripData.toString());
					fragment.setArguments(bundle);
					
					if (fragment != null) {
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager
								.beginTransaction()
								.replace(
										R.id.frame_container,
										fragment).commit();
					}
					
				} else if (response.getStatusLine().getStatusCode() == 400) {
					Toast.makeText(getActivity(),
							jsnobject.getString("message"), Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected HttpResponse doInBackground(String... params) {
			String fromCity = params[0];
			String toCity = params[1];
			String availableSeats = params[2];
			String dateOfDeparture = params[3];

			JSONObject jobj = new JSONObject();

			try {
				jobj.put("from", fromCity);
				jobj.put("to", toCity);
				jobj.put("availableSeats", availableSeats);
				jobj.put("departureTime", dateOfDeparture);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://spa2014.bgcoder.com/api/trips");

			httppost.setHeader("Authorization", "Bearer " + bearer);
			httppost.setHeader("Content-type", "application/json");

			try {
				httppost.setEntity(new StringEntity(jobj.toString()));
			} catch (UnsupportedEncodingException e1) {
				Log.d("D1", e1.toString());
			}

			Log.d("D1", "Grum 1 ");

			try {
				HttpResponse response = httpClient.execute(httppost);

				return response;
			} catch (Exception e) {
				Log.d("D1", e.toString() + e.toString());
			}

			return null;
		}
	}
}
