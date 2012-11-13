package nat.scan;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;


public class Posts extends AsyncTask<String, Void, ArrayList<String>> {
	private final static String SERVICE_URI = "http://scan.soelinmyat.com/api";

	@Override
	protected ArrayList<String> doInBackground(String... urls) {
		ArrayList<String> result = new ArrayList<String>();
		
		try {
	        //validate stuff//
			
	        boolean isValid = true;
	 
	        if (isValid) {
	 
	            // POST request to <service>
	            HttpPost request = new HttpPost(SERVICE_URI + urls[0]);
	            
	            if (urls[0].toString().equals("/login"))
	            {
	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);   
	            	nameValuePairs.add(new BasicNameValuePair("username", urls[1]));
	            	nameValuePairs.add(new BasicNameValuePair("password", urls[2]));
		 
		            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            }  else if (urls[0].toString().equals("/getRequests"))
	            {
	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);   
	            	nameValuePairs.add(new BasicNameValuePair("latitude", urls[1]));
	            	nameValuePairs.add(new BasicNameValuePair("longitude", urls[2]));
		            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            } else if (urls[0].toString().equals("/register"))
	            {
	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);   
	            	nameValuePairs.add(new BasicNameValuePair("username", urls[1]));
	            	nameValuePairs.add(new BasicNameValuePair("password", urls[2]));
	            	nameValuePairs.add(new BasicNameValuePair("dob", urls[3]));
	            	nameValuePairs.add(new BasicNameValuePair("isHelper", urls[4]));
	            	request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            } else if (urls[0].toString().equals("/requestHelp")) {
	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	            	nameValuePairs.add(new BasicNameValuePair("username", urls[1]));
	            	nameValuePairs.add(new BasicNameValuePair("latitude", urls[2]));
	            	nameValuePairs.add(new BasicNameValuePair("longitude", urls[3]));
	            	nameValuePairs.add(new BasicNameValuePair("details", urls[4]));
	            	request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            } else if (urls[0].toString().equals("/acceptRequest")) {
	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//	            	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	            	nameValuePairs.add(new BasicNameValuePair("requestID", urls[1]));
//	            	nameValuePairs.add(new BasicNameValuePair("responderID", urls[2]));
	            	request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            }
	 
	            // Send request to service
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpResponse response = httpClient.execute(request);

				HttpEntity responseEntity = response.getEntity();
				
				result.add(Integer.toString(response.getStatusLine().getStatusCode()));
				
				if (urls[0].toString().equals("/login") || urls[0].toString().equals("/getRequests") || urls[0].toString().equals("/register") || urls[0].toString().equals("/requestHelp") || urls[0].toString().equals("/acceptRequest"))
	            { 
		            char[] buffer = new char[(int) responseEntity.getContentLength()];
					InputStream stream = responseEntity.getContent();
					InputStreamReader reader = new InputStreamReader(stream);
					reader.read(buffer);
					stream.close();
					String bufString = new String (buffer);
					result.add(bufString);
	            } else
	            	result.add("");
				return result;
	        }
	 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return result;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends List<?>> T cast(Object obj) {
	    return (T) obj;
	}

}