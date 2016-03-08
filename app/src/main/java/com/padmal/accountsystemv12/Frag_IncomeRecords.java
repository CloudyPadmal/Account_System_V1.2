package com.padmal.accountsystemv12;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.padmal.adapters.Adapter_RecordsList;
import com.padmal.helpers.Helper_DB;
import com.padmal.items.Item_Record;

import java.util.ArrayList;

public class Frag_IncomeRecords extends Fragment {

    private ArrayList<Item_Record> recordsArrayList;
    private Adapter_RecordsList adapter_recordsList;
    private Helper_DB dbHelp;
    private ListView incomerecordsList;

    /******************************************************************************************
     * Constructors
     *****************************************************************************************/
    public static Frag_IncomeRecords newInstance() {

        Frag_IncomeRecords fragment = new Frag_IncomeRecords();
        return fragment;
    }

    public Frag_IncomeRecords() {

    }

    /******************************************************************************************
     * On View Create
     *****************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activity_records_income, container, false);
        incomerecordsList = (ListView) rootView.findViewById(R.id.Income_Records_ListView);

        // Register list view for a context menu
        registerForContextMenu(incomerecordsList);

        new populate_List().execute();

        incomerecordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item_Record item_record = (Item_Record) view.getTag();
                String description = dbHelp.getCategoryDescription_String(item_record.getCategory());

                Toast.makeText(getActivity(), description, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    /******************************************************************************************
     * On Create
     *****************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelp = new Helper_DB(getActivity());
        recordsArrayList = new ArrayList<>();
    }

    /******************************************************************************************
     * Context Menu Stuff
     *****************************************************************************************/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = new MenuInflater(getActivity());

        menuInflater.inflate(R.menu.longclickmenu_income, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Get adapter view's info
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // Get the tagged object of selected item
        Item_Record record = (Item_Record) info.targetView.getTag();

        // Generate Strings from object item
        final String Date = record.getDate();
        final String Category = record.getCategory();
        final String Amount = String.valueOf(record.getAmount());
        final String Table_name_to_get = "Income";

        switch (item.getItemId()) {

            // Deleting an item
            case R.id.inc_longmenudelete:

                // Build and create an alert dialog with YES NO options
                new AlertDialog.Builder(getActivity())
                        .setTitle(getResources().getString(R.string.entry_delete_title))
                        .setMessage(getResources().getString(R.string.entry_delete_msg))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Perform deletion ...
                                if (dbHelp.deleteRecord(Table_name_to_get, Date, Category, Amount) > 0) {

                                    // Toast action to show success
                                    Toast.makeText(getActivity(), getResources().getString(R.string.entry_deleted), Toast.LENGTH_SHORT).show();
                                    new populate_List().execute();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancelled
                            }
                        })
                        .show();
                return true;

            case R.id.inc_longmenuedit:

                // Create an intent to update entries from new record entry
                Intent update_record_from = new Intent(getActivity(), Activity_NewRecord.class);

                // Put valued parameters into the intent
                update_record_from.putExtra("From", "Update Records");
                update_record_from.putExtra("Table", Table_name_to_get);
                update_record_from.putExtra("Date", Date);
                update_record_from.putExtra("Category", Category);
                update_record_from.putExtra("Amount", Amount);

                // Start the activity
                getActivity().finish();
                startActivity(update_record_from);

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /******************************************************************************************
     * Async Task to populate the records list
     *****************************************************************************************/
    private class populate_List extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Get all summary items into an array list
            recordsArrayList = dbHelp.getAllRecords_ArrayList("Income");
            // Set that to an adapter
            adapter_recordsList = new Adapter_RecordsList(getActivity(), recordsArrayList);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // Set the adapter to the list view
            incomerecordsList.setAdapter(adapter_recordsList);
        }
    }
}

