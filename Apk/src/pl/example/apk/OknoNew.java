package pl.example.apk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.view.Display;
import android.view.Gravity;
import android.widget.LinearLayout;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.android.gms.maps.model.LatLng;

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
import android.graphics.Point;
import android.widget.ImageButton;
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
	Button buttonConfirm, buttonChangeLocation, buttonChangeLocation2;
	ImageButton buttonDate;
	TextView counter, gps, dateView;
	EditText content2;
	ActionBar ab;
	final String s="";
	public ImageView postPhoto;
	Uri path;
	LocationManager locManager;
	Spinner categories, locations;
	public SimpleDateFormat date;
	public String content, photo, addTime, token, photoB;
	public StringBuilder strAddress;
	public Bitmap bitmap, bitmapx;
	public String place= "";
	public String eventTime, loc="";
	public String serwer = "";
	public String tag = "";
	public String[] tags, faculties, coords, tagsId;
	Bitmap bitmapRotated;
	boolean test = false;
	boolean testLocation = false;
	LatLng locat = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknonew_layout);
        serwer = getResources().getString(R.string.server);
        context = getApplicationContext();  

        ab = getActionBar();
        ab.setTitle("PicNews - Nowy Post");
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        ab.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900"))); 
        
        getExtras();
        catchPhoto();
        
        postPhoto = (ImageView) findViewById(R.id.imageViewPostPhoto);   
        postPhoto.setImageBitmap(bitmap);
        
    	locations = (Spinner) findViewById(R.id.listOfLocations);
    	locations.setVisibility(View.GONE);
    	
    	dateView = (TextView) findViewById(R.id.editTextDate);
    	setCurrentDate();
        
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
				getLocationFromList();
			}
		});
    	
    	buttonChangeLocation2 = (Button) findViewById(R.id.locationChange3);
    	buttonChangeLocation2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLocationFromMap();
			}
		});
    	
    	buttonDate = (ImageButton) findViewById(R.id.buttonDate);
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
    	postCounter();
    	
    		//GPS location
    		gps = (TextView) findViewById(R.id.textViewGps);
    		gps.setText("Lokalizacja posta:\n"+"...wczytywanie lokalizacji...");
    		getLocation();
    		if(locat!=null)
    		{
    	        newAdress();
    	    }
    	
    }   
    
    public void getLocationFromMap()
    {
    	Intent intent = new Intent(getApplicationContext(), OknoMapaAdres.class);
    	intent.putExtra("imgurl", path);
    	intent.putExtra("token", token);
    	intent.putExtra("tagsId",tagsId);
    	intent.putExtra("tags", tags);
    	intent.putExtra("faculties", faculties);
    	intent.putExtra("coords",coords);
    	startActivity(intent);
    }

    public int getScreenType()
    {
    	int screenTest=0;
    	Display display = getWindowManager().getDefaultDisplay();
   		Point size = new Point();
   		display.getSize(size);
   		int width = size.x;
   		int height = size.y;
   		if( (width>1100) && (height>1500) )
   		{
   			screenTest=1;
   		}
   		return screenTest;
    }
    
    public void getLocation()
    {
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
	            //tu co siê dzieje gdy lokalizacja jest w zasiêgu wydzia³u
	            for(int i=0; i<coords.length; i++){
	            	String[] tokens = coords[i].split(",");
		        	double lat2 = Double.parseDouble(tokens[0]);
		        	double lon2 = Double.parseDouble(tokens[1]);
		        	if((lat - lat2 < 0.0005)
       						&& (lat - lat2 > -0.0005)
       					&&(lon - lon2 < 0.0005)
       					&& (lon - lon2 < 0.0005))
		        	{
		        		if(!testLocation)
		        		{
		        		place = coords[i];
		        		test=true;
		        		}
		        	}
		        		
	            }
	            if((!test) && (!testLocation))
	            {
    	            place = lat + "," + lon;
    	        }
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
	                      if(!testLocation)
	                      {
	                      gps.setText("Lokalizacja:\n" +strAddress.toString());
	                      }
	                  } else
	                  {
	                	  if(!testLocation)
	                	  {
	                      gps.setText("Lokalizacja:\n" + "No location found..!"); 
	                	  }
	                  }
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
			locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 10, myLocationListener);
 			getLastKnownLocation();
 		}
 		else 
 			{ 			
 			locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000, 10, myLocationListener);
 			getLastKnownLocation();
 			}
    }

    public void getLastKnownLocation()
    {
  	  
  		Location fastLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
  		Location fastLocation2 = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  		if(fastLocation!=null)
  		{
  			Geocoder geocoder = new Geocoder(getBaseContext());
  			List<Address> addresses;
  			try {
  				addresses = geocoder.getFromLocation(fastLocation.getLatitude(),fastLocation.getLongitude(), 1);
  				if(addresses != null) {
                    Address fetchedAddress = addresses.get(0);
                    strAddress = new StringBuilder();
                    for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                          strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                    }
  				}
  				if(!testLocation)
                {
                gps.setText(strAddress.toString());
                
                for(int i=0; i<coords.length; i++){
	            	String[] tokens = coords[i].split(",");
		        	double lat2 = Double.parseDouble(tokens[0]);
		        	double lon2 = Double.parseDouble(tokens[1]);
		        	if((fastLocation.getLatitude() - lat2 < 0.0005)
       						&& (fastLocation.getLatitude() - lat2 > -0.0005)
       					&&(fastLocation.getLongitude() - lon2 < 0.0005)
       					&& (fastLocation.getLongitude() - lon2 < 0.0005))
		        	{
		        		if(!testLocation)
		        		{
		        		place = coords[i];
		        		test=true;
		        		}
		        	}
		        		
	            }
	            if((!test) && (!testLocation))
	            {
	            	place = fastLocation.getLatitude()+","+fastLocation.getLatitude();
    	        }
                }
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
            
            
             
  		}
  		if(fastLocation2!=null)
  		{
  			Geocoder geocoder = new Geocoder(getBaseContext());
  			List<Address> addresses;
  			try {
  				addresses = geocoder.getFromLocation(fastLocation2.getLatitude(),fastLocation2.getLongitude(), 1);
  				if(addresses != null) {
                    Address fetchedAddress = addresses.get(0);
                    strAddress = new StringBuilder();
                    for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                          strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                    }
  				}
  				if(!testLocation)
                {
  					gps.setText(strAddress.toString());
  					
  					for(int i=0; i<coords.length; i++){
  		            	String[] tokens = coords[i].split(",");
  			        	double lat2 = Double.parseDouble(tokens[0]);
  			        	double lon2 = Double.parseDouble(tokens[1]);
  			        	if((fastLocation2.getLatitude() - lat2 < 0.0005)
  	       						&& (fastLocation2.getLatitude() - lat2 > -0.0005)
  	       					&&(fastLocation2.getLongitude() - lon2 < 0.0005)
  	       					&& (fastLocation2.getLongitude() - lon2 < 0.0005))
  			        	{
  			        		if(!testLocation)
  			        		{
  			        		place = coords[i];
  			        		test=true;
  			        		}
  			        	}
  			        		
  		            }
  		            if((!test) && (!testLocation))
  		            {
  		            	place = fastLocation2.getLatitude()+","+fastLocation2.getLatitude();
  	    	        }
                }
  			} catch (IOException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
             
  		}
    }

    public void newAdress()
    {
        	Geocoder geocoder = new Geocoder(getBaseContext());
 				List<Address> addresses;
 			try {
 				addresses = geocoder.getFromLocation(locat.latitude,locat.longitude, 1);
 				if(addresses != null) {
                    Address fetchedAddress = addresses.get(0);
                    strAddress = new StringBuilder();
                    for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                          strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                    }
 				}
 				 gps.setText(strAddress.toString());
 				 place = locat.latitude+","+locat.longitude;
                    testLocation=true;
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
        
    }
    
    public void getExtras()
    {
    	Bundle extras= getIntent().getExtras();
        if(extras!=null)
        {
           path = (Uri) extras.get("imgurl");
           token = extras.getString("token");
           tagsId = extras.getStringArray("tagsId");
           tags = extras.getStringArray("tags");
           faculties = extras.getStringArray("faculties");
           coords = extras.getStringArray("coords"); 
           locat = (LatLng) extras.get("location");
        }        
    }
    
    public void catchPhoto()
    {
    	String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(path, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imagepath = cursor.getString(columnIndex); 
        @SuppressWarnings("unused")
		File f = new File(imagepath);   
        cursor.close();
        
        if((bitmapx!=null) && (!bitmapx.isRecycled())){
        	bitmapx.recycle();
        	bitmapx = null;
        }
       	bitmapx = BitmapFactory.decodeFile(imagepath);
       	
        scalePicture();
    }
    
    private void scalePicture()
    {
    	if((bitmap!=null) && (!bitmap.isRecycled())) {
        	bitmap.recycle();
       		bitmap  = null;
        }
        
        if((bitmapRotated!=null) && (!bitmapRotated.isRecycled())) {
        	bitmapRotated.recycle();
       		bitmapRotated = null;
        }
        
        int or = getOrientation(context, path);     
        if (or==90)
        {
        	bitmapRotated = Bitmap.createScaledBitmap(bitmapx,853 , 600, true);
        	if(getScreenType()==0)
        	{
        		bitmap = Bitmap.createScaledBitmap(bitmapx, 240, 200, true);
        	}
        	if(getScreenType()==1)
        	{
        		bitmap = Bitmap.createScaledBitmap(bitmapx, 853, 600, true);
        	}
        	bitmap = RotateBitmap(bitmap, 90);
        	bitmapRotated = RotateBitmap(bitmapRotated, 90);
        	photoB = encodeTobase64(bitmapRotated);
        	if((bitmap!=null) && (!bitmap.isRecycled()))
        	{
         	bitmapRotated.recycle();
         	bitmapRotated = null;
        	}
        	
        } else {
        	bitmapRotated = Bitmap.createScaledBitmap(bitmapx,853 , 600, true);
        	if(getScreenType()==0)
        	{
        		bitmap = Bitmap.createScaledBitmap(bitmapx,300, 200, true);
        	}
        	if(getScreenType()==1)
        	{
        		bitmap = Bitmap.createScaledBitmap(bitmapx, 853, 600, true);
        	}
        	photoB = encodeTobase64(bitmapRotated);
        }
    }

    
    
    public void setCurrentDate()
    {
    	final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateView.setText(String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(day));
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        addTime = date.format(new Date());
    }
    
    public void postCounter()
    {
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
    }
    
    public void getLocationFromList()
    {
    	buttonChangeLocation.setVisibility(View.GONE);				
		gps.setText("Wybierz lokalizacjê: ");
		testLocation = true;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.TOP;
		locations.setLayoutParams(params);
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
    /**
     * metoda pobieraj¹ca dane tworzonego posta i wywo³uj¹ca asynchroniczn¹ metodê w celu przes³ania danych do web serwisu
     */
    public void addNewPost(View vw) { 
    	content = content2.getText().toString();
    	if(content.length()==0)
    	{
    		Toast.makeText(this, "Dodaj treœæ posta!", Toast.LENGTH_LONG).show();
    	}
    	else
    	{
    	photo = photoB;      	
    	eventTime = dateView.getText().toString();
    	String sampleURL = serwer + "/post";
    	System.out.println("place "+place);
        WebServiceTask wst = new WebServiceTask(WebServiceTask.NEW_TASK, this, "Dodawanie posta...", content, photo, addTime, place, eventTime, tag, token);   
        wst.execute(new String[] { sampleURL }); 
    	}
	}
    
    /**
     * metoda konwertuj¹ca bitmapê (zdjêcie) na string
     */
    public static String encodeTobase64(Bitmap image)
    {
    	Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        if((immagex!=null) && (!immagex.isRecycled()))
        {
        immagex.recycle();
        immagex = null;
        }
        byte[] b = null;
        b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        b = null;
        return imageEncoded;
    }
    
    /**
     * metoda przyjmuj¹ca jako parametr odpowiedŸ od serwera i wykonuj¹ca przejœcie do innego okna
     */
    public void handleResponse(String response) {   
        if(response.equals("TRUE")){
        	Toast.makeText(this, "Pomyslnie dodano posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(this, OknoNews.class);
        	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	startActivity(i);        	
        } else {
        	Toast.makeText(this, "B³¹d! Nie uda³o siê dodaæ posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(this, OknoNews.class);
        	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	startActivity(i);
        }
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        startActivity(new Intent(OknoNew.this, OknoNews.class));
        finish();

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


