package nat.scan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsFragment extends Fragment {

	private final static String SERVICE_URL = "http://scan.soelinmyat.com/api/update";
	//private final static String SERVICE_URL = "http://137.132.82.133/scan/api/update";
	
	public static final String PREFERED_NAME_KEY = "PREFERED_NAME_KEY";
    public static final String PREFERED_AGE_KEY = "PREFERED_AGE_KEY";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		restoreUIState();
    }
    
    private void restoreUIState(){
    	SharedPreferences settings = getActivity().getSharedPreferences("scan", 1);
    	
    	String name = settings.getString("name", "");
    	EditText editText = (EditText)getView().findViewById(R.id.NameInput);
    	editText.setText(name);
    	
    	// to get password that user input
    	String password = settings.getString("password", "");
    	EditText editPassword = (EditText)getView().findViewById(R.id.PasswordInput);
    	editPassword.setText(password);
    	
    	// to get date that user input
    	String date_of_birth = settings.getString("date_of_birth", "");
    	EditText editDate = (EditText) getView().findViewById(R.id.DateInput);
    	editDate.setText(date_of_birth);

    	String helper = settings.getString("isHelper", "");
    	
    	boolean helperBoolean;
    	if (helper.equals("1")){
    		helperBoolean = true;
    	}else{
    		helperBoolean = false;
    	}

		final CheckBox checkBox = (CheckBox) getView().findViewById(R.id.update_checkbox);
        checkBox.setChecked(helperBoolean);   	
    }
  
    /** Called when the user clicks the Register button */
    public void update_button_click() {
    	System.out.println("SettingsFragment button click");
    	// to get name that user input
    	EditText editText = (EditText) getView().findViewById(R.id.NameInput);
    	String inputName = editText.getText().toString();
    	System.out.println("1");
    	// to get password that user input
    	EditText editPassword = (EditText) getView().findViewById(R.id.PasswordInput);
    	String inputPassword = editPassword.getText().toString();
    	//System.out.println("inputPassword is "+ inputPassword);
    	System.out.println("2");
    	// to get date that user input
    	EditText editDate = (EditText) getView().findViewById(R.id.DateInput);
    	String inputDate = editDate.getText().toString();
    	//System.out.println("inputDate is "+ inputDate);
    	System.out.println("3");
    	if (inputName.equals(""))
    	{
    		Toast.makeText(getActivity(), "Unsuccessful:\nName field is empty.", Toast.LENGTH_LONG).show();
    	} else {
    		
    		Toast.makeText(getActivity(), "Your profile has been updated.", Toast.LENGTH_LONG).show();
    		System.out.println("4");
	    	update(inputName, inputPassword, inputDate);
    	}
        
    }
    
    private void update(String inputName, String inputPassword, String inputDate)
    {
    	System.out.println("5");
    	final CheckBox checkBox = (CheckBox) getView().findViewById(R.id.update_checkbox);
    	boolean isHelper = checkBox.isChecked();
    	String temp;
    	if(isHelper) {
    		temp = "1";
    	}
    	else {
    		temp = "0";
    	}
    	System.out.println("6");
    	HttpClient httpclient = new DefaultHttpClient();   
    	HttpPost httppost = new HttpPost(SERVICE_URL); 

    	// data to send to server   
    	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);   
    	//nameValuePairs.add(new BasicNameValuePair("id", id));
    	nameValuePairs.add(new BasicNameValuePair("username", inputName));
    	nameValuePairs.add(new BasicNameValuePair("password", inputPassword));
    	nameValuePairs.add(new BasicNameValuePair("dob", inputDate));
    	nameValuePairs.add(new BasicNameValuePair("isHelper", temp));
    	System.out.println("7");
    	try {
    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    		httpclient.execute(httppost);    		
    		System.out.println("8");
    		//save data
    		savePreferences(inputName, inputPassword, inputDate, temp);

    	} catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("9");
    	}    	
    }
    
    protected void savePreferences(String inputName, String inputPassword, String inputDate, String isHelper){
    	
		//create or retrieve the shared preference object
		SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("scan", 1);
		
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
}