package pl.example.apk;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class OknoLog extends Activity {
	//private static final String TAG = "OknoLog";
	private EditText elogin, epassword;
	public String login, password;
	private String cryptedpass;
	public String serwer = "";
	String name, pass;
	private final static String ALGORITHM = "AES";
	private final static String HEX = "0123456789ABCDEF";
	
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
    
    /**
     * metoda pobieraj¹ca login i has³o
     */
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

    /**
	    * metoda konwertuj¹ca string na tablicê bajtów
	    */
    private static byte[] toByte(String hexString) {
    	int len = hexString.length()/2;
    	byte[] result = new byte[len];
    	for (int i = 0; i < len; i++)
    		result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
    	return result;
    }

    /**
	    * metoda konwertuj¹ca tablicê bajtów na string
	    */
    public static String toHex(byte[] stringBytes) {
    	StringBuffer result = new StringBuffer(2*stringBytes.length);
    	for (int i = 0; i < stringBytes.length; i++) {
    		result.append(HEX.charAt((stringBytes[i]>>4)&0x0f)).append(HEX.charAt(stringBytes[i]&0x0f));
    	}
    	return result.toString();
    }

}

	