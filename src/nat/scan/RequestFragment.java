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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class RequestFragment extends Fragment {
	
	private final static String SERVICE_URL = "http://scan.soelinmyat.com/api/requestHelp";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.fragment_request, container, false);
    }
	
	  /** Called when the user clicks on help request button  */
    public void help_button_click(View view) {
        
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
    	
//    	//sent lat,long,x,y,z to application server
    	SharedPreferences settings = getActivity().getSharedPreferences("scan", 1);
    	
    	String name = settings.getString("name", "");
    	String requested_help = settings.getString("requested_help", "");
    	
    	String lat = String.valueOf(latitude);
    	String lon = String.valueOf(longitude);
    	
    	//System.out.println("lat is "+ lat);
    	//System.out.println("lon is "+ lon);
    	if (requested_help.equals("")){
    		request_help(name, lat, lon);
    		
    		Toast.makeText(getActivity(), "Help request sent!", Toast.LENGTH_LONG).show();
    	
	    	//save requested state in the preference
	    	SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("scan", 1);
	    	SharedPreferences.Editor editor = mySharedPreferences.edit();
			editor.putString("requested_help", "1");
			editor.commit();
	    	
	    	Intent intent = new Intent(getActivity(), CancelRequestActivity.class);
	    	startActivity(intent);
    	}
    	else{
    		Toast.makeText(getActivity(), "Help request has already been sent!", Toast.LENGTH_LONG).show();
    		Intent intent = new Intent(getActivity(), CancelRequestActivity.class);
        	startActivity(intent);
    	}
    }
    
    private String request_help(String username, String lat, String lon)
    {	
    	HttpClient httpclient = new DefaultHttpClient();   
    	HttpPost httppost = new HttpPost(SERVICE_URL); 
    	
    	// data to send to server   
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
    	nameValuePairs.add(new BasicNameValuePair("username", username));
    	nameValuePairs.add(new BasicNameValuePair("longitude", lon));
    	nameValuePairs.add(new BasicNameValuePair("latitude", lat));
    	//nameValuePairs.add(new BasicNameValuePair("x", x));
    	//nameValuePairs.add(new BasicNameValuePair("y", y));
    	//nameValuePairs.add(new BasicNameValuePair("z", z));
    	//nameValuePairs.add(new BasicNameValuePair("googleId", googleId));

    	String id="";
    	
    	try {
    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    		HttpResponse response = httpclient.execute(httppost);
    		
    		HttpEntity entity = response.getEntity();
    		
    		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
    		StringBuilder builder = new StringBuilder();
    		
    		for (String line = null; (line = reader.readLine()) != null;) {
    		    builder.append(line).append("\n");
    		}
    		
    		id = builder.toString();
    			
    		JSONTokener tokener = new JSONTokener(builder.toString());
    		JSONObject finalResult = new JSONObject(tokener);

    		id = finalResult.getString("request_id");
    		System.out.println(id);
    		

    	} catch (ClientProtocolException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		//System.out.println("IO errors");
    		e.printStackTrace();
    	} catch (JSONException e) {
    		//System.out.println("JSON error");
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	return id;
    }
}