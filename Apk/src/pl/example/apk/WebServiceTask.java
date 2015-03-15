package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import pl.example.apk.*;
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
import android.app.FragmentManager;

import android.app.Fragment;
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
    private int taskType, number;
    Fragment newpost, newpost2;
    public int idItem;
    public String postId;
    public String userId;
    public String content;
    public String photo;
    public String postType;
    public String addTime;
    public String place, token, tags, faculties;
    private static final String TAG = "WebServiceTask";
    private static final int CONN_TIMEOUT = 5000;        
    private static final int SOCKET_TIMEOUT = 5000;        
    private Context mContext = null;
    private String processMessage = "Processing...";
    public String login, password, email, role, url2, location, eventTime, tag;
    public String serwer = "";
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private ProgressDialog pDlg = null;
    public String [] coords, fac, tagName, tagId;
    
    public WebServiceTask(int taskType, Context mContext, String processMessage, String login, String password) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.login = login;
        this.password = password;
    }
    
    public WebServiceTask(int taskType, Context mContext, String processMessage, String login, String password, String email, String role) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
    }
    
    public WebServiceTask(int taskType, Context mContext, String processMessage, int number) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.number = number;
    }
    
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
        		//OknoNew oknonew = new OknoNew();
        		//oknonew.handleResponse(response);
        		break;
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
            			StringEntity se = new StringEntity(jsonr.toString());
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
            			StringEntity se = new StringEntity(jsonl.toString());
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
            		break;
            	case NEWS_TASK: 
            		url2 = serwer + "/news2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
                    JSONObject jsonn = new JSONObject();
                    try{
      					HttpPost httpPost = new HttpPost(url2);
      					jsonn.put("id",number);
      					StringEntity se = new StringEntity(jsonn.toString());
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
            			//json.put("login", login);  
            			json.put("content", content);
            			json.put("photo",photo);
            			json.put("addTime", addTime);  
            			json.put("place", place);
            			json.put("eventTime", eventTime);
            			json.put("tag", tag);
            			StringEntity se = new StringEntity(json.toString());
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
    			if(jsonarray.getString(0).equals("")){
    				Toast.makeText(mContext, "B��dny login lub has�o!", Toast.LENGTH_LONG).show();
    			} else {
    				JSONArray jarraytag = jsonarray.getJSONArray(1);
    				JSONArray jarrayfac = jsonarray.getJSONArray(2);
    			
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
    				Intent in = new Intent(mContext,OknoNews.class);
    				//	in.putExtra("Token",response);
    				in.putExtra("tagsId", tagId);
    				in.putExtra("tags", tagName);
    				in.putExtra("faculties", fac);
    				in.putExtra("coords", coords);
    				mContext.startActivity(in);
    			}
    		}
         } catch (Exception e) {
              Log.e(TAG, e.getLocalizedMessage(), e);
         }
    }
    
    public void handleResponseREGISTER(String response) {   
        if(response.equals("TRUE")){
        	Toast.makeText(mContext, "Rejestracja przebieg�a pomy�lnie! Za chwil� zostaniesz przeniesiony na stron� logowania", Toast.LENGTH_LONG).show();
        	Intent i = new Intent(mContext,OknoGlowne.class);
        	mContext.startActivity(i);
        } else if(response.equals("LOGINISTNIEJE")) {
        	Toast.makeText(mContext, "Login jest ju� zaj�ty!", Toast.LENGTH_LONG).show();
        } else if(response.equals("EMAILISTNIEJE")){
        	Toast.makeText(mContext, "Ten adres email jest ju� zarejestrowany!", Toast.LENGTH_LONG).show();
        } else {
        	Toast.makeText(mContext, "Nie mo�na dokona� rejestracji!", Toast.LENGTH_LONG).show();
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
        	Toast.makeText(mContext, "B��d! Nie uda�o si� doda� posta!", Toast.LENGTH_LONG).show();
        	android.os.SystemClock.sleep(2000);
        	Intent i = new Intent(mContext,OknoNews.class);
        	//i.putExtra("Token",response);
        	mContext.startActivity(i);
        }
    }
}