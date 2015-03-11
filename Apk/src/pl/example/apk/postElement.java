package pl.example.apk;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class postElement extends Fragment {
       TextView postContent,readPost;
       ImageView photo;
       LinearLayout layout; 
       Context context;
       
       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
    
    	   
           String napis = "nowy post";
           
    
           // Creating view correspoding to the fragment
           View v = inflater.inflate(R.layout.postelement_layout, container, false);
    
           // Getting reference to the TextView of the Fragment
           postContent = (TextView) v.findViewById(R.id.postContent);
           
           readPost = (TextView) v.findViewById(R.id.readMore);
           readPost.setOnClickListener(new View.OnClickListener(){
        	   
        	   @Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	startActivity(intent);
                  }
           });
    
    
           // Setting currently selected river name in the TextView
           postContent.setText(napis);
           
           postContent.setOnClickListener(new View.OnClickListener() {
      			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	startActivity(intent);
                  }
      			
      		});
           
           photo = (ImageView) v.findViewById(R.id.authorPhoto);
           photo.setImageResource(R.drawable.pobrane);
       	   LayoutParams params = (LayoutParams) this.photo.getLayoutParams();
       	   params.width = 100;
       	   params.height = 160;
       	   
       	   photo.setOnClickListener(new View.OnClickListener() {
      			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	startActivity(intent);
                  }
      			
      		});

           
    
           return v;
       }
 
  

}