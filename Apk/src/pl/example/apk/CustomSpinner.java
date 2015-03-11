package pl.example.apk;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

class CustomSpinner extends ArrayAdapter<String> {
        public CustomSpinner(Context context, int resource,
                int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

    public View getView(int position, View convertView,
            ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        // apply the style and sizes etc to the Text view from this view v
        // like ((TextView)v).setTextSize(...) etc
        return v;
    }

    public View getDropDownView(int position, View convertView,
            ViewGroup parent) {
    	View v2 = super.getView(position, convertView, parent);
        // apply the style and sizes etc to the Text view from this view v
        // like ((TextView)v).setTextSize(...) etc
        return v2;
        }
}