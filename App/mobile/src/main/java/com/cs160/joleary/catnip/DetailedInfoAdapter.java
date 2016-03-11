package com.cs160.joleary.catnip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by jefftan on 3/9/16.
 */
public class DetailedInfoAdapter extends BaseAdapter {

    private Context context;
    private LinearLayout bckgrnd;
    private ArrayList<String> mInfo;
    private TextView str;

    public DetailedInfoAdapter(Context context, ArrayList<String> moreInfo) {
        this.context = context;
        mInfo = moreInfo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.detailed_list_view, parent, false);
        str = (TextView) repRow.findViewById(R.id.txt);
        str.setText(mInfo.get(position));
        str.setTextColor(Color.parseColor("#000000"));

        return repRow;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
