package pl.example.apk;

import java.util.ArrayList;

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
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Toast;

public class OknoEdytujProfil extends Activity {
	
	ActionBar actionBar;
	Context context;
	ListView lv;
	TagCheckModel[] modelItems;
	Button changePhoto, submit;
	Bitmap yourSelectedImage, newImage;
	ImageView icon;
	MyCustomAdapter dataAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edytujprofil_layout);
		
		actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
		actionBar.setTitle("PicNews - Edycja profilu");
		actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
		
		icon = (ImageView) findViewById(R.id.imageIcon);
		
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
		  ArrayList<TagCheckModel> listOfTags = new ArrayList<TagCheckModel>();
		  TagCheckModel tcm = new TagCheckModel("Afghanistan",false);
		  listOfTags.add(tcm);
		  tcm = new TagCheckModel("Albania",true);
		  listOfTags.add(tcm);
		  tcm = new TagCheckModel("Algeria",false);
		  listOfTags.add(tcm);
		  tcm = new TagCheckModel("American Samoa",true);
		 
		  //create an ArrayAdaptar from the String Array
		  dataAdapter = new MyCustomAdapter(this,
		    R.layout.edytujprofil_rowlayout, listOfTags);
		  // Assign adapter to ListView
		  lv.setAdapter(dataAdapter);
		 
		 
		  lv.setOnItemClickListener(new OnItemClickListener() {
		   public void onItemClick(AdapterView<?> parent, View view,
		     int position, long id) {
		    // When clicked, show a toast with the TextView text
		    TagCheckModel tcm = (TagCheckModel) parent.getItemAtPosition(position);
		    Toast.makeText(getApplicationContext(),
		      "Clicked on Row: " + tcm.getName()+position, 
		      Toast.LENGTH_LONG).show();
		   }
		  });
		
		submit = (Button) findViewById(R.id.buttonAcceptChanges);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(newImage!=null)
				{
					//tu dodaæ newImage do bazy jako nowe zdjêcie u¿ytkownika
				}
				
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
		   final int id = position;
		 
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
		      Toast.makeText(getApplicationContext(),
		       "Clicked on Checkbox: " + cb.getText() +
		       " is " + cb.isChecked()+id, 
		       Toast.LENGTH_LONG).show();
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