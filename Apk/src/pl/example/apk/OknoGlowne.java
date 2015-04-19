package pl.example.apk;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class OknoGlowne extends Activity {

	Context context;
	Button buttonWithoutLogin;
	Button buttonLogin;
	Button buttonRegistration;
	ActionBar actionBar;
	public String login, password;
	public String serwer = "";
	private final static String ALGORITHM = "AES";
	private final static String HEX = "0123456789ABCDEF";
	private String cryptedpass;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OknoGlowne.this); //Get the preferences
   		login = prefs.getString("name", "defaultName"); //get a String
   		password = prefs.getString("passwd", "defPasswd");
   		prefs.getBoolean("rememberCredentials", true);
        
        if((login!=null) && (password!=null) && (login!="") && (password!="") && (!login.equals("defaultName")) && (!password.equals("defPasswd")))
        {
        	serwer = getResources().getString(R.string.server);
        	String sampleURL = serwer + "/login";
        	String key = "key-0123123451";
            try {
    			cryptedpass = cipher(key, password);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	WebServiceTask wst = new WebServiceTask(WebServiceTask.LOG_TASK, this, "Logging...", login, cryptedpass);   
            wst.execute(new String[] { sampleURL });          	
        }
        else
        {
        setContentView(R.layout.oknoglowne_layout);
        context = getApplicationContext();
        
        actionBar = getActionBar();
        actionBar.hide();
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegistration = (Button) findViewById(R.id.buttonRegistration);
        
        buttonLogin.setOnClickListener(new View.OnClickListener() {			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoLog.class);
            	startActivity(intent);
            }			
		});
        
        buttonRegistration.setOnClickListener(new View.OnClickListener() {			
			@Override
        	public void onClick(View v) {
            	Intent intent = new Intent(context, OknoRejestracja.class);
            	startActivity(intent);
            }			
		});     
        
        }
       
    }   
	
	/**
	    * Encrypt data
	    * metoda szyfruj�ca has�o
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
	    * metoda deszyfruj�ca has�o
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

	    /**
		    * metoda konwertuj�ca string na tablic� bajt�w
		    */
	    private static byte[] toByte(String hexString) {
	    	int len = hexString.length()/2;
	    	byte[] result = new byte[len];
	    	for (int i = 0; i < len; i++)
	    		result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
	    	return result;
	    }

	    /**
		    * metoda konwertuj�ca tablic� bajt�w na string
		    */
	    public static String toHex(byte[] stringBytes) {
	    	StringBuffer result = new StringBuffer(2*stringBytes.length);
	    	for (int i = 0; i < stringBytes.length; i++) {
	    		result.append(HEX.charAt((stringBytes[i]>>4)&0x0f)).append(HEX.charAt(stringBytes[i]&0x0f));
	    	}
	    	return result.toString();
	    }
    
}
