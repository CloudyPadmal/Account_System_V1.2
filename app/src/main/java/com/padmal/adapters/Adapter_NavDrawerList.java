package com.padmal.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.padmal.accountsystemv12.R;
import com.padmal.items.Item_NavDrawer;

import java.util.ArrayList;

public class Adapter_NavDrawerList extends BaseAdapter {

   // Context and array list variables
   private Context context;
   private ArrayList<Item_NavDrawer> NavDrawerItems;

   // Constructor for the custom adapter
   public Adapter_NavDrawerList (Context context, ArrayList<Item_NavDrawer> navDrawerItems) {
      this.context = context;
      this.NavDrawerItems = navDrawerItems;
   }

   @Override
   public int getCount () {
      return NavDrawerItems.size();
   }

   @Override
   public Object getItem (int position) {
      return NavDrawerItems.get(position);
   }

   @Override
   public long getItemId (int position) {
      return position;
   }

   @Override
   public View getView (int position, View convertView, ViewGroup parent) {

      // Handling null convertView
      if (convertView == null) {

         LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
         // Set view to the list item xml we created
         convertView = mInflater.inflate(R.layout.list_item_navigation, null);
      }
      // Point out different views within convertView
      ImageView navIcon = (ImageView) convertView.findViewById(R.id.nav_icon);
      TextView navTitle = (TextView) convertView.findViewById(R.id.nav_title);
      // Set values to those views
      navIcon.setImageResource(NavDrawerItems.get(position).getIcon());
      navTitle.setText(NavDrawerItems.get(position).getTitle());

      // Return the view
      return convertView;
   }
}
