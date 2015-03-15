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

	TextView postsNumber,postsNumberDeleted,userRate;
	ImageView yourPicture;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknoautora_layout);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - Lamia");
        
        postsNumber = (TextView) findViewById(R.id.textViewPostCounter);
        postsNumberDeleted = (TextView) findViewById(R.id.textViewPostsCounterDeleted);
        userRate = (TextView) findViewById(R.id.textViewCounterRates);
        
        postsNumber.setText("10");
        postsNumberDeleted.setText("0");
        userRate.setText("5,0");
        
       
    }
}