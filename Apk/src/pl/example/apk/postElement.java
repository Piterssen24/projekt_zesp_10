package pl.example.apk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import pl.example.apk.WebServiceTask;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.view.Window;
import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class postElement extends Fragment {
       TextView postContent, readPost, report;
       ImageView photoView;
       LinearLayout layout; 
       String postText, post, photo, location;
       Bitmap picture, pictureMin;
       String userLogin, place, eventTime;
       public static int[] repPostId, repUserId;
       public static String which;
       public static String[] faculties, coords, folUserName;
       Context context;
       public String serwer = "";
       int postId;
       public String myLogin, token;
       
       public postElement(){
    	   
       }
       
       public postElement(String token, String postId, String userLogin, String content, String photo, String categoryId, String addTime, String place, String eventTime, String[] faculties, String[] coords, String which, int[] repPostId, int[] repUserId, String[] folUserName, String myLogin){
    	   this.token = token;
    	   this.postId = Integer.parseInt(postId);
    	   this.postText = content;
    	   this.photo = photo;
    	   this.userLogin = userLogin;
    	   this.place = place;
    	   this.eventTime = eventTime;
    	   this.faculties = faculties;
    	   this.coords = coords;
    	   this.which = which;
    	   this.repPostId = repPostId;
    	   this.repUserId = repUserId;
    	   this.folUserName = folUserName;
    	   this.myLogin = myLogin;
       }
       
       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {   	   
           serwer = getResources().getString(R.string.server);
           // Creating view correspoding to the fragment
           View v = inflater.inflate(R.layout.postelement_layout, container, false);   
           // Getting reference to the TextView of the Fragment
           postContent = (TextView) v.findViewById(R.id.postContent);   
           report = (TextView) v.findViewById(R.id.report);
           if(which.equals("Konto")){
        	   report.setText(getResources().getString(R.string.deletePost));
           } else {
        	   report.setText(getResources().getString(R.string.stringzglosnaduzycie));
           }
           readPost = (TextView) v.findViewById(R.id.readMore);
           readPost.setOnClickListener(new View.OnClickListener() {  			
        	    @Override
           		public void onClick(View v) {
        	    	Intent intent = new Intent(getActivity(), OknoPost.class);
        	    	intent.putExtra("postId", postId);
        	    	intent.putExtra("postText",postText);
        	    	intent.putExtra("photo", photo);
        	    	intent.putExtra("userLogin", userLogin);
        	    	intent.putExtra("place", place);
        	    	intent.putExtra("eventTime", eventTime);
        	    	intent.putExtra("faculties", faculties);
        	    	intent.putExtra("coords", coords);
        	    	intent.putExtra("token", token);
        	    	intent.putExtra("repPostId", repPostId);
        	    	intent.putExtra("repUserId", repUserId);
        	    	intent.putExtra("folUserName", folUserName);
        	    	intent.putExtra("myLogin", myLogin);
        	    	startActivity(intent);
        	    }  			
           });
           
           report.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(which.equals("Konto")){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    builder.setTitle("Ostrze¿enie");
				    builder.setMessage("Czy na pewno chcesz usun¹æ posta?");
				    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				        	String sampleURL = serwer + "/removePost";
				       		WebServiceTask wst = new WebServiceTask(WebServiceTask.POSTDELETE_TASK, getActivity(), "Trwa usuwanie posta, proszê czekaæ...", token, postId);   
				       		wst.execute(new String[] { sampleURL }); 
				            dialog.dismiss();
				        }
				    });
				    
				    builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				            // Do nothing
				            dialog.dismiss();
				        }
				    });
				    AlertDialog alert = builder.create();
				    alert.show();
				} else {
					 List<Integer> list1 = new ArrayList<Integer>();
					 for(int i=0; i<repPostId.length; i++){
						 list1.add(repPostId[i]);
					 }
					if(list1.contains(postId)){
						Toast.makeText(getActivity(), "Ten post zosta³ ju¿ przez Ciebie zg³oszony!", Toast.LENGTH_LONG).show();
					} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    builder.setTitle("Ostrze¿enie");
				    builder.setMessage("Czy na pewno chcesz zg³osiæ posta?");
				    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				        	int[] temp = new int[repPostId.length + 1];
				            for (int i = 0; i < repPostId.length; i++){
				                temp[i] = repPostId[i];
				            }
				            temp[repPostId.length] = postId;
				            repPostId = temp;
				        	String sampleURL = serwer + "/report";
				       		WebServiceTask wst = new WebServiceTask(WebServiceTask.POSTREPORT_TASK, getActivity(), "Trwa zg³aszanie posta, proszê czekaæ...", postId, token);   
				       		wst.execute(new String[] { sampleURL }); 
				            dialog.dismiss();
				        }
				    });
				    
				    builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				            // Do nothing
				            dialog.dismiss();
				        }
				    });
				    AlertDialog alert = builder.create();
				    alert.show();
				}
				}
			}
		});
     
           // Setting currently selected river name in the TextView
           post = postText;
   		   post = truncate(post,60);
           postContent.setText(post); 
           postContent.setOnClickListener(new View.OnClickListener() {   			
      			@Override
              	public void onClick(View v) {
                  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	intent.putExtra("postId", postId);
                  	intent.putExtra("postText",postText);
                  	intent.putExtra("photo", photo);
                  	intent.putExtra("userLogin", userLogin);
                  	intent.putExtra("place", place);
        	    	intent.putExtra("eventTime", eventTime);
        	    	intent.putExtra("faculties", faculties);
        	    	intent.putExtra("coords", coords);
        	    	intent.putExtra("token", token);
        	    	intent.putExtra("repPostId", repPostId);
        	    	intent.putExtra("repUserId", repUserId);
        	    	intent.putExtra("folUserName", folUserName);
        	    	intent.putExtra("myLogin", myLogin);
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
                /*  	Intent intent = new Intent(getActivity(), OknoPost.class);
                  	intent.putExtra("postText",postText);
                  	intent.putExtra("photo", photo);
                  	intent.putExtra("userLogin", userLogin);
                  	intent.putExtra("place", place);
        	    	intent.putExtra("eventTime", eventTime);
        	    	intent.putExtra("faculties", faculties);
        	    	intent.putExtra("coords", coords);
        	    	intent.putExtra("token", token);
                  	startActivity(intent);*/
      				Dialog builder = new Dialog(getActivity());
      			    builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
      			    builder.getWindow().setBackgroundDrawable(
      			        new ColorDrawable(android.graphics.Color.TRANSPARENT));
      			    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
      			        @Override
      			        public void onDismiss(DialogInterface dialogInterface) {
      			            //nothing;
      			        }
      			    });

      			    ImageView imageView = new ImageView(getActivity());
      			    Bitmap b = Bitmap.createScaledBitmap(picture, picture.getWidth()*2-140, picture.getHeight()*2-100, true);
      			    imageView.setImageBitmap(b);
      			    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
      			            ViewGroup.LayoutParams.MATCH_PARENT, 
      			            ViewGroup.LayoutParams.MATCH_PARENT));
      			    builder.show();
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
       
       public static String truncate(final String content, final int lastIndex) {
			if(content.length()>60)
			{
				String result = content.substring(0, lastIndex);
				if (content.charAt(lastIndex) != ' ') 
				{
					result = result.substring(0, result.lastIndexOf(" "));
				}
				return result+"...";
			}
			else return content;
   	}
       
}