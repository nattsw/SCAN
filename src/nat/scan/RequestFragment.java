package nat.scan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RequestFragment extends Fragment {
	
	private final static String SERVICE_URL = "http://scan.soelinmyat.com/api/requestHelp";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request, container, false);
    }
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final TextView characterCount = (TextView)getView().findViewById(R.id.charactercount_textview);
		final EditText detailsTextfield = (EditText) getView().findViewById( R.id.details_textfield );
		final CheckBox addCheckbox = ( CheckBox ) getView().findViewById( R.id.additional_checkbox );
		final TextWatcher textWatcher = new TextWatcher() {
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        	characterCount.setText(String.valueOf(160-s.length())+" characters left");
	        }

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
		};
		detailsTextfield.addTextChangedListener(textWatcher);
		addCheckbox.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0) {
				if ( addCheckbox.isChecked()) {
					characterCount.setVisibility(View.VISIBLE);
					detailsTextfield.setVisibility(View.VISIBLE);
		        } else {
		        	characterCount.setVisibility(View.INVISIBLE);
					detailsTextfield.setVisibility(View.INVISIBLE);
		        }
			}
		});
    }
	public void onPause() {
		super.onPause();
		final TextView characterCount = (TextView)getView().findViewById(R.id.charactercount_textview);
		final EditText detailsTextfield = (EditText) getView().findViewById( R.id.details_textfield );
		final CheckBox addCheckbox = ( CheckBox ) getView().findViewById( R.id.additional_checkbox );
		characterCount.setText("160 characters left");
		detailsTextfield.setText("");
		addCheckbox.setChecked(false);
	}
	  /** Called when the user clicks on help request button  */
    public void help_button_click() {
        
    	// to get GPS coordinates
    	LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
    	Criteria criteria = new Criteria ();
    	String bestProvider = locationManager.getBestProvider (criteria, false);
    	Location location = locationManager.getLastKnownLocation (bestProvider);

    	LocationListener loc_listener = new LocationListener() {
    		public void onLocationChanged(Location l) {
    		}

    		public void onProviderEnabled(String p) {
    		}

    		public void onProviderDisabled(String p) {
    		}

    		public void onStatusChanged(String p, int status, Bundle extras) {
    		}      
    	};
    	locationManager.requestLocationUpdates(bestProvider,0 ,0, loc_listener);
    	location = locationManager.getLastKnownLocation (bestProvider);   

    	double latitude;
    	double longitude;
    	
    	try
    	{
    		latitude = location.getLatitude ();
    		longitude = location.getLongitude ();
    	}
    	catch (NullPointerException e)
    	{
    		latitude = -1.0;
    		longitude = -1.0;
    	}
    	
    	SharedPreferences settings = getActivity().getSharedPreferences("scan", 1);
    	
    	String name = settings.getString("name", "");
    	String lat = String.valueOf(latitude);
    	String lon = String.valueOf(longitude);
    	String requested_help = settings.getString("requested_help", "");
    	String details = "";
    	final CheckBox addCheckbox = ( CheckBox ) getView().findViewById( R.id.additional_checkbox );
		if (addCheckbox.isChecked()) {
			final EditText detailsTextfield = (EditText) getView().findViewById( R.id.details_textfield );
			details = detailsTextfield.getText().toString();
		}
    	System.out.println(name + " " + lat + " " + lon + " rh:" + requested_help);
    	
    	if (requested_help.equals("0")) {
    		if (!request_help(name, lat, lon, details).equals(""))
    		{
	    		Toast.makeText(getActivity(), "Help request sent!", Toast.LENGTH_SHORT).show();
	    	
		    	//save requested state in the preference
		    	SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("scan", 1);
		    	SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putString("requested_help", "1");
				editor.commit();
		    	
		    	Intent intent = new Intent(getActivity(), CancelRequestActivity.class);
		    	startActivity(intent);	
    		}
    		else {
    			Toast.makeText(getActivity(), "Unable to request for help!", Toast.LENGTH_SHORT).show();
    		}
    	}
    	else{
    		Toast.makeText(getActivity(), "Help request has already been sent!", Toast.LENGTH_SHORT).show();
    		Intent intent = new Intent(getActivity(), CancelRequestActivity.class);
        	startActivity(intent);
    	}
    }
    
    private String request_help(String username, String lat, String lon, String details)
    {	
    	ArrayList<String> result = new ArrayList<String>();
    	
    	try {
    		Posts requestHelp = new Posts();
    		requestHelp.execute("/requestHelp", username, lat, lon, details);
        	result = requestHelp.get();
        	
        	System.out.println(result.get(0)); //statuscode
        	System.out.println(result.get(1)); //entity
//    		//save data
        	if (result.get(0).equals("200")) {
        		JSONObject reply = new JSONObject(result.get(1));
        		System.out.println("id is " + reply.getString("request_id"));
        		return reply.getString("request_id");
        	}
        	else {
        		return "";
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return "";
    }
}