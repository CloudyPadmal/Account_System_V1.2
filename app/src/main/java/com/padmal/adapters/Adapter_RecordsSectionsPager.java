package com.padmal.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.padmal.accountsystemv12.Frag_ExpenseRecords;
import com.padmal.accountsystemv12.Frag_IncomeRecords;

public class Adapter_RecordsSectionsPager extends FragmentPagerAdapter {

    public Adapter_RecordsSectionsPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page
        switch (position) {
            case 0:
                return Frag_ExpenseRecords.newInstance();
            case 1:
                return Frag_IncomeRecords.newInstance();
        } return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages
        return 2;
    }
}

