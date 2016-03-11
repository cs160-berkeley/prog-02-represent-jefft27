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
    private String [] repsInfo;

    public GridPageAdapter(Context context, FragmentManager fm, String [] lst) {
        super(fm);
        mContext = context;
        this.repsInfo = lst;
        initPages();
    }

    private void initPages() {
        mPages = new ArrayList<SimpleRow>();

        SimpleRow row1 = new SimpleRow();
        int size = Integer.parseInt(repsInfo[0]);
        for (int i = 1; i <= size; i += 2) {
            row1.addPages(new SimplePage(repsInfo[i], repsInfo[i + 1]));
        }
        String obama_vote = repsInfo[size + 2];
        String romney_vote = repsInfo[size + 3];
        row1.addPages(new SimplePage(repsInfo[size + 1], obama_vote + ";" + romney_vote));
        mPages.add(row1);
    }

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        SimplePage page = ((SimpleRow)mPages.get(row)).getPages(col);
        PersonFragment p = new PersonFragment();
        return p.newInstance(page.mTitle, page.mText);
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