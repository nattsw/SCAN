package nat.scan;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SubActivity extends Activity {
	SettingsFragment sFrag;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sFrag = new SettingsFragment();
        Bundle extras = getIntent().getExtras();
        
        ActionBar actionBar = getActionBar();
 
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        String label1 = getResources().getString(R.string.request_label);
        Tab tab1 = actionBar.newTab();
        tab1.setText(label1);
        TabListener<RequestFragment> tl = new TabListener<RequestFragment>(this,
                label1, RequestFragment.class);
        tab1.setTabListener(tl);
        actionBar.addTab(tab1);
 
        if (extras != null && extras.getString("isSubscriber").equals("1")) {
	        String label2 = getResources().getString(R.string.feeds_label);
	        Tab tab2 = actionBar.newTab();
	        tab2.setText(label2);
	        TabListener<FeedsFragment> tl2 = new TabListener<FeedsFragment>(this,
	        		label2, FeedsFragment.class);
	        tab2.setTabListener(tl2);
	        actionBar.addTab(tab2);
        }
        
        String label3 = getResources().getString(R.string.settings_label);
        Tab tab3 = actionBar.newTab();
        tab3.setText(label3);
        TabListener<SettingsFragment> tl3 = new TabListener<SettingsFragment>(this,
                label3, SettingsFragment.class);
        tab3.setTabListener(tl3);
        actionBar.addTab(tab3); 
    }
 
	@Override
	public void onBackPressed() {
		
	}
	
    private class TabListener<T extends Fragment> implements
            ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
 
        /**
         * Constructor used each time a new tab is created.
         * 
         * @param activity
         *            The host Activity, used to instantiate the fragment
         * @param tag
         *            The identifier tag for the fragment
         * @param clz
         *            The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }
 
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                System.out.println("*******CLASSNAME: " + mClass.getName() + "********");
                
                if (mClass.getName().equals("nat.scan.SettingsFragment"))
                	sFrag = (SettingsFragment) mFragment;
                
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }
 
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }
 
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
    
    public void update_button_click(View view) {
    	sFrag.update_button_click();
    }
    public void logout_button_click(View view) {
    	sFrag.logout_button_click();
    }
}
