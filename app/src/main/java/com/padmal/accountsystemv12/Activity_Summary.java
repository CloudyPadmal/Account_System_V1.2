package com.padmal.accountsystemv12;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.padmal.adapters.Adapter_SummarySectionsPager;


/***************************************************************************************************
 * Summary
 *
 * A fragment list layout with all the categories set in a custom adapter Adapter_CategoriesList.
 * Background of item elements is set to @drawable/abc_item_background_holo_dark in the default
 * drawables set.
 *
 * No long clicks or click events are implemented.
 *
 **************************************************************************************************/

public class Activity_Summary extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        // Set up the action bar
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for income and expense views
        Adapter_SummarySectionsPager sectionsPagerAdapter = new Adapter_SummarySectionsPager(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter
        viewPager = (ViewPager) findViewById(R.id.summary_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        // When swiping between different sections, select the corresponding tab
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // Adding tabs to tab view
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.expense)).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText(getResources().getString(R.string.income)).setTabListener(this));
    }

    /***********************************************************************************************
     * Tab Select Overrides
     **********************************************************************************************/
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}
