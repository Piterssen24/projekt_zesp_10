package pl.example.apk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;


public class OknoGlowne extends Activity {

	Context context;
	Button buttonWithoutLogin;
	Button buttonLogin;
	Button buttonRegistration;
	ActionBar actionBar;
	public String login, password;
	public String serwer = "";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OknoGlowne.this); //Get the preferences
   		String name = prefs.getString("name", "defaultName"); //get a String
   		String pass = prefs.getString("passwd", "defPasswd");
   		boolean rememberCredentials = prefs.getBoolean("rememberCredentials", true);
        
        if((name!=null) && (pass!=null) && (name!="") && (pass!=""))
        {
        	serwer = getResources().getString(R.string.server);
        	String sampleURL = serwer + "/login";
        	WebServiceTask wst = new WebServiceTask(WebServiceTask.LOG_TASK, this, "Logging...", login, password);   
            wst.execute(new String[] { sampleURL });          	
        }
        else
        {
        setContentView(R.layout.oknoglowne_layout);
        context = getApplicationContext();
        
        actionBar = getActionBar();
        actionBar.hide();
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoLog.class);
            	startActivity(intent);
            }			
		});
        
        buttonRegistration.setOnClickListener(new View.OnClickListener() {			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoRejestracja.class);
            	startActivity(intent);
            }			
		});        
       
        } 
    }   
    
}
