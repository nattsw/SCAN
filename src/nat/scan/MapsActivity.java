package nat.scan;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.os.Bundle;
import android.graphics.drawable.Drawable;

public class MapsActivity extends MapActivity  {
    /** Called when the activity is first created. */
	private MapView mapView;
	private MapController mapController;
	
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
            
            double[] location = extras.getDoubleArray("LAT_LONG");
            double lat = location[0];
            double lng = location[1];
            GeoPoint requesterLoc = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
            
//            String problem = extras.getString("PROBLEM");
            
            List<Overlay> listOfOverlays = mapView.getOverlays();
            Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);

            ItemsOverlay itemizedoverlay = new ItemsOverlay(drawable, this);
            
            //create an overlay using the Requester Details
            OverlayItem overlayitem1 = new OverlayItem(requesterLoc, requesterName, "Location "
            						+ Double.toString(lat) + ", "  + Double.toString(lng) + "\nProblem: ");

            //---add an overlayitem---
            itemizedoverlay.addOverlay(overlayitem1);
            //---add the overlay---
            listOfOverlays.add(itemizedoverlay);
            
            mapController.animateTo(requesterLoc);
            mapController.setZoom(20);
            mapView.invalidate();
        }
    }

    //DO NOT REMOVE
    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }
}