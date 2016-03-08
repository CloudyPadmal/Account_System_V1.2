package com.padmal.accountsystemv12;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.padmal.helpers.Helper_DB;

import java.util.ArrayList;
import java.util.Calendar;

public class Activity_NewRecord extends AppCompatActivity {

    /******************************************************************************************
     * Initiating Views [DBHelper, Buttons, String resources]
     *****************************************************************************************/
    private static Button newRecordDate;                            //
    static private int _id;                                          //
    private Button nRBtnUpdate, nRDescription;                      //
    private ToggleButton nRType;                                    //
    private Helper_DB dbHelp;                                       //
    private Spinner nRSpinner;                                      //
    private EditText nRAmount;                                      //
    private Bundle details_from_up;                                 //
    private String NRE_Date, NRE_Type, NRE_Category, NRE_Amount;    //
    private int position_of_category = 0;                           //
    /*****************************************************************************************/

    /******************************************************************************************
     * On create method for New Record
     *****************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); setContentView(R.layout.activity_new_record);

        dbHelp = new Helper_DB(this);
        newRecordDate = (Button) findViewById(R.id.newrecordentrydate); newRecordDate.setText(setDate());
        nRType = (ToggleButton) findViewById(R.id.newrecordentrytype);
        nRSpinner = (Spinner) findViewById(R.id.newrecordentrycategory);
        nRDescription = (Button) findViewById(R.id.newrecordentrydescription); nRDescription.setClickable(false);
        nRAmount = (EditText) findViewById(R.id.newrecordentryamount);
        nRBtnUpdate = (Button) findViewById(R.id.newrecordentrybuttonupdate);

        // Gets a bundle of information from the previous activity
        details_from_up = getIntent().getExtras();

        // Get the selected category from category list and show it in the description button
        nRSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // When an item is selected from the spinner, respective description will be shown
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String description_to_show = dbHelp.getCategoryDescription_String(nRSpinner.getSelectedItem().toString());
                Activity_NewRecord.this.nRDescription.setText(description_to_show);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {/**/}
        });

        // If there are stuff coming from a previous view,
        if (details_from_up != null) {

            // If the view is created after browsing of categories, the selected item has to be selected
            if (String.valueOf(details_from_up.getString("From")).equalsIgnoreCase("View Categories")) {handleAfterBrowsing();}
            // I've sent "Update Records" as extra but no need to check if View Categories is checked
            else {handleUpdateRecords();}
        }
        // If no bundle came in, go alone with New entry ...
        else {handleNewRecords();}
    }

    /******************************************************************************************
     * New Records
     *****************************************************************************************/
    private void handleNewRecords() {

        new setSpinner().execute("Expense", "0");

        // Set the onCheckedChange listener to grab type changes and change category list accordingly
        nRType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new setSpinner().execute(isChecked ? "Income" : "Expense", "0");
            }
        });

        nRBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input data from views
                getInputData();
                // Try to update entries
                if (dbHelp.insertNewEntry(NRE_Type, _id, NRE_Date, NRE_Category, NRE_Amount)) {
                    uponSuccess(getString(R.string.entry_successful));
                }
                // Fails if SQLite sends an error ...
                else {
                    Toast.makeText(Activity_NewRecord.this, getString(R.string.entry_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /******************************************************************************************
     * Updating Records
     *****************************************************************************************/
    private void handleUpdateRecords() {

        // Setting onCheckedChange Lister to grab type changes and change the category list accordingly
        nRType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new setSpinner().execute(isChecked ? "Income" : "Expense", "0");
            }
        });

        // Get details from view records
        final String Old_Date = String.valueOf(details_from_up.getString("Date"));
        final String Old_Table = String.valueOf(details_from_up.getString("Table"));
        final String Old_Amount = details_from_up.getString("Amount");
        final String Old_Category = details_from_up.getString("Category");

        // Set title of the page
        getSupportActionBar().setTitle("Edit Record");
        // Change button name to update
        nRBtnUpdate.setText(getString(R.string.update));
        // Set Date to Old one
        newRecordDate.setText(Old_Date);
        // Set type to Old one
        nRType.setChecked(Old_Table.equalsIgnoreCase("Income")); nRType.setClickable(false);
        // Set Amount to Old one
        nRAmount.setText(Old_Amount);
        // Set the spinner
        new setSpinner().execute(Old_Table, Old_Category);

        nRBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from user
                getInputData();
                // Try updating that entry
                if (dbHelp.updateRecord(NRE_Type, Old_Date, Old_Category, _id, NRE_Date, NRE_Category, NRE_Amount) > 0) {
                    uponSuccess(getString(R.string.update_successful));
                }
                // Fails if SQLite sends an error ...
                else {
                    Toast.makeText(Activity_NewRecord.this, getString(R.string.entry_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /******************************************************************************************
     * Browse Categories
     *****************************************************************************************/
    private void handleAfterBrowsing() {

        // Setting onCheckedChange Lister to grab type changes and change the category list accordingly
        nRType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new setSpinner().execute(isChecked ? "Income" : "Expense", "0");
            }
        });

        // Get the selected category
        final String category_browsed = details_from_up.getString("Category");
        final String type_browsed = String.valueOf(details_from_up.getString("Type"));
        final String date_came_in = details_from_up.getString("Date");
        final String amount_came_in = details_from_up.getString("Amount");

        // Set date and amount if there weren't any before
        if (date_came_in != null) {newRecordDate.setText(date_came_in);}
        if (amount_came_in != null) {nRAmount.setText(amount_came_in);}
        // Type of categories must be the browsed type categories
        nRType.setChecked(type_browsed.equalsIgnoreCase("Income"));
        // Set the spinner and browsed item as selected
        new setSpinner().execute(type_browsed, category_browsed);

        // Once the button UPDATE is clicked .......
        nRBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input from views
                getInputData();
                // Insert those values to the respective database table ...
                if (dbHelp.insertNewEntry(NRE_Type, _id, NRE_Date, NRE_Category, NRE_Amount)) {
                    uponSuccess(getString(R.string.entry_successful));
                }
                // Fails if SQLite sends an error ...
                else {
                    Toast.makeText(Activity_NewRecord.this, getString(R.string.entry_failed), Toast.LENGTH_SHORT).show();
                }
                }


        });
    }

    /******************************************************************************************
     * Get Input from User
     *****************************************************************************************/
    private void getInputData() {

        // Get date
        Activity_NewRecord.this.NRE_Date = newRecordDate.getText().toString();
        // Get type
        Activity_NewRecord.this.NRE_Type = nRType.isChecked() ? "Income" : "Expense";
        // Get category
        Activity_NewRecord.this.NRE_Category = nRSpinner.getSelectedItem().toString();
        // Get amount
        Activity_NewRecord.this.NRE_Amount = nRAmount.getText().toString();
    }

    /******************************************************************************************
     * Clean Up Process
     *****************************************************************************************/
    private void uponSuccess(String Msg) {

        // Closes the DB handler
        Activity_NewRecord.this.dbHelp.close();

        // Display successful message
        Toast.makeText(Activity_NewRecord.this, Msg, Toast.LENGTH_SHORT).show();

        // Go back to main screen
        Intent go_to_main_page = new Intent(Activity_NewRecord.this, Activity_Main.class);
        Activity_NewRecord.this.finish();
        startActivity(go_to_main_page);
    }

    /******************************************************************************************
     * Options Menu
     *****************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            // View category summary. This will view a list of categories and create a path back.
            case R.id.new_record_entry_view_cat_summary:

                // Go for browse categories
                Intent view_cat_summary = new Intent(Activity_NewRecord.this, Activity_Categories.class);

                // Send that to the next view
                view_cat_summary.putExtra("Type", nRType.isChecked() ? "Income" : "Expense");
                view_cat_summary.putExtra("From", "New Record");
                view_cat_summary.putExtra("Date", newRecordDate.getText().toString());
                view_cat_summary.putExtra("Amount", nRAmount.getText().toString());

                // Start activity
                Activity_NewRecord.this.finish();
                startActivity(view_cat_summary);
                return true;

            case R.id.new_record_entry_new_cat:

                // New category entry view
                Intent new_category = new Intent(Activity_NewRecord.this, Activity_NewCategory.class);

                // Send the selected category type to set category type in new category entry
                new_category.putExtra("Type", nRType.isChecked() ? "Income" : "Expense");
                new_category.putExtra("From", "New Record");

                // Start activity
                Activity_NewRecord.this.finish();
                startActivity(new_category);
                return true;

            default:
                return true;
        }
    }

    /******************************************************************************************
     * Clear Button Click Handler
     *****************************************************************************************/
    public void clearButtonClicked(View view) {

        nRAmount.setText("");
    }

    /******************************************************************************************
     * Set Date for the first time
     *****************************************************************************************/
    private String setDate() {

        String[] month_list = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        _id = Integer.parseInt(String.valueOf(year-2000) + String.valueOf(month+10) + String.valueOf(day+10));
        // Return the date set
        return (Integer.toString(day) + "-" + month_list[month] + "-" + Integer.toString(year).substring(2));
    }

    /******************************************************************************************
     * Date Picker Fragment
     *****************************************************************************************/
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            String[] month_list = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            newRecordDate.setText(Integer.toString(day) + "-" + month_list[month] + "-" + Integer.toString(year).substring(2));

            _id = Integer.parseInt(String.valueOf(year-2000) + String.valueOf(month+10) + String.valueOf(day+10));
        }
    }

    public void showDatePickerDialog(View view) {

        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "DatePickerFrag");
    }

    /******************************************************************************************
     * AsyncTask
     *****************************************************************************************/
    private class setSpinner extends AsyncTask<String, Void, ArrayAdapter<String>> {

        @Override
        protected ArrayAdapter<String> doInBackground(String... Table_details) {

            // Makes an array list and calculate the position of the old category if there's any
            ArrayList<String> List_to_set = dbHelp.getListOfThisTypeCategories_ArrayList(Table_details[0]);
            position_of_category = List_to_set.indexOf(Table_details[1]);

            return new ArrayAdapter<>(Activity_NewRecord.this, android.R.layout.simple_spinner_dropdown_item, List_to_set);
        }

        @Override
        protected void onPostExecute(ArrayAdapter<String> category_list) {

            nRSpinner.setAdapter(category_list);
            nRSpinner.setSelection(position_of_category);
        }
    }

}
