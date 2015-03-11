package pl.example.apk;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class OknoKonto extends Activity {
	
	ImageView yourPicture;
	ExpandableListAdapter listAdapter;
	ExpandableListAdapterCheck listAdapterCheck;
    ExpandableListView expListView;
    ExpandableListView expListView2;
    ArrayList<String> listDataHeader;
    ArrayList<String> listDataHeaderCheck;
    HashMap<String, List<String>> listDataChild;
    HashMap<String, List<String>> listDataChildCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oknokonto_layout);
        
        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        bar.setTitle("PicNews - Twoje konto");
        bar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
        
        yourPicture = (ImageView) findViewById(R.id.userPhoto);
        LayoutParams params = (LayoutParams) yourPicture.getLayoutParams();
        params.width = 120;
        params.height = 200;
        // existing height is ok as is, no need to edit it
        yourPicture.setLayoutParams(params);
        
        
        //expandablelist
        
        
     // get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableList);
 
        // preparing list data
        // Adding child data
        listDataHeader = new ArrayList<String>();
        listDataHeader.add("Typy news'ów");
        listDataHeader.add("Obserwowani dziennikarze");
        listDataHeader.add("Obserwuj¹cy u¿ytkownicy");
 
        // Adding child data
        List<String> typy = new ArrayList<String>();
        typy.add("Sport");
        typy.add("Kultura");
        typy.add("Utrudnienia na drodze");
        typy.add("Polityczne");
        typy.add("Charytatywne");
        typy.add("Imprezy");
        typy.add("12 Angry Men");
 
        List<String> obserwowani = new ArrayList<String>();
        obserwowani.add("The Conjuring");
        obserwowani.add("Despicable Me 2");
        obserwowani.add("Turbo");
        obserwowani.add("Grown Ups 2");
        obserwowani.add("Red 2");
        obserwowani.add("The Wolverine");
 
        List<String> obserwujacy = new ArrayList<String>();
        obserwujacy.add("2 Guns");
        obserwujacy.add("The Smurfs 2");
        obserwujacy.add("The Spectacular Now");
        obserwujacy.add("The Canyons");
        obserwujacy.add("Europa Report");
 
        listDataChild = new HashMap<String, List<String>>();
        //listDataChildCheck = new HashMap<String, List<String>>();
       // listDataChildCheck.put(listDataHeaderCheck.get(0), typy); // Header, Child data
        listDataChild.put(listDataHeader.get(0), typy); // Header, Child data
        listDataChild.put(listDataHeader.get(1), obserwowani);
        listDataChild.put(listDataHeader.get(2), obserwujacy);
 
        //listAdapterCheck = new ExpandablelistAdapterCheck(this, listDataHeaderCheck, listDataChildCheck);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
       // expListView.setAdapter(listAdapterCheck);
        expListView.setAdapter(listAdapter);
 
        // Listview Group click listener
       /* expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        }); 
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        }); */
        
        //endofexpandablelist
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
        case R.id.home:
      	  	Intent intent = new Intent(getApplicationContext(), OknoNews.class);
        		startActivity(intent);
            return true;
        case R.id.map:
           
            return true;
        case R.id.news:
            
            return true;
        case R.id.konto:
      	  Intent intentkonto = new Intent(getApplicationContext(), OknoKonto.class);
      	  startActivity(intentkonto);
            
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
    }
    
    //explist
    


    
    //endexplist
    
    
    
    
}