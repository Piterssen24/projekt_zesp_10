package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import pl.example.apk.*;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class WebServiceTask extends AsyncTask<String, Integer, String> {
    public static final int LOG_TASK = 1;
    public static final int REGISTER_TASK = 2;  
    public static final int NEWS_TASK = 3;
    public static final int NEW_TASK = 4; 
    public static final int ACCOUNT_TASK = 5;
    public static final int AUTHOR_TASK = 6;
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
    private static final int CONN_TIMEOUT = 5000;        
    private static final int SOCKET_TIMEOUT = 5000;        
    private Context mContext = null;
    private String processMessage = "Processing...";
    public String login, password, email, role, url2, location, eventTime, tag, userLogin;
    public String serwer = "";
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private ProgressDialog pDlg = null;
    public String [] coords, fac, tagName, tagId;
    public static int id = 0; 
  	public static int id2 = 0;
  	public static int max = 0;
    
  	
  	// konstruktor do OknoLog
    public WebServiceTask(int taskType, Context mContext, String processMessage, String login, String password) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.login = login;
        this.password = password;
    }
    
    // konstruktor do OknoRejestracja
    public WebServiceTask(int taskType, Context mContext, String processMessage, String login, String password, String email, String role) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    
    // konstruktor do OknoNews
    public WebServiceTask(int taskType, Context mContext, String processMessage, int number) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.number = number;
    }
    
    // konstruktor do OknoNew
    public WebServiceTask(int taskType, Context mContext, String processMessage, String content, String photo, String addTime, String place, String eventTime, String tag, String token) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.content = content;
        this.photo = photo;
        this.addTime = addTime;
        this.place = place;
        this.eventTime = eventTime;
        this.tag = tag;
        this.token = token;
    }
    
    // konstruktor do OknoKonto
    public WebServiceTask(int taskType, Context mContext, String token){
    	this.taskType = taskType;
        this.mContext = mContext;
        this.token = token;
    }
    
    // konstruktor do oknoAutora
    public WebServiceTask(int taskType, Context mContext, String processMessage, String userLogin){
    	this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.userLogin = userLogin;
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
        	case LOG_TASK:
        		pDlg.dismiss(); 
        		handleResponseLOG(response);
        		break;
        	case REGISTER_TASK:
        		pDlg.dismiss(); 
        		handleResponseREGISTER(response);
        		break;
        	case NEWS_TASK:
        		pDlg.dismiss(); 
        		OknoNews oknonews = new OknoNews();
        		oknonews.handleResponse(response);
        		break;
        	case NEW_TASK:
        		pDlg.dismiss(); 
        		handleResponseNEW(response);
        		break;
        	case ACCOUNT_TASK:
        		pDlg.dismiss();
        		OknoKonto ok = new OknoKonto();
        		ok.handleResponse(response);
        	case AUTHOR_TASK:
        		pDlg.dismiss();
        		oknoAutora oa = new oknoAutora();
        		oa.handleResponse(response);
    	}
                  
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
            	case REGISTER_TASK:
            		url2 = serwer + "/register2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            		JSONObject jsonr = new JSONObject();
            		try{
            			HttpPost httpPost = new HttpPost(url2);
            			jsonr.put("login", login);
            			jsonr.put("password", password);
            			jsonr.put("email", email);
            			jsonr.put("role", role);
            			StringEntity se = new StringEntity(jsonr.toString(), "UTF-8");
            			httpPost.addHeader("Content-Type","application/json");
            			httpPost.setEntity(se);
            			response = httpClient.execute(httpPost);
            			if(response != null){
            				InputStream in = response.getEntity().getContent();
            			}
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            		HttpGet httpgetregister = new HttpGet(url);
            		response = httpclient.execute(httpgetregister);               
            		break;
            	case LOG_TASK: 
            		url2 = serwer + "/login2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            		JSONObject jsonl = new JSONObject();
            		try{
            			HttpPost httpPost = new HttpPost(url2);
            			jsonl.put("login", login);
            			jsonl.put("password", password);
            			StringEntity se = new StringEntity(jsonl.toString(), "UTF-8");
            			httpPost.addHeader("Content-Type","application/json");
            			httpPost.setEntity(se);
            			response = httpClient.execute(httpPost);					
            			if(response != null){
            				InputStream in = response.getEntity().getContent();
            			}
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            		HttpGet httpgetlogin = new HttpGet(url);
            		response = httpclient.execute(httpgetlogin);
            		System.out.println("response w doResponse: " + response);
            		break;
            	case NEWS_TASK: 
            		url2 = serwer + "/news2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                    JSONObject jsonn = new JSONObject();
                    try{
      					HttpPost httpPost = new HttpPost(url2);
      					jsonn.put("id",number);
      					StringEntity se = new StringEntity(jsonn.toString(), "UTF-8");
      					httpPost.addHeader("Content-Type","application/json");
      					httpPost.setEntity(se);
      					response = httpClient.execute(httpPost);     					
      					if(response != null){
      						InputStream in = response.getEntity().getContent();
      					}
                    }catch(Exception e){
      					e.printStackTrace();
      				}
                    HttpGet httpgetnews = new HttpGet(url);
                    response = httpclient.execute(httpgetnews);               
                    break;
            	case NEW_TASK: 
            		url2 = serwer + "/post2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            		JSONObject json = new JSONObject();
            		try{
            			HttpPost httpPost = new HttpPost(url2);
            			json.put("token", token);  
            			json.put("content", content);
            			json.put("photo",photo);
            			json.put("addTime", addTime);  
            			json.put("place", place);
            			json.put("eventTime", eventTime);
            			json.put("tag", tag);
            			StringEntity se = new StringEntity(json.toString(), "UTF-8");
            			httpPost.addHeader("Content-Type","application/json");
            			httpPost.setEntity(se);
            			response = httpClient.execute(httpPost);				
            			if(response != null){
            				InputStream in = response.getEntity().getContent();
            			}	
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            		HttpGet httpgetnew = new HttpGet(url);
            		response = httpclient.execute(httpgetnew);  
            		break;
            	case ACCOUNT_TASK:
            		url2 = serwer + "/account2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            		JSONObject jsona = new JSONObject();
            		try{
            			HttpPost httpPost = new HttpPost(url2);
            			jsona.put("token", token);
            			StringEntity se = new StringEntity(jsona.toString(), "UTF-8");
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
    
    public final void handleResponseLOG(String response) {  
    	try {
    		JSONArray jsonarray = new JSONArray(response);
    		if(jsonarray!=null){
    			token = jsonarray.getString(0);
    			role = jsonarray.getString(1);
    			if(token.equals("")){
    				Toast.makeText(mContext, "B³êdny login lub has³o!", Toast.LENGTH_LONG).show();
    			} else {
    				JSONArray jarraytag = jsonarray.getJSONArray(2);
    				JSONArray jarrayfac = jsonarray.getJSONArray(3);
    			
    				tagId = new String[jarraytag.length()];
    				for(int i=0; i<jarraytag.length(); i++) {
    					JSONObject jo = jarraytag.getJSONObject(i);
    					tagId[i] = jo.getString("categoryId");
    				}
    			
    				tagName = new String[jarraytag.length()];
    				for(int i=0; i<jarraytag.length(); i++) {
    					JSONObject jo = jarraytag.getJSONObject(i);
    					tagName[i] = jo.getString("tag");
    				}

    				fac = new String[jarrayfac.length()];
    				for(int i=0; i<jarrayfac.length(); i++) {
    					JSONObject jo = jarrayfac.getJSONObject(i);
    					fac[i] = jo.getString("facultyName");
    				}
    			
    				coords = new String[jarrayfac.length()];
    				for(int i=0; i<jarrayfac.length(); i++) {
    					JSONObject jo = jarrayfac.getJSONObject(i);
    					coords[i] = jo.getString("coordinates");
    				}
    			
    				Toast.makeText(mContext, "Zalogowano!", Toast.LENGTH_LONG).show();
    				Intent in = new Intent(mContext, OknoNews.class);
    				in.putExtra("token",token);
    				in.putExtra("role", role);
    				in.putExtra("tagsId", tagId);
    				in.putExtra("tags", tagName);
    				in.putExtra("faculties", fac);
    				in.putExtra("coords", coords);
    				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
    				mContext.startActivity(in);
    			}
    		}
         } catch (Exception e) {
              Log.e(TAG, e.getLocalizedMessage(), e);
         }
    }
    
    public void handleResponseREGISTER(String response) {   
        if(response.equals("TRUE")){
        	Toast.makeText(mContext, "Rejestracja przebieg³a pomyœlnie! Za chwilê zostaniesz przeniesiony na stronê logowania", Toast.LENGTH_LONG).show();
        	Intent i = new Intent(mContext,OknoGlowne.class);
        	mContext.startActivity(i);
        } else if(response.equals("LOGINISTNIEJE")) {
        	Toast.makeText(mContext, "Login jest ju¿ zajêty!", Toast.LENGTH_LONG).show();
        } else if(response.equals("EMAILISTNIEJE")){
        	Toast.makeText(mContext, "Ten adres email jest ju¿ zarejestrowany!", Toast.LENGTH_LONG).show();
        } else {
        	Toast.makeText(mContext, "Nie mo¿na dokonaæ rejestracji!", Toast.LENGTH_LONG).show();
        }
    }
    
    public void handleResponseNEW(String response) {   
        if(response.equals("TRUE")){
        	Toast.makeText(mContext, "Pomyslnie dodano posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(mContext,OknoNews.class);
        	//i.putExtra("Token",response);
        	mContext.startActivity(i);   	
        } else {
        	Toast.makeText(mContext, "B³¹d! Nie uda³o siê dodaæ posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(mContext,OknoNews.class);
        	//i.putExtra("Token",response);
        	mContext.startActivity(i);
        }
    }
}