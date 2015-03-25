package pl.example.apk;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


public class OknoPost extends Activity {

	TextView content, author;
	ImageView photoView;
	CharSequence authorTemp;
	String postText, photo;
	Bitmap picture;
	String userLogin;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknopost_layout);
        
        Bundle b = getIntent().getExtras();
        if(b!=null){
        	postText = b.getString("postText");
        	photo = b.getString("photo");
        	userLogin = b.getString("userLogin");
        }
        
        picture = decodeBase64(photo);
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews");
        
        photoView = (ImageView) findViewById(R.id.picture);
        photoView.setImageBitmap(picture);
        
        content = (TextView) findViewById(R.id.post);
        content.setText(postText);
        author = (TextView) findViewById(R.id.author);
        authorTemp= author.getText();
        authorTemp = authorTemp + " " + userLogin;
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
    
    public static Bitmap decodeBase64(String input) 
    {
        byte[] decodedByte = Base64.decode(input, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        startActivity(new Intent(OknoPost.this, OknoNews.class));
        finish();

    }
}