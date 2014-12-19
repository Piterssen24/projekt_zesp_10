package pl.example.apk;

import android.app.Activity;
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
       TextView trescPosta,czytajPosta;
       ImageView zdjecie;
       LinearLayout layout; 
       Context context;
       
       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
    
    	   
           String napis = "nowy post";
           
    
           // Creating view correspoding to the fragment
           View v = inflater.inflate(R.layout.postelement_layout, container, false);
    
           // Getting reference to the TextView of the Fragment
           trescPosta = (TextView) v.findViewById(R.id.trescPosta);
           
           czytajPosta = (TextView) v.findViewById(R.id.czytajdalej);
           czytajPosta.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
           	public void onClick(View v) {
               	Intent intent = new Intent(getActivity(), OknoPost.class);
               	startActivity(intent);
               }
   			
   		});
    
           // Setting currently selected river name in the TextView
           trescPosta.setText(napis);
           
           trescPosta.setOnClickListener(new View.OnClickListener() {
      			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	startActivity(intent);
                  }
      			
      		});
           
           zdjecie = (ImageView) v.findViewById(R.id.zdjeciedziennikarza);
           zdjecie.setImageResource(R.drawable.gaz3min);
       	   LayoutParams params = (LayoutParams) this.zdjecie.getLayoutParams();
       	   params.width = 100;
       	   params.height = 160;
       	   
       	   zdjecie.setOnClickListener(new View.OnClickListener() {
      			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	startActivity(intent);
                  }
      			
      		});

           
    
           return v;
       }
 
  

}