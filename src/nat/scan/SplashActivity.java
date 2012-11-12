package nat.scan;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends Activity {
	private long splashDelay = 3000;
	public static String TAG = "GCMIntentService";

	String SENDER_ID = "215861736980";

	private String getId() {
		SharedPreferences settings = getSharedPreferences("scan", 1);

		String id = settings.getString("id", "");
		return id;
	}

	private void saveGoogleId(String id)
	{
		//create or retrieve the shared preference object
		SharedPreferences mySharedPreferences = getSharedPreferences("scan", 1);

		//Retrieve an editor to modify the shared preference
		SharedPreferences.Editor editor = mySharedPreferences.edit();

		//store new primitive types in the shared preferences object 
		editor.putString("googleId", id);
	
		editor.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
		    	SharedPreferences settings = getSharedPreferences("scan", 1);
				String requested_help = settings.getString("requested_help", "");
				
//				finish();
				String id = getId();
				if(id == "") {
					Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
					startActivity(mainIntent);
				}
				else{
					Intent mainIntent = new Intent().setClass(SplashActivity.this, SubActivity.class);
					startActivity(mainIntent);
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
	}
}
