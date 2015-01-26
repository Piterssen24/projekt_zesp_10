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
import android.widget.LinearLayout.LayoutParams;

public class OknoPost extends Activity {

	TextView tresc,autor;
	ImageView zdjecie;
	CharSequence aut;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknopost_layout);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews");
        
        zdjecie = (ImageView) findViewById(R.id.picture);
        zdjecie.setImageResource(R.drawable.gaz3);
        
        tresc = (TextView) findViewById(R.id.post);
        tresc.setText("Tresc posta");
        
        autor = (TextView) findViewById(R.id.autor);
        aut= autor.getText();
        aut = aut + " Lamia";
        autor.setText(aut);
        autor.setTextColor(Color.parseColor("#CC009900"));
        autor.setOnClickListener(new View.OnClickListener() {
  			
  			@Override
          	public void onClick(View v) {
              	Intent intent = new Intent(getApplicationContext(), oknoAutora.class);
              	startActivity(intent);
              }
  			
  		});
       
    }
}