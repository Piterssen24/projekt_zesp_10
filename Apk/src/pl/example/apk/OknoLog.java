package pl.example.apk;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class OknoLog extends Activity {
	private static final String TAG = "OknoLog";
	private EditText elogin, epassword;
	public String login, password;
	public String serwer = "";
	String name, pass;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknologowania_layout);
        
        serwer = getResources().getString(R.string.server);
        elogin = (EditText) findViewById(R.id.editlogin);
        epassword = (EditText) findViewById(R.id.editPassword);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - logowanie");
    }
    
    public void retrieveSampleData(View vw) {
    	 	
    	login = elogin.getText().toString();
    	password = epassword.getText().toString();
    	String sampleURL = serwer + "/login";
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OknoLog.this); //Get the preferences
        Editor edit = prefs.edit(); //Needed to edit the preferences
        edit.putString("name", login);  //add a String
        edit.putString("passwd", password);
        edit.putBoolean("rememberCredentials", true); //add a boolean
        edit.commit();  // save the edits. 
 
        WebServiceTask wst = new WebServiceTask(WebServiceTask.LOG_TASK, this, "Logging...", login, password);   
        wst.execute(new String[] { sampleURL });       
    }

}

	