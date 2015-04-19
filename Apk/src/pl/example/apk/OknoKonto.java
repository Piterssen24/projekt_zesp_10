package pl.example.apk;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.preference.PreferenceManager;
import android.widget.PopupMenu;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
    PopupMenu menu1, menu2;
    TextView list1, list2;
    Fragment newpost;
    Button editAccount, logOut;
    public String serwer = "";
    public String s1,s2;
    public static int[] repPostId, repUserId;
    public static String token;
	public static String login, myLogin;
	public String place, postId, content, photo, categoryId, addTime, eventTime;
	private static final String TAG = "OknoKonto";
	public TextView tv;
	public static String[] faculties, coords, favUserId, favCategoryId, tags, tagsId, folUserName;
	Context context;
	public String photou;
	public int itemId;
	Bitmap userPhoto;
	
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
   			tags = b.getStringArray("tags");
   			tagsId = b.getStringArray("tagsId");
   			favUserId = b.getStringArray("favUserId");
   			favCategoryId = b.getStringArray("favCategoryId");
   			repPostId = b.getIntArray("repPostId");
   			repUserId = b.getIntArray("repUserId");
   			folUserName = b.getStringArray("folUserName");
   			myLogin = b.getString("myLogin");
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
        logOut = (Button) findViewById(R.id.buttonLogOut);
        logOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OknoKonto.this); //Get the preferences
		        Editor edit = prefs.edit(); //Needed to edit the preferences
		        edit.putString("name", "");  //add a String
		        edit.putString("passwd", "");
		        edit.putBoolean("rememberCredentials", true); //add a boolean
		        edit.commit();  // save the edits. 
		        String sampleURL = serwer + "/logout";
		        WebServiceTask wst = new WebServiceTask(WebServiceTask.LOGOUT_TASK, OknoKonto.this, "Logging out...", myLogin);   
		        wst.execute(new String[] { sampleURL });  
				/*Intent intent = new Intent(getApplicationContext(), OknoGlowne.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
	        	startActivity(intent);*/
				
			}
		});
        editAccount = (Button) findViewById(R.id.buttonEdit);
        editAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), OknoEdytujProfil.class);
				intent.putExtra("photou", photou);
				intent.putExtra("favUserId", favUserId);
				intent.putExtra("favCategoryId", favCategoryId);
				intent.putExtra("tags", tags);
				intent.putExtra("token", token);
				intent.putExtra("tagsId", tagsId);
            	startActivity(intent);
				
			}
		});
        // existing height is ok as is, no need to edit it
        //yourPicture.setLayoutParams(params);       
/*
        expListView = (ExpandableListView) findViewById(R.id.expandableList); 
        // preparing list data
        // Adding child data
        listDataHeader = new ArrayList<String>();
        listDataHeader.add("Typy news'ów");
        listDataHeader.add("Obserwowani dziennikarze");
        listDataHeader.add("Obserwujący użytkownicy");
 
        // Adding child data
        List<String> typy = new ArrayList<String>();
        typy.add("Sport");
        typy.add("Kultura");
        typy.add("Utrudnienia na drodze");
        typy.add("Polityczne");
        typy.add("Charytatywne");
        typy.add("Imprezy");
        typy.add("12 Angry Men");
 
        List<String> obserwowani = new ArrayList<String>();
        obserwowani.add("The Conjuring");
        obserwowani.add("Despicable Me 2");
        obserwowani.add("Turbo");
        obserwowani.add("Grown Ups 2");
        obserwowani.add("Red 2");
        obserwowani.add("The Wolverine");
 
        List<String> obserwujacy = new ArrayList<String>();
        obserwujacy.add("2 Guns");
        obserwujacy.add("The Smurfs 2");
        obserwujacy.add("The Spectacular Now");
        obserwujacy.add("The Canyons");
        obserwujacy.add("Europa Report");
 
        listDataChild = new HashMap<String, List<String>>();
        listDataChild.put(listDataHeader.get(0), typy); // Header, Child data
        listDataChild.put(listDataHeader.get(1), obserwowani);
        listDataChild.put(listDataHeader.get(2), obserwujacy);
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
     
        //endofexpandablelist*/
        
        yourPicture.setLayoutParams(params);
        
        list1 = (TextView) findViewById(R.id.popup1);
        list2 = (TextView) findViewById(R.id.popup2);
        
        //dodanie do tekstu liczbu wybranych tag�w i ulubionych u�ytkownik�w - zsumowa� z bazy
        String s11,s22;
        s1=list1.getText().toString();
        int liczbatagow=favCategoryId.length, liczbaulubionych=folUserName.length;
        //int liczbatagow=5, liczbaulubionych=6;
        s11 = s1 + " ("+liczbatagow+")";
        list1.setText(s11);
        
        s2=list2.getText().toString();
        s22 = s2 + " ("+liczbaulubionych+")";
        list2.setText(s22);
        
        list1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu1 = new PopupMenu(OknoKonto.this, v);
				//dodanie kolejnych nazw tag�w
				for(int i=0; i<favCategoryId.length;i++){
					for(int j=0; j<tags.length; j++){
						if(favCategoryId[i].equals(tagsId[j])){
							menu1.getMenu().add(tags[j].toString());
						}
					}
				}
			/*	//dodanie kolejnych nazw tagów
				menu1.getMenu().add("sport");
				menu1.getMenu().add("konkurs");
				menu1.getMenu().add("impreza");
				menu1.getMenu().add("koncert");
				menu1.getMenu().add("wykład");*/
				menu1.show();
				
			}
		});
        
        list2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				menu2 = new PopupMenu(OknoKonto.this, v);
				for(int i=0; i<folUserName.length;i++){
					menu2.getMenu().add(0,i,0,folUserName[i] + "     usuń");
				}
				//dodanie kolejnych nazw u�ytkownik�w
				/*menu2.getMenu().add(0,0,0,"Julka                     usuń");
				menu2.getMenu().add(0,1,0,"Dominika              usuń");
				menu2.getMenu().add(0,2,0,"Sebastian             usuń");
				menu2.getMenu().add(0,3,0,"Piotr                      usuń");
				menu2.getMenu().add(0,4,0,"Bartek                   usuń");
				menu2.getMenu().add(0,5,0,"Lamia                 usuń");
				menu2.getMenu().add(0,6,0,"Julka                 usuń");
				menu2.getMenu().add(0,7,0,"Dominika              usuń");
				menu2.getMenu().add(0,8,0,"Sebastian             usuń");
				menu2.getMenu().add(0,9,0,"Piotr                 usuń");
				menu2.getMenu().add(0,10,0,"Bartek                usuń");
				menu2.getMenu().add(0,11,0,"Lamia                 usuń");*/
				menu2.show();
				menu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
		             public boolean onMenuItemClick(MenuItem item) { 
		            	 itemId = item.getItemId();
		            	 System.out.println("item: " + itemId);
		            	 String sampleURL = serwer + "/stopFollow";
				        WebServiceTask wst = new WebServiceTask(WebServiceTask.STOPFOLLOW_TASK, "Trwa usuwanie użytkownika z listy ulubionych użytkowników...", folUserName[item.getItemId()], token, OknoKonto.this);   
				        wst.execute(new String[] { sampleURL });
		            //  Toast.makeText(OknoKonto.this,"Usunięto : " + item.getItemId(),Toast.LENGTH_SHORT).show(); 
		              return true;  
		             }  
		            });  
				
			}
		});
        
        String sampleURL = serwer + "/account";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.ACCOUNT_TASK, this, token);   
   		wst.execute(new String[] { sampleURL }); 
    }  
        
    /**
     * metoda przyjmuje jako parametr string, który zawiera odpowiedź od serwera
     * i tworzy z niego obiekt JSON, który następnie jest parsowany. Tworzy obiekty postElement,
     * które są wyswietlane na stronie z newsami.
     */
    public void handleResponse(String response) { 
    	try {
   			FragmentTransaction ft = null;
   			JSONArray jsonarray = new JSONArray(response);
   			if(jsonarray!=null){
    			login = jsonarray.getString(1);
    			tv.setText(login);
    			JSONArray jarrayPosts = jsonarray.getJSONArray(0);
    			photou = jsonarray.getString(2);
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
   					newpost = new postElement(token, postId, login, content, photo, categoryId, addTime, place, eventTime, faculties, coords, "Konto", repPostId, repUserId, folUserName, myLogin);
   					ft = getFragmentManager().beginTransaction();
   					ft.add(R.id.content, newpost, "f1");
   					ft.commit();
    			}
   			}
   		} catch (Exception e) {
   			Log.e(TAG, e.getLocalizedMessage(), e);
   		}
    }
    
    public void handleResponseLOGOUT(String response){
    	if(response.equals("TRUE")){
    		Intent intent = new Intent(getApplicationContext(), OknoGlowne.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        	startActivity(intent);
    	} else {
    		Toast.makeText(this, "Wystąpił błąd podczas wykonywania żądanej operacji!", Toast.LENGTH_LONG).show();
    	}
    }
    
    public void handleResponseSTOPFOLLOW(String response){
    	if(response.equals("OK")){
    		String s22;
    		int followcount = folUserName.length-1;
            s22 = s2 + " (" + followcount + ")";
            list2.setText(s22);
    		menu2.getMenu().removeItem(itemId);
    		List<String> list1 = new ArrayList<String>();
   		 	for(int i=0; i<folUserName.length; i++){
   		 		list1.add(folUserName[i]);
   		 	}
   		 	list1.remove(itemId);
   		 	String[] tmp = new String[list1.size()];
   		 	for(int i=0; i<list1.size(); i++){
   		 		tmp[i] = list1.get(i);
   		 	}
   		 	folUserName = tmp;
            menu2.show();
    		Toast.makeText(this, "Pomyślnie usunięto użytkownika z listy obserwowanych!", Toast.LENGTH_LONG).show();
    	} else {
    		Toast.makeText(this, "Wystąpił błąd podczas wykonywania żądanej operacji!", Toast.LENGTH_LONG).show();
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
      	 // 	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

  /*  @Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        startActivity(new Intent(OknoKonto.this, OknoNews.class));
        finish();

    }*/
    
    /**
     * klasa wewnętrzna, która wykonuje asynchroniczne działanie w tle.
     */
private class WebServiceTask extends AsyncTask<String, Integer, String> {
    public static final int ACCOUNT_TASK = 1;
    public static final int STOPFOLLOW_TASK = 2;
    public static final int LOGOUT_TASK = 3;
    private int taskType;
    public String userLogin;
    static final String TAG = "WebServiceTask";
    private static final int CONN_TIMEOUT = 100000;        
    private static final int SOCKET_TIMEOUT = 100000;        
    private Context mContext = null;
    private String processMessage = "Processing...";
    public String url2, token;
    public String serwer = "";
   // private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private ProgressDialog pDlg = null;
    
    // konstruktor do okna konto
    public WebServiceTask(int taskType, Context mContext, String token){
    	this.taskType = taskType;
        this.mContext = mContext;
        this.token = token;
    }
    
    public WebServiceTask(int taskType, Context mContext, String processMessage, String userLogin){
    	this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.userLogin = userLogin;
    }
    
    public WebServiceTask(int taskType, String processMessage, String userLogin, String token, Context mContext) {
        this.taskType = taskType;
        this.mContext = mContext;
        this.processMessage = processMessage;
        this.userLogin = userLogin;
        this.token = token;
    }

   /* public void addNameValuePair(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }*/

    /**
     * metoda , która tworzy i wyświetla obiekt progress bar.
     */
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

    /**
     * główna metoda wykonująca działanie w tle.
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
    	switch (taskType){
    	case ACCOUNT_TASK:
        		pDlg.dismiss();
        		handleResponse(response);
        		break;
    	case STOPFOLLOW_TASK:
    		pDlg.dismiss();
    		handleResponseSTOPFOLLOW(response);
    		break;
    	case LOGOUT_TASK:
    		pDlg.dismiss();
    		handleResponseLOGOUT(response);
    		break;
    	}
        		
    }
     
    /**
     * metoda ustawiająca parametry połączenia http.
     */
    private HttpParams getHttpParams() {            
        HttpParams htpp = new BasicHttpParams();             
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);           
        return htpp;
    }
     
    /**
     * metoda wysyłająca oraz odbierająca dane od web serwisu.
     */
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
            			/*if(response != null){
            				response.getEntity().getContent();
            			}*/
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            		HttpGet httpgetaccount = new HttpGet(url);
            		response = httpclient.execute(httpgetaccount);     
            		break;
            	case LOGOUT_TASK: 
            		url2 = serwer + "/logout2";
            		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
            		JSONObject jsonl = new JSONObject();
            		try{
            			HttpPost httpPost = new HttpPost(url2);
            			jsonl.put("userLogin", userLogin);
            			StringEntity se = new StringEntity(jsonl.toString(), "UTF-8");
            			httpPost.addHeader("Content-Type","application/json");
            			httpPost.setEntity(se);
            			response = httpClient.execute(httpPost);					
            			/*if(response != null){
            				response.getEntity().getContent();
            			}*/
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            		HttpGet httpgetlogin = new HttpGet(url);
            		response = httpclient.execute(httpgetlogin);
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
            			/*if(response != null){
            				InputStream in = response.getEntity().getContent();
            			}*/
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
     * metoda konwertująca odpowiedź serwera na String.
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
 * metoda konwertująca String na obiekt bitmap (zdjęcie).
 */
public static Bitmap decodeBase64(String input) 
{
    byte[] decodedByte;
    decodedByte = Base64.decode(input, Base64.URL_SAFE);
    Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    decodedByte = null;
    return b;
}

@Override
public void onBackPressed()
{
    super.onBackPressed(); 
    startActivity(new Intent(OknoKonto.this, OknoNews.class));
    finish();
}

}