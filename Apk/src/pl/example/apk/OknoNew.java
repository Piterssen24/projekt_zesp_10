package pl.example.apk;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class OknoNew extends Activity {

	Context context;
	Button przyciskzatwierdzenia;
	TextView licznik,gps;
	EditText tresc;
	ActionBar ab;
	ImageView zdjecieposta;
	int znaczek = 1;
	Uri path;
	LocationManager locManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknonew_layout);
        context = getApplicationContext();
        
        ab = getActionBar();
        ab.setTitle("PicNews - Nowy News");
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        ab.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        
        
        Bundle extras= getIntent().getExtras();
        if(extras!=null)
        {
           path = (Uri) extras.get("imgurl");
           
        }
        
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(path, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagepath = cursor.getString(columnIndex); 
        File f = new File(imagepath);   
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
        
        /*String image_path = getIntent().getStringExtra("path");
        Bitmap bitmap = BitmapFactory.decodeFile(image_path);*/
    
        zdjecieposta = (ImageView) findViewById(R.id.imageViewzdjecieposta);   
        zdjecieposta.setImageBitmap(bitmap);
       // zdjecieposta.setImageURI(path);
        LayoutParams params = (LayoutParams) this.zdjecieposta.getLayoutParams();
    	params.width = 200;
    	params.height = 260;
    	   
    	
    	   
    	   
    	   
    	przyciskzatwierdzenia = (Button) findViewById(R.id.buttondodaj);
    	
    	// licznik znakow posta
    	
    	licznik = (TextView) findViewById(R.id.textViewlicznik);
    	tresc = (EditText) findViewById(R.id.editTexttrescposta);
    	licznik.setText("140");
    	
    	final TextWatcher txwatcher = new TextWatcher() {
    		   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    		   }

    		   public void onTextChanged(CharSequence s, int start, int before, int count) {

    		      licznik.setText(String.valueOf(140-s.length()));
    		      
    		   }

    		   public void afterTextChanged(Editable s) {
    			  int liczba = Integer.parseInt(licznik.getText().toString());
     		      if (liczba<11)
     		      {
     		    	  licznik.setTextColor(Color.RED);
     		      }
     		      else licznik.setTextColor(Color.BLACK);
    		   }
    		};
    		
    		tresc.addTextChangedListener(txwatcher);
    		
    		//GPS location
    		gps = (TextView) findViewById(R.id.textViewgps);
    		gps.setText("Lokalizacja newsa:\n"+"...wczytywanie lokalizacji...");
    		
    		final LocationListener myLocationListener = new LocationListener(){   		       

    	        @Override
    	        public void onProviderDisabled(String provider){
    	            
    	        }

    	        @Override
    	        public void onProviderEnabled(String provider){
    	            
    	        }


    			@Override
    			public void onLocationChanged(Location loc) {
    				// TODO Auto-generated method stub
    				
    				Log.d("tag", "Finding Latitude");
    	            double lat = loc.getLatitude();
    	            Log.d("tag", "Lat: "+String.valueOf(lat));
    	            Log.d("tag", "Finding Longitude");
    	            double lon = loc.getLongitude();
    	            Log.d("tag", "Lon: "+String.valueOf(lon));
    	            
    	            
    	            Geocoder geocoder= new Geocoder(context, Locale.ENGLISH);
    	            
    	            try {
    	                   
    	                  //Place your latitude and longitude
    	                  List<Address> addresses = geocoder.getFromLocation(lat,lon, 1);
    	                  
    	                  if(addresses != null) {
    	                   
    	                      Address fetchedAddress = addresses.get(0);
    	                      StringBuilder strAddress = new StringBuilder();
    	                    
    	                      for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
    	                            strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
    	                      }
    	                    
    	                      gps.setText("Lokalizacja newsa:\n" +strAddress.toString());
    	                   
    	                  }
    	                   
    	                  else
    	                      gps.setText("Lokalizacja newsa:\n" + "No location found..!");
    	              
    	            } 
    	            catch (IOException e) {
    	                     // TODO Auto-generated catch block
    	                     e.printStackTrace();
    	                     Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
    	            }
    	            
    	           
    	            locManager.removeUpdates(this);
    				
    			}

    			@Override
    			public void onStatusChanged(String provider, int status, Bundle extras) {
    				// TODO Auto-generated method stub
    				
    			}
    			
    		};
    		
    		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    		boolean isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    		if(isGPSEnabled)
    		{
    			locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
    		}
    		else locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
           
            
    		
    		
    	
    }
        
}


