package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by jefftan on 3/1/16.
 */
public class RepsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> reps_names;
    private ArrayList<String> position;
    private ArrayList<Integer> photos;
    private String zipCode;

    public RepsAdapter(Context context, ArrayList<String> reps_names,
                       ArrayList<String> position, ArrayList<Integer> photos, String zipCode) {
        this.context = context;
        this.reps_names = reps_names;
        this.position = position;
        this.photos = photos;
        this.zipCode = zipCode;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.list_view, parent, false);

        TextView nameView = (TextView) repRow.findViewById(R.id.repName);
        TextView pos = (TextView) repRow.findViewById(R.id.pos);
        ImageView pictureView = (ImageView) repRow.findViewById(R.id.picture);

        nameView.setText(this.reps_names.get(position));
        pos.setText(this.position.get(position));
        pictureView.setImageResource(this.photos.get(position));

        repRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendInfo = new Intent(context, DetailedInfoActivity.class);
                sendInfo.putExtra("zip", zipCode);
                sendInfo.putExtra("name", reps_names.get(position));
                context.startActivity(sendInfo);
            }
        });

        return repRow;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return this.reps_names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}
