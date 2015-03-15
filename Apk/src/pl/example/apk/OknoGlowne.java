package pl.example.apk;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class OknoGlowne extends Activity {

	Context context;
	Button buttonWithoutLogin;
	Button buttonLogin;
	Button buttonRegistration;
	ActionBar actionBar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknoglowne_layout);
        context = getApplicationContext();
        
        actionBar = getActionBar();
        actionBar.hide();
        buttonWithoutLogin = (Button) findViewById(R.id.buttonWithoutLogin); 
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);

        Bundle b = getIntent().getExtras();  
        buttonWithoutLogin.setOnClickListener(new View.OnClickListener() {			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoNews.class);
            	startActivity(intent);
            }		
		});
        
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
