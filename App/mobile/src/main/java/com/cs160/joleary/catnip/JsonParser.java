package com.cs160.joleary.catnip;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jefftan on 3/6/16.
 */
public class JsonParser {

    private String json_str, lookup, repName;
    private String DEBUG_TAG = "JsonParser";
    protected String bioguide_id, repr_name, email, party,
                     end_of_term, position, website,state,
                     twitter_id;
    private ArrayList<String> lst;
    private ArrayList<ArrayList<String>> committees_holder;
    private Context mContext;

    public JsonParser(String obj, String query_word, String rep_name){
        json_str = obj;
        lookup = query_word;
        repName = rep_name;
        lst = new ArrayList<String>();
    }

    public JsonParser(ArrayList<String> data){
        lst = data;
        committees_holder = new ArrayList<ArrayList<String>>();
    }

    public JsonParser(){}

    protected String grabCounty(String jsonData) {
        String county = "administrative_area_level_2";
        String state = "administrative_area_level_1";
        String str = "";
        try {
            JSONObject obj = new JSONObject(jsonData);
            JSONArray data = obj.getJSONArray("results");
            JSONArray address_comp = data.getJSONObject(0).getJSONArray("address_components");
            for (int i = 0; i  < address_comp.length(); i += 1) {
                JSONObject datum = address_comp.getJSONObject(i);
                String type = datum.getJSONArray("types").get(0).toString();
                if (type.equals(county)) {
                    str += datum.getString("short_name");
                }
                if (type.equals(state)) {
                    str = str + ", " + datum.getString("short_name");
                    return str;
                }
            }
        } catch(JSONException e) {
            Log.d(DEBUG_TAG, "could not parse json string in votes");
        }
        return "";
    }

    /* Parses the legislator json */
    protected void parseJson(){
        try {
            JSONObject json_obj = new JSONObject(json_str);
            JSONArray data = json_obj.getJSONArray("results");
            for (int i = 0; i < data.length(); i += 1) {
                JSONObject jsonArr2 = new JSONObject(data.getString(i));
                if (lookup.equals("legislators")){
                    bioguide_id = jsonArr2.getString("bioguide_id");
                    repr_name = jsonArr2.getString("first_name") + " " + jsonArr2.getString("last_name");
                    email = jsonArr2.getString("oc_email");
                    party = jsonArr2.getString("party");
                    end_of_term = jsonArr2.getString("term_end");
                    position = jsonArr2.getString("title");
                    state = jsonArr2.getString("state");
                    website = jsonArr2.getString("website");
                    twitter_id = jsonArr2.getString("twitter_id");
                    populateMapInfo();
                }
            }
        } catch (JSONException e){
            Log.d(DEBUG_TAG, "could not parse json string");
        }
    }

    /* Parses the rest of the data (committees, bills, tweets) */
    protected void parseRestOfData() {
        try {
            // committees
            for (int i = 0; i < lst.size()/2; i += 1) {
                JSONObject json_obj = new JSONObject(lst.get(i));
                JSONArray data = json_obj.getJSONArray("results");
                ArrayList<String> arr = new ArrayList<String>();
                for (int j = 0; j < data.length(); j += 1) {
                    JSONObject jsonArr2 = new JSONObject(data.getString(j));
                    if (jsonArr2.has("name")) {
                        String committee = jsonArr2.getString("name");
                        arr.add(committee);
                    }
                }
                committees_holder.add(arr);
            }
            populateMoreInfo("committees");
            committees_holder.clear();
            // bills.
            // 3 is the number of stuff
            for (int i = RepsInfo.getSize() ; i < (lst.size()/2) + RepsInfo.getSize() ; i += 1) {
                JSONObject json_obj = new JSONObject(lst.get(i));
                JSONArray data = json_obj.getJSONArray("results");
                ArrayList<String> arr = new ArrayList<String>();
                for (int j = 0; j < data.length(); j += 1) {
                    JSONObject jsonArr2 = new JSONObject(data.getString(j));
                    if (jsonArr2.has("short_title") && jsonArr2.has("introduced_on")) {
                        if (!jsonArr2.getString("short_title").equals("null")) {
                            String bill = jsonArr2.getString("short_title")
                                          + " (" + jsonArr2.getString("introduced_on") + ")";
                            arr.add(bill);
                        }
                    }
                }
                committees_holder.add(arr);
            }
            populateMoreInfo("bills");

        }catch (JSONException e){
            Log.d(DEBUG_TAG, "could not parse json string in parseRestOfData");
        }
    }

    private void populateMapInfo(){
        RepsInfo.addKey(repr_name);
        RepsInfo.add(repr_name, "bioguide_id", bioguide_id);
        RepsInfo.add(repr_name, "name", repr_name);
        RepsInfo.add(repr_name, "email", email);
        RepsInfo.add(repr_name, "party", party);
        RepsInfo.add(repr_name, "end_of_term", end_of_term);
        RepsInfo.add(repr_name, "position", position);
        RepsInfo.add(repr_name, "website", website);
        RepsInfo.add(repr_name, "state", state);
        RepsInfo.add(repr_name, "twitter_id", twitter_id);
    }

    private void populateMoreInfo(String name){
        int index = 0;

        for (String rep_name : RepsInfo.getMap().keySet()) {
            if (!RepsInfo.containsKeyMoreInfo(rep_name)) {
                RepsInfo.addKeyToInfo(rep_name);
                RepsInfo.addToMoreInfo(rep_name, name, committees_holder.get(index));
            } else {
                HashMap<String, ArrayList<String>> info = RepsInfo.getMoreInfo().get(rep_name);
                info.put(name, committees_holder.get(index));
            }
            index += 1;
        }
    }
}
