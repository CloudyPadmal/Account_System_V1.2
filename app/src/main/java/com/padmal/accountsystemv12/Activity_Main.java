package com.padmal.accountsystemv12;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.padmal.adapters.Adapter_NavDrawerList;
import com.padmal.helpers.Helper_DB;
import com.padmal.items.Item_NavDrawer;

import java.util.ArrayList;

public class Activity_Main extends AppCompatActivity {

   /*******************************************************************************************
    * Initiating Views [Navigation Drawer, Buttons, String resources]
    ******************************************************************************************/
   private DrawerLayout Nav_Drawer_Layout;                  //
   private ListView Nav_Drawer_List;                        //
   private ActionBarDrawerToggle Nav_Drawer_Toggle;         //
   private Button totalincomeview, totalexpenseview;        //
   private Helper_DB dbhelp;                                //
   private CharSequence Nav_Drawer_Title;                   //
   private String[] Nav_Menu_Titles;                        //
   private TypedArray Nav_Menu_Icons;                       //
   private String total_income, total_expense;              //
   private Adapter_NavDrawerList Nav_Drawer_Adapter;        //
   private int backButtonCount = 0;                         //
   /******************************************************************************************/

   /*******************************************************************************************
    * On create method for Main Activity
    ******************************************************************************************/
   @Override
   protected void onCreate (Bundle savedInstanceState) {

      // Initiate OnCreate Method and set UI
      super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);
      // Initiate database handler
      dbhelp = new Helper_DB(this);
      // Initiate HOLD button
      Button mainholdbutton = (Button) findViewById(R.id.mainholdbutton);
      // Initiate INCOME & EXPENSE Button views
      totalincomeview = (Button) findViewById(R.id.mainincomebutton);
      totalincomeview.setClickable(false);
      totalexpenseview = (Button) findViewById(R.id.mainexpensebutton);
      totalexpenseview.setClickable(false);

      // Get title of the page
      Nav_Drawer_Title = getTitle();

      // Get Menu items into respective arrays declared here ...
      Nav_Menu_Titles = getResources().getStringArray(R.array.array_nav_drawer_items);
      Nav_Menu_Icons = getResources().obtainTypedArray(R.array.array_nav_drawer_icons);

      Nav_Drawer_Layout = (DrawerLayout) findViewById(R.id.drawer_layout);
      Nav_Drawer_List = (ListView) findViewById(R.id.list_drawermenu);

      // Initiate the array list and let Async Task populate it ...
      new do_in_Background().execute("Populate List");

      // Set click listener to navigation drawer list
      Nav_Drawer_List.setOnItemClickListener(new SlideMenuClickListener());

      // Set a long click listener to show totals and when released, on click happens and content gets blank
      mainholdbutton.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick (View v) {

            // Calls background process to get totals and set view values ...
            new do_in_Background().execute("Set Totals");
            return false;
         }
      });

      mainholdbutton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick (View v) {

            // Clears the fields
            totalincomeview.setText("");
            totalexpenseview.setText("");
         }
      });

      // Enable and set action bar icons
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);

      Nav_Drawer_Toggle = new ActionBarDrawerToggle(this, Nav_Drawer_Layout, R.string.app_name, R.string.app_name)
      {
         public void onDrawerClosed(View view) {
            getSupportActionBar().setTitle(Nav_Drawer_Title);
            // calling onPrepareOptionsMenu() to show action bar icons
            invalidateOptionsMenu();
         }

         public void onDrawerOpened(View drawerView) {
            getSupportActionBar().setTitle(Nav_Drawer_Title);
            // calling onPrepareOptionsMenu() to hide action bar icons
            invalidateOptionsMenu();
         }
      };
      Nav_Drawer_Layout.setDrawerListener(Nav_Drawer_Toggle);
   }

   /*******************************************************************************************
    * Slide Menu Click Listener
    ******************************************************************************************/
   private class SlideMenuClickListener implements ListView.OnItemClickListener {

      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

         // Action on slider item click
         displayView(position);
      }
   }

   /*******************************************************************************************
    * Options Menu
    ******************************************************************************************/
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_main, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      // toggle nav drawer on selecting action bar app icon/title
      return Nav_Drawer_Toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
   }

   /*******************************************************************************************
    * Intent generator for different views
    ******************************************************************************************/
   private void displayView(int position) {

      switch (position) {

         /**** New Record Entries ****/
         case 0:
            Intent new_record = new Intent(getApplicationContext(), Activity_NewRecord.class);
            startActivity(new_record);
            break;

         /**** New Category Entries ****/
         case 1:
            Intent new_category = new Intent(getApplicationContext(), Activity_NewCategory.class);
            startActivity(new_category);
            break;

         /**** View Records ****/
         case 2:
            Intent view_record = new Intent(getApplicationContext(), Activity_Records.class);
            startActivity(view_record);
            break;

         /**** View Categories ****/
         case 3:
            Intent view_category = new Intent(getApplicationContext(), Activity_Categories.class);
            startActivity(view_category);
            break;

         /**** View Summary ****/
         case 4:
            Intent view_summary = new Intent(getApplicationContext(), Activity_Summary.class);
            startActivity(view_summary);
            break;

         default:
            break;
      }
      // Update selected item and title, then close the drawer
      Nav_Drawer_List.setItemChecked(position, true);
      Nav_Drawer_List.setSelection(position);
      Nav_Drawer_Layout.closeDrawer(Nav_Drawer_List);
   }

   /*******************************************************************************************
    * Supplementary method calls when using drawer toggle
    ******************************************************************************************/
   @Override
   protected void onPostCreate(Bundle savedInstanceState) {

      super.onPostCreate(savedInstanceState);
      // Sync the toggle state after onRestoreInstanceState has occurred.
      Nav_Drawer_Toggle.syncState();
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {

      super.onConfigurationChanged(newConfig);
      // Pass any configuration change to the drawer toggles
      Nav_Drawer_Toggle.onConfigurationChanged(newConfig);
   }

   /*******************************************************************************************
    * Async Task to populate the drawer and summary total values from DB
    ******************************************************************************************/
   private class do_in_Background extends AsyncTask<String, Void, Integer> {

      @Override
      protected Integer doInBackground (String... Command) {

         if (Command[0].equalsIgnoreCase("Populate List")) {
            // Create an array list of NavDrawer items
            ArrayList<Item_NavDrawer> nav_Drawer_Items_ArrayList = new ArrayList<>();

            // New Record
            nav_Drawer_Items_ArrayList.add(new Item_NavDrawer(Nav_Menu_Titles[ 0 ], Nav_Menu_Icons.getResourceId(0, - 1)));
            // New Category
            nav_Drawer_Items_ArrayList.add(new Item_NavDrawer(Nav_Menu_Titles[ 1 ], Nav_Menu_Icons.getResourceId(1, - 1)));
            // Records
            nav_Drawer_Items_ArrayList.add(new Item_NavDrawer(Nav_Menu_Titles[ 2 ], Nav_Menu_Icons.getResourceId(2, - 1)));
            // Categories
            nav_Drawer_Items_ArrayList.add(new Item_NavDrawer(Nav_Menu_Titles[ 3 ], Nav_Menu_Icons.getResourceId(3, - 1)));
            // Summary
            nav_Drawer_Items_ArrayList.add(new Item_NavDrawer(Nav_Menu_Titles[ 4 ], Nav_Menu_Icons.getResourceId(4, - 1)));

            // Recycle the typed array (Requirement)
            Nav_Menu_Icons.recycle();
            // Setting Drawer Adapter
            Nav_Drawer_Adapter = new Adapter_NavDrawerList(getApplicationContext(), nav_Drawer_Items_ArrayList);
            return 100;
         }

         if (Command[0].equalsIgnoreCase("Set Totals")) {
            try {
               // Get totals from the DB helper
               total_income = dbhelp.getTableTotals_String("Income").toString();
               total_expense = dbhelp.getTableTotals_String("Expense").toString();
            }
            // If the app is started for the first time, there won't be any entries ...
            catch (NullPointerException e) {
               // Makes it all zeros so ...
               total_income = "0.00";
               total_expense = "0.00";
            }
            // Return 200 to post execute to set values to buttons
            return 200;
         }

         return null;
      }

      @Override
      protected void onPostExecute (Integer Command) {

         if (Command == 100) {
            Nav_Drawer_List.setAdapter(Nav_Drawer_Adapter);
         }

         if (Command == 200) {
            // Make button text income and expense
            totalincomeview.setText(total_income);
            totalexpenseview.setText(total_expense);
         }
      }
   }

   /*******************************************************************************************
    * Back Key Control
    ******************************************************************************************/
   @Override
   public void onBackPressed () {

      if(backButtonCount >= 1)
      {
         Intent intent = new Intent(Intent.ACTION_MAIN);
         intent.addCategory(Intent.CATEGORY_HOME);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         startActivity(intent);
      }
      else
      {
         Toast.makeText(this, getResources().getString(R.string.back_key_pressed), Toast.LENGTH_SHORT).show();
         backButtonCount++;
      }
   }
}
