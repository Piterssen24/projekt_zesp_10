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
	Button przyciskbezlogowania;
	Button przycisklogowania;
	Button przyciskrejestracji;
	ActionBar ab;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknoglowne_layout);
        context = getApplicationContext();
        ab = getActionBar();
        ab.hide();
        przyciskbezlogowania = (Button) findViewById(R.id.buttonbezlogowania); 
        przycisklogowania = (Button) findViewById(R.id.buttonlogowanie);
        przyciskrejestracji = (Button) findViewById(R.id.buttonrejestracja);

        
        
        przyciskbezlogowania.setOnClickListener(new View.OnClickListener() {
			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoNews.class);
            	startActivity(intent);
            }
			
		});
        
        przycisklogowania.setOnClickListener(new View.OnClickListener() {
			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoLog.class);
            	startActivity(intent);
            }
			
		});
        
        przyciskrejestracji.setOnClickListener(new View.OnClickListener() {
			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoRejestracja.class);
            	startActivity(intent);
            }
			
		});
        
       
    }
    
    
    
}
