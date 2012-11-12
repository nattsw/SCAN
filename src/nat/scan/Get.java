package nat.scan;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class Get extends AsyncTask<String, Void, String> {
	private final static String SERVICE_URI = "http://scan.soelinmyat.com/api";

	@Override
	protected String doInBackground(String... urls) {
		try {

			// Send GET request to <service>
			HttpGet request = new HttpGet(SERVICE_URI + urls[0]);
			
			/*std*/
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity responseEntity = response.getEntity();
			// Read response data into buffer
			char[] buffer = new char[(int) responseEntity.getContentLength()];
			InputStream stream = responseEntity.getContent();
			InputStreamReader reader = new InputStreamReader(stream);
			reader.read(buffer);
			stream.close();
			
			String returnThis = new String (buffer);
			return returnThis;
			
			/*std*/
			//JSONObject for Objects
			//JSONArray for Arrays of Objects
//			JSONArray plates = new JSONArray(new String(buffer));

//			return plates.toString();
			
			/* for objects
			JSONObject vehicle = new JSONObject(new String(buffer));
 
	        // Populate text fields
	        makeEdit.setText(vehicle.getString("make"));
	        plateEdit.setText(vehicle.getString("plate"));
	        modelEdit.setText(vehicle.getString("model"));
	        yearEdit.setText(vehicle.getString("year"));
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "Error";
	}

	@Override
	protected void onPostExecute(String result) {
		
	}

}