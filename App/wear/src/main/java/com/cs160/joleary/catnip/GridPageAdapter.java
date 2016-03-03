package com.cs160.joleary.catnip;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

import java.util.ArrayList;

/**
 * Created by jefftan on 3/1/16.
 */
public class GridPageAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private ArrayList<SimpleRow> mPages;
    private String zipCode;
    private String [] repsCal1;
    private String [] repsCal2;
    private String [] reps;
    private int [] picsIds;

    public GridPageAdapter(Context context, FragmentManager fm, String zipCode) {
        super(fm);
        mContext = context;
        this.zipCode = zipCode;
        populateMap();
        initPages();

    }

    private void initPages() {
        mPages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();
        for (int i = 0; i < reps.length; i += 1) {
            String [] items = reps[i].split(",");
            row1.addPages(new SimplePage(items[0], items[1], picsIds[i]));
        }
        mPages.add(row1);
    }

    private void populateMap() {
        if (this.zipCode != null && this.zipCode.equals("94801")) {
            String rep1 = "Kevin McCarthy, Republican";
            String rep2 = "Dianne Feinstein, Democrat";
            String rep3 = "Barbara Boxer, Democrat";
            String pres_vote = "presvote, ''";
            String [] repsCal1 = {rep1, rep2, rep3, pres_vote};
            int [] picsCal1 = {R.drawable.johnmccarthy,
                    R.drawable.diannefeinstein,
                    R.drawable.barbaraboxer, R.drawable.presvote};
            reps = repsCal1;
            picsIds = picsCal1;
        } else if (this.zipCode != null && this.zipCode.equals("94811")) {
            String rep1 = "Dianne Feinstein, Democrat";
            String rep2 = "Barbara Boxer, Democrat";
            String rep3 = "Kevin McCarthy, Republican";
            String pres_vote = "presvote, ''";
            String[] repsCal1 = {rep1, rep2, rep3, pres_vote};
            int[] picsCal1 = {R.drawable.diannefeinstein,
                    R.drawable.barbaraboxer,
                    R.drawable.johnmccarthy, R.drawable.presvote};
            reps = repsCal1;
            picsIds = picsCal1;
        } else {
            String rep1 = "Chuck Schumer, Democrat";
            String rep2 = "Lee Zeldin, Republican";
            String rep3 = "Kristen Gillibrand, Democrat";
            String pres_vote = "presvote2, ''";
            String [] repsCal2 = {rep1, rep2, rep3, pres_vote};
            int [] picsCal2 = {R.drawable.cs,
                    R.drawable.lz,
                    R.drawable.kg, R.drawable.presvote2};
            reps = repsCal2;
            picsIds = picsCal2;
        }
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        SimplePage page = ((SimpleRow)mPages.get(row)).getPages(col);
        PersonFragment p = new PersonFragment();
        return p.newInstance(page.mTitle, page.mText, page.mimageID);
    }

    @Override
    public int getRowCount() {
        return mPages.size();
    }

    @Override
    public int getColumnCount(int row) {
        return mPages.get(row).size();
    }
}