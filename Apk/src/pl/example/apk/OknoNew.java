package pl.example.apk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Spinner;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.ActionBar;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.util.Base64;

public class OknoNew extends FragmentActivity {

	Context context;
	Button buttonConfirm, buttonChangeLocation, buttonDate;
	TextView counter, gps, dateView;
	EditText content2;
	ActionBar ab;
	public ImageView postPhoto;
	Uri path;
	LocationManager locManager;
	Spinner categories, locations;
	public SimpleDateFormat date;
	public String content, photo, addTime, token="aaa";
	public StringBuilder strAddress;
	public Bitmap bitmap, bitmapx;
	public String place= "";
	public String eventTime, loc="";
	public String serwer = "";
	public String tag = "";
	public String[] tags, faculties, coords, tagsId;
	Bitmap bitmapRotated;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknonew_layout);
        serwer = getResources().getString(R.string.server);
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
           tagsId = extras.getStringArray("tagsId");
           tags = extras.getStringArray("tags");
           faculties = extras.getStringArray("faculties");
           coords = extras.getStringArray("coords");           
        }

        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(path, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagepath = cursor.getString(columnIndex); 
        File f = new File(imagepath);   
        cursor.close();
        
        if(bitmapx!=null){
        	bitmapx.recycle();
        	bitmapx = null;
        }
       	bitmapx = BitmapFactory.decodeFile(imagepath);
       	
        if(bitmap!=null) {
        	bitmap.recycle();
       		bitmap  = null;
        }
        bitmap = Bitmap.createScaledBitmap(bitmapx, 300, 300, true);
        int or = getOrientation(context, path);     
        if (or==90)
        {
        	bitmap = RotateBitmap(bitmap, 90);
        }
        postPhoto = (ImageView) findViewById(R.id.imageViewPostPhoto);   
        postPhoto.setImageBitmap(bitmap);

        LayoutParams params = (LayoutParams) this.postPhoto.getLayoutParams();
        if (or==90)
    	{
    		params.width = 200;
    		params.height = 260;
    	} else {
    		params.width = 260;
    		params.height = 200;
    	}	 
    	locations = (Spinner) findViewById(R.id.listOfLocations);
    	locations.setVisibility(View.GONE);
    	
    	dateView = (TextView) findViewById(R.id.editTextDate);
    	final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateView.setText(String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(day));
    	date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        addTime = date.format(new Date());
        
        categories = (Spinner) findViewById(R.id.listOfCategories);
    	//ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.listCategories, R.layout.customspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.customspinner, tags);
    	adapter.setDropDownViewResource(R.layout.customspinner);
    	categories.setAdapter(adapter);
    	categories.setOnItemSelectedListener(new OnItemSelectedListener() { 	    
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				tag = parent.getItemAtPosition(position).toString();
				categories.setPrompt(tag);
				for(int i=0; i<tags.length; i++){
					if(tags[i].equals(tag)){
						tag = tagsId[i];
					}
				}
				
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
				//ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(context, R.array.listLocations, R.layout.customspinner);
				ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, R.layout.customspinner, faculties);
		    	adapter2.setDropDownViewResource(R.layout.customspinner);
		    	locations.setAdapter(adapter2);
		    	locations.setOnItemSelectedListener(new OnItemSelectedListener() {		    	    
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						loc = parent.getItemAtPosition(position).toString();
						categories.setPrompt(loc);
						for(int i=0; i<faculties.length; i++){
							if(faculties[i].equals(loc)){
								place = coords[i];
							}
						}			
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub						
					}
		    	});			
			}
		});
    	
    	buttonDate = (Button) findViewById(R.id.buttonDate);
    	buttonDate.setTypeface(font);
    	buttonDate.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {	        
		        showDatePicker();
			}		
		});
    	
    	buttonConfirm = (Button) findViewById(R.id.buttonAdd);
    	// licznik znakow posta
    	counter = (TextView) findViewById(R.id.textViewCounter);
    	content2 = (EditText) findViewById(R.id.editTextPostContent);
    	counter.setText("140");
    	
    	final TextWatcher txwatcher = new TextWatcher() {
    		   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    		   
    		   }

    		   public void onTextChanged(CharSequence s, int start, int before, int count) {
    		      counter.setText(String.valueOf(140-s.length()));
    		   }

    		   public void afterTextChanged(Editable s) {
    			  int liczba = Integer.parseInt(counter.getText().toString());
     		      if (liczba<11)
     		      {
     		    	  counter.setTextColor(Color.RED);
     		      }
     		      else counter.setTextColor(Color.BLACK);
    		   }
    		};
    		
    		content2.addTextChangedListener(txwatcher);
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
    	            place = lat + "," + lon; 
    	            Log.d("tag", "Lon: "+String.valueOf(lon));
    	            Geocoder geocoder= new Geocoder(context, Locale.ENGLISH);
    	            try {
    	                  //Place your latitude and longitude
    	                  List<Address> addresses = geocoder.getFromLocation(lat,lon, 1);
    	                  if(addresses != null) {
    	                      Address fetchedAddress = addresses.get(0);
    	                      strAddress = new StringBuilder();
    	                      for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
    	                            strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
    	                      }
    	                      gps.setText("Lokalizacja newsa:\n" +strAddress.toString());
    	                  } else
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
    
    public void addNewPost(View vw){ 
    	photo =  encodeTobase64(bitmap); 
    	bitmap.recycle();
    	bitmap = null;
    	content = content2.getText().toString();
    	eventTime = dateView.getText().toString();
    	String sampleURL = serwer + "/post";
        WebServiceTask wst = new WebServiceTask(WebServiceTask.NEW_TASK, this, "Dodawanie posta...", content, photo, addTime, place, eventTime, tag, token);   
        wst.execute(new String[] { sampleURL }); 
	}
    
    public static String encodeTobase64(Bitmap image)
    {
    	Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        immagex.recycle();
        immagex = null;
        byte[] b = null;
        b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        b = null;
        return imageEncoded;
    }
    
    public void handleResponse(String response) {   
        if(response.equals("TRUE")){
        	Toast.makeText(this, "Pomyslnie dodano posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(this, OknoNews.class);
        	//i.putExtra("Token",response);
        	startActivity(i);        	
        } else {
        	Toast.makeText(this, "B³¹d! Nie uda³o siê dodaæ posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(this, OknoNews.class);
        	//i.putExtra("Token",response);
        	startActivity(i);
        }
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
  		 public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
  			 dateView.setText(
  					 String.valueOf(year) + "/" + String.valueOf(monthOfYear+1) + "/" + String.valueOf(dayOfMonth) );
  		 }
  	 };
  	 
  	public static Bitmap RotateBitmap(Bitmap source, float angle)
  	{
  		Matrix matrix = new Matrix();
  	    matrix.postRotate(angle);
  	    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  	}

  	public static int getOrientation(Context context, Uri photoUri) 
  	{
  	    Cursor cursor = context.getContentResolver().query(photoUri, new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
  	    try 
  	    {
  	        if (cursor.moveToFirst()) 
  	        {
  	            return cursor.getInt(0);
  	        } 
  	        else 
  	        {
  	            return -1;
  	        }
  	    } finally {
  	        cursor.close();
  	    }
  	}
    
}


