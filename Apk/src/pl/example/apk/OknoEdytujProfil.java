package pl.example.apk;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.example.apk.WebServiceTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

public class OknoEdytujProfil extends Activity {
	
	ActionBar actionBar;
	Context context;
	ListView lv;
	TagCheckModel[] modelItems;
	Button changePhoto, submit;
	Bitmap yourSelectedImage, newImage;
	ImageView icon;
	TagCheckModel tcm;
	MyCustomAdapter dataAdapter = null;
	public String photou;
	Bitmap userPhoto;
	public String photo="";
	public String serwer = "";
	public static String token,myLogin;
    public static int[] repPostId, repUserId;
	public static String[] faculties, coords, favUserId, favCategoryId, tags, tagsId, folUserName;
	public List<Integer> favouritesTags;
	CheckBox cb;
	ArrayList<TagCheckModel> listOfTags;
	public int screenTest;
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edytujprofil_layout);
		serwer = getResources().getString(R.string.server);
		
		getExtras();
		
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
		actionBar.setTitle("PicNews - Edycja profilu");
		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
		
		icon = (ImageView) findViewById(R.id.imageIcon);		
        icon.setImageBitmap(userPhoto);
		
		changePhoto = (Button) findViewById(R.id.buttonEditPicture);
		changePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openGallery();				
			}
		});
		
		lv = (ListView) findViewById(R.id.listOfTags);
		listOfTags();
		dataAdapter = new MyCustomAdapter(this, R.layout.edytujprofil_rowlayout, listOfTags);
		lv.setAdapter(dataAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
		   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			   
			   TagCheckModel tcm = (TagCheckModel) parent.getItemAtPosition(position);
			    if(tcm.selected == true){
			    	tcm.selected = false;
			    }
			    if(tcm.selected == false){
			    	tcm.selected = true;
			    }
			   }
		  });
				
		submit = (Button) findViewById(R.id.buttonAcceptChanges);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				submitChanges();				
			}
		});
		
		}
	
public void getExtras()
{
	Bundle b = getIntent().getExtras();
		if(b!=null) {
			
			token = b.getString("token");
			if(userPhoto!=null)
			{
	        	 userPhoto.recycle();
	        	 userPhoto = null;
	        }
			userPhoto = Global.img;
			tags = b.getStringArray("tags");
			tagsId = b.getStringArray("tagsId");
			favUserId = b.getStringArray("favUserId");
			favCategoryId = b.getStringArray("favCategoryId");
			screenTest = b.getInt("screenTest");
			faculties = b.getStringArray("faculties");
			coords = b.getStringArray("coords");
			repPostId = b.getIntArray("repPostId");
			repUserId = b.getIntArray("repUserId");
			folUserName = b.getStringArray("folUserName");
			myLogin = b.getString("myLogin");
		}
}
	
public void openGallery()
{
	Intent i = new Intent(Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	startActivityForResult(i, RESULT_LOAD_IMAGE);
}

public void listOfTags()
{
	listOfTags = new ArrayList<TagCheckModel>();
	  for(int i=0; i<tags.length; i++){
			if(favCategoryId.length>0){
				for(int j=0; j<favCategoryId.length; j++){
					if(Integer.parseInt(favCategoryId[j]) == Integer.parseInt((tagsId[i]))) {
						tcm = new TagCheckModel(tags[i], true);
						listOfTags.add(tcm);
						break;
					} 
					if(Integer.parseInt(favCategoryId[j]) != Integer.parseInt((tagsId[i])) && j == favCategoryId.length-1){
						tcm = new TagCheckModel(tags[i], false);
						listOfTags.add(tcm);
					}
				}
			} else {
					tcm = new TagCheckModel(tags[i], false);
					listOfTags.add(tcm);
			}
		}
}

public void submitChanges()
{
	if(newImage!=null)
	{
		photo =  encodeTobase64(newImage); 
    	newImage.recycle();
    	newImage = null;
	} else {
		photo = "";
	}
	favouritesTags = new ArrayList<Integer>();
	for(int i=0; i<listOfTags.size(); i++){
		tcm = listOfTags.get(i);
		if(tcm.isSelected()==true){
			favouritesTags.add(Integer.parseInt(tagsId[i]));
		}
	}
	String sampleURL = serwer + "/edit";
		WebServiceTask wst = new WebServiceTask(WebServiceTask.EDIT_TASK, OknoEdytujProfil.this , "Saving changes...", photo, token, favouritesTags);   
		wst.execute(new String[] { sampleURL });
}

public static Bitmap RotateBitmap(Bitmap source, float angle)
  	{
  		Matrix matrix = new Matrix();
  	    matrix.postRotate(angle);
  	    return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
  	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            String[] filePathOrientation = {MediaStore.Images.ImageColumns.ORIENTATION};
 
            Cursor cursorOr = getContentResolver().query(selectedImage, filePathOrientation, null, null, null);
            cursorOr.moveToFirst();
            int or = cursorOr.getInt(0);
            
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();        
            
            yourSelectedImage = BitmapFactory.decodeFile(picturePath);
            if(or==90)
            {
            	yourSelectedImage = RotateBitmap(yourSelectedImage, 90);
            }
            newImage = Bitmap.createScaledBitmap(yourSelectedImage,300,300, true);
            //Bitmap iconB = Bitmap.createScaledBitmap(yourSelectedImage, 60, 60, true);
            icon.setImageBitmap(newImage);
         
        }


	}
	
	@Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        Intent intentkonto = new Intent(getApplicationContext(), OknoKonto.class);
        intentkonto.putExtra("faculties", faculties);
		intentkonto.putExtra("coords",coords);
		intentkonto.putExtra("repPostId",repPostId);
		intentkonto.putExtra("repUserId",repUserId);
		intentkonto.putExtra("folUserName",folUserName);
		intentkonto.putExtra("myLogin",myLogin);
		intentkonto.putExtra("token", token);
        intentkonto.putExtra("tags", tags);
        intentkonto.putExtra("tagsId", tagsId);
        intentkonto.putExtra("favUserId", favUserId);
        intentkonto.putExtra("favCategoryId", favCategoryId);
        intentkonto.putExtra("screenTest", screenTest);   		
    	startActivity(intentkonto);
        //startActivity(new Intent(OknoEdytujProfil.this, OknoKonto.class));
        finish();

    }
	
	/**
     * metoda , która zamienia string na bitmapê (obraz).
     */
	public static Bitmap decodeBase64(String input) 
	{
	    byte[] decodedByte;
	    decodedByte = Base64.decode(input, Base64.URL_SAFE);
	    Bitmap b = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	    decodedByte = null;
	    return b;
	}
	
	/**
     * metoda , która zamienia obraz (bitmapê) na string.
     */
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
	
	private class MyCustomAdapter extends ArrayAdapter<TagCheckModel> {
		 
		  private ArrayList<TagCheckModel> listOfTags;
		 
		  public MyCustomAdapter(Context context, int textViewResourceId, 
		    ArrayList<TagCheckModel> listOfTags) {
		   super(context, textViewResourceId, listOfTags);
		   this.listOfTags = new ArrayList<TagCheckModel>();
		   this.listOfTags.addAll(listOfTags);
		  }
		 
		  private class ViewHolder {
		   CheckBox name;
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		 
		   ViewHolder holder = null;
		   Log.v("ConvertView", String.valueOf(position));
	//	   final int id = position;
		   if (convertView == null) {
			   LayoutInflater vi = (LayoutInflater)getSystemService(
			     Context.LAYOUT_INFLATER_SERVICE);
			   convertView = vi.inflate(R.layout.edytujprofil_rowlayout, parent, false);
			 
			   holder = new ViewHolder();
			   holder.name = (CheckBox) convertView.findViewById(R.id.checkBoxTag);
			   convertView.setTag(holder);
			 
			    holder.name.setOnClickListener( new View.OnClickListener() {  
			     public void onClick(View v) {  
			      CheckBox cb = (CheckBox) v ;  
			      TagCheckModel country = (TagCheckModel) cb.getTag();  
			      country.setSelected(cb.isChecked());
			     }  
			    });  
			   } 
			   else {
			    holder = (ViewHolder) convertView.getTag();
			   }
		   TagCheckModel country = listOfTags.get(position);
		   holder.name.setText(country.getName());
		   holder.name.setChecked(country.isSelected());
		   holder.name.setTag(country);
		 
		   return convertView;
		 
		  }
		 
		 
	}

}