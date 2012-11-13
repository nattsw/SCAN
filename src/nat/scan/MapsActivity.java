package nat.scan;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.os.Bundle;
import android.graphics.drawable.Drawable;

public class MapsActivity extends MapActivity  {
    /** Called when the activity is first created. */
	private MapView mapView;
	private MapController mapController;
	List<Overlay> listOfOverlays;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	mapView = (MapView)findViewById(R.id.mapView);
        	mapController = mapView.getController();
        	mapView.setBuiltInZoomControls(true);
        	
        	//set Requester Details
            String requesterName = extras.getString("REQUESTER");
            double requesterLatitude = Double.parseDouble(extras.getString("LATITUDE"));
            double requesterLongitude = Double.parseDouble(extras.getString("LONGITUDE"));
            String requestedTime = extras.getString("TIME");
            String requestInProgress = extras.getString("PROGRESS");
            double myLatitude = Double.parseDouble(extras.getString("MYLATITUDE"));
            double myLongitude = Double.parseDouble(extras.getString("MYLONGITUDE"));
            AddOverlay(requesterName, requesterLatitude, requesterLongitude, requestedTime, requestInProgress);
            AddOverlay("Your Location", myLatitude, myLongitude, "" , "1");
            MyLocationOverlay a; 
            GeoPoint requesterLocation = new GeoPoint((int) (requesterLatitude * 1E6), (int) (requesterLongitude * 1E6));
            mapController.animateTo(requesterLocation);
            mapController.setZoom(20);
            mapView.invalidate();
        }
    }
    
    public void AddOverlay(String title, double lat, double lon, String time, String respondButton)
    {
        GeoPoint overlayLoc = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
    	listOfOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);

        ItemsOverlay itemizedoverlay = new ItemsOverlay(drawable, this);
        OverlayItem overlayitem;
        if (!time.equals(""))	
        	overlayitem = new OverlayItem(overlayLoc, title, "Location " + Double.toString(lat) + ", "  + Double.toString(lon) + "\nTime: " + time + " " + respondButton);
        else
        	overlayitem = new OverlayItem(overlayLoc, title, "Location " + Double.toString(lat) + ", "  + Double.toString(lon) + " " + respondButton);

        //---add an overlayitem---
        itemizedoverlay.addOverlay(overlayitem);
        //---add the overlay---
        listOfOverlays.add(itemizedoverlay);
    }

    //DO NOT REMOVE
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}