package com.padmal.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.padmal.accountsystemv12.R;
import com.padmal.items.Item_Category;

import java.util.ArrayList;


public class Adapter_CategoriesList extends BaseAdapter {

    // Context and array list variables
    private Context context;
    private ArrayList<Item_Category> SummaryListItems;

    // Constructor for the custom adapter
    public Adapter_CategoriesList (Context context, ArrayList<Item_Category> ListItems) {
        // Define and point out to this instance
        this.context = context;
        this.SummaryListItems = ListItems;
    }

    @Override
    public int getCount() {
        return SummaryListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return SummaryListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Handle null views first and set it to the custom list view
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            // Set view to the list item xml we created
            convertView = layoutInflater.inflate(R.layout.list_item_category, null);
        }
        // Get text views instantiated
        TextView title = (TextView) convertView.findViewById(R.id.Category_Title);
        TextView description = (TextView) convertView.findViewById(R.id.Category_Description);
        // Set text views with values
        title.setText(SummaryListItems.get(position).getCategory());
        description.setText(SummaryListItems.get(position).getDescription());
        // Set a tag of the item to refer clicked item details.
        convertView.setTag(SummaryListItems.get(position));

        return convertView;
    }
}
