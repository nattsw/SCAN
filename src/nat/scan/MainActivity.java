package nat.scan;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

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
    	Intent intent = new Intent(this, SubActivity.class);
        startActivity(intent);
    }
    
    public void login(View view) {
    	try {        
	    	Requests svc = new Requests();
	    	EditText userTextField = (EditText)findViewById(R.id.userTextField);
	        svc.execute("/GetPassword/?uid=" + userTextField.getText().toString());
	        String data = svc.get();
	        EditText passwordTextField = (EditText)findViewById(R.id.passwordTextField);
	        if (data.compareTo(passwordTextField.getText().toString()) == 0) {
		        Intent intent = new Intent(this, SubActivity.class);
		        startActivity(intent);
	        }
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
