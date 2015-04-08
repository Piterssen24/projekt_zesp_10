package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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

	LatLng yourLocation;
	private GoogleMap googleMap;
	LocationManager locManager;
	public String serwer = "";
	public String token;
	private static final String TAG = "OknoMapa";
	public String postId;
    public String userLogin;
    public String content;
    public String photo;
    public String categoryId;
    public String addTime;
    public String place;
    public String eventTime;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknomapa_layout);
        serwer = getResources().getString(R.string.server);
        Bundle b = getIntent().getExtras();
        if(b != null){
        	token = b.getString("token");
        }
        String sampleURL = serwer + "/map";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.MAP_TASK, this, "Loading posts on map...", token);   
   		wst.execute(new String[] { sampleURL }); 
        
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
    	            yourLocation = new LatLng(lat, lon);
    	            googleMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
    	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    	            Marker TP = googleMap.addMarker(new MarkerOptions().position(yourLocation).title("Tu jesteœ"));
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
	
	
	public void handleResponse(String resp) {   
   		try {
   			System.out.println(resp);
   			JSONArray jsonarray = new JSONArray(resp);
   			for(int i=0; i<jsonarray.length(); i++){
   				JSONObject jso = jsonarray.getJSONObject(i);
   				if(jso!=null){
   					postId = jso.getString("postId");
   					userLogin = jso.getString("userLogin");
   					content = jso.getString("content");
   					content = postId + content;
   					photo = jso.getString("photo");
   					categoryId = jso.getString("categoryId");
   					addTime = jso.getString("addTime");
   					place = jso.getString("place");
   					System.out.println("place: " + place);
   					eventTime = jso.getString("eventTime");
   					String[] tokens = place.split(",");
		        	double lat = Double.parseDouble(tokens[0]);
		        	double lon = Double.parseDouble(tokens[1]);
   					LatLng loc = new LatLng(lat, lon);
   					Marker TP = googleMap.addMarker(new MarkerOptions().position(loc).title(content));
   				}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
   	}
	
	
	private class WebServiceTask extends AsyncTask<String, Integer, String> {
   		public static final int MAP_TASK = 1;
   		private static final String TAG = "WebServiceTask";
   		// connection timeout, in milliseconds (waiting to connect)
   		private static final int CONN_TIMEOUT = 50000;        
   		// socket timeout, in milliseconds (waiting for data)
   		private static final int SOCKET_TIMEOUT = 50000;  
   		private int taskType, number;
   		private Context mContext = null;
   		private String token;
   		private String processMessage = "Processing...";
   		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
   		private ProgressDialog pDlg = null;
   		private String[] favCategoryId;
   		private String tag;
   		public WebServiceTask(int taskType, Context mContext, String processMessage, String token){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.token = token;
   		}
      
   		public void addNameValuePair(String name, String value) {
   			params.add(new BasicNameValuePair(name, value));
   		}

   		private void showProgressDialog() {    
   			pDlg = new ProgressDialog(mContext);
   			pDlg.setMessage(processMessage);
   			pDlg.setProgressDrawable(mContext.getWallpaper());
   			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
   			pDlg.setCancelable(false);
   			pDlg.show(); 
   		}

   		@Override
   		protected void onPreExecute() { 
   			showProgressDialog(); 
   		}

   		protected String doInBackground(String... urls) {
   			String url = urls[0];
   			String result = ""; 
   			String resp = "";
   			HttpResponse response = doResponse(url); 
   			if (response == null) {
   				return result;
   			} else { 
   				try {
   					result = inputStreamToString(response.getEntity().getContent());
   					resp = response.toString();
   				} catch (IllegalStateException e) {
   					Log.e(TAG, e.getLocalizedMessage(), e); 
   				} catch (IOException e) {
   					Log.e(TAG, e.getLocalizedMessage(), e);
   				}
   			}
   			return result;
   		}

   		@Override
   		protected void onPostExecute(String resp) {   
   			pDlg.dismiss(); 
   			handleResponse(resp);
   		}
       
   		// Establish connection and socket (data retrieval) timeouts
   		private HttpParams getHttpParams() {            
   			HttpParams htpp = new BasicHttpParams();             
   			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
   			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);           
   			return htpp;
   		}
       
   		private HttpResponse doResponse(String url) {  
   			// Use our connection and data timeouts as parameters for our
   			// DefaultHttpClient
   			HttpClient httpclient = new DefaultHttpClient(getHttpParams());
   			HttpClient httpClient = new DefaultHttpClient();
   			HttpResponse response = null;
   			try {        
   				switch (taskType) {
            	case MAP_TASK:
            		String url2 = serwer + "/map2";
   				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                JSONObject json = new JSONObject();
                try{
                	HttpPost httpPost = new HttpPost(url2);
  					json.put("token", token);
  					StringEntity se = new StringEntity(json.toString());
  					httpPost.addHeader("Content-Type","application/json");
  					httpPost.setEntity(se);
  					response = httpClient.execute(httpPost);				
  					if(response != null){
  						InputStream in = response.getEntity().getContent();
  					}
  				}catch(Exception e){
  					e.printStackTrace();
  					//createDialog("Error", "Cannot Estabilish Connection");
  				}
                  	HttpGet httpget = new HttpGet(url);
                  	response = httpclient.execute(httpget);
                  	break;
                      	
   				}
   			} catch (Exception e) {
   				Log.e(TAG, e.getLocalizedMessage(), e);
   			}
   			return response;
      }
       
      private String inputStreamToString(InputStream is) {
          String line = "";
          StringBuilder total = new StringBuilder(); 
          // Wrap a BufferedReader around the InputStream         
          try {
        	  BufferedReader rd = new BufferedReader(new InputStreamReader(is,"UTF-8")); 
              // Read response until the end
              while ((line = rd.readLine()) != null) {
                  total.append(line);
              }
          } catch (IOException e) {
              Log.e(TAG, e.getLocalizedMessage(), e);
          } 
          // Return full string
          return total.toString();
      }
   }
    
}
