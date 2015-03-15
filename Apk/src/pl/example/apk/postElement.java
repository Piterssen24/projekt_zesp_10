package pl.example.apk;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class postElement extends Fragment {
       TextView postContent, readPost;
       ImageView photoView;
       LinearLayout layout; 
       Context context;
       String postText, photo;
       Bitmap picture;
       
       public postElement(){
    	   
       }
       
       public postElement(String postId, String userId, String content, String photo, String categoryId, String addTime, String place, String eventTime){
    	   this.postText = content;
    	   this.photo = photo;
       }
       
       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {   	   
           String napis = "nowy post";  
           // Creating view correspoding to the fragment
           View v = inflater.inflate(R.layout.postelement_layout, container, false);   
           // Getting reference to the TextView of the Fragment
           postContent = (TextView) v.findViewById(R.id.postContent);        
           readPost = (TextView) v.findViewById(R.id.readMore);
           readPost.setOnClickListener(new View.OnClickListener() {  			
        	    @Override
           		public void onClick(View v) {
        	    	Intent intent = new Intent(getActivity(), OknoPost.class);
        	    	intent.putExtra("postText",postText);
        	    	intent.putExtra("photo", photo);
        	    	startActivity(intent);
        	    }  			
           });
     
           // Setting currently selected river name in the TextView
           postContent.setText(postText);     
           postContent.setOnClickListener(new View.OnClickListener() {   			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	intent.putExtra("postText",postText);
                  	intent.putExtra("photo", photo);
                  	startActivity(intent);
                  }     			
      		});
           
           if(picture!=null)
           {
        	   picture.recycle();
        	   picture = null;
           }
           picture = decodeBase64(photo);
           photoView = (ImageView) v.findViewById(R.id.authorPhoto);
           photoView.setImageBitmap(picture);
           
       	   LayoutParams params = (LayoutParams) this.photoView.getLayoutParams();
       	   params.width = 100;
       	   params.height = 160;     	   
       	   photoView.setOnClickListener(new View.OnClickListener() {      			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	intent.putExtra("postText",postText);
                  	intent.putExtra("photo", photo);
                  	startActivity(intent);
                }      			
       	   });             
           return v;
       }
       
       public static Bitmap decodeBase64(String input) 
       {
           byte[] decodedByte;
           decodedByte = Base64.decode(input, Base64.URL_SAFE);
           Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
           decodedByte = null;
           return b;
       }
       
}