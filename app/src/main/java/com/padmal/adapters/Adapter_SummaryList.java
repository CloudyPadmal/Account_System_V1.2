package com.padmal.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.padmal.accountsystemv12.R;
import com.padmal.items.Item_Summary;

import java.util.ArrayList;

public class Adapter_SummaryList extends BaseAdapter {

    // Context and array list variables
    private Context context;
    private ArrayList<Item_Summary> SummaryListItems;

    // Constructor for the custom adapter
    public Adapter_SummaryList (Context context, ArrayList<Item_Summary> ListItems) {

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
            convertView = layoutInflater.inflate(R.layout.list_item_summary, null);
        }
        // Instantiate the text views in the list view
        TextView Category = (TextView) convertView.findViewById(R.id.Summary_Category);
        TextView Amount = (TextView) convertView.findViewById(R.id.Summary_Amount);
        // Assign values to the text views
        Category.setText(SummaryListItems.get(position).getCategory());
        Amount.setText(SummaryListItems.get(position).getTotal().toString());
        // Set a tag of item to refer later
        convertView.setTag(SummaryListItems.get(position));

        return convertView;
    }
}
