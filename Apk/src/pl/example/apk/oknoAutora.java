package pl.example.apk;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import com.google.android.gms.internal.ex;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class oknoAutora extends Activity {

	TextView postsNumber,postsNumberDeleted,userRate, author;
	ImageView yourPicture;
	public static int[] repPostId, repUserId;
	public String serwer = "";
	public String userLogin, myLogin;
	Fragment newpost, newpost2;
	public String removedPostsCount;
	public String place, postId, content, photo, categoryId, addTime, eventTime;
	private static final String TAG = "OknoAutora";
	Context context;
	public static String[] faculties, coords, folUserName;
	public String photou;
	Bitmap userPhoto;
	public static String token;
	Button likeOrNot;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknoautora_layout);
        context = getApplicationContext();
        serwer = getResources().getString(R.string.server);
        Bundle extras= getIntent().getExtras();
        if(extras!=null){
           userLogin = extras.getString("userLogin"); 
           faculties = extras.getStringArray("faculties");
           coords = extras.getStringArray("coords");
           token = extras.getString("token");
           repPostId = extras.getIntArray("repPostId");
           repUserId = extras.getIntArray("repUserId");
           folUserName = extras.getStringArray("folUserName");
           myLogin = extras.getString("myLogin");
        }
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - " + userLogin);
        
        postsNumber = (TextView) findViewById(R.id.textViewPostCounter);
        //postsNumberDeleted = (TextView) findViewById(R.id.textViewPostsCounterDeleted);
        likeOrNot = (Button) findViewById(R.id.buttonLike);
        author = (TextView) findViewById(R.id.textViewAuthorName);
        yourPicture = (ImageView) findViewById(R.id.userAuthorPhoto);
        
        List<String> list1 = new ArrayList<String>();
		 for(int i=0; i<folUserName.length; i++){
			 list1.add(folUserName[i]);
		 }
		 if(myLogin.equals(userLogin)){
			 likeOrNot.setVisibility(View.GONE);
		 } else{
			 likeOrNot.setVisibility(View.VISIBLE);
			 if(list1.contains(userLogin)){
				 likeOrNot.setText("Usuñ z obserwowanych");
			 } else {
				 likeOrNot.setText("Obserwuj");
			 }
		 }
        
        likeOrNot.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(likeOrNot.getText().toString().equals("Obserwuj")){
					String sampleURL = serwer + "/Follow";
			        WebServiceTask wst = new WebServiceTask(WebServiceTask.FOLLOW_TASK, "Trwa dodawanie u¿ytkownika do listy ulubionych u¿ytkowników...", userLogin, token, oknoAutora.this);   
			        wst.execute(new String[] { sampleURL });
				} else if(likeOrNot.getText().toString().equals("Usuñ z obserwowanych")){
					String sampleURL = serwer + "/stopFollow";
			        WebServiceTask wst = new WebServiceTask(WebServiceTask.STOPFOLLOW_TASK, "Trwa usuwanie u¿ytkownika z listy ulubionych u¿ytkowników...", userLogin, token, oknoAutora.this);   
			        wst.execute(new String[] { sampleURL });
				}
			}
		});
        
        author.setText(userLogin);
        //postsNumber.setText("10");
        //postsNumberDeleted.setText("0");
        //userRate.setText("5,0");
        
        String sampleURL = serwer + "/author";
        WebServiceTask wst = new WebServiceTask(WebServiceTask.AUTHOR_TASK, this, "Pobieranie informacji o u¿ytkowniku...", userLogin);   
        wst.execute(new String[] { sampleURL });
        
       
    }
    
    /**
     * metoda przyjmuje jako parametr string, który zawiera odpowiedŸ od serwera
     * i tworzy z niego obiekt JSON, który nastêpnie jest parsowany. Tworzy obiekty postElement,
     * które s¹ wyswietlane na stronie z newsami.
     */
    public void handleResponse(String response) { 
    	try {
   			FragmentTransaction ft = null;
   			JSONArray jarray = new JSONArray(response);
   			if(jarray!=null){
    			JSONArray jarrayPosts = jarray.getJSONArray(0);
    			String postsCount = jarray.getString(1);
    			photou = jarray.getString(2);
    			postsNumber.setText(postsCount);
    			if(userPhoto!=null)
    			{
    	        	 userPhoto.recycle();
    	        	 userPhoto = null;
    	        }
    	        userPhoto = decodeBase64(photou);
    	        yourPicture.setImageBitmap(userPhoto);
    			for(int i=0; i<jarrayPosts.length(); i++){
    				JSONObject jso = jarrayPosts.getJSONObject(i);
   					postId = jso.getString("postId");
   					content = jso.getString("content");
   					photo = jso.getString("photo");
   					categoryId = jso.getString("categoryId");
   					addTime = jso.getString("addTime");
   					place = jso.getString("place");
   					eventTime = jso.getString("eventTime");
   					newpost = new postElement(token, postId, userLogin, content, photo, categoryId, addTime, place, eventTime, faculties, coords, "Autora", repPostId, repUserId, folUserName, myLogin);
   					ft = getFragmentManager().beginTransaction();
   					ft.add(R.id.content, newpost, "f1");
   					ft.commit();
    			}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
    }
    
    public void handleResponseFOL(String response){
    	if(response.equals("OK")){
    		likeOrNot.setText("Usuñ z obserwowanych");
    	} else {
    		Toast.makeText(this, "Wyst¹pi³ b³¹d podczas wykonywania ¿¹danej operacji!", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void handleResponseSTOPFOL(String response){
    	if(response.equals("OK")){
	        likeOrNot.setText("Obserwuj");
    	} else {
    		Toast.makeText(this, "Wyst¹pi³ b³¹d podczas wykonywania ¿¹danej operacji!", Toast.LENGTH_LONG).show();
    	}
    }
    
    /**
     * klasa wewnêtrzna, która wykonuje asynchroniczne dzia³anie w tle.
     */
    private class WebServiceTask extends AsyncTask<String, Integer, String> {
        public static final int LOG_TASK = 1;
        public static final int REGISTER_TASK = 2;  
        public static final int NEWS_TASK = 3;
        public static final int NEW_TASK = 4; 
        public static final int ACCOUNT_TASK = 5;
        public static final int AUTHOR_TASK = 6;
        public static final int FOLLOW_TASK = 7;
        public static final int STOPFOLLOW_TASK = 8;
        private int taskType, number;
        private static final String TAG = "WebServiceTask";
        private static final int CONN_TIMEOUT = 50000;        
        private static final int SOCKET_TIMEOUT = 50000;        
        private Context mContext = null;
        private String processMessage = "Processing...";
        public String login, password, email, role, url2, location, eventTime, tag, userLogin;
        public String serwer = "";
        private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        private ProgressDialog pDlg = null;
        public String [] coords, fac, tagName, tagId;
        public String token;
        

        public WebServiceTask(int taskType, Context mContext, String processMessage, String userLogin){
        	this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
            this.userLogin = userLogin;
        }
        
      //konstruktor do follow i stopfollow
        public WebServiceTask(int taskType, String processMessage, String userLogin, String token, Context mContext) {
            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
            this.userLogin = userLogin;
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
            HttpResponse response = doResponse(url);
            if (response == null) {
                return result;
            } else { 
                try {
                    result = inputStreamToString(response.getEntity().getContent());
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
        	switch (taskType) {
        		case AUTHOR_TASK:
        			pDlg.dismiss(); 
        			handleResponse(response);
        			break;
        		case FOLLOW_TASK:
        			pDlg.dismiss(); 
        			handleResponseFOL(response);
        			break;
        		case STOPFOLLOW_TASK:
        			pDlg.dismiss(); 
        			handleResponseSTOPFOL(response);
        			break;
        	}
        }
         
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
        	serwer = mContext.getString(R.string.server);
            HttpClient httpclient = new DefaultHttpClient(getHttpParams());
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;
            try {
                switch (taskType) {
                	case AUTHOR_TASK:
                		url2 = serwer + "/author2";
                		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                		JSONObject jsonau = new JSONObject();
                		try{
                			HttpPost httpPost = new HttpPost(url2);
                			jsonau.put("userLogin", userLogin);
                			StringEntity se = new StringEntity(jsonau.toString(), "UTF-8");
                			httpPost.addHeader("Content-Type","application/json");
                			httpPost.setEntity(se);
                			response = httpClient.execute(httpPost);
                			if(response != null){
                				InputStream in = response.getEntity().getContent();
                			}
                		}catch(Exception e){
                			e.printStackTrace();
                		}
                		HttpGet httpgetauthor = new HttpGet(url);
                		response = httpclient.execute(httpgetauthor);               
                		break;
                	case FOLLOW_TASK:
                		url2 = serwer + "/Follow2";
                		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                		JSONObject jsonf = new JSONObject();
                		try{
                			HttpPost httpPost = new HttpPost(url2);
                			jsonf.put("token", token);
                			jsonf.put("userLogin", userLogin);
                			StringEntity se = new StringEntity(jsonf.toString(), "UTF-8");
                			httpPost.addHeader("Content-Type","application/json");
                			httpPost.setEntity(se);
                			response = httpClient.execute(httpPost);
                			if(response != null){
                				InputStream in = response.getEntity().getContent();
                			}
                		}catch(Exception e){
                			e.printStackTrace();
                		}
                		HttpGet httpgetfollow = new HttpGet(url);
                		response = httpclient.execute(httpgetfollow);               
                		break;
                	case STOPFOLLOW_TASK:
                		url2 = serwer + "/stopFollow2";
                		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                		JSONObject jsonsf = new JSONObject();
                		try{
                			HttpPost httpPost = new HttpPost(url2);
                			jsonsf.put("token", token);
                			jsonsf.put("userLogin", userLogin);
                			StringEntity se = new StringEntity(jsonsf.toString(), "UTF-8");
                			httpPost.addHeader("Content-Type","application/json");
                			httpPost.setEntity(se);
                			response = httpClient.execute(httpPost);
                			if(response != null){
                				InputStream in = response.getEntity().getContent();
                			}
                		}catch(Exception e){
                			e.printStackTrace();
                		}
                		HttpGet httpgetstopfollow = new HttpGet(url);
                		response = httpclient.execute(httpgetstopfollow);               
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
    
    /**
     * metoda konwertuj¹ca String na obiekt bitmap (zdjêcie).
     */
    public static Bitmap decodeBase64(String input) 
    {
        byte[] decodedByte;
        decodedByte = Base64.decode(input, Base64.URL_SAFE);
        Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        decodedByte = null;
        return b;
    }
}