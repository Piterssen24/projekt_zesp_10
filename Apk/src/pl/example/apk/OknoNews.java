package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;

@SuppressWarnings("deprecation")
public class OknoNews extends Activity implements ScrollViewListener {
	String[] menu;
    ArrayList<FragmentTransaction> arrayList;
    DrawerLayout dLayout;
    ScrollView scroll;
    ListView dList;
    ArrayAdapter<String> adapter;
    ArrayAdapter<FragmentTransaction> adapter2;
    ActionBarDrawerToggle mDrawerToggle;
    TextView postContent, postAuthor;
    Context context;
    ImageView  photoView;
    LinearLayout linearLayout;
    View loadMoreView;
    Fragment newpost, newpost2;
    public int idItem;
    public static int[] repUserId, repPostId;
    public String postId, myLogin;
    public String userLogin;
    public String content;
    public String photo;
    public String categoryId;
    public String addTime;
    public String place;
    public String eventTime, count;
    public String[] list, folUserName;
    public static String role, token;
    public final static String APP_PATH_SD_CARD = "/PicNews";
  	public ProgressBar spinner;
  	public static String number = "";
  	public static int lastId = 0; 
  	public static int id2 = 0;
  	public static int max = 0; 
  	public boolean loadingMore = false;
   	public ObservableScrollView scrollView;
   	public static int threshold = 982;
   	public String serwer = "";
   	public static String[] tags, faculties, coords, tagsId, favUserId, favCategoryId;
   	private static final String TAG = "OknoLog";
   	public int pos, variant, screenTest=0, width, height;
   	public boolean over = false;
   	Bitmap bmp;
   	
   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
   		super.onCreate(savedInstanceState);
   		setContentView(R.layout.oknonews_layout);;
   		
   		serwer = getResources().getString(R.string.server);
   		context = getApplicationContext();
   		linearLayout = (LinearLayout) findViewById(R.id.content);
   		
   		getExtras();
   		getScreenType();   		
   		
   		scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
   		scrollView.setScrollViewListener(this);
   		// setup action bar for tabs
   		ActionBar actionBar =getActionBar();
   		actionBar.setTitle("PicNews");
   		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
   		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
    
   		//DRAWERLAYOUT      
   		dLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
   		dList = (ListView) findViewById(R.id.leftDrawer);
   		getLeftMenu();
   		

   		// Enabling Home button
   		actionBar.setHomeButtonEnabled(true);
   		// Enabling Up navigation
   		actionBar.setDisplayHomeAsUpEnabled(true); 
   		// Getting reference to the ActionBarDrawerToggle
   		
    
   		//idItem = 0;
   		variant = 0;
   		String sampleURL = serwer + "/news";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.NEWS_TASK, this, "£adowanie postów...", "", token);   
   		wst.execute(new String[] { sampleURL }); 
   	}

   public void getLeftMenu()
   {
	   final LinearLayout mDrawerLinear = (LinearLayout) findViewById(R.id.linearDrawer);
	   mDrawerToggle = new ActionBarDrawerToggle( this, dLayout, R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close){
           /** Called when drawer is closed */
           public void onDrawerClosed(View view) {
           	getActionBar().setTitle("PicNews");
               invalidateOptionsMenu();
           }

           /** Called when a drawer is opened */
           public void onDrawerOpened(View drawerView) {
               getActionBar().setTitle("PicNews - opcje");
               invalidateOptionsMenu();
           }
  		};
   
  		// Setting DrawerToggle on DrawerLayout
  		dLayout.setDrawerListener(mDrawerToggle);
  		list = new String[tags.length + 3];
  		list[0] = "Wszystkie";
  		list[1] = "Ulubione kategorie";
  		list[2] = "Obserwowani u¿ytkownicy";
  		for(int i=0; i<tags.length; i++){
  			list[i+3] = tags[i];
  		}
   	// Creating an ArrayAdapter to add items to the listview mDrawerList
  		adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.drawer_list_item , list);
  		// Setting the adapter on mDrawerList
  		dList.setAdapter(adapter);   
  		
  		//dList.getSelectedItem()
  		
  		// Setting item click listener for the listview mDrawerList
  		dList.setOnItemClickListener(new OnItemClickListener() {
  			@Override
  			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  				linearLayout.removeAllViews();
               dLayout.closeDrawer(mDrawerLinear);
               switch (position) {
               case 0:
               	variant = 1;
               	over = false;
              		String sampleURL = serwer + "/news";
              		WebServiceTask wst = new WebServiceTask(WebServiceTask.NEWS_TASK, OknoNews.this, "£adowanie postów...", number, token);   
              		wst.execute(new String[] { sampleURL }); 
               	break;
               case 1:
               	variant = 2;
               	over = false;
              		sampleURL = serwer + "/newsFavourites";
              		wst = new WebServiceTask(WebServiceTask.NEWSFAVOURITES_TASK, OknoNews.this, "£adowanie postów...", number, token, favCategoryId);   
              		wst.execute(new String[] { sampleURL }); 
               	break;
               case 2:
               	variant = 3;
               	over = false;
              		sampleURL = serwer + "/newsFollowed";
              		wst = new WebServiceTask(WebServiceTask.NEWSFOLLOWED_TASK, "£adowanie postów...", number, token, folUserName, OknoNews.this);   
              		wst.execute(new String[] { sampleURL }); 
               	break;
               default:
               	variant = 4;
               	over = false;
              		sampleURL = serwer + "/newsFiltered";
              		pos = position - 3;
              		wst = new WebServiceTask(WebServiceTask.NEWSFILTERED_TASK, OknoNews.this, "£adowanie postów...", number, token, tagsId[pos]);   
              		wst.execute(new String[] { sampleURL }); 
               	break;
               }
  			}
  		});
   }
   	
   	public void getScreenType()
   	{
   		Display display = getWindowManager().getDefaultDisplay();
   		Point size = new Point();
   		display.getSize(size);
   		width = size.x;
   		height = size.y;
   		if( (width>1100) && (height>1500) )
   		{
   			screenTest=1;
   		}
   	}
   	
   public void getExtras()
   {
	   Bundle b = getIntent().getExtras();
  		if(b!=null) {
  			token = b.getString("token");
  			role = b.getString("role");
  			tagsId = b.getStringArray("tagsId");
  			tags = b.getStringArray("tags");
  			faculties = b.getStringArray("faculties");
  			coords = b.getStringArray("coords");
  		}
   }
   	
   	/** Handling the touch event of app icon */
   	@Override
   	public boolean onOptionsItemSelected(MenuItem item) {
   		if (mDrawerToggle.onOptionsItemSelected(item)) {
   			return true;
   		}      
   		switch (item.getItemId()) {
   		case R.id.home:
    	  	Intent intent = new Intent(getApplicationContext(), OknoNews.class);
    	  	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
      		startActivity(intent);
      		return true;
   		case R.id.map:
   			Intent intentmapa = new Intent(getApplicationContext(), OknoMapa.class);
   			intentmapa.putExtra("token", token);
   			intentmapa.putExtra("faculties", faculties);
	    	intentmapa.putExtra("coords", coords);
	    	intentmapa.putExtra("repPostId", repPostId);
	    	intentmapa.putExtra("repUserId", repUserId);
	    	intentmapa.putExtra("folUserName", folUserName);
	    	intentmapa.putExtra("myLogin", myLogin);
	    	intentmapa.putExtra("screenTest", screenTest);
   			startActivity(intentmapa);  
   			return true;
   		case R.id.news:
   			if(role.equals("D")) {
   				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
   				startActivityForResult(cameraIntent, 0);
   			} else {
   				Toast.makeText(this, "Nie masz uprawnieñ do wykonania tej operacji!", Toast.LENGTH_LONG).show();
   			}
   			return true;
   		case R.id.konto:
   			Intent intentkonto = new Intent(getApplicationContext(), OknoKonto.class);
   			intentkonto.putExtra("screenTest",screenTest);
   			intentkonto.putExtra("token", token);
   			intentkonto.putExtra("faculties", faculties);
	    	intentkonto.putExtra("coords", coords);
	    	intentkonto.putExtra("tags", tags);
	    	intentkonto.putExtra("tagsId", tagsId);
	    	intentkonto.putExtra("favUserId", favUserId);
	    	intentkonto.putExtra("favCategoryId", favCategoryId);
	    	intentkonto.putExtra("repPostId", repPostId);
	    	intentkonto.putExtra("repUserId", repUserId);
	    	intentkonto.putExtra("folUserName", folUserName);
	    	intentkonto.putExtra("myLogin", myLogin);
	    	intentkonto.putExtra("role",role);
   			startActivity(intentkonto);
         	return true;
   		default:
   			return super.onOptionsItemSelected(item);
   		}
   	}

   	/** Called whenever we call invalidateOptionsMenu() */
   	/* @Override
  	public boolean onPrepareOptionsMenu(Menu menu) {
      	// If the drawer is open, hide action items related to the content view
      	boolean drawerOpen = dLayout.isDrawerOpen(mDrawerLinear);
      	return super.onPrepareOptionsMenu(menu);
  	}*/
   	
   	
  
   	@Override
   	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
   		// TODO Auto-generated method stub
   		super.onActivityResult(requestCode, resultCode, data);
   		Uri u = data.getData();
   		Intent intentPhoto = new Intent(this, OknoNew.class);
   		intentPhoto.putExtra("imgurl", u);
   		intentPhoto.putExtra("token", token);
   		intentPhoto.putExtra("tags", tags);
   		intentPhoto.putExtra("faculties", faculties);
   		intentPhoto.putExtra("coords", coords);
   		intentPhoto.putExtra("tagsId", tagsId);
   		startActivity(intentPhoto);
   	}   
   	


   	@Override
   	public boolean onCreateOptionsMenu(Menu menu) {
   		// Inflate the menu; this adds items to the action bar if it is present.
   		getMenuInflater().inflate(R.menu.main, menu);
   		return true;
   	}
   
   	/**
     * metoda przyjmuje jako parametr string, który zawiera odpowiedŸ od serwera
     * i tworzy z niego obiekt JSON, który nastêpnie jest parsowany. Tworzy obiekty postElement,
     * które s¹ wyswietlane na stronie z newsami.
     */
   	public void handleResponse(String resp) {   
   		try {
   			FragmentTransaction ft = null;
   			JSONArray jsonarray = new JSONArray(resp);
   			JSONArray jarrayPosts = jsonarray.getJSONArray(1);
			JSONArray jarrayFav = jsonarray.getJSONArray(0);
			JSONArray jarrayRep = jsonarray.getJSONArray(2);
			JSONArray jarrayFol = jsonarray.getJSONArray(3);
			favUserId = new String[jarrayFav.length()];
			favCategoryId = new String[jarrayFav.length()];
			repPostId = new int[jarrayRep.length()];
			repUserId = new int[jarrayRep.length()];
			folUserName = new String[jarrayFol.length()];
			myLogin = jsonarray.getString(4);
			for(int i=0; i<jarrayFol.length(); i++){
				JSONObject jso = jarrayFol.getJSONObject(i);
				if(jso!=null){
					folUserName[i] = jso.getString("folUserName");
				}
			}
			for(int i=0; i<jarrayFav.length(); i++){
				JSONObject jso = jarrayFav.getJSONObject(i);
				if(jso!=null){
					favUserId[i] = jso.getString("favUserId");
					favCategoryId[i] = jso.getString("favCategoryId");
				}
			}
			for(int i=0; i<jarrayRep.length(); i++){
				JSONObject jso = jarrayRep.getJSONObject(i);
				if(jso!=null){
					repPostId[i] = jso.getInt("repPostId");
					repUserId[i] = jso.getInt("repUserId");
				}
			}
   			for(int i=0; i<jarrayPosts.length(); i++){
   				JSONObject jso = jarrayPosts.getJSONObject(i);
   				if(jso!=null){
   					postId = jso.getString("postId");
   					userLogin = jso.getString("userLogin");
   					content = jso.getString("content");
   					photo = jso.getString("photo");
   					categoryId = jso.getString("categoryId");
   					addTime = jso.getString("addTime");
   					place = jso.getString("place");
   					eventTime = jso.getString("eventTime");
   					newpost = new postElement(token, postId, userLogin, content, photo, categoryId, addTime, place, eventTime, faculties, coords, "News", repPostId, repUserId, folUserName, myLogin,screenTest);
   					ft = getFragmentManager().beginTransaction();
   					ft.add(R.id.content, newpost, "f1");
   					ft.commit();
   					//if(i == jsonarray.length()-1){
   						lastId = Integer.parseInt(postId);
   						lastId = lastId - 1;
   					//}
   					if(postId.equals("1")){
   						over = true;
   					}
   				}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
   		loadingMore = false;
   	}
   	
   	/**
     * metoda "³aduj¹ca" dalsze posty po przewiniêciu scrolla na sam koniec
     */
   	@Override
   	public void onScrollEnded(ObservableScrollView scrollView, int x, int y, int oldx,   int oldy) {
   		if(loadingMore == false){
   			loadingMore = true;
   		if(over == false){
   			switch (variant) {
   				case 0:
   					String sampleURL = serwer + "/news";
               		WebServiceTask wst = new WebServiceTask(WebServiceTask.NEWS_TASK, OknoNews.this, "£adowanie postów...", Integer.toString(lastId), token);   
               		wst.execute(new String[] { sampleURL });    
   					break;
   				case 1:
   					sampleURL = serwer + "/news";
               		wst = new WebServiceTask(WebServiceTask.NEWS_TASK, OknoNews.this, "£adowanie postów...", Integer.toString(lastId), token);   
               		wst.execute(new String[] { sampleURL });    
   					break;
   				case 2:
               		sampleURL = serwer + "/newsFavourites";
               		wst = new WebServiceTask(WebServiceTask.NEWSFAVOURITES_TASK, OknoNews.this, "£adowanie postów...", Integer.toString(lastId), token, favCategoryId);   
               		wst.execute(new String[] { sampleURL }); 
                	break;
   				case 3:
               		sampleURL = serwer + "/newsFollowed";
               		wst = new WebServiceTask(WebServiceTask.NEWSFOLLOWED_TASK, "£adowanie postów...", Integer.toString(lastId), token, folUserName, OknoNews.this);   
               		wst.execute(new String[] { sampleURL }); 
                	break;
   				case 4:
               		sampleURL = serwer + "/newsFiltered";
               		wst = new WebServiceTask(WebServiceTask.NEWSFILTERED_TASK, OknoNews.this, "£adowanie postów...", Integer.toString(lastId), token, tagsId[pos]);   
               		wst.execute(new String[] { sampleURL }); 
                	break;
   			}
   		}	
   		}
   	}
   
   	/**
     * klasa wewnêtrzna, wykonuj¹ca asynchroniczne dzia³anie w tle.
     */
   	private class WebServiceTask extends AsyncTask<String, Integer, String> {
   		public static final int NEWS_TASK = 2;
   		public static final int NEWSFILTERED_TASK = 3;
   		public static final int NEWSFAVOURITES_TASK = 4;
   		public static final int NEWSFOLLOWED_TASK = 5;
   		private static final String TAG = "WebServiceTask";
   		// connection timeout, in milliseconds (waiting to connect)
   		private static final int CONN_TIMEOUT = 50000;        
   		// socket timeout, in milliseconds (waiting for data)
   		private static final int SOCKET_TIMEOUT = 50000;  
   		private int taskType;
   		private Context mContext = null;
   		private String token, number;
   		private String processMessage = "Processing...";
   	//	private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
   		private ProgressDialog pDlg = null;
   		private String[] favCategoryId, folUserName;
   		private String tag;
   		public WebServiceTask(int taskType, Context mContext, String processMessage, String number, String token){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.number = number;
   			this.token = token;
   		}
   		
   		public WebServiceTask(int taskType, Context mContext, String processMessage, String number, String token, String[] favCategoryId){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.number = number;
   			this.token = token;
   			this.favCategoryId = favCategoryId;
   		}
   		
   		public WebServiceTask(int taskType, String processMessage, String number, String token, String[] folUserName, Context mContext){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.number = number;
   			this.token = token;
   			this.folUserName = folUserName;
   		}
   		
   		public WebServiceTask(int taskType, Context mContext, String processMessage, String number, String token, String tag){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.number = number;
   			this.token = token;
   			this.tag = tag;
   		}
      
   		/*public void addNameValuePair(String name, String value) {
   			params.add(new BasicNameValuePair(name, value));
   		}*/

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
   				//	resp = response.toString();
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
            	case NEWS_TASK:
            		String url2 = serwer + "/news2";
   				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                JSONObject json = new JSONObject();
                try{
                	HttpPost httpPost = new HttpPost(url2);
  					json.put("id", number);
  					json.put("token", token);
  					StringEntity se = new StringEntity(json.toString(), "UTF-8");
  					httpPost.addHeader("Content-Type","application/json");
  					httpPost.setEntity(se);
  					response = httpClient.execute(httpPost);				
  					/*if(response != null){
  						response.getEntity().getContent();
  					}*/
  				}catch(Exception e){
  					e.printStackTrace();
  					//createDialog("Error", "Cannot Estabilish Connection");
  				}
                  	HttpGet httpget = new HttpGet(url);
                  	response = httpclient.execute(httpget);
                  	break;
                  	
            	case NEWSFILTERED_TASK:
            		url2 = serwer + "/newsFiltered2";
       				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                    json = new JSONObject();
                    try{
                    	HttpPost httpPost = new HttpPost(url2);
      					json.put("id", number);
      					json.put("token", token);
      					json.put("tag", tag);
      					StringEntity se = new StringEntity(json.toString(), "UTF-8");
      					httpPost.addHeader("Content-Type","application/json");
      					httpPost.setEntity(se);
      					response = httpClient.execute(httpPost);				
      					if(response != null){
      						response.getEntity().getContent();
      					}
      				}catch(Exception e){
      					e.printStackTrace();
      					//createDialog("Error", "Cannot Estabilish Connection");
      				}
                      	httpget = new HttpGet(url);
                      	response = httpclient.execute(httpget);
                      	break;
                      	
            		case NEWSFAVOURITES_TASK:
                      	url2 = serwer + "/newsFavourites2";
           				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                        JSONArray ja = new JSONArray();
                        try{
                        	HttpPost httpPost = new HttpPost(url2);
                        	JSONArray j = new JSONArray();
                        	for(int i=0; i<favCategoryId.length; i++){
                				j.put(favCategoryId[i]);
                			}
          					ja.put(number);
          					ja.put(token);
          					ja.put(j);
          					StringEntity se = new StringEntity(ja.toString(), "UTF-8");
          					httpPost.addHeader("Content-Type","application/json");
          					httpPost.setEntity(se);
          					response = httpClient.execute(httpPost);				
          					if(response != null){
          						response.getEntity().getContent();
          					}
          				}catch(Exception e){
          					e.printStackTrace();
          					//createDialog("Error", "Cannot Estabilish Connection");
          				}
                          	httpget = new HttpGet(url);
                          	response = httpclient.execute(httpget);
                          	break;
                          	
            		case NEWSFOLLOWED_TASK:
            			url2 = serwer + "/newsFollowed2";
       				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
       				ja = new JSONArray();
                    try{
                    	HttpPost httpPost = new HttpPost(url2);
                    	JSONArray j = new JSONArray();
                    	for(int i=0; i<folUserName.length; i++){
            				j.put(folUserName[i]);
            			}
      					ja.put(number);
      					ja.put(token);
      					ja.put(j);
      					StringEntity se = new StringEntity(ja.toString(), "UTF-8");
      					httpPost.addHeader("Content-Type","application/json");
      					httpPost.setEntity(se);
      					response = httpClient.execute(httpPost);				
      					if(response != null){
      						response.getEntity().getContent();
      					}
      				}catch(Exception e){
      					e.printStackTrace();
      					//createDialog("Error", "Cannot Estabilish Connection");
      				}
                      	httpget = new HttpGet(url);
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

