package pl.example.apk;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class DetailFragment extends Fragment{
 
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
 
        // Retrieving the currently selected item number
        int position = getArguments().getInt("position");
 
        String[] rivers = getResources().getStringArray(R.array.rivers);
        // List of rivers
        
 
        // Creating view correspoding to the fragment
        View v = inflater.inflate(R.layout.menu_detail_fragment, container, false);
 
        // Getting reference to the TextView of the Fragment
        TextView tv = (TextView) v.findViewById(R.id.listCategories);
 
        // Setting currently selected river name in the TextView
        tv.setText(rivers[position]);
 
        // Updating the action bar title
        getActivity().getActionBar().setTitle(rivers[position]);
 
        return v;
    }
}