package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.AppSession;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jefftan on 3/1/16.
 */
public class RepsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> reps_names;
    private ArrayList<String> position;
    private ArrayList<Bitmap> photos;
    private ArrayList<String> email;
    private ArrayList<String> website;
    private String zipCode;
    private ArrayList<String> t_ids;

    private static final String TWITTER_KEY = "vp1K0BPj2HXSNqRBFmpP1A7uT";
    private static final String TWITTER_SECRET = "AxxgxIN6jLnfXaFryr2LlCVjacBakdsHgNxS8yw3EvaGuiCbtS";

    public RepsAdapter(DisplayRepsActivity context, ArrayList<String> reps_names,
                       ArrayList<String> position, ArrayList<Bitmap> photos, String zipCode,
                       ArrayList<String> email, ArrayList<String> website, ArrayList<String> twitter_ids) {
        this.context = context;
        this.reps_names = reps_names;
        this.position = position;
        this.photos = photos;
        this.zipCode = zipCode;
        this.email = email;
        this.website = website;
        t_ids = twitter_ids;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.list_view, parent, false);

        TextView nameView = (TextView) repRow.findViewById(R.id.repName);
        TextView pos = (TextView) repRow.findViewById(R.id.pos);
        TextView email = (TextView) repRow.findViewById(R.id.email);
        TextView website = (TextView) repRow.findViewById(R.id.website);
        ImageView pictureView = (ImageView) repRow.findViewById(R.id.picture);
        final TextView tweet_view = (TextView) repRow.findViewById(R.id.latest_tweet);

        tweet_view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        nameView.setText(this.reps_names.get(position));
        pos.setText(this.position.get(position));
        pictureView.setImageBitmap(this.photos.get(position));
        email.setText(this.email.get(position));
        website.setText(this.website.get(position));

        String party = RepsInfo.getValue(this.reps_names.get(position), "party");

        if (party.equals("D")) {
            nameView.setTextColor(Color.parseColor("#2F80ED"));
            pos.setTextColor(Color.parseColor("#2F80ED"));
        } else if (party.equals("R")) {
            nameView.setTextColor(Color.parseColor("#E53535"));
            pos.setTextColor(Color.parseColor("#E53535"));
        }

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(context, new Twitter(authConfig));
        TwitterCore.getInstance().logInGuest(new Callback<AppSession>() {
            @Override
            public void success(Result<AppSession> result) {
                AppSession session = result.data;
                TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
                StatusesService statusesService = twitterApiClient.getStatusesService();
                statusesService.userTimeline(null, t_ids.get(position), 1, null, null,
                                             false, true, false, false, new com.twitter.sdk.android.core.Callback<List<Tweet>>(){
                            @Override
                            public void success (Result<List<Tweet>> result) {
                                for (Tweet Tweet : result.data) {
                                    tweet_view.setText(Tweet.text);
                                }
                            }
                            public void failure(TwitterException e) {
                                e.printStackTrace();
                            }
                        });
            }
            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        repRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendInfo = new Intent(context, DetailedInfoActivity.class);
                sendInfo.putExtra("zip", zipCode);
                sendInfo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
