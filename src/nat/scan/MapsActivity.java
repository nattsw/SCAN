package nat.scan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.os.Bundle;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

public class MapsActivity extends MapActivity  {
    /** Called when the activity is first created. */
	private MapView mapView;
	private MapController mapController;
	List<Overlay> listOfOverlays;
	JSONObject request;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        //setup mapView and mapController
        mapView = (MapView)findViewById(R.id.mapView);
    	mapController = mapView.getController();
    	mapView.setBuiltInZoomControls(true);
    	
    	//get stuff from previous intent
        Bundle extras = getIntent().getExtras();
        
        //check if has requester details in json
        if (extras.containsKey("REQJSON")) {
        	
        	//check if has responder (my) details and add overlay on map, last value "1" means do not show respond button
        	if (extras.containsKey("MYLATITUDE") && extras.containsKey("MYLONGITUDE")) {    
	        	double myLatitude = Double.parseDouble(extras.getString("MYLATITUDE"));
	            double myLongitude = Double.parseDouble(extras.getString("MYLONGITUDE"));
	            AddOverlay("Your Location", myLatitude, myLongitude, "" , "1");
	        }
        	
        	//add requester overlay and include respond button if can be responded to
        	try {
				request = new JSONObject(extras.getString("REQJSON"));
				double requesterLatitude = Double.parseDouble(request.get("latitude").toString());
				double requesterLongitude = Double.parseDouble(request.get("longitude").toString());
				
				//check if should show respond button for the responder to /acceptRequest
				String respondButton = "";
				if (request.get("in_progress").toString().equals("0"))
					respondButton = "0";
				else
					respondButton = "1";
				
				AddOverlay(request.get("requester_name").toString(), requesterLatitude, 
						requesterLongitude, request.get("requested_time").toString(), respondButton);
				
				//zoom mapview to requester location
	            GeoPoint requesterLocation = new GeoPoint((int) (requesterLatitude * 1E6), (int) (requesterLongitude * 1E6));
	            mapController.animateTo(requesterLocation);
			} catch (Exception e) {
				Toast.makeText(this, "Error: Cannot show requester location.", Toast.LENGTH_LONG).show();
			}
	        
            mapController.setZoom(15);
            mapView.invalidate();
        }
    }
    
    public void AddOverlay(String title, double lat, double lon, String time, String respondButton)
    {
        GeoPoint overlayLoc = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
    	listOfOverlays = mapView.getOverlays();
        Drawable drawable;
        ItemsOverlay itemizedoverlay;
        OverlayItem overlayitem;
        
        if (!time.equals("")) {
        	drawable = this.getResources().getDrawable(R.drawable.redandroid);
        	itemizedoverlay = new ItemsOverlay(drawable, this);
        	overlayitem = new OverlayItem(overlayLoc, title, "Location " + Double.toString(lat) + ", "  + Double.toString(lon) + "\nTime: " + time + " " + respondButton);
        }
        else {
        	drawable = this.getResources().getDrawable(R.drawable.blueandroid);
        	itemizedoverlay = new ItemsOverlay(drawable, this);
        	overlayitem = new OverlayItem(overlayLoc, title, "Location " + Double.toString(lat) + ", "  + Double.toString(lon) + " " + respondButton);
        }

        //add an overlayitem
        itemizedoverlay.addOverlay(overlayitem);
        //add the overlay
        listOfOverlays.add(itemizedoverlay);
    }
    
    public void ReAddOverlay(GeoPoint loc, String title, String snip)
    {
    	listOfOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        ItemsOverlay itemizedoverlay = new ItemsOverlay(drawable, this);
        OverlayItem overlayitem;
        
       	overlayitem = new OverlayItem(loc, title, snip);
        
        //add an overlayitem
        itemizedoverlay.addOverlay(overlayitem);
        //add the overlay
        listOfOverlays.add(itemizedoverlay);
    }
    
    public String RespondToRequest(int overlayIndex, GeoPoint loc, String title, String snip) {
		try {        
	    	Posts getRequests = new Posts();
	    	getRequests.execute("/acceptRequest", request.get("id").toString());

//			SharedPreferences settings = getSharedPreferences("scan", 1);
//			String responderID = settings.getString("id", "");
//	    	getRequests.execute("/acceptRequest", request.get("id").toString(), responderID);
	    	System.out.println("!!/acceptRequest!!");
	    	ArrayList<String> result = getRequests.get();
	    	
		    	if (result.get(0).equals("200")) {
		    		Toast.makeText(this, "Responded to " + request.get("requester_name").toString(), Toast.LENGTH_LONG).show();
		    		
		    		listOfOverlays.remove(overlayIndex);
		    		ReAddOverlay(loc, title, snip);
		    	    return "1";
		    	}
  		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
    }

    //DO NOT REMOVE
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}