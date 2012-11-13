package nat.scan;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity{
	//private final static String SERVICE_URL = "http://137.132.82.133/scan/api/register?json";
	private final static String SERVICE_URL = "http://scan.soelinmyat.com/api/register";	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_register);
		
    }
  
    /** Called when the user clicks the Register button */
    public void register_button_click(View view) {
    	try{
	    	EditText editName = (EditText) findViewById(R.id.username_textfield);
	    	String inputName = editName.getText().toString();
	
	    	EditText editPassword = (EditText) findViewById(R.id.password_textfield);
	    	String inputPassword = editPassword.getText().toString();
	    	
	    	EditText editDate = (EditText) findViewById(R.id.date_textfield);
	    	String inputDate = editDate.getText().toString();
	
	    	CheckBox checkBox = (CheckBox) findViewById(R.id.subscriber_checkbox);
	    	boolean isHelper = checkBox.isChecked();
	    	
	    	if ((inputName.equals(""))|(inputPassword.equals("")) | (inputDate.equals("")) )
	    	{
	    		Toast.makeText(this, "Incomplete Particulars", Toast.LENGTH_SHORT).show();
	    	} else {
	    		String result = register(inputName, inputPassword, inputDate, isHelper);
		    	if (!result.equals("")) {
		    		Toast.makeText(this, "Successful registration, " + inputName + "!", Toast.LENGTH_LONG).show();
		    		super.onBackPressed();
		    	}
		    	else
		    		Toast.makeText(this, "Your registration was unsuccessful. (Change username?)", Toast.LENGTH_LONG).show();
	    	}
    	} catch (Exception e)
    	{
    		System.out.println("registerbuttonclick exception: " + e.getMessage());
    	}
    }
    
    protected void savePreferences(String inputName, String inputPassword, String inputDate, String isHelper){
	
		//create or retrieve the shared preference object
		SharedPreferences mySharedPreferences = getSharedPreferences("scan", 1);
		
		//Retrieve an editor to modify the shared preference
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		//store new primitive types in the shared preferences object 
		//editor.putString("id", id);
		editor.putString("name", inputName);
		editor.putString("password", inputPassword);
		editor.putString("date_of_birth", inputDate);
		editor.putString("isHelper", isHelper);
		
		editor.commit();

    }

    private String register(String inputName, String inputPassword, String inputDate, boolean isHelper)
    {
    	ArrayList<String> result = new ArrayList<String>();
    
    	String  isHelperString = (isHelper == true) ? "1" : "2";
    	
    	try {
    		Posts getRequests = new Posts();
        	getRequests.execute("/register", inputName, inputPassword, inputDate, isHelperString);
        	result = getRequests.get();
        	
        	System.out.println("/register: Status Code: " + result.get(0)); //statuscode
        	System.out.println("/register: Entity : " + result.get(1)); //entity
//    		//save data
        	if (result.get(0).equals("200")) {
//        		savePreferences(inputName, inputPassword, inputDate, isHelperString);
        		JSONObject reply = new JSONObject(result.get(1));
        		System.out.println("/register: userID is " + reply.getString("id"));
        		return reply.getString("id");
        	}
        	else {
        		return "";
        	}
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    	return "";
    }
}


