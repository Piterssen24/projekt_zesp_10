package pl.example.picnews;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

	Button home;
	Context context;
	TextView textView;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
       android.support.v7.app.ActionBar bar = getSupportActionBar();
       bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));

       
        
      /*  home.setOnClickListener(new View.OnClickListener() {
        
        public void onClick(View v) {
			//textView.setText("Przycisk dzia³a!");
		}
        
        }); */
	
	
    }
    
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      
      // action with ID action_settings was selected
      case R.id.action_settings:
        Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
            .show();
        break;
      default:
        break;
      }

      return true;
    } 

    
}
