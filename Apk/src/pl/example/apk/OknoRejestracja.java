package pl.example.apk;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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
	public String userPhoto;
	private final static String ALGORITHM = "AES";
	private final static String HEX = "0123456789ABCDEF";
	private String cryptedpass;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknorejestracja_layout);
        serwer = getResources().getString(R.string.server);
        
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
    	Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gaz3min); 
    	userPhoto = encodeTobase64(bmp);
    	
    	elogin.setText("");
    	epassword.setText("");
    	erepassword.setText("");
    	eemail.setText("");
    
    	if (login.equals("") || password.equals("") || repassword.equals("") || email.equals("") ) {
    		Toast.makeText(this, "Pola nie mog¹ byæ puste!", Toast.LENGTH_LONG).show();
    	}else if(password.equals(repassword)){
    			role = "D";
    			String key = "key-0123123451";
    	        try {
    				cryptedpass = cipher(key, password);
    				System.out.println("pass: " + password);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		WebServiceTask wst = new WebServiceTask(WebServiceTask.REGISTER_TASK, this, "Registering...", login, cryptedpass, email, role, userPhoto);   
        	wst.execute(new String[] { sampleURL });
    	}else{
    		Toast.makeText(this, "Has³a siê nie zgadzaj¹!", Toast.LENGTH_LONG).show();
    	}	
    }
    
    public static String encodeTobase64(Bitmap image)
    {
    	Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        immagex.recycle();
        immagex = null;
        byte[] b = null;
        b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        b = null;
        return imageEncoded;
    }
    
    /**
     * Encrypt data
     * @param secretKey - a secret key used for encryption
     * @param data - data to encrypt
     * @return Encrypted data
     * @throws Exception
     */
     public static String cipher(String secretKey, String data) throws Exception {
     	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
     	KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 128, 128);
     	SecretKey tmp = factory.generateSecret(spec);
     	SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
     	Cipher cipher = Cipher.getInstance(ALGORITHM);
     	cipher.init(Cipher.ENCRYPT_MODE, key);
     	return toHex(cipher.doFinal(data.getBytes()));
     }

     /**
     * Decrypt data
     * @param secretKey - a secret key used for decryption
     * @param data - data to decrypt
     * @return Decrypted data
     * @throws Exception
     */
     public static String decipher(String secretKey, String data) throws Exception {
     	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
     	KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), secretKey.getBytes(), 128, 128);
     	SecretKey tmp = factory.generateSecret(spec);
     	SecretKey key = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
     	Cipher cipher = Cipher.getInstance(ALGORITHM);
     	cipher.init(Cipher.DECRYPT_MODE, key);
     	return new String(cipher.doFinal(toByte(data)));
     }

     // Helper methods

     private static byte[] toByte(String hexString) {
     	int len = hexString.length()/2;
     	byte[] result = new byte[len];
     	for (int i = 0; i < len; i++)
     		result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
     	return result;
     }

     public static String toHex(byte[] stringBytes) {
     	StringBuffer result = new StringBuffer(2*stringBytes.length);
     	for (int i = 0; i < stringBytes.length; i++) {
     		result.append(HEX.charAt((stringBytes[i]>>4)&0x0f)).append(HEX.charAt(stringBytes[i]&0x0f));
     	}
     	return result.toString();
     }
}
