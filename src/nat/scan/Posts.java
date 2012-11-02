package nat.scan;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;

import android.os.AsyncTask;


public class Posts extends AsyncTask<JSONStringer, Void, String> {
	private final static String SERVICE_URI = "http://invoicesafe.com/main0/Service1.svc/REST";

	@Override
	protected String doInBackground(JSONStringer... urls) {
		try {
	        //validate stuff//
			
	        boolean isValid = true;
	 
	        // Data validation goes here
	 
	        if (isValid) {
	 
	            // POST request to <service>/SaveVehicle
	            HttpPost request = new HttpPost(SERVICE_URI + "/SaveVehicle");
	            request.setHeader("Accept", "application/json");
	            request.setHeader("Content-type", "application/json");
	 
	            // Build JSON string somewhere else
//	            JSONStringer vehicle = new JSONStringer()
//	                .object()
//	                    .key("vehicle")
//	                        .object()
//	                            .key("plate").value(plate)
//	                            .key("make").value(make)
//	                            .key("model").value(model)
//	                            .key("year").value(Integer.parseInt(year.toString()))
//	                        .endObject()
//	                    .endObject();
	            StringEntity entity = new StringEntity(urls[0].toString());
	 
	            request.setEntity(entity);
	 
	            // Send request to WCF service
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpResponse response = httpClient.execute(request);
	 
//	            Log.d("WebInvoke", "Saving : " + response.getStatusLine().getStatusCode());
	        }
	 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		
		return "Error";
	}

	@Override
	protected void onPostExecute(String result) {
		
	}

}