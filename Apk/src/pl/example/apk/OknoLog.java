package pl.example.apk;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OknoLog extends Activity {
	//private static final String TAG = "OknoLog";
	private EditText elogin, epassword;
	private TextView forgotPassword;
	public String login, password;
	private String cryptedpass, deviceId;
	public String serwer = "";
	String name, pass;
	private final static String ALGORITHM = "AES";
	private final static String HEX = "0123456789ABCDEF";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknologowania_layout);
        
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        serwer = getResources().getString(R.string.server);
        elogin = (EditText) findViewById(R.id.editlogin);
        epassword = (EditText) findViewById(R.id.editPassword);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - logowanie");
        
        forgotPassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(OknoLog.this, "W celu przypomnienia has³a prosimy o wys³anie wiadomoœci do administratora na adres admin@admin.com z adresu email podanego podczas rejestracji", 3000).show();
			}
		});
    }
    
    /**
     * metoda pobieraj¹ca login i has³o
     */
    public void retrieveSampleData(View vw) {
    	String key = "key-0123123451";
    	login = elogin.getText().toString();
    	password = epassword.getText().toString();
    	TelephonyManager tManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
    	deviceId = tManager.getDeviceId();
        String sampleURL = serwer + "/login";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OknoLog.this); //Get the preferences
        Editor edit = prefs.edit(); //Needed to edit the preferences
        String Password = "";
		try {
			Password = cipher(key, password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        edit.putString("name", login);  //add a String
        edit.putString("passwd", Password);
        edit.putBoolean("rememberCredentials", true); //add a boolean
        edit.commit();  // save the edits. 
        try {
			cryptedpass = cipher(key, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        WebServiceTask wst = new WebServiceTask(WebServiceTask.LOG_TASK, this, "Logowanie...", login, cryptedpass, deviceId);   
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

	