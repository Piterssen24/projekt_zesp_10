package pl.example.apk;


import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class oknoAutora extends Activity {

	TextView liczbaPostow,liczbaPostowUsunietych,ocenaUzytkownika;
	ImageView yourpicture;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknoautora_layout);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - Lamia");
        
        liczbaPostow = (TextView) findViewById(R.id.textViewlicznikpostow);
        liczbaPostowUsunietych = (TextView) findViewById(R.id.textViewlicznikpostowusunietych);
        ocenaUzytkownika = (TextView) findViewById(R.id.textViewlicznikoceny);
        
        liczbaPostow.setText("10");
        liczbaPostowUsunietych.setText("0");
        ocenaUzytkownika.setText("5,0");
        
       
    }
}