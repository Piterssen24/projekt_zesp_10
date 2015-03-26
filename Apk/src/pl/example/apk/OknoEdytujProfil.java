package pl.example.apk;

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
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
		//tu trzeba pobraæ listê tagów z bazy 
		modelItems = new TagCheckModel[5];
		modelItems[0] = new TagCheckModel("pizza", 0);
		modelItems[1] = new TagCheckModel("burger", 1);
		modelItems[2] = new TagCheckModel("olives", 1); 
		modelItems[3] = new TagCheckModel("orange", 0); 
		modelItems[4] = new TagCheckModel("tomato", 1);
		CustomAdapter adapter = new CustomAdapter(this, modelItems);
		lv.setAdapter(adapter);
		
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

}