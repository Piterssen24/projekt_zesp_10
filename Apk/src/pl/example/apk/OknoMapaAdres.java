package pl.example.apk;

import java.io.IOException;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;


public class OknoMapaAdres extends FragmentActivity{

	LatLng yourLocation, markerLocation;
	private GoogleMap googleMap;
	LocationManager locManager;
    public Location mLastLocation;
    EditText etPlace;
    Button mBtnFind, btnOk;
   Geocoder geocoder;
   double Lat, Lan;
   LatLng loc, latLng, defLocation;
   MarkerOptions markerOptions;
   Uri path;
   String token;
   public String[] faculties, coords, tagsId, tags;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknomapaadres_layout);
        getExtras(); 
        geocoder= new Geocoder(getApplicationContext());
        
        try { 
            if (googleMap == null) {
               googleMap = ((MapFragment) getFragmentManager().
               findFragmentById(R.id.map)).getMap();               
            }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            
        etPlace = (EditText) findViewById(R.id.et_place);
        mBtnFind = (Button) findViewById(R.id.btn_show);
        btnOk = (Button) findViewById(R.id.btn_ok);
        
        setMap();
        
        // Setting click event listener for the find button
        mBtnFind.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // Getting the place entered
                String location = etPlace.getText().toString();
 
                if(location==null || location.equals("")){
                    Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
                    return;
                }
                else  if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
 
                
            }
        });  	
        } catch (Exception e) {
         e.printStackTrace();
      }
        
        btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				putExtra();
				
			}
		});
       
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
        }        
    }
	
	public void putExtra()
	{
		Intent intent = new Intent(getApplicationContext(), OknoNew.class);
		intent.putExtra("location",defLocation);
		intent.putExtra("imgurl", path);
    	intent.putExtra("token", token);
    	intent.putExtra("tagsId",tagsId);
    	intent.putExtra("tags", tags);
    	intent.putExtra("faculties", faculties);
    	intent.putExtra("coords",coords);
    	startActivity(intent);
	}
	
	public void setMap()
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
 			Location fastLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
 			Location fastLocation2 = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
 			if(fastLocation!=null)
 			{
 				googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fastLocation.getLatitude(),fastLocation.getLongitude())));
 	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
 	            
 			}
 			if(fastLocation2!=null)
 			{
 				googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fastLocation2.getLatitude(),fastLocation2.getLongitude())));
 	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
 	            
 			}
 		}
 		else 
 			{
 			Location fastLocation = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
 			Location fastLocation2 = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
 			locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000, 10, myLocationListener);
 			if(fastLocation!=null)
 			{
 				googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fastLocation.getLatitude(),fastLocation.getLongitude())));
 	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
 	            
 			}
 			if(fastLocation2!=null)
 			{
 				googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(fastLocation2.getLatitude(),fastLocation2.getLongitude())));
 	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
 	            
 			}
 			}
	}
	
	private void hideKeyboard() {   
	    // Check if no view has focus:
	    View view = this.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
	private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
		 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                {
                etPlace.setText("");
                hideKeyboard();
                LatLng northeast = new LatLng(latLng.latitude-0.005,latLng.longitude-0.005);
        		LatLng southwest = new LatLng(latLng.latitude+0.005,latLng.longitude+0.005);
        		LatLngBounds bounds = new LatLngBounds(northeast,southwest);
        		googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                defLocation = latLng;
                }
            }
        }
    }
}


    

