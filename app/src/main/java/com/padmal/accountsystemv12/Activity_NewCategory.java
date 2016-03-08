package com.padmal.accountsystemv12;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.padmal.helpers.Helper_DB;

public class Activity_NewCategory extends AppCompatActivity {

   // Defining types
   private EditText newcategoryTextCategory, newcategoryTextDescription;
   private ToggleButton newcategoryType;
   private Helper_DB dbhelp;
   private String NCEType, NCECategory, NCEDescription;
   private Bundle details_from_up;
   private int getinfo = 0;

   @Override
   protected void onCreate (Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_new_category);

      // Opens a database helper
      dbhelp = new Helper_DB(this);

      // Initiating Type toggle button
      newcategoryType = (ToggleButton) findViewById(R.id.newcategorytype);
      // Initiating Category text view
      newcategoryTextCategory = (EditText) findViewById(R.id.newcategorytextcategory);
      // Initiating Description text view
      newcategoryTextDescription = (EditText) findViewById(R.id.newcategorytextdescription);
      // Initiating buttons UPDATE
      Button newcategoryButtonUpdate = (Button) findViewById(R.id.newcategorybuttonupdate);

      // Get Bundle from if this is called from a previous view
      details_from_up = getIntent().getExtras();

      // If bundle has extras, it is invoked by update an existing category action
      if (details_from_up != null) {

         final String Old_category = details_from_up.getString("Category");
         final String Old_description = details_from_up.getString("Description");
         final String Old_type = details_from_up.getString("Type");
         final String what_to_do;

         if (String.valueOf(details_from_up.getString("From")).equalsIgnoreCase("New Record")) {
            what_to_do = "New";
            newcategoryType.setChecked(String.valueOf(Old_type).equalsIgnoreCase("Income"));
            getinfo = 10;
         }
         else {
            what_to_do = "Update";
            newcategoryButtonUpdate.setText("Update");
            getSupportActionBar().setTitle("Update Category");
            newcategoryTextCategory.setText(Old_category);
            newcategoryTextDescription.setText(Old_description);
            newcategoryType.setChecked(String.valueOf(Old_type).equalsIgnoreCase("Income"));
            getinfo = 20;
         }

         // Once the button UPDATE is clicked ....
         newcategoryButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getUserInput();
               new handleDBActions().execute(what_to_do);
            }
         });
      }
        /* Then this view is called by the main activity directly where user creates a new category */
      else {

         // Once the button UPDATE is clicked ....
         newcategoryButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getUserInput();
               new handleDBActions().execute("New");
            }
         });
      }
   }

   /******************************************************************************************
    * Get Input from User
    *****************************************************************************************/
   private void getUserInput() {

      // Get Type
      NCEType = newcategoryType.isChecked() ? "Income" : "Expense";
      // Get Category
      NCECategory = (newcategoryTextCategory.getText().toString()).replace(" ", "_");
      // Get Description
      NCEDescription = newcategoryTextDescription.getText().toString();
   }

   /******************************************************************************************
    * Options Menu
    *****************************************************************************************/
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_new_category, menu);
      return true;
   }

   /******************************************************************************************
    * Clear Button
    *****************************************************************************************/
   public void clearButtonClicked(View view) {

      newcategoryTextCategory.setText("");
      newcategoryTextDescription.setText("");
      newcategoryTextCategory.setHint("");
      newcategoryTextDescription.setHint("");
   }

   /******************************************************************************************
    * Async Task
    *****************************************************************************************/
   private class handleDBActions extends AsyncTask<String, Void, Integer> {

      @Override
      protected Integer doInBackground(String... Entries) {

         String ASType = NCEType;
         String ASCategory = NCECategory;
         String ASDescription = NCEDescription;
         
         if (Entries[0].equalsIgnoreCase("New")){

            // Insert this category into the table and return result received
            return dbhelp.insertNewCategory(ASType, ASCategory, ASDescription) ? 1 : 0;
         } else {

            String OldCategory = details_from_up.getString("Category");
            // Else this is Updating, Update this and return result
            // Update all the existing records of this category if category changed
            if (!(String.valueOf(OldCategory).equalsIgnoreCase(NCECategory))) {
               dbhelp.updateUpdatedCategoryRecord(NCEType, OldCategory, NCECategory);
            }
            return dbhelp.updateCategory(OldCategory, NCEType, NCECategory, NCEDescription) > 0 ? 2 : 0;
         }
      }

      @Override
      protected void onPostExecute(Integer result) {

         if (result == 1){

            dbhelp.close();
            Toast.makeText(Activity_NewCategory.this, "Added Successfully", Toast.LENGTH_SHORT).show();

            // Go back to main page
            Intent intent = new Intent(Activity_NewCategory.this, Activity_Main.class);
            Activity_NewCategory.this.finish();
            startActivity(intent);

         } else if (result == 2) {

            dbhelp.close();
            Toast.makeText(Activity_NewCategory.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

            // Go back to main page
            Intent intent = new Intent(Activity_NewCategory.this, Activity_Main.class);
            Activity_NewCategory.this.finish();
            startActivity(intent);
         } else {

            Toast.makeText(Activity_NewCategory.this, "May be something is blank", Toast.LENGTH_SHORT).show();
         }
      }
   }

   /******************************************************************************************
    * Back click operations
    *****************************************************************************************/
   @Override
   public void onBackPressed() {

      if (getinfo == 10) {
         Intent back_to_new_record = new Intent(getApplicationContext(), Activity_NewRecord.class);
         Activity_NewCategory.this.finish();
         startActivity(back_to_new_record);
      }
      else if (getinfo == 20) {
         Intent back_to_categories = new Intent(getApplicationContext(), Activity_Categories.class);
         Activity_NewCategory.this.finish();
         startActivity(back_to_categories);
      }
      else {
         Intent back_to_main = new Intent(getApplicationContext(), Activity_Main.class);
         Activity_NewCategory.this.finish();
         startActivity(back_to_main);
      }
   }

}
