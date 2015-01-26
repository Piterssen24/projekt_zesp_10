package pl.example.apk;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;

public class OknoNews extends Activity {
       String[] menu;
       DrawerLayout dLayout;
       ListView dList;
       ArrayAdapter<String> adapter;
       ActionBarDrawerToggle mDrawerToggle;
       TextView trescPosta,autorPosta;
       Context context;
       ImageView zdjecie;
       LinearLayout ll;
       Fragment newpost, newpost2;
       public final static String APP_PATH_SD_CARD = "/PicNews";
       
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.oknonews_layout);
    
    context = getApplicationContext();
    ll = (LinearLayout) findViewById(R.id.zawartosc);
    newpost = new postElement();
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.add(R.id.zawartosc, newpost, "f1");
    newpost2 = new postElement();
    ft.add(R.id.zawartosc, newpost2,"f2");
    ft.commit();
   
    
    
 // setup action bar for tabs
    ActionBar actionBar =getActionBar();
    actionBar.setTitle("PicNews");
    actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
    actionBar.setSplitBackgroundDrawable(new ColorDrawable(Color.parseColor("#009900")));
    
    
    
    
    //DRAWERLAYOUT
       
    dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    final LinearLayout mDrawerLinear = (LinearLayout) findViewById(R.id.linear_drawer);
    dList = (ListView) findViewById(R.id.left_drawer);
    
    
    
    
    // Enabling Home button
    actionBar.setHomeButtonEnabled(true);

    // Enabling Up navigation
    actionBar.setDisplayHomeAsUpEnabled(true);
    
    
 // Getting reference to the ActionBarDrawerToggle
    mDrawerToggle = new ActionBarDrawerToggle( this,
        dLayout,
        R.drawable.ic_launcher,
        R.string.drawer_open,
        R.string.drawer_close){

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
            	getActionBar().setTitle("PicNews");
                invalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("PicNews - opcje");
                invalidateOptionsMenu();
            }
    };
    
 // Setting DrawerToggle on DrawerLayout
    dLayout.setDrawerListener(mDrawerToggle);

    // Creating an ArrayAdapter to add items to the listview mDrawerList
    adapter = new ArrayAdapter<String>(
        getBaseContext(),
        R.layout.drawer_list_item ,
        getResources().getStringArray(R.array.rivers)
    );

    // Setting the adapter on mDrawerList
    dList.setAdapter(adapter);
          
    
    
 // Setting item click listener for the listview mDrawerList
    dList.setOnItemClickListener(new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
            View view,
            int position,
            long id) {

                // Getting an array of rivers
                String[] rivers = getResources().getStringArray(R.array.rivers);

                //Currently selected river
                getActionBar().setTitle(rivers[position]);

                // Creating a fragment object
                DetailFragment rFragment = new DetailFragment();

                // Creating a Bundle object
                Bundle data = new Bundle();

                // Setting the index of the currently selected item of mDrawerList
                data.putInt("position", position);
                

                // Setting the position to the fragment
                rFragment.setArguments(data);
               
                
                // Getting reference to the FragmentManager
                FragmentManager fragmentManager = getFragmentManager();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment);

                // Committing the transaction
                ft.commit();

                // Closing the drawer
                dLayout.closeDrawer(mDrawerLinear);
        }
    });
    
    //dList.getSelectedView().setBackgroundColor(getResources().getColor(Color.RED));
          
  }
  

  /** Handling the touch event of app icon */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      if (mDrawerToggle.onOptionsItemSelected(item)) {
          return true;
      }
      
      switch (item.getItemId()) {
      case R.id.home:
    	  	Intent intent = new Intent(getApplicationContext(), OknoNews.class);
      		startActivity(intent);
          return true;
      case R.id.map:
    	  Intent intentmapa = new Intent(getApplicationContext(), OknoMapa.class);
    	  startActivity(intentmapa);         
          return true;
      case R.id.news:
    	  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
    	  startActivityForResult(cameraIntent, 0);
          return true;
      case R.id.konto:
    	  Intent intentkonto = new Intent(getApplicationContext(), OknoKonto.class);
    	  startActivity(intentkonto);
          
          return true;
      default:
          return super.onOptionsItemSelected(item);
  }
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     // TODO Auto-generated method stub
     super.onActivityResult(requestCode, resultCode, data);
     Uri u = data.getData();
     Intent intentzdjecie = new Intent(this, OknoNew.class);
     intentzdjecie.putExtra("imgurl", u);
     startActivity(intentzdjecie);
  }   

  

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
  }




}

