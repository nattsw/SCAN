package nat.scan;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashActivity extends Activity {
	private long splashDelay = 3000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		TimerTask task = new TimerTask(){
			@Override
			public void run(){
		    	SharedPreferences settings = getSharedPreferences("scan", 1);
				String id = settings.getString("id", "");
				if(id == "") {
					System.out.println("no id");
					Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);
					startActivity(mainIntent);
				}
				else{
					System.out.println("id: " + id);
					Intent mainIntent = new Intent().setClass(SplashActivity.this, SubActivity.class);
					startActivity(mainIntent);
				}
			}
		};
		
		Timer timer = new Timer();
		timer.schedule(task, splashDelay);
	}
}
