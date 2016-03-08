package com.padmal.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.padmal.accountsystemv12.R;
import com.padmal.items.Item_Record;

import java.util.ArrayList;


public class Adapter_RecordsList extends BaseAdapter {

    // Context and array list variables
    private Context context;
    private ArrayList<Item_Record> SummaryListItems;

    // Constructor for the custom adapter
    public Adapter_RecordsList (Context context, ArrayList<Item_Record> ListItems) {

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
            convertView = layoutInflater.inflate(R.layout.list_item_record, null);
        }
        // Define text views and instantiate them
        TextView date = (TextView) convertView.findViewById(R.id.Record_Date);
        TextView category = (TextView) convertView.findViewById(R.id.Record_Category);
        TextView amount = (TextView) convertView.findViewById(R.id.Record_Amount);
        // Set a tag of items to refer when clicked
        convertView.setTag(SummaryListItems.get(position));
        // Set text views with values
        date.setText(SummaryListItems.get(position).getDate());
        category.setText(SummaryListItems.get(position).getCategory());
        amount.setText(SummaryListItems.get(position).getAmount().toString());

        return convertView;
    }
}
