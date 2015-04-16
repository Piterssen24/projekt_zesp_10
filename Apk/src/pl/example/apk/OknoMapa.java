package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
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
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

	LatLng yourLocation, markerLocation;
	private GoogleMap googleMap;
	LocationManager locManager;
	public String serwer = "";
	public String token, myLogin;
	private static final String TAG = "OknoMapa";
	public static int[] repPostId, repUserId;
	public String[] postId;
    public String[] userLogin;
    public String[] content, postText;
    public String[] photo;
    public String[] categoryId;
    public String[] addTime;
    public String[] place;
    public String[] eventTime;
    public static String[] faculties, coords, folUserName;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknomapa_layout);
        serwer = getResources().getString(R.string.server);
        Bundle b = getIntent().getExtras();
        if(b != null){
        	token = b.getString("token");
        	faculties = b.getStringArray("faculties");
   			coords = b.getStringArray("coords");
   			repPostId = b.getIntArray("repPostId");
   			repUserId = b.getIntArray("repUserId");
   			folUserName = b.getStringArray("folUserName");
   			myLogin = b.getString("myLogin");
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
	
	/**
     * metoda skracaj¹ca d³ugoœæ stringu
     */
	public static String truncate(final String content, final int lastIndex) {
		if(content.length()>60)
		{
			String result = content.substring(0, lastIndex);
			if (content.charAt(lastIndex) != ' ') 
			{
				result = result.substring(0, result.lastIndexOf(" "));
			}
			return result+"...";
		}
		else return content;
	}
	
	
	/**
     * metoda przyjmuje jako parametr string, który zawiera odpowiedŸ od serwera
     * i tworzy z niego obiekt JSON, który nastêpnie jest parsowany. Tworzy obiekty, które s¹ wyœwietlane na mapie.
     */
	public void handleResponse(String resp) {   
   		try {
   			JSONArray jsonarray = new JSONArray(resp);
   			for(int i=0; i<jsonarray.length(); i++){
   				postId = new String[jsonarray.length()];
   				userLogin = new String[jsonarray.length()];
   				content = new String[jsonarray.length()];
   				postText = new String[jsonarray.length()];
   				photo = new String[jsonarray.length()];
   				categoryId = new String[jsonarray.length()];
   				addTime = new String[jsonarray.length()];
   				place = new String[jsonarray.length()];
   				eventTime = new String[jsonarray.length()];
   				JSONObject jso = jsonarray.getJSONObject(i);
   				if(jso!=null){
   					postId[i] = jso.getString("postId");
   					userLogin[i] = jso.getString("userLogin");
   					content[i] = jso.getString("content");
   					postText[i] = content[i];
   					content[i] = truncate(content[i],40);
   					photo[i] = jso.getString("photo");
   					categoryId[i] = jso.getString("categoryId");
   					addTime[i] = jso.getString("addTime");
   					place[i] = jso.getString("place");
   					eventTime[i] = jso.getString("eventTime");
   					String[] tokens = place[i].split(",");
		        	double lat = Double.parseDouble(tokens[0]);
		        	double lon = Double.parseDouble(tokens[1]);
   					LatLng loc = new LatLng(lat, lon);
   					markerLocation = loc;
   					Marker TP = googleMap.addMarker(new MarkerOptions().position(loc).title(content[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
   					TP.setDraggable(false);
   				    //googleMap.setOnMarkerDragListener(new OnMarkerDragListener()
   					/*googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

   			            // Use default InfoWindow frame
   			            @Override
   			            public View getInfoWindow(Marker args) {
   			                return null;
   			            }

   			            // Defines the contents of the InfoWindow
   			            @Override
   			            public View getInfoContents(Marker args) {

   			                // Getting view from the layout file info_window_layout
   			                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

   			                // Getting the position from the marker
   			                LatLng ll;
   			                ll = args.getPosition();

   			                TextView title = (TextView) v.findViewById(R.id.mapContent);
   			                title.setText(args.getTitle());
   			             googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
			                    public void onInfoWindowClick(Marker marker) 
			                    {
			                    	for(int i=0; i<content.length; i++){
			                    	if(marker.getTitle().equals(content[i])) // if marker source is clicked
			                        {
			                    	ProgressDialog pDlg = new ProgressDialog(OknoMapa.this);
			               			pDlg.setMessage("£adowanie informacji o poœcie...");
			               			pDlg.setProgressDrawable(OknoMapa.this.getWallpaper());
			               			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			               			pDlg.setCancelable(false);
			               			pDlg.show(); 
									Intent intent = new Intent(OknoMapa.this, OknoPost.class);
			            	    	intent.putExtra("postText",postText[i]);
			            	    	intent.putExtra("photo", photo[i]);
			            	    	intent.putExtra("userLogin", userLogin[i]);
			            	    	intent.putExtra("place", place[i]);
			            	    	intent.putExtra("eventTime", eventTime[i]);
			            	    	intent.putExtra("faculties", faculties);
			            	    	intent.putExtra("coords", coords);
			            	    	intent.putExtra("token", token);
			            	    	pDlg.dismiss();
			            	    	startActivity(intent);
			                        }
			                    	}
			                    }
			                });

			                // Returning the view containing InfoWindow contents
			                return v;

			            }
			        });  */
   					/*googleMap.setOnMarkerDragListener(new OnMarkerDragListener()
   				    {

					@Override
					public void onMarkerDrag(Marker marker) {
						// TODO Auto-generated method stub
						
					}
					@Override
					public void onMarkerDragEnd(Marker marker) {
						// TODO Auto-generated method stub
						marker.setPosition(markerLocation);
						//Loading post...
						if(marker.getTitle().equals(content)) // if marker source is clicked
                        {
						Intent intent = new Intent(OknoMapa.this, OknoPost.class);
            	    	intent.putExtra("postText",postText);
            	    	intent.putExtra("photo", photo);
            	    	intent.putExtra("userLogin", userLogin);
            	    	intent.putExtra("place", place);
            	    	intent.putExtra("eventTime", eventTime);
            	    	intent.putExtra("faculties", faculties);
            	    	intent.putExtra("coords", coords);
            	    	intent.putExtra("token", token);
            	    	startActivity(intent);
                        }
						
					}
					@Override
					public void onMarkerDragStart(Marker marker) {
						// TODO Auto-generated method stub
						/*if(marker.getTitle().equals(content)) // if marker source is clicked
                        {
						Intent intent = new Intent(OknoMapa.this, OknoPost.class);
            	    	intent.putExtra("postText",postText);
            	    	intent.putExtra("photo", photo);
            	    	intent.putExtra("userLogin", userLogin);
            	    	intent.putExtra("place", place);
            	    	intent.putExtra("eventTime", eventTime);
            	    	intent.putExtra("faculties", faculties);
            	    	intent.putExtra("coords", coords);
            	    	intent.putExtra("token", token);
            	    	startActivity(intent);
                        }*/
						
				//	}

              //   }); 
   				}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
   		
   		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(Marker args) {
	                return null;
	            }

	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker args) {

	                // Getting view from the layout file info_window_layout
	                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

	                // Getting the position from the marker
	                LatLng ll;
	                ll = args.getPosition();

	                TextView title = (TextView) v.findViewById(R.id.mapContent);
	                title.setText(args.getTitle());
	             googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
                    public void onInfoWindowClick(Marker marker) 
                    {
                    	for(int i=0; i<content.length; i++){
                    	if(marker.getTitle().equals(content[i])) // if marker source is clicked
                        {
                    	ProgressDialog pDlg = new ProgressDialog(OknoMapa.this);
               			pDlg.setMessage("£adowanie informacji o poœcie...");
               			pDlg.setProgressDrawable(OknoMapa.this.getWallpaper());
               			pDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
               			pDlg.setCancelable(false);
               			pDlg.show(); 
						Intent intent = new Intent(OknoMapa.this, OknoPost.class);
						intent.putExtra("postId", postId[i]);
            	    	intent.putExtra("postText",postText[i]);
            	    	intent.putExtra("photo", photo[i]);
            	    	intent.putExtra("userLogin", userLogin[i]);
            	    	intent.putExtra("place", place[i]);
            	    	intent.putExtra("eventTime", eventTime[i]);
            	    	intent.putExtra("faculties", faculties);
            	    	intent.putExtra("coords", coords);
            	    	intent.putExtra("token", token);
            	    	intent.putExtra("repPostId", repPostId);
            	    	intent.putExtra("repUserId", repUserId);
            	    	intent.putExtra("folUserName", folUserName);
            	    	intent.putExtra("myLogin", myLogin);
            	    	pDlg.dismiss();
            	    	startActivity(intent);
                        }
                    	}
                    }
                });

                // Returning the view containing InfoWindow contents
                return v;

            }
        }); 
   	}
	
	/**
     * klasa wewnêtrzna, która wykonuje asynchroniczne dzia³anie w tle.
     */
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

   		/**
         * metoda , która tworzy i wyœwietla obiekt progress bar.
         */
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

   		/**
         * g³ówna metoda wykonuj¹ca dzia³anie w tle.
         */
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
   		/**
         * metoda ustawiaj¹ca parametry po³¹czenia http.
         */
   		private HttpParams getHttpParams() {            
   			HttpParams htpp = new BasicHttpParams();             
   			HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
   			HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);           
   			return htpp;
   		}
       
   		/**
         * metoda wysy³aj¹ca oraz odbieraj¹ca dane od web serwisu.
         */
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
       
   		/**
         * metoda konwertuj¹ca odpowiedŸ serwera na String.
         */
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
