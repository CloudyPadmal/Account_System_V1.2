package com.padmal.accountsystemv12;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.padmal.adapters.Adapter_CategoriesList;
import com.padmal.helpers.Helper_DB;
import com.padmal.items.Item_Category;

import java.util.ArrayList;

/***************************************************************************************************
 * Categories
 *
 * A list layout with all the categories set in a custom adapter Adapter_CategoriesList. Background
 * of item elements is set to @drawable/abc_item_background_holo_dark in the default drawables set.
 *
 * Have two ways to access into this class.
 *  1. Main Page
 *  2. Browse Categories from New Record Entry Page
 *
 * Allows editing or deletion of a single category item upon long click.
 *
 **************************************************************************************************/

public class Activity_Categories extends AppCompatActivity {

   // Defining variables
   private ListView categoryScrollView;
   private Helper_DB dbHelp;
   private Bundle details_brought_in;

   @Override
   protected void onCreate (Bundle savedInstanceState) {

      // Set content to activity_categories
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_categories);

      // Assigning variables to objects
      categoryScrollView = (ListView) findViewById(R.id.viewcatsummary);

      // Create a database object
      dbHelp = new Helper_DB(this);

      // Registering for the context menu
      registerForContextMenu(categoryScrollView);

      // Get a bundle of information from previous view
      details_brought_in = getIntent().getExtras();

      // View categories carries in information from New Record Entry, else it is from a normal view
      if (details_brought_in != null) {

         // "From" data will be sent only from New Record view
         if (String.valueOf(details_brought_in.getString("From")).equalsIgnoreCase("New Record")) {
            handleFromNewEntry();
         }
      }
      // Then this is invoked by main menu
      else {

         // Set the list of categories using AsyncTask
         new setCategoryList().execute("All");
      }
   }

   /************************************************************************************************
    * Handle New Entry View Invokes
    ***********************************************************************************************/
   private void handleFromNewEntry () {

      // Get the type of categories wanted
      final String type_of_categories_wanted = details_brought_in.getString("Type");

      // Set the list accordingly
      new setCategoryList().execute(type_of_categories_wanted);

        /* If the previous view is from New Record, onItemSelected method will be called and a new
        intent action will be coded to go back to the new record view and I will change settings in
        New Record to set a special view to get details sent from here */

      categoryScrollView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

            /* When an item is clicked, get the view of the row and get the category name and send
            it to New Record view to set the spinner there */

            // Creates a TextView to get text view from the selected item
            TextView Category = (TextView) view.findViewById(R.id.Category_Title);

            Intent back_to_new_entry = new Intent(getApplicationContext(), Activity_NewRecord.class);

            back_to_new_entry.putExtra("From", "View Categories");
            back_to_new_entry.putExtra("Type", details_brought_in.getString("Type"));
            back_to_new_entry.putExtra("Date", details_brought_in.getString("Date"));
            back_to_new_entry.putExtra("Category", Category.getText().toString());
            back_to_new_entry.putExtra("Amount", details_brought_in.getString("Amount"));

            Activity_Categories.this.finish();
            startActivity(back_to_new_entry);
         }
      });
   }

   /************************************************************************************************
    * Context Menu Stuff
    ***********************************************************************************************/
   @Override
   public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      MenuInflater inflater = getMenuInflater();

      // Set view to long click menu created by me
      inflater.inflate(R.menu.longclickmenu_category, menu);
   }

   @Override
   public boolean onContextItemSelected (MenuItem item) {

      // Get adapter view's info
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

      Item_Category category = (Item_Category) info.targetView.getTag();

      // Get Category and Description from the view
      final String _Category = category.getCategory();
      String _Description = category.getDescription();
      String _Type = category.getType();

      switch (item.getItemId()) {

         // When Delete clicked ...
         case R.id.longmenudelete:

            // Build and create an alert view to get confirmation
            new AlertDialog.Builder(Activity_Categories.this)
                  .setTitle("Delete Category")
                  .setMessage("Are you sure you want to delete this category?")
                  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                     public void onClick (DialogInterface dialog, int which) {

                        // Perform deletion ...
                        if (dbHelp.deleteCategory(_Category) > 0) {

                           // Create a toast to show success
                           Toast.makeText(getApplicationContext(), _Category + " Deleted Successfully", Toast.LENGTH_SHORT).show();

                           // Calls AsyncTask to make the list again ...
                           new setCategoryList().execute("All");
                        }
                        else {
                           Toast.makeText(getApplicationContext(), "Something went Wrong :(", Toast.LENGTH_SHORT).show();
                        }
                     }
                  })
                  .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                     public void onClick (DialogInterface dialog, int which) {
                        // Cancelled
                     }
                  })
                  .show();
            return true;

         // When Update clicked ...
         case R.id.longmenuedit:

            // Create a new intent to go to the Update Category class
            Intent record_update = new Intent(getApplicationContext(), Activity_NewCategory.class);

            // Send Type, Category and Description with it
            record_update.putExtra("Category", _Category);
            record_update.putExtra("Description", _Description);
            record_update.putExtra("Type", _Type);

            // Start update category
            startActivity(record_update);

            return true;

         default:
            return super.onContextItemSelected(item);
      }
   }

   /************************************************************************************************
    * ASYNC TASK to get totals from database
    ***********************************************************************************************/
   private class setCategoryList extends AsyncTask<String, Void, Adapter_CategoriesList> {

      @Override
      protected Adapter_CategoriesList doInBackground (String... Table_Name) {

         // This will be called in two places depending on how this view was started.
         // If it is from main menu, all categories must be shown ...
         if (Table_Name[ 0 ].equalsIgnoreCase("All")) {

            // Get the list of categories from database
            ArrayList<Item_Category> arrayList = dbHelp.getAllCategoriesWithDescription_ArrayList();
            return new Adapter_CategoriesList(getApplicationContext(), arrayList);
         }
         // I invoke this view from New Record Entry sometimes. Then I need a specific type of
         // categories only unlike before.
         else {

            // Get the list of type_of categories from database
            ArrayList<Item_Category> arrayList = dbHelp.getAllCategoriesAndDescriptionsOfThisType_ArrayList(Table_Name[ 0 ]);
            return new Adapter_CategoriesList(getApplicationContext(), arrayList);
         }
      }

      @Override
      protected void onPostExecute (Adapter_CategoriesList Adapter) {

         // Assign the adapter to the list view
         categoryScrollView.setAdapter(Adapter);
      }
   }
   /**********************************************************************************************/
}
