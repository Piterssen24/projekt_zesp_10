package pl.example.picnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OknoGlowne extends Activity {

	Context context;
	Button rejestracjabutton;
	Button logowaniebutton;
	Button bezlogowaniabutton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okno_glowne);
        context = getApplicationContext();
        rejestracjabutton = (Button) findViewById(R.id.buttonrejestracja);
        logowaniebutton = (Button) findViewById(R.id.buttonlogowanie);
        bezlogowaniabutton = (Button) findViewById(R.id.buttonbezlogowania);
        
        rejestracjabutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoRejestracji.class);
            	startActivity(intent);
            }
			
		});
        
        logowaniebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
        	public void onClick(View v2) {
            	Intent intent2 = new Intent(context, OknoLogowania.class);
            	startActivity(intent2);
            }
			
		});
        
        bezlogowaniabutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
        	public void onClick(View v3) {
            	Intent intent3 = new Intent(context, OknoNews.class);
            	startActivity(intent3);
            }
			
		});
    }
    
    
    
}
