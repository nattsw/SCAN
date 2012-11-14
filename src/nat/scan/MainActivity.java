package nat.scan;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void register(View view) {
    	Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    
    public void login(View view) {
        /*logincheck*/
    	try {
    		/*setup HTTPGet*/
	    	final Posts svc = new Posts();
	    	EditText userTextField = (EditText)findViewById(R.id.userTextField);
	    	String username = userTextField.getText().toString();
	    	EditText passwordTextField = (EditText)findViewById(R.id.passwordTextField);
	    	String password = passwordTextField.getText().toString();
	        svc.execute("/login", username, password);
	        System.out.println("/login " + username+ " " + password);
	        /*retrieve results from HTTPGet*/
	        Handler handler = new Handler();
	        handler.postDelayed(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					svc.cancel(true);
				}
	        }, 10000 );
	        ArrayList<String> result = svc.get(10000, TimeUnit.MILLISECONDS);
	        
	        if (result.get(0).equals("200")) { //code 200 means success
				JSONObject reply1 = new JSONObject(result.get(1));
				JSONObject userDetails = new JSONObject(reply1.getString("info"));
				System.out.println("Login User Details from Server: \n" + userDetails.toString());
				
		        //create or retrieve the shared preference object
				SharedPreferences mySharedPreferences = getSharedPreferences("scan", 1);				
				//Retrieve an editor to modify the shared preference
				SharedPreferences.Editor editor = mySharedPreferences.edit();	
				//store new primitive types in the shared preferences object 
				editor.putString("id", userDetails.getString("id")); 
				editor.putString("name", userDetails.getString("username")); 
				editor.putString("isHelper", userDetails.getString("isHelper")); 
				editor.putString("date_of_birth", userDetails.getString("dob"));
				editor.putString("requested_help", "0");
				editor.putString("responded_help", "0");
				editor.commit();
				
				Intent intent = new Intent(this, SubActivity.class);
				intent.putExtra("isSubscriber", userDetails.getString("isHelper"));
		        startActivity(intent);
				Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show();
	        }
	        else
	        	Toast.makeText(this, "Login Fail: Error " + result.get(0), Toast.LENGTH_SHORT).show();
    	} catch (Exception e) {
    		e.printStackTrace();
    		Toast.makeText(this, "Check your internet connectivity.", Toast.LENGTH_SHORT).show();
    	}
    }

	@Override
	public void onBackPressed() {
		
	}
}