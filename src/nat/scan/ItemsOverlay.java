package nat.scan;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class ItemsOverlay extends ItemizedOverlay<OverlayItem> {

    //---array of OverlayItem objects---
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    Context mContext;

    public ItemsOverlay(Drawable defaultMarker) {
          super(boundCenterBottom(defaultMarker));
    }

    public ItemsOverlay(Drawable defaultMarker, Context context) {
          super(boundCenterBottom(defaultMarker));
          mContext = context;
    }

    //---add an OverlayItem object to the map---
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        //---call this to draw the OverLayItem objects---
        populate();
    }

    //---remove an OverlayItem object from the map---
    public void removeOverlay(OverlayItem overlay) {
        mOverlays.remove(overlay);
        //---call this to draw the OverLayItem objects---
        populate();
    }

    //---called when populate() is called; returns each OverlayItem object
    // in the array---
    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }

    //---returns the number of OverlayItem objects---
    @Override
    public int size() {
        return mOverlays.size();
    }

    //---called when the user taps on the OverlayItem objects---
    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(item.getTitle());
        String msg = item.getSnippet();
        if (msg.endsWith("0"))
        {
	        dialog.setPositiveButton("Respond!", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                 //do things
	            }
	        });
        }
        msg = msg.substring(0, msg.length()-1);
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }
}