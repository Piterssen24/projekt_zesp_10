package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class OknoMapa extends FragmentActivity{

	public LatLng yourLocation, markerLocation;
	private GoogleMap googleMap;
	public LocationManager locManager;
	public String serwer = "";
	public String token, myLogin;
	private static final String TAG = "OknoMapa";
	public static int[] repPostId, repUserId;
	public int[] postId;
    public String[] userLogin;
    public String[] content, postText;
    public String[] photo;
    public String[] categoryId;
    public String[] addTime;
    public String[] place;
    public String[] eventTime;
    public String[] newContent;
    public static String[] faculties, coords, folUserName;
    public List<LatLng> list, listPoint;
    public List<Marker> markerList;
    public List<Marker> markerYellow;
    public boolean test = false, listTest=false, backTest=false;
    public float zoomTest;
    public int test2=0;
    public Location mLastLocation;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknomapa_layout);
        serwer = getResources().getString(R.string.server);
        
        getExtras();
   		
   		list = new ArrayList<LatLng>();
   		listPoint = new ArrayList<LatLng>();
   		markerList = new ArrayList<Marker>();
   		markerYellow = new ArrayList<Marker>();
   		
   		loadPosts();
        
        loadMap();
       
    }  

	
	public void getExtras()
	{
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
	}
	
	public void loadPosts()
	{
		String sampleURL = serwer + "/map";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.MAP_TASK, this, "Loading posts on map...", token);   
   		wst.execute(new String[] { sampleURL }); 
	}
	
	public void getLastKnowLocation(LocationManager locManager)
	{
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
 	            Log.d("tag", "Lon: "+String.valueOf(lon));
 	            yourLocation = new LatLng(lat, lon);
 	            googleMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
 	            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
 	            googleMap.addMarker(new MarkerOptions().position(yourLocation).title("Tu jesteœ"));
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
 			getLastKnowLocation(locManager);
 			locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 10, myLocationListener);    		
 		}
 		else 
 			{
 			getLastKnowLocation(locManager);
 			locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 5000, 10, myLocationListener);
 			}
 		
	}
	
	public void openPost(int i)
	{
		Intent intent = new Intent(OknoMapa.this, OknoPost.class);
		intent.putExtra("postId", postId[i]);
    	intent.putExtra("postText",postText[i]);
    	Global.img = decodeBase64(photo[i]);
    	//intent.putExtra("photo", photo[i]);
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
    	startActivity(intent);
	}
	
	public void openGroupPost(Marker marker)
	{
		if(marker.getTitle()!="Tu jesteœ")
		{
		for(int j=0;j<listPoint.size();j++)
    	{
    		if((marker.getPosition().latitude==listPoint.get(j).latitude) && (marker.getPosition().longitude==listPoint.get(j).longitude))
    		{
    			LatLng newPosition = new LatLng(listPoint.get(j).latitude,listPoint.get(j).longitude);
    			LatLng northeast = new LatLng(listPoint.get(j).latitude-0.001,listPoint.get(j).longitude-0.001);
    			LatLng southwest = new LatLng(listPoint.get(j).latitude+0.001,listPoint.get(j).longitude+0.001);
    			LatLngBounds bounds = new LatLngBounds(northeast,southwest);
    			googleMap.moveCamera(CameraUpdateFactory.newLatLng(newPosition));
    			googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    			marker.setVisible(false);
    			zoomTest = googleMap.getCameraPosition().zoom;
    			for(int k=0;k<markerList.size();k++)
       			{
    				if((markerList.get(k).getPosition().latitude - listPoint.get(j).latitude < 0.0005)
       						&& (markerList.get(k).getPosition().latitude - listPoint.get(j).latitude > -0.0005)
       					&&(markerList.get(k).getPosition().longitude - listPoint.get(j).longitude < 0.0005)
       					&& (markerList.get(k).getPosition().longitude - listPoint.get(j).longitude < 0.0005))
       				{
       					markerList.get(k).setVisible(true);
       					test2=0;
       				}
       			}
    		}
    		test=true;
    	}
	}
	}
	
	 public static Bitmap decodeBase64(String input) 
	    {
	        byte[] decodedByte = Base64.decode(input, Base64.URL_SAFE);
	        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	    }
	
	public void markerClicked(Marker marker)
	{
		for(int i=0; i<content.length; i++){
        	if(marker.getTitle().equals(content[i])) // if marker source is clicked
            { 
        		openPost(i);
            }
        	else
        	{
        		openGroupPost(marker);
        	}
        	}
	}
	
	public void loadMap()
	{
		try { 
            if (googleMap == null) {
               googleMap = ((MapFragment) getFragmentManager().
               findFragmentById(R.id.map)).getMap();               
            }

           getLocation();
            
    		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    		
    		googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(Marker args) {
	                return null;
	            }

	            // Defines the contents of the InfoWindow
	            @SuppressLint("InflateParams") @Override
	            public View getInfoContents(Marker args) {

	                // Getting view from the layout file info_window_layout
	                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
	                
	                TextView title = (TextView) v.findViewById(R.id.mapContent);
	                title.setText(args.getTitle());
	                googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
                    public void onInfoWindowClick(Marker marker) 
                    {
                    	markerClicked(marker);                    
                    }
                });

                // Returning the view containing InfoWindow contents
                return v;

            }
        }); 
      
    		googleMap.setOnCameraChangeListener(getCameraChangeListener());
    		
        } catch (Exception e) {
         e.printStackTrace();
      }
	}
	
	public void closeGroupPost(CameraPosition arg0)
	{
		if((zoomTest > arg0.zoom) && (test) && (test2>0) && (zoomTest<17))
		{
			for(int i=0; i<markerYellow.size();i++)
			{
				if((markerYellow.get(i).getPosition().latitude<arg0.target.latitude+0.0005) && ((markerYellow.get(i).getPosition().longitude<arg0.target.longitude+0.0005)))
				{
					for(int k=0;k<markerList.size();k++)
           			{
        				if((markerList.get(k).getPosition().latitude - markerYellow.get(i).getPosition().latitude < 0.0005)
           						&& (markerList.get(k).getPosition().latitude - markerYellow.get(i).getPosition().latitude > -0.0005)
           					&&(markerList.get(k).getPosition().longitude - markerYellow.get(i).getPosition().longitude < 0.0005)
           					&& (markerList.get(k).getPosition().longitude - markerYellow.get(i).getPosition().longitude < 0.0005))
           				{
           					markerList.get(k).setVisible(false);
           					markerYellow.get(i).setVisible(true);
           					test=false;
           				}
           			}
					
				}
			}
		
		}
		zoomTest = arg0.zoom;
		test2++;
	}
	
	public OnCameraChangeListener getCameraChangeListener()
	{
	    return new OnCameraChangeListener() 
	    {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				
				closeGroupPost(arg0);
				
				
			}
	    };
	}
	
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
	
	public void handleResponse(String resp) {   
   		try {
   			JSONArray jsonarray = new JSONArray(resp);
   				postId = new int[jsonarray.length()];
				userLogin = new String[jsonarray.length()];
				content = new String[jsonarray.length()];
				newContent = new String[jsonarray.length()];
				postText = new String[jsonarray.length()];
				photo = new String[jsonarray.length()];
				categoryId = new String[jsonarray.length()];
				addTime = new String[jsonarray.length()];
				place = new String[jsonarray.length()];
				eventTime = new String[jsonarray.length()];
   			for(int i=0; i<jsonarray.length(); i++){	
   				JSONObject jso = jsonarray.getJSONObject(i);
   				if(jso!=null){
   					postId[i] = jso.getInt("postId");
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
   					for(int j=0; j<list.size(); j++)
   					{
   						if ( (list.get(j).latitude-loc.latitude<0.0005) && (list.get(j).latitude-loc.latitude>-0.0005) && (list.get(j).longitude-loc.longitude<0.0005) && (list.get(j).longitude-loc.longitude>-0.0005) )
   						{
   							listTest=true;
   						}
   					}
   					if(list.contains(loc) || (listTest))
   					{		
   							if(!listPoint.contains(loc) && (!(listTest)))
   							{
   								listPoint.add(loc);
   							}
   					        double nlat = lat + (Math.random() -.5) / 1500;
   					        double nlng = lon + (Math.random() -.5) / 1500;
   					        LatLng nloc = new LatLng(nlat,nlng);
   					        Marker TP = googleMap.addMarker(new MarkerOptions().position(nloc).title(content[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
   					        markerList.add(TP);
   					        TP.setVisible(false);
   					        list.add(nloc);
   					        listTest=false;
   					}
   					else
   					{
   						if(!listPoint.contains(loc))
							{
								listPoint.add(loc);
							}
   						Marker TP = googleMap.addMarker(new MarkerOptions().position(markerLocation).title(content[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
   						list.add(markerLocation);
   						markerList.add(TP);
   						TP.setVisible(false);
   					}
   					
   				}
   			}
   				
   				
   			

   			
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
   		
   		
   		
   		for(int i=0; i<listPoint.size();i++)
   		{
   			int number = 0;
   			for(int j=0; j<markerList.size(); j++)
   			{
   				if((markerList.get(j).getPosition().latitude - listPoint.get(i).latitude < 0.0005)
   						&& (markerList.get(j).getPosition().latitude - listPoint.get(i).latitude > -0.0005)
   					&&(markerList.get(j).getPosition().longitude - listPoint.get(i).longitude < 0.0005)
   					&& (markerList.get(j).getPosition().longitude - listPoint.get(i).longitude < 0.0005))
   				{
   					number++;
   				}
   			}
   			if (number==1)
   			{
   				for(int j=0; j<markerList.size(); j++)
   	   			{
   	   				if((markerList.get(j).getPosition().latitude - listPoint.get(i).latitude < 0.0005)
   	   						&& (markerList.get(j).getPosition().latitude - listPoint.get(i).latitude > -0.0005)
   	   					&&(markerList.get(j).getPosition().longitude - listPoint.get(i).longitude < 0.0005)
   	   					&& (markerList.get(j).getPosition().longitude - listPoint.get(i).longitude < 0.0005))
   	   				{
   	   					markerList.get(j).setVisible(true);
   	   				}
   	   			}
   			}
   			else
   			{
   			double nlat = listPoint.get(i).latitude;
		    double nlng = listPoint.get(i).longitude;
		    LatLng nloc = new LatLng(nlat,nlng);
		    Marker TP = googleMap.addMarker(new MarkerOptions().position(nloc).title("Iloœæ postów: "+number).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
		    markerYellow.add(TP);
   			}
   		}

   		
   		
   	}
	
	
	private class WebServiceTask extends AsyncTask<String, Integer, String> {
   		public static final int MAP_TASK = 1;
   		private static final String TAG = "WebServiceTask";
   		// connection timeout, in milliseconds (waiting to connect)
   		private static final int CONN_TIMEOUT = 50000;        
   		// socket timeout, in milliseconds (waiting for data)
   		private static final int SOCKET_TIMEOUT = 50000;  
   		private int taskType;
   		private Context mContext = null;
   		private String token;
   		private String processMessage = "Processing...";
   		//private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
   		private ProgressDialog pDlg = null;
   		//private String[] favCategoryId;
   		//private String tag;
   		public WebServiceTask(int taskType, Context mContext, String processMessage, String token){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.token = token;
   		}
      
   		/*public void addNameValuePair(String name, String value) {
   			params.add(new BasicNameValuePair(name, value));
   		}*/

   		@SuppressWarnings("deprecation")
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
   			//String resp = "";
   			HttpResponse response = doResponse(url); 
   			if (response == null) {
   				return result;
   			} else { 
   				try {
   					result = inputStreamToString(response.getEntity().getContent());
   					//resp = response.toString();
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
  					/*if(response != null){
  						InputStream in = response.getEntity().getContent();
  					}*/
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
