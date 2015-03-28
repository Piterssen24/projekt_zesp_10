package pl.example.apk;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.view.View.OnClickListener;
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

import pl.example.apk.*;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class OknoKonto extends Activity {
	
	ImageView yourPicture;
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ExpandableListView expListView2;
    ArrayList<String> listDataHeader;
    ArrayList<String> listDataHeaderCheck;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChildCheck;
    Fragment newpost;
    Button editAccount;
    PopupMenu menu1, menu2;
    TextView list1, list2;
    public String serwer = "";
    public String token;
	public static String login;
	public String place, postId, content, photo, categoryId, addTime, eventTime;
	private static final String TAG = "OknoKonto";
	public TextView tv;
	public static String[] faculties, coords;
	Context context;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknokonto_layout);
        context = getApplicationContext();
        serwer = getResources().getString(R.string.server);
        
        Bundle b = getIntent().getExtras();
   		if(b!=null) {
   			token = b.getString("token");
   			faculties = b.getStringArray("faculties");
   			coords = b.getStringArray("coords");
   		}
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - Twoje konto");
        bar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        tv = (TextView) findViewById(R.id.textViewUserName);
        yourPicture = (ImageView) findViewById(R.id.userPhoto);
        LayoutParams params = (LayoutParams) yourPicture.getLayoutParams();
        params.width = 120;
        params.height = 200;
        
        editAccount = (Button) findViewById(R.id.buttonEdit);
        editAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), OknoEdytujProfil.class);
            	startActivity(intent);
				
			}
		});
        
        // existing height is ok as is, no need to edit it
        yourPicture.setLayoutParams(params);
        
        list1 = (TextView) findViewById(R.id.popup1);
        list2 = (TextView) findViewById(R.id.popup2);
        
        //dodanie do tekstu liczbu wybranych tagów i ulubionych u¿ytkowników - zsumowaæ z bazy
        String s1,s2;
        s1=list1.getText().toString();
        int liczbatagow=5, liczbaulubionych=6;
        s1+=" ("+liczbatagow+")";
        list1.setText(s1);
        
        s2=list2.getText().toString();
        s2+=" ("+liczbaulubionych+")";
        list2.setText(s2);
        
        list1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu1 = new PopupMenu(OknoKonto.this, v);
				//dodanie kolejnych nazw tagów
				menu1.getMenu().add("sport");
				menu1.getMenu().add("konkurs");
				menu1.getMenu().add("impreza");
				menu1.getMenu().add("koncert");
				menu1.getMenu().add("wyk³ad");
				menu1.show();
				
			}
		});
        
 list2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu2 = new PopupMenu(OknoKonto.this, v);
				//dodanie kolejnych nazw u¿ytkowników
				menu2.getMenu().add(0,0,0,"Julka                     usuñ");
				menu2.getMenu().add(0,1,0,"Dominika              usuñ");
				menu2.getMenu().add(0,2,0,"Sebastian             usuñ");
				menu2.getMenu().add(0,3,0,"Piotr                      usuñ");
				menu2.getMenu().add(0,4,0,"Bartek                   usuñ");
				menu2.getMenu().add(0,5,0,"Lamia                 usuñ");
				menu2.getMenu().add(0,6,0,"Julka                 usuñ");
				menu2.getMenu().add(0,7,0,"Dominika              usuñ");
				menu2.getMenu().add(0,8,0,"Sebastian             usuñ");
				menu2.getMenu().add(0,9,0,"Piotr                 usuñ");
				menu2.getMenu().add(0,10,0,"Bartek                usuñ");
				menu2.getMenu().add(0,11,0,"Lamia                 usuñ");
				menu2.show();
				
				menu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
		             public boolean onMenuItemClick(MenuItem item) {  
		              Toast.makeText(OknoKonto.this,"Usun¹³eœ : " + item.getItemId(),Toast.LENGTH_SHORT).show();  
		              menu2.getMenu().removeItem(item.getItemId());
		              menu2.show();
		              return true;  
		             }  
		            });  
				
			}
		});
        
        String sampleURL = serwer + "/account";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.ACCOUNT_TASK, this, token);   
   		wst.execute(new String[] { sampleURL }); 
    }  
        
    
    public void handleResponse(String response) { 
    	System.out.println(response);
    	try {
   			FragmentTransaction ft = null;
   			System.out.println(response);
   			JSONArray jsonarray = new JSONArray(response);
   			if(jsonarray!=null){
    			login = jsonarray.getString(1);
    			System.out.println("login: " + login);
    			tv.setText(login);
    			JSONArray jarrayPosts = jsonarray.getJSONArray(0);
    			for(int i=0; i<jarrayPosts.length(); i++){
    				JSONObject jso = jarrayPosts.getJSONObject(i);
   					postId = jso.getString("postId");
   					content = jso.getString("content");
   					photo = jso.getString("photo");
   					categoryId = jso.getString("categoryId");
   					addTime = jso.getString("addTime");
   					place = jso.getString("place");
   					eventTime = jso.getString("eventTime");
   					newpost = new postElement(postId, login, content, photo, categoryId, addTime, place, eventTime, faculties, coords);
   					ft = getFragmentManager().beginTransaction();
   					ft.add(R.id.content, newpost, "f1");
   					ft.commit();
    			}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        
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
        	
            return true;
        case R.id.konto:
      	  Intent intentkonto = new Intent(getApplicationContext(), OknoKonto.class);
      	  startActivity(intentkonto);
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
    }   
    //explist    
    //enxplist           

private class WebServiceTask extends AsyncTask<String, Integer, String> {
    public static final int LOG_TASK = 1;
    public static final int REGISTER_TASK = 2;  
    public static final int NEWS_TASK = 3;
    public static final int NEW_TASK = 4; 
    public static final int ACCOUNT_TASK = 5;
    private int taskType, number;
    Fragment newpost, newpost2;
    public int idItem;
    public String postId;
    public String userId;
    public String content;
    public String photo;
    public String postType;
    public String addTime;
    public String categoryId, count;
    public String place, token, tags, faculties;
    private static final String TAG = "WebServiceTask";
    private static final int CONN_TIMEOUT = 50000;        
    private static final int SOCKET_TIMEOUT = 50000;        
    private Context mContext = null;
    private String processMessage = "Processing...";
    public String login, password, email, role, url2, location, eventTime, tag;
    public String serwer = "";
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private ProgressDialog pDlg = null;
    
    // konstruktor do okna konto
    public WebServiceTask(int taskType, Context mContext, String token){
    	this.taskType = taskType;
        this.mContext = mContext;
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
        HttpResponse response = doResponse(url); 
        if (response == null) {
            return result;
        } else { 
            try {
                result = inputStreamToString(response.getEntity().getContent());
                System.out.println("result: " + result);
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getLocalizedMessage(), e); 
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {             
    	
        		pDlg.dismiss();
        		handleResponse(response);         
    }
     
    private HttpParams getHttpParams() {            
        HttpParams htpp = new BasicHttpParams();             
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);           
        return htpp;
    }
     
    private HttpResponse doResponse(String url) {   
    	serwer = mContext.getString(R.string.server);
        HttpClient httpclient = new DefaultHttpClient(getHttpParams());
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            switch (taskType) {
            	case ACCOUNT_TASK:
            		url2 = serwer + "/account2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            		JSONObject jsona = new JSONObject();
            		try{
            			HttpPost httpPost = new HttpPost(url2);
            			jsona.put("token", token);
            			StringEntity se = new StringEntity(jsona.toString());
            			httpPost.addHeader("Content-Type","application/json");
            			httpPost.setEntity(se);
            			response = httpClient.execute(httpPost);
            			if(response != null){
            				InputStream in = response.getEntity().getContent();
            			}
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            		HttpGet httpgetaccount = new HttpGet(url);
            		response = httpclient.execute(httpgetaccount);     
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
        BufferedReader rd = new BufferedReader(new InputStreamReader(is)); 
        try {
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        } 
        return total.toString();
    }
}

@Override
public void onBackPressed()
{
    super.onBackPressed(); 
    startActivity(new Intent(OknoKonto.this, OknoNews.class));
    finish();

}
    
}