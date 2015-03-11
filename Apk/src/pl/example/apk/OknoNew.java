package pl.example.apk;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.Toast;

public class OknoNew extends FragmentActivity {

	Context context;
	Button buttonConfirm, buttonChangeLocation, buttonDate;
	TextView counter,gps,date;
	EditText content;
	ActionBar ab;
	ImageView postPhoto;
	Uri path;
	LocationManager locManager;
	Spinner categories, locations;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknonew_layout);
        context = getApplicationContext();
        
        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        
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
        String imagePath = cursor.getString(columnIndex); 
        File f = new File(imagePath);   
        cursor.close();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
    
        postPhoto = (ImageView) findViewById(R.id.imageViewPostPhoto);   
        postPhoto.setImageBitmap(bitmap);
        LayoutParams params = (LayoutParams) this.postPhoto.getLayoutParams();
    	params.width = 200;
    	params.height = 260;
    	
    	locations = (Spinner) findViewById(R.id.listOfLocations);
    	locations.setVisibility(View.GONE);
    	
    	categories = (Spinner) findViewById(R.id.listOfCategories);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.listCategories, R.layout.customspinner);
    	adapter.setDropDownViewResource(R.layout.customspinner);
    	categories.setAdapter(adapter);
    	categories.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String tag = parent.getItemAtPosition(position).toString();
				categories.setPrompt(tag);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}

    	});
    	 
    	buttonChangeLocation = (Button) findViewById(R.id.locationChange);
    	buttonChangeLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonChangeLocation.setVisibility(View.GONE);				
				gps.setVisibility(View.GONE);
				locations.setVisibility(View.VISIBLE);
				ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.listLocations, R.layout.customspinner);
		    	adapter2.setDropDownViewResource(R.layout.customspinner);
		    	locations.setAdapter(adapter2);
		    	locations.setOnItemSelectedListener(new OnItemSelectedListener() {
		    	    
					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						String loc = parent.getItemAtPosition(position).toString();
						categories.setPrompt(loc);
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						
					}

		    	});
				
			}
		});
    	
    	date = (TextView) findViewById(R.id.editTextDate);
    	final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date.setText(String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(day));
    	
    	buttonDate = (Button) findViewById(R.id.buttonDate);
    	buttonDate.setTypeface(font);
    	buttonDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
		        
		        showDatePicker();
			}
				
		});
    	
    	
    	buttonConfirm = (Button) findViewById(R.id.buttonAdd);
    	
    	// counter znakow posta
    	
    	counter = (TextView) findViewById(R.id.textViewCounter);
    	content = (EditText) findViewById(R.id.editTextPostContent);
    	counter.setText("140");
    	
    	final TextWatcher txWatcher = new TextWatcher() {
    		   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    		   }

    		   public void onTextChanged(CharSequence s, int start, int before, int count) {

    		      counter.setText(String.valueOf(140-s.length()));
    		      
    		   }

    		   public void afterTextChanged(Editable s) {
    			  int number = Integer.parseInt(counter.getText().toString());
     		      if (number<11)
     		      {
     		    	  counter.setTextColor(Color.RED);
     		      }
     		      else counter.setTextColor(Color.BLACK);
    		   }
    		};
    		
    		content.addTextChangedListener(txWatcher);
    		
    		//GPS location
    		gps = (TextView) findViewById(R.id.textViewGps);
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
        

    private void showDatePicker() {
    	  DatePickerFragment date = new DatePickerFragment();
    	  /**
    	   * Set Up Current Date Into dialog
    	   */
    	  Calendar calender = Calendar.getInstance();
    	  Bundle args = new Bundle();
    	  args.putInt("year", calender.get(Calendar.YEAR));
    	  args.putInt("month", calender.get(Calendar.MONTH));
    	  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
    	  date.setArguments(args);
    	  /**
    	   * Set Call back to capture selected date
    	   */
    	  date.setCallBack(ondate);
    	  date.show(getSupportFragmentManager(), "Date Picker");
    	 }

    	 OnDateSetListener ondate = new OnDateSetListener() {
    	  @Override
    	  public void onDateSet(DatePicker view, int year, int monthOfYear,
    	    int dayOfMonth) {
    	   date.setText(
    	     String.valueOf(year) + "/" + String.valueOf(monthOfYear+1)
    	       + "/" + String.valueOf(dayOfMonth) );
    	  }
    	 };


}




