package pl.example.apk;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<TagCheckModel>{
 TagCheckModel[] modelItems = null;
 Context context;
 CheckBox cb;
 
 public CustomAdapter(Context context, TagCheckModel[] resource) {
 super(context,R.layout.tagrow_layout,resource);
 // TODO Auto-generated constructor stub
 this.context = context;
 this.modelItems = resource;
 }
 @Override
 public View getView(int position, View convertView, ViewGroup parent) {
 // TODO Auto-generated method stub
 LayoutInflater inflater = ((Activity)context).getLayoutInflater();
 convertView = inflater.inflate(R.layout.tagrow_layout, parent, false); 
 TextView name = (TextView) convertView.findViewById(R.id.textView1);
 cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
 name.setText(modelItems[position].getName());
 /*if(modelItems[position].getValue() == 1)
 cb.setChecked(true);
 else
 cb.setChecked(false);*/
 return convertView;
 }
 
}