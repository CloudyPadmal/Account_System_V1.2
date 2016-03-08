package com.padmal.accountsystemv12;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.padmal.adapters.Adapter_SummaryList;
import com.padmal.helpers.Helper_DB;
import com.padmal.items.Item_Record;
import com.padmal.items.Item_Summary;

import java.util.ArrayList;

public class Frag_IncomeSummary extends Fragment {

    private ArrayList<Item_Summary> summaryArrayList;
    private Adapter_SummaryList adapter_summaryList;
    private Helper_DB dbHelp;
    private ListView summaryList;

    public static Frag_IncomeSummary newInstance() {

        Frag_IncomeSummary fragment = new Frag_IncomeSummary();
        return fragment;
    }

    public Frag_IncomeSummary() {

    }

    /***********************************************************************************************
     * On View Create
     **********************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activity_summary, container, false);
        summaryList = (ListView) rootView.findViewById(R.id.Summary_ListView);

        new populate_List().execute();

        summaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item_Summary item_summary = (Item_Summary) view.getTag();
                String description = dbHelp.getCategoryDescription_String(item_summary.getCategory());

                Toast.makeText(getActivity(), description, Toast.LENGTH_LONG).show();
            }
        });
        return rootView;
    }

    /***********************************************************************************************
     * On Create
     **********************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelp = new Helper_DB(getActivity());
        summaryArrayList = new ArrayList<>();
    }

    /***********************************************************************************************
     * Async Task to populate the summary list
     **********************************************************************************************/
    private class populate_List extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Get all summary items into an array list
            summaryArrayList = dbHelp.getTotalOfEachCategory_ArrayList("Income");
            // Set that to an adapter
            adapter_summaryList = new Adapter_SummaryList(getActivity(), summaryArrayList);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // Set the adapter to the list view
            summaryList.setAdapter(adapter_summaryList);
        }
    }
}
