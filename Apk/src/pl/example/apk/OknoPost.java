package pl.example.apk;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OknoPost extends Activity {

	TextView content,author;
	ImageView zdjecie;
	CharSequence authorTemp;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknopost_layout);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews");
        
        zdjecie = (ImageView) findViewById(R.id.picture);
        zdjecie.setImageResource(R.drawable.gaz3);
        
        content = (TextView) findViewById(R.id.post);
        content.setText("Treœæ posta");
        
        author = (TextView) findViewById(R.id.author);
        authorTemp= author.getText();
        authorTemp = authorTemp + " Lamia";
        author.setText(authorTemp);
        author.setTextColor(Color.parseColor("#CC009900"));
        author.setOnClickListener(new View.OnClickListener() {
  			
  			@Override
          	public void onClick(View v) {
              	Intent intent = new Intent(getApplicationContext(), oknoAutora.class);
              	startActivity(intent);
              }
  			
  		});
       
    }
}