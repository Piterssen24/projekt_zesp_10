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
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;

public class OknoKonto extends Activity {
	
	ImageView yourPicture;
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
    public static String token, role;
	public static String login, myLogin;
	public String place, postId, content, photo, categoryId, addTime, eventTime;
	private static final String TAG = "OknoKonto";
	public TextView tv;
	public static String[] faculties, coords, favUserId, favCategoryId, tags, tagsId, folUserName;
	Context context;
	public String photou;
	public int itemId, screenTest;
	Bitmap userPhoto;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknokonto_layout);
        context = getApplicationContext();
        serwer = getResources().getString(R.string.server);
        
        getExtras();
        getScreenType();
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - Twoje konto");
        bar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        tv = (TextView) findViewById(R.id.textViewUserName);
        yourPicture = (ImageView) findViewById(R.id.userPhoto);
        LayoutParams params = (LayoutParams) yourPicture.getLayoutParams();
        if(screenTest==0)
        {
        	params.width = 130;
            params.height = 130;
        }
        else
        {
        	params.width = 400;
            params.height = 400;
        }
        
        yourPicture.setLayoutParams(params);
        yourPicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				pictureFullScreen();			
			}
		});
        
        logOut = (Button) findViewById(R.id.buttonLogOut);
        logOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logOut();				
			}
		});
        
        editAccount = (Button) findViewById(R.id.buttonEdit);
        editAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentEditProfile();				
			}
		});
       
        list1 = (TextView) findViewById(R.id.popup1);
        list2 = (TextView) findViewById(R.id.popup2);
        
        String s11,s22;
        s1=list1.getText().toString();
        int liczbatagow=favCategoryId.length, liczbaulubionych=folUserName.length;
        s11 = s1 + " ("+liczbatagow+")";
        list1.setText(s11);
        
        s2=list2.getText().toString();
        s22 = s2 + " ("+liczbaulubionych+")";
        list2.setText(s22);
        
        list1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getFavTags(v);			
			}
		});
        
        list2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				favUsers(v);
				
			}
		});
        
        String sampleURL = serwer + "/account";
   		WebServiceTask wst = new WebServiceTask(WebServiceTask.ACCOUNT_TASK, "£adowanie profilu...", this, token);   
   		wst.execute(new String[] { sampleURL }); 
    }  
      
    
    
    /**
     * metoda przyjmuje jako parametr string, kt√≥ry zawiera odpowied≈∫ od serwera
     * i tworzy z niego obiekt JSON, kt√≥ry nastƒôpnie jest parsowany. Tworzy obiekty postElement,
     * kt√≥re sƒÖ wyswietlane na stronie z newsami.
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
   					newpost = new postElement(token, postId, login, content, photo, categoryId, addTime, place, eventTime, faculties, coords, "Konto", repPostId, repUserId, folUserName, myLogin, screenTest);
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
    		Toast.makeText(this, "Wystπpi≥Ç b≥πd podczas wykonywania øπdanej operacji!", Toast.LENGTH_LONG).show();
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
    		Toast.makeText(this, "Pomyúlnie usuniÍto uøytkownika z listy obserwowanych!", Toast.LENGTH_LONG).show();
    	} else {
    		Toast.makeText(this, "Wystπpi≥Ç b≥πd podczas wykonywania øπdanej operacji!", Toast.LENGTH_LONG).show();
    	}
    }
    
public Bitmap pictureClickZoom()
    {
    	Bitmap bmp;
    	if(screenTest==1)
		  {
		  bmp = Bitmap.createScaledBitmap(userPhoto, userPhoto.getWidth()*3, userPhoto.getHeight()*3, true);
		  }
		  else
		  {
			bmp = Bitmap.createScaledBitmap(userPhoto, userPhoto.getWidth(), userPhoto.getHeight(), true); 
		  }
		    
		    return bmp;
    }
    
public void getExtras()
    {
    	Bundle b = getIntent().getExtras();
   		if(b!=null) {
   			role = b.getString("role");
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
    }

public void getScreenType()
{
	Display display = getWindowManager().getDefaultDisplay();
	Point size = new Point();
	display.getSize(size);
	int width = size.x;
	int height = size.y;
	if( (width>1100) && (height>1500) )
	{
		screenTest=1;
	}
}
    
public void intentEditProfile()
    {
    	Intent intent = new Intent(getApplicationContext(), OknoEdytujProfil.class);
		//intent.putExtra("photou", photou);
		Global.img = userPhoto;
		intent.putExtra("faculties", faculties);
		intent.putExtra("coords",coords);
		intent.putExtra("repPostId",repPostId);
		intent.putExtra("repUserId",repUserId);
		intent.putExtra("folUserName",folUserName);
		intent.putExtra("myLogin",myLogin);
		intent.putExtra("favUserId", favUserId);
		intent.putExtra("favCategoryId", favCategoryId);
		intent.putExtra("tags", tags);
		intent.putExtra("token", token);
		intent.putExtra("tagsId", tagsId);
		intent.putExtra("screenTest", screenTest);
    	startActivity(intent);
    }
    
public void logOut()
    {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OknoKonto.this); //Get the preferences
        Editor edit = prefs.edit(); //Needed to edit the preferences
        edit.putString("name", "");  //add a String
        edit.putString("passwd", "");
        edit.putBoolean("rememberCredentials", true); //add a boolean
        edit.commit();  // save the edits. 
        String sampleURL = serwer + "/logout";
        WebServiceTask wst = new WebServiceTask(WebServiceTask.LOGOUT_TASK, OknoKonto.this, "Wylogowywanie...", myLogin);   
        wst.execute(new String[] { sampleURL }); 
    }
    
public void pictureFullScreen()
    {
    	Dialog builder = new Dialog(OknoKonto.this);
		    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    builder.getWindow().setBackgroundDrawable(
		        new ColorDrawable(android.graphics.Color.TRANSPARENT));
		    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
		        @Override
		        public void onDismiss(DialogInterface dialogInterface) {
		            //nothing;
		        }
		    });
		    
		  ImageView imageView = new ImageView(OknoKonto.this);  			  
	  imageView.setImageBitmap(pictureClickZoom()); 
	    
	  builder.addContentView(imageView, new RelativeLayout.LayoutParams(
	            ViewGroup.LayoutParams.MATCH_PARENT, 
	            ViewGroup.LayoutParams.MATCH_PARENT));
	    builder.show();
    }
    
public void getFavTags(View v)
    {
    	menu1 = new PopupMenu(OknoKonto.this, v);
		for(int i=0; i<favCategoryId.length;i++){
			for(int j=0; j<tags.length; j++){
				if(favCategoryId[i].equals(tagsId[j])){
					menu1.getMenu().add(tags[j].toString());
				}
			}
		}
		menu1.show();
    }
    
public void favUsers(View v)
    {
    	menu2 = new PopupMenu(OknoKonto.this, v);
		for(int i=0; i<folUserName.length;i++){
			menu2.getMenu().add(0,i,0,folUserName[i] + "     usuÒ");
		}
		menu2.show();
		menu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
             public boolean onMenuItemClick(MenuItem item) { 
            	 itemId = item.getItemId();
            	 System.out.println("item: " + itemId);
            	 String sampleURL = serwer + "/stopFollow";
		        WebServiceTask wst = new WebServiceTask(WebServiceTask.STOPFOLLOW_TASK, "Usuwanie uøytkownika z listy ulubionych uøytkownikÛw...", folUserName[item.getItemId()], token, OknoKonto.this);   
		        wst.execute(new String[] { sampleURL });
            //  Toast.makeText(OknoKonto.this,"Usuniƒôto : " + item.getItemId(),Toast.LENGTH_SHORT).show(); 
              return true;  
             }  
            });  
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
    				Toast.makeText(this, "Nie masz uprawnieÒ do wykonania tej operacji!", Toast.LENGTH_LONG).show();
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
  
    /**
     * metoda konwertujƒÖca String na obiekt bitmap (zdjƒôcie).
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
    
    
    /**
     * klasa wewnƒôtrzna, kt√≥ra wykonuje asynchroniczne dzia≈Çanie w tle.
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
    public WebServiceTask(int taskType, String processMessage, Context mContext, String token){
    	this.taskType = taskType;
    	this.processMessage = processMessage;
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
     * metoda , kt√≥ra tworzy i wy≈õwietla obiekt progress bar.
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
     * g≈Ç√≥wna metoda wykonujƒÖca dzia≈Çanie w tle.
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
     * metoda ustawiajƒÖca parametry po≈ÇƒÖczenia http.
     */
    private HttpParams getHttpParams() {            
        HttpParams htpp = new BasicHttpParams();             
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);           
        return htpp;
    }
     
    /**
     * metoda wysy≈ÇajƒÖca oraz odbierajƒÖca dane od web serwisu.
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
     * metoda konwertujƒÖca odpowied≈∫ serwera na String.
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



}