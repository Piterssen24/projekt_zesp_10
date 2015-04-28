package pl.example.apk;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import pl.example.apk.WebServiceTask;
import android.widget.RelativeLayout;
import android.view.Window;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
       Bitmap picture, pictureMin, pictureOriginal,b, pictureSee;
       String userLogin, place, eventTime;
       public int[] repPostId, repUserId;
       public String which;
       public String[] faculties, coords, folUserName;
       public String serwer = "";
       int postId;
       public String myLogin, token;
       int screenTest, pictureTest=0;
       
       public postElement(){
    	   
       }
       
       public postElement(String token, String postId, String userLogin, String content, String photo, String categoryId, String addTime, String place, String eventTime, String[] faculties, String[] coords, String which, int[] repPostId, int[] repUserId, String[] folUserName, String myLogin, int screenTest){
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
    	   this.screenTest = screenTest;
    	   
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
        	    	openPost();
        	    }  			
           });
           
           report.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(which.equals("Konto")){
					deletePost();
				} else {
					reportPost(); 
				}
			}
		});
           
           
           post = postText;
   		   post = truncate(post,60);
           postContent.setText(post); 
           postContent.setOnClickListener(new View.OnClickListener() {   			
      			@Override
              	public void onClick(View v) {
      				openPost();
                  }     			
      		});
           
           if(picture!=null)
           {
        	   picture.recycle();
        	   picture = null;
           }
           if(pictureOriginal!=null)
           {
        	   pictureOriginal.recycle();
        	   pictureOriginal = null;
           }
           if(pictureSee!=null)
           {
        	   pictureSee.recycle();
        	   pictureSee = null;
           }
           pictureOriginal = decodeBase64(photo);
           picture = pictureOriginal;
           int w = picture.getWidth();
           int h = picture.getHeight();
           if(h>w)
           {
        	   pictureTest=1;
           }
           photoView = (ImageView) v.findViewById(R.id.authorPhoto);
          // photoView.setImageBitmap(picture);
           
       	  
       	   //photoView.setImageBitmap(pictureSee);
       	   photoView.setLayoutParams(setPictureParams());
       	   photoView.setImageBitmap(picture);
       	   
       	   photoView.setOnClickListener(new View.OnClickListener() {      			
      			@Override
              	public void onClick(View v) {
      				pictureFulscreen();
                }      			
       	   });           
           return v;
       }
       
       public void zoomPicture(ImageView imageView)
       {
    	   if(screenTest==1)
			    {
			    	if(pictureTest==0)
			    	{
			    		b = Bitmap.createScaledBitmap(picture, picture.getWidth()*2-300, picture.getHeight()*2-130, true);
	      			    imageView.setImageBitmap(b);
			    	}
			    	if(pictureTest==1)
			    	{
			    		b = Bitmap.createScaledBitmap(picture, picture.getWidth()*2+100, picture.getHeight()*2+180, true);
	      			    imageView.setImageBitmap(b);
			    	}
			    
			    }
			    else
			    {
			    	if(pictureTest==0)
			    	{
			    		b = Bitmap.createScaledBitmap(picture, picture.getWidth()-400, picture.getHeight()-250, true);
	      			    imageView.setImageBitmap(b);
			    	}
			    	if(pictureTest==1)
			    	{
			    		b = Bitmap.createScaledBitmap(picture, picture.getWidth()-150, picture.getHeight()-220, true);
	      			    imageView.setImageBitmap(b);
			    	}
			    }
       }
       
       public LayoutParams setPictureParams()
       {
    	   LayoutParams params = (LayoutParams) this.photoView.getLayoutParams();
       	     
       	   
       	   if(screenTest==1)
       	   {
       		   if(pictureTest==0)
       		   {
       			 //  pictureSee = Bitmap.createScaledBitmap(picture, 240, 160, true);       			  
       		   params.width = 420;
       		   params.height = 360; 
       		   }
       		   if(pictureTest==1)
       		   {
       			//pictureSee = Bitmap.createScaledBitmap(picture, 160, 240, true);
       			   params.width = 420;
       			   params.height = 440;
       		   }
       	   }
       	   else
       	   {
       		   if(pictureTest==0)
    		   {
       			//pictureSee = Bitmap.createScaledBitmap(picture, 160, 100, true);
    		   params.width = 140;
    		   params.height = 140; 
    		   }
    		   if(pictureTest==1)
    		   {
    			   //pictureSee = Bitmap.createScaledBitmap(picture, 100, 160, true);
    			   params.width = 140;
    			   params.height = 160;
    		   }
       	   }
       	   return params;
       }
       
       public void pictureFulscreen()
       {
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
			    zoomPicture(imageView);
			    
			    builder.addContentView(imageView, new RelativeLayout.LayoutParams(
			            ViewGroup.LayoutParams.MATCH_PARENT, 
			            ViewGroup.LayoutParams.MATCH_PARENT));
			    builder.show();
       }
       
       public void openPost()
       {
    	   Intent intent = new Intent(getActivity(), OknoPost.class);
	    	intent.putExtra("postId", postId);
	    	intent.putExtra("postText",postText);
	    	//intent.putExtra("photo", photo);
	    	Global.img = decodeBase64(photo);
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
	    	intent.putExtra("screenTest",screenTest);
	    	startActivity(intent);
       }
       
       public void deletePost()
       {
    	   System.out.println("screenTest= "+screenTest);
    	   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle("Ostrze¿enie");
		    builder.setMessage("Czy na pewno chcesz usun¹æ posta?");
		    builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        	String sampleURL = serwer + "/removePost";
		       		WebServiceTask wst = new WebServiceTask(WebServiceTask.POSTDELETE_TASK, getActivity(), "Trwa usuwanie posta, proszê czekaæ...", token, postId);   
		       		wst.execute(new String[] { sampleURL }); 
		            dialog.dismiss();
		           /* Intent intentkonto = new Intent(getActivity().getApplicationContext(), OknoKonto.class);
		        	startActivity(intentkonto);*/
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
       
       public void reportPost()
       {
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
		            dialog.dismiss();
		        }
		    });
		    AlertDialog alert = builder.create();
		    alert.show();
		}
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