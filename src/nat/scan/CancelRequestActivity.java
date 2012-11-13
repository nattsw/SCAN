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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CancelRequestActivity extends Activity {
	
	private final static String SERVICE_URL = "http://scan.soelinmyat.com/api/cancelRequest";
	//Context ctx;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_cancel_request);
    }
	
	public void onCancelClicked(View view) {
		
		SharedPreferences settings = getSharedPreferences("scan", 1);
    	String name = settings.getString("name", "");
    	
    	cancel_request(name); 
    	Toast.makeText(this, "The request has been cancelled!", Toast.LENGTH_LONG).show();
    	
    	//save requested state in the preference
    	SharedPreferences mySharedPreferences = getSharedPreferences("scan", 1);
    	SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("requested_help", "0");
		editor.commit();
    	
		super.finish();
    }
	
	private void cancel_request(String username)
    {	
    	HttpClient httpclient = new DefaultHttpClient();   
    	HttpPost httppost = new HttpPost(SERVICE_URL); 
    	
    	// data to send to server   
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
    	nameValuePairs.add(new BasicNameValuePair("username", username));

    	//String id="";
    	
    	try {
    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

    		HttpResponse response = httpclient.execute(httppost);
    		
    		HttpEntity entity = response.getEntity();
    		
    		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
    		StringBuilder builder = new StringBuilder();
    		
    		for (String line = null; (line = reader.readLine()) != null;) {
    		    builder.append(line).append("\n");
    		}
    		
    		//id = builder.toString();
    			
    		//JSONTokener tokener = new JSONTokener(builder.toString());
    		//JSONObject finalResult = new JSONObject(tokener);
    		

    	} catch (Exception e) {
    		e.printStackTrace();
    	} 
    }
}
