package nat.scan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
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
	int debug = 0;
	
	LocationManager locationManager;
	
	ArrayList<String> reqID;
	ArrayList<String> username;
	ArrayList<String> time;
	ArrayList<String> latitude;
	ArrayList<String> longitude;
	
	String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	  "Linux", "OS/2" };
	
	double[][] location = new double[][] { {100,1}, {1,100}, {103.8,1.3667}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0}, {0,0} };
	
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
	    	
	    	
	    	
	    	JSONObject reply1 = new JSONObject(result.get(1));
	    	JSONArray requests = new JSONArray(reply1.getString("requests"));
			
	    	System.out.println("Result " + requests.toString());
	    	username = new ArrayList<String>();
	    	time = new ArrayList<String>();
	    	latitude = new ArrayList<String>();
	    	longitude = new ArrayList<String>();
	    	Toast.makeText(getActivity(), Integer.toString(requests.length()) + " request(s)", Toast.LENGTH_SHORT).show();
	    	for(int i=0;i<requests.length();i++)
	    	{
	    		JSONObject request = (JSONObject) requests.get(i);
	    		reqID.add(request.get("id").toString());
	    		username.add(request.get("requester_name").toString());
	    		time.add(request.get("requested_time").toString());
	    		latitude.add(request.get("latitude").toString());
	    		longitude.add(request.get("longitude").toString());
	    	}
	    	
	  		} catch (Exception e) {
				e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity() ,
				  android.R.layout.simple_list_item_1, username);
		setListAdapter(adapter);
System.out.println("listadapterset");
		debug ++;
//		Toast.makeText(getActivity(), "list created " + Integer.toString(debug) + " time(s)", Toast.LENGTH_SHORT).show();
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String requester = (String) getListAdapter().getItem(position);
		Toast.makeText(getActivity(), requester + " selected", Toast.LENGTH_LONG).show();
		Intent intent = new Intent(getActivity(), MapsActivity.class);
		intent.putExtra("REQUESTER", requester);
		intent.putExtra("LATITUDE", latitude.get(position));
		intent.putExtra("LONGITUDE", longitude.get(position));
		intent.putExtra("TIME", time.get(position));
		System.out.println("Pos: " + Integer.toString(position) + " Time: " + time.get(position));
        startActivity(intent);
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
    	
    	System.out.println("Lat: " + lat);
    	System.out.println("Lon: " + lon);
    	
    	ArrayList<String> result = new ArrayList<String>();
    	result.add(Double.toString(lat));
    	result.add(Double.toString(lon));
    	return result;
    }
}