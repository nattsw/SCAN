package nat.scan;


import java.io.BufferedReader;
import java.io.IOException;
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
    
    /** Called when the user clicks on checkbox  */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        		
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        if (checkBox.isChecked()) {
            checkBox.setChecked(true);
        }   
    }
  
    /** Called when the user clicks the Register button */
    public void Register_button_click(View view) {
    	
        // Do something in response to button
    	final Dialog dialog = new Dialog(RegisterActivity.this);
    	dialog.setContentView(R.layout.dialog_view);
    	
    	// to get name that user input
    	EditText editName = (EditText) findViewById(R.id.NameInput);
    	String inputName = editName.getText().toString();
    	System.out.println("inputName is "+ inputName);

    	// to get password that user input
    	EditText editPassword = (EditText) findViewById(R.id.PasswordInput);
    	String inputPassword = editPassword.getText().toString();
    	System.out.println("inputPassword is "+ inputPassword);
    	
    	// to get date that user input
    	EditText editDate = (EditText) findViewById(R.id.DateInput);
    	String inputDate = editDate.getText().toString();
    	System.out.println("inputDate is "+ inputDate);

    	if ((inputName.equals(""))|(inputPassword.equals("")) | (inputDate.equals("")) )
    	{
    		dialog.setTitle("Error"); 
    		
    		Toast.makeText(this, "Incomplete Particulars", Toast.LENGTH_SHORT).show();
    	} else {
    		
    		dialog.setTitle("Success"); 
    		
	    	TextView text = (TextView)dialog.findViewById(R.id.dialogText); 
	    	text.setText("Your registration is successful.");

	    	String id = register(inputName, inputPassword, inputDate );
	    	text.setText("Your registration is successful. " + id);
	    	
	    	dialog.show();			
    	}
    }
    
    /*
    private String getGoogleId()
    {
    	SharedPreferences settings = getSharedPreferences("scan", 0);

		String id = settings.getString("googleId", "");
		return id;
    }*/
    
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

    private String register(String inputName, String inputPassword, String inputDate)
    {
    	final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
    	boolean isHelper = checkBox.isChecked();
    	String temp;
    	if(isHelper) {
    		temp = "1";
    	}
    	else {
    		temp = "0";
    	}
    	
    	HttpClient httpclient = new DefaultHttpClient();   
    	HttpPost httppost = new HttpPost(SERVICE_URL); 

    	//String googleId = getGoogleId();
    	
    	// data to send to server   
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);   
    	nameValuePairs.add(new BasicNameValuePair("username", inputName));
    	nameValuePairs.add(new BasicNameValuePair("password", inputPassword));
    	nameValuePairs.add(new BasicNameValuePair("dob", inputDate));
    	nameValuePairs.add(new BasicNameValuePair("isHelper", temp));
    	//nameValuePairs.add(new BasicNameValuePair("googleId", googleId));

    	String id="";
    	
    	try {
    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    		HttpResponse response = httpclient.execute(httppost);
    		
    		HttpEntity entity = response.getEntity();
    		
    		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
    		StringBuilder builder = new StringBuilder();
    		
    		for (String line = null; (line = reader.readLine()) != null;) {
    		    builder.append(line).append("\n");
    		}
    		
    		id = builder.toString();
    			
    		JSONTokener tokener = new JSONTokener(builder.toString());
    		JSONObject finalResult = new JSONObject(tokener);

    		id = finalResult.getString("id");
    		System.out.println(id);
    		
    		//save data
    		savePreferences(inputName, inputPassword, inputDate, temp);

    	} catch (ClientProtocolException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		//System.out.println("IO errors");
    		e.printStackTrace();
    	} catch (JSONException e) {
    		//System.out.println("JSON error");
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    	return id;
    }
    
}


