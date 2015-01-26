package pl.example.apk;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class OknoMapa extends FragmentActivity {

	LatLng twojalokalizacja;
	private GoogleMap googleMap;
	LocationManager locManager;
	boolean zmienna = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknomapa_layout);
        
        try { 
            if (googleMap == null) {
               googleMap = ((MapFragment) getFragmentManager().
               findFragmentById(R.id.map)).getMap();
            }

            final LocationListener myLocationListener = new LocationListener(){   		       

    	        @Override
    	        public void onProviderDisabled(String provider){
    	            System.out.println("disabled");
    	        }

    	        @Override
    	        public void onProviderEnabled(String provider){
    	        	System.out.println("Provider enables");
    	            
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
    	            twojalokalizacja = new LatLng(lat, lon);
    	            googleMap.moveCamera(CameraUpdateFactory.newLatLng(twojalokalizacja));
    	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    	            Marker TP = googleMap.addMarker(new MarkerOptions().position(twojalokalizacja).title("Tu jesteœ"));
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
    		
    		  
    		
    		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    		

      } catch (Exception e) {
         e.printStackTrace();
      }
        
    }
    
    
    
}
