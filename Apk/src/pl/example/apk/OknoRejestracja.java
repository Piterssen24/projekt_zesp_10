package pl.example.apk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Intent;

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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OknoRejestracja extends Activity {
	private static final String TAG = "OknoRejestracja";
	public RadioGroup radioGrouRola;
	public int id;
	public String role, login, password, repassword, email;
	private EditText elogin, epassword, erepassword, eemail;
	public String serwer = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknorejestracja_layout);
        serwer = getResources().getString(R.string.server);
        
        radioGrouRola = (RadioGroup) findViewById(R.id.radioGroupType);
        id = radioGrouRola.getCheckedRadioButtonId();
        elogin = (EditText) findViewById(R.id.editTextUsername);
        epassword = (EditText) findViewById(R.id.editTextPassword);
        erepassword = (EditText) findViewById(R.id.editTextPasswordRepeat);
        eemail = (EditText) findViewById(R.id.editTextEmail);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - rejestracja");              
    }

    public void rejestruj(View vw) {
    	String sampleURL = serwer + "/register";
    	
    	login = elogin.getText().toString();
    	password = epassword.getText().toString();
    	repassword = erepassword.getText().toString();
    	email = eemail.getText().toString();
    	
    	elogin.setText("");
    	epassword.setText("");
    	erepassword.setText("");
    	eemail.setText("");
    
    	if (login.equals("") || password.equals("") || repassword.equals("") || email.equals("") ) {
    		Toast.makeText(this, "Pola nie mog¹ byæ puste!", Toast.LENGTH_LONG).show();
    	}else if(password.equals(repassword)){
    		if(id == R.id.radio0){
    			role = "D";
    		}else if(id == R.id.radio1){
    			role = "U";
    		}
    		WebServiceTask wst = new WebServiceTask(WebServiceTask.REGISTER_TASK, this, "Registering...", login, password, email, role);   
        	wst.execute(new String[] { sampleURL });
    	}else{
    		Toast.makeText(this, "Has³a siê nie zgadzaj¹!", Toast.LENGTH_LONG).show();
    	}	
    }
}
