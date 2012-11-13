package nat.scan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FeedsFragment extends ListFragment {
	LocationManager locationManager;
	
	//to populate the list
	ArrayList<String> username;
	JSONArray requests;
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		/*get the user location to update server*/
		ArrayList<String> loc = _getLocation();
	    String lat = loc.get(0);
	    String lon = loc.get(1);
	    
		try {        
			/*HTTPGet for Requests*/
	    	Posts getRequests = new Posts();
	    	getRequests.execute("/getRequests", lat, lon);
	    	ArrayList<String> result = getRequests.get();
	    	
		    	if (result.get(0).equals("200")) {
			    	JSONObject reply1 = new JSONObject(result.get(1));
			    	requests = new JSONArray(reply1.getString("requests"));
					
			    	System.out.println("/getRequest: Result " + requests.toString());

			    	username = new ArrayList<String>();
			    	Toast.makeText(getActivity(), Integer.toString(requests.length()) + " request(s)", Toast.LENGTH_SHORT).show();
			    	for(int i=0;i<requests.length();i++)
			    	{
			    		JSONObject request = (JSONObject) requests.get(i);
			    		username.add(request.get("requester_name").toString());
			    	}
		    	}
  		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),	android.R.layout.simple_list_item_1, username);
		setListAdapter(adapter);
    }
	
	//when the user returns to this fragment from e.g. maps
	public void onResume() {
		super.onResume();

		/*get the user location to update server*/
		ArrayList<String> loc = _getLocation();
	    String lat = loc.get(0);
	    String lon = loc.get(1);
	    
		try {        
			/*HTTPGet for Requests*/
	    	Posts getRequests = new Posts();
	    	getRequests.execute("/getRequests", lat, lon);
	    	ArrayList<String> result = getRequests.get();
	    	
		    	if (result.get(0).equals("200")) {
			    	JSONObject reply1 = new JSONObject(result.get(1));
			    	requests = new JSONArray(reply1.getString("requests"));
					
			    	System.out.println("/getRequest: Result " + requests.toString());

			    	username = new ArrayList<String>();
			    	Toast.makeText(getActivity(), Integer.toString(requests.length()) + " request(s)", Toast.LENGTH_SHORT).show();
			    	for(int i=0;i<requests.length();i++)
			    	{
			    		JSONObject request = (JSONObject) requests.get(i);
			    		username.add(request.get("requester_name").toString());
			    	}
		    	}
  		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),	android.R.layout.simple_list_item_1, username);
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		try {
			JSONObject reqMaps = (JSONObject) requests.get(position);
			SharedPreferences settings = getActivity().getSharedPreferences("scan", 1);
			String responderID = settings.getString("id", "");
			if (reqMaps.get("in_progress").toString().equals("0") || reqMaps.get("in_progress").toString().equals(responderID))
			{
				Intent intent = new Intent(getActivity(), MapsActivity.class);
				intent.putExtra("REQJSON", requests.get(position).toString());
				ArrayList<String> loc = _getLocation();
			    String lat = loc.get(0);
			    String lon = loc.get(1);
				intent.putExtra("MYLATITUDE", lat);
				intent.putExtra("MYLONGITUDE", lon);
		        startActivity(intent);
			} else 
				Toast.makeText(getActivity(), "Request has been responded by someone else.", Toast.LENGTH_SHORT).show();
		} catch (JSONException e) {
			Toast.makeText(getActivity(), "Error: Refresh Page please.", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	private ArrayList<String> _getLocation()
    {
    	// Get the location manager
		locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
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

    	double lat;
    	double lon;
    	
    	try
    	{
    		lat = location.getLatitude();
    		lon = location.getLongitude();
    	}
    	catch (NullPointerException e)
    	{
    		lat = -1.0;
    		lon = -1.0;
    	}
    	
    	System.out.println("Responder Latitude: " + lat + " Longitude: " + lon);
    	
    	ArrayList<String> result = new ArrayList<String>();
    	result.add(Double.toString(lat));
    	result.add(Double.toString(lon));
    	return result;
    }
}