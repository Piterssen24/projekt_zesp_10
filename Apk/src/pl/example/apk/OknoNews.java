package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import android.graphics.Bitmap;
import org.apache.http.HttpEntity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.ScrollView;

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
    public String postId;
    public String userLogin;
    public String content;
    public String photo;
    public String categoryId;
    public String addTime;
    public String place;
    public String eventTime, count, token;
    public final static String APP_PATH_SD_CARD = "/PicNews";
  	public ProgressBar spinner;
  	static int number = 0;
  	public static int id = 0; 
  	public static int id2 = 0;
  	public static int max = 0; 
  	static boolean loadingMore = false;
   	public ObservableScrollView scrollView;
   	public static int threshold = 982;
   	public String serwer = "";
   	public String[] tags, faculties, coords, tagsId;
   	private static final String TAG = "OknoLog";
   	
   	@Override
   	protected void onCreate(Bundle savedInstanceState) {
   		super.onCreate(savedInstanceState);
   		setContentView(R.layout.oknonews_layout); 
   		serwer = getResources().getString(R.string.server);
   		context = getApplicationContext();
   		linearLayout = (LinearLayout) findViewById(R.id.content);
   		
   		Bundle b = getIntent().getExtras();
   		if(b!=null) {
   			token = b.getString("token");
   			tagsId = b.getStringArray("tagsId");
   			tags = b.getStringArray("tags");
   			faculties = b.getStringArray("faculties");
   			coords = b.getStringArray("coords");
   		}
   		
   		scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
   		scrollView.setScrollViewListener(this);
    
   		// setup action bar for tabs
   		ActionBar actionBar =getActionBar();
   		actionBar.setTitle("PicNews");
   		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
   		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
    
   		//DRAWERLAYOUT      
   		dLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
   		final LinearLayout mDrawerLinear = (LinearLayout) findViewById(R.id.linearDrawer);
   		dList = (ListView) findViewById(R.id.leftDrawer);

   		// Enabling Home button
   		actionBar.setHomeButtonEnabled(true);
   		// Enabling Up navigation
   		actionBar.setDisplayHomeAsUpEnabled(true); 
   		// Getting reference to the ActionBarDrawerToggle
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
    	// Creating an ArrayAdapter to add items to the listview mDrawerList
   		adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.drawer_list_item , getResources().getStringArray(R.array.rivers));
   		// Setting the adapter on mDrawerList
   		dList.setAdapter(adapter);    
   		// Setting item click listener for the listview mDrawerList
   		dList.setOnItemClickListener(new OnItemClickListener() {
   			@Override
   			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.rivers);
                //Currently selected river
                getActionBar().setTitle(rivers[position]);
                // Creating a fragment object
                DetailFragment rFragment = new DetailFragment();
                // Creating a Bundle object
                Bundle data = new Bundle();
                // Setting the index of the currently selected item of mDrawerList
                data.putInt("position", position);
                // Setting the position to the fragment
                rFragment.setArguments(data);
                // Getting reference to the FragmentManager
                FragmentManager fragmentManager = getFragmentManager();
                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();
                // Adding a fragment to the fragment transaction
                ft.replace(R.id.contentFrame, rFragment);
                // Committing the transaction
                ft.commit();
                // Closing the drawer
                dLayout.closeDrawer(mDrawerLinear);
   			}
   		});
    
   		idItem = 0;
   		String sampleURL = serwer + "/news";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.NEWS_TASK, this, "Loading posts...", idItem);   
   		wst.execute(new String[] { sampleURL }); 
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
      		startActivity(intent);
      		return true;
   		case R.id.map:
   			Intent intentmapa = new Intent(getApplicationContext(), OknoMapa.class);
   			startActivity(intentmapa);  
   			return true;
   		case R.id.news:
   			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
   			startActivityForResult(cameraIntent, 0);
   			return true;
   		case R.id.konto:
   			Intent intentkonto = new Intent(getApplicationContext(), OknoKonto.class);
   			intentkonto.putExtra("token", token);
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
   
   	public void handleResponse(String resp) {   
   		try {
   			FragmentTransaction ft = null;
   			JSONArray jsonarray = new JSONArray(resp);
   			for(int i=0; i<jsonarray.length(); i++){
   				JSONObject jso = jsonarray.getJSONObject(i);
   				if(jso!=null){
   					postId = jso.getString("postId");
   					userLogin = jso.getString("userLogin");
   					content = jso.getString("content");
   					photo = jso.getString("photo");
   					categoryId = jso.getString("categoryId");
   					addTime = jso.getString("addTime");
   					place = jso.getString("place");
   					eventTime = jso.getString("eventTime");
   					newpost = new postElement(postId, userLogin, content, photo, categoryId, addTime, place, eventTime);
   					ft = getFragmentManager().beginTransaction();
   					ft.add(R.id.content, newpost, "f1");
   					ft.commit();
   					if(i==jsonarray.length()-1){
   						count = jso.getString("count");
   						id = Integer.parseInt(count);
   						id2 = id2 + id;	       
   					}
   				}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
   	}
   
   	@Override
   	public void onScrollEnded(ObservableScrollView scrollView, int x, int y, int oldx,   int oldy) {
   		if (y == threshold ) {
   			number = id2;
   			String sampleURL = serwer + "/news";
   			WebServiceTask wst = new WebServiceTask(WebServiceTask.NEWS_TASK, OknoNews.this , "Loading posts...", number);   
   			wst.execute(new String[] { sampleURL });    
   			threshold = threshold + 1600;
   		}
   	}
   
   	private class WebServiceTask extends AsyncTask<String, Integer, String> {
   		public static final int POST_TASK = 1;
   		public static final int NEWS_TASK = 2;
   		private static final String TAG = "WebServiceTask";
   		// connection timeout, in milliseconds (waiting to connect)
   		private static final int CONN_TIMEOUT = 5000;        
   		// socket timeout, in milliseconds (waiting for data)
   		private static final int SOCKET_TIMEOUT = 5000;  
   		private int taskType, number;
   		private Context mContext = null;
   		private String processMessage = "Processing...";
   		private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
   		private ProgressDialog pDlg = null;
   		public WebServiceTask(int taskType, Context mContext, String processMessage, int number){
   			this.taskType = taskType;
   			this.mContext = mContext;
   			this.processMessage = processMessage;
   			this.number = number;
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
   			String url2 = serwer + "/news2";
   			HttpClient httpclient = new DefaultHttpClient(getHttpParams());
   			HttpResponse response = null;
   			try {                          	               	
   				HttpClient httpClient = new DefaultHttpClient();
   				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                JSONObject json = new JSONObject();
                try{
                	HttpPost httpPost = new HttpPost(url2);
  					json.put("id",number);
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

