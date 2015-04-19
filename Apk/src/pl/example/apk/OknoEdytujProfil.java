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
	public static String token;
	public static String[] tags, tagsId, favUserId, favCategoryId;
	public List<Integer> favouritesTags;
	CheckBox cb;
	CustomAdapter adapter;
	ArrayList<TagCheckModel> listOfTags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edytujprofil_layout);
		serwer = getResources().getString(R.string.server);
		Bundle b = getIntent().getExtras();
   		if(b!=null) {
   			token = b.getString("token");
   			photou = b.getString("photou");
   			tags = b.getStringArray("tags");
   			tagsId = b.getStringArray("tagsId");
   			favUserId = b.getStringArray("favUserId");
   			favCategoryId = b.getStringArray("favCategoryId");
   		}
		
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
		actionBar.setTitle("PicNews - Edycja profilu");
		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
		
		icon = (ImageView) findViewById(R.id.imageIcon);
		if(userPhoto!=null)
		{
        	 userPhoto.recycle();
        	 userPhoto = null;
        }
        userPhoto = decodeBase64(photou);
        icon.setImageBitmap(userPhoto);
//      icon.setImageBitmap - aktualne zdjêcie u¿ytkownika
		
		changePhoto = (Button) findViewById(R.id.buttonEditPicture);
		changePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,
		                   android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		        final int ACTIVITY_SELECT_IMAGE = 1234;
		        startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
				
			}
		});
		
		lv = (ListView) findViewById(R.id.listOfTags);
		
		//Array list of countries
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
		 
		  //create an ArrayAdaptar from the String Array
		  dataAdapter = new MyCustomAdapter(this, R.layout.edytujprofil_rowlayout, listOfTags);
		  // Assign adapter to ListView
		  lv.setAdapter(dataAdapter);
		 
		 
		  lv.setOnItemClickListener(new OnItemClickListener() {
		   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    // When clicked, show a toast with the TextView text
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
				
				//tu dodaæ wszystko do bazy
				
			}
		});
		
		}
	
protected void onActivityResult(int requestCode, int resultCode, Intent data) 
{
	super.onActivityResult(requestCode, resultCode, data); 

	switch(requestCode) { 
	case 1234:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();


	            yourSelectedImage = BitmapFactory.decodeFile(filePath);
	            newImage = Bitmap.createScaledBitmap(yourSelectedImage,300,300, true);
	            Bitmap iconB = Bitmap.createScaledBitmap(yourSelectedImage, 60, 60, true);
	            icon.setImageBitmap(iconB);	        
	        }
	    }

	};
	
	@Override
    public void onBackPressed()
    {
        super.onBackPressed(); 
        startActivity(new Intent(OknoEdytujProfil.this, OknoKonto.class));
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
			   convertView = vi.inflate(R.layout.edytujprofil_rowlayout, null);
			 
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