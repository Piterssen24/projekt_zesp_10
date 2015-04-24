package pl.example.apk;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.view.View.OnClickListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;


public class OknoPost extends Activity {

	TextView content, author, postDate, postLoc, report;
	ImageView photoView;
	CharSequence authorTemp, date, location, loc;
	String postText, photo;
	Bitmap picture, bmp;
	String userLogin, place, eventTime;
	public static String[] faculties, coords, folUserName;
	Context context;
	public static int[] repPostId, repUserId;
    public StringBuilder strAddress;
    public static String token;
    public String myLogin, serwer = "";
	public int postId, screenTest, pictureTest=0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknopost_layout);
        context = getApplicationContext();
        serwer = getResources().getString(R.string.server);
        report = (TextView) findViewById(R.id.rateReport);
        
       getExtras();
       getAdres(); 
        
        
        picture = Global.img;
        scalePicture();
        
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews");
        
        report.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				report();
			}
			});
        
        photoView = (ImageView) findViewById(R.id.picture);
        photoView.setImageBitmap(picture);
        System.out.println("Rozmiar zdjêcia out"+picture.getWidth()+" "+picture.getHeight());
        photoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				pictureFulscreen();				
			}
		});
        
        postDate = (TextView) findViewById(R.id.postDate);
        date = postDate.getText();
        date = date + " " + eventTime;
        postDate.setText(date);
        
        postLoc = (TextView) findViewById(R.id.postLoc);
        location = postLoc.getText();
        location = location + " " + loc;
        postLoc.setText(location);
        
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
  				
  				if(userLogin.equals(myLogin))
  				{
  		    		Toast.makeText(getApplicationContext(), "Ten post zosta³ dodany przez Ciebie!", Toast.LENGTH_LONG).show();
  				}
  				else
  				{
              	Intent intent = new Intent(getApplicationContext(), oknoAutora.class);
              	intent.putExtra("userLogin", userLogin); 
              	intent.putExtra("faculties", faculties);
    	    	intent.putExtra("coords", coords);
    	    	intent.putExtra("token", token);
    	    	intent.putExtra("repPostId", repPostId);
    	    	intent.putExtra("repUserId", repUserId);
    	    	intent.putExtra("folUserName", folUserName);
    	    	intent.putExtra("myLogin", myLogin);
    	    	intent.putExtra("screenTest",screenTest);
              	startActivity(intent);
  				}
            }			
  		});   
    }

    public void scalePicture()
    {
    	int w = picture.getWidth();
        int h = picture.getHeight();
        if(h>w)
        {
     	   pictureTest=1;
        }
        if(screenTest==0)
        {
        	System.out.println("jestem tu");
        	if(pictureTest==0)
    		{
        		picture = Bitmap.createScaledBitmap(picture, picture.getWidth()-480, picture.getHeight()-350, true);
        		System.out.println("Rozmiar zdjêcia "+picture.getWidth()+" "+picture.getHeight());
    		}
    		if(pictureTest==1)
    		{
    			picture = Bitmap.createScaledBitmap(picture, picture.getWidth()-330, picture.getHeight()-530, true);
    			System.out.println("Rozmiar zdjêcia "+picture.getWidth()+" "+picture.getHeight());
    		}
        	
        }
    }
    
    public void report()
    {
    	List<Integer> list1 = new ArrayList<Integer>();
		 for(int i=0; i<repPostId.length; i++){
			 list1.add(repPostId[i]);
		 }
		if(list1.contains(postId)){
			Toast.makeText(OknoPost.this, "Ten post zosta³ ju¿ przez Ciebie zg³oszony!", Toast.LENGTH_LONG).show();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(OknoPost.this);
		    builder.setTitle("Ostrze¿enie");
		    builder.setMessage("Czy na pewno chcesz zg³osiæ posta?");
		    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	int[] temp = new int[repPostId.length + 1];
		            for (int i = 0; i < repPostId.length; i++){
		                temp[i] = repPostId[i];
		            }
		            temp[repPostId.length] = postId;
		            repPostId = temp;
		        	String sampleURL = serwer + "/report";
		       		WebServiceTask wst = new WebServiceTask(WebServiceTask.POSTREPORT_TASK, OknoPost.this, "Trwa zg³aszanie posta, proszê czekaæ...", postId, token);   
		       		wst.execute(new String[] { sampleURL }); 
		            dialog.dismiss();
		        }
		    });
		    
		    builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            // Do nothing
		            dialog.dismiss();
		        }
		    });
		    AlertDialog alert = builder.create();
		    alert.show();
		}
    }
    
    public void getExtras()
    {
    	 Bundle b = getIntent().getExtras();
         if(b!=null){
         	postId = b.getInt("postId");
         	postText = b.getString("postText");
         	//photo = b.getString("photo");
         	userLogin = b.getString("userLogin");
         	place = b.getString("place");
         	eventTime = b.getString("eventTime");
         	faculties = b.getStringArray("faculties");
             coords = b.getStringArray("coords");
             token = b.getString("token");
             repPostId = b.getIntArray("repPostId");
             repUserId = b.getIntArray("repUserId");
             folUserName = b.getStringArray("folUserName");
             myLogin = b.getString("myLogin");
             screenTest = b.getInt("screenTest");
         }
    }
    
    public void getAdres()
    {
    	for(int j=0; j<faculties.length; j++) {
			if(place.equals(coords[j])) {
				loc = faculties[j];
				break;
			} else {
				Geocoder geocoder= new Geocoder(context, Locale.ENGLISH);
		        try {
		              //Place your latitude and longitude
		        	String[] tokens = place.split(",");
		        	double lat = Double.parseDouble(tokens[0]);
		        	double lon = Double.parseDouble(tokens[1]);
		              List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
		              if(addresses != null) {
		                  Address fetchedAddress = addresses.get(0);
		                  strAddress = new StringBuilder();
		                  for(int k=0; k<fetchedAddress.getMaxAddressLineIndex(); k++) {
		                        strAddress.append(fetchedAddress.getAddressLine(k)).append("\n");
		                  }
		                  loc = strAddress.toString();
		              } 
		        } catch (IOException e) {
		              // TODO Auto-generated catch block
		              e.printStackTrace();
		        }
			}
		}
    }
    
    public void pictureZoom(ImageView imageView)
    {
    	int w = picture.getWidth();
	    int h = picture.getHeight();
	    if(h>w)
        {
     	   pictureTest=1;
        }
	    if(screenTest==1)
		    {
		    	if(pictureTest==0)
		    	{
		    		bmp = Bitmap.createScaledBitmap(picture, picture.getWidth()*2-300, picture.getHeight()*2-130, true);
    			    imageView.setImageBitmap(bmp);
		    	}
		    	if(pictureTest==1)
		    	{
		    		bmp = Bitmap.createScaledBitmap(picture, picture.getWidth()*2+120, picture.getHeight()*2+200, true);
    			    imageView.setImageBitmap(bmp);
		    	}
		    
		    }
		    else
		    {
		    	if(pictureTest==0)
		    	{
		    		bmp = Bitmap.createScaledBitmap(picture, picture.getWidth()+70, picture.getHeight()+70, true);
    			    imageView.setImageBitmap(bmp);
		    	}
		    	if(pictureTest==1)
		    	{
		    		bmp = Bitmap.createScaledBitmap(picture, picture.getWidth()*2-100, picture.getHeight()*2, true);
    			    imageView.setImageBitmap(bmp);
		    	}
		    }
        
        /*else
        {
        	Bitmap b = Bitmap.createScaledBitmap(picture, w*2-140, h*2-100, true);
        	imageView.setImageBitmap(b);
        }*/
    }
    
    public void pictureFulscreen()
    {
    	Dialog builder = new Dialog(OknoPost.this);
		    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    builder.getWindow().setBackgroundDrawable(
		        new ColorDrawable(android.graphics.Color.TRANSPARENT));
		    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
		        @Override
		        public void onDismiss(DialogInterface dialogInterface) {
		            //nothing;
		        }
		    });
		    
		    ImageView imageView = new ImageView(OknoPost.this);
		    pictureZoom(imageView);
	    
	    
	    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT, 
	            ViewGroup.LayoutParams.MATCH_PARENT));
	    builder.show();
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