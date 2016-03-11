package com.cs160.joleary.catnip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jefftan on 3/6/16.
 */
public class RepsInfo {

    private static LinkedHashMap<String, HashMap<String, String>> map =
            new LinkedHashMap<String, HashMap<String, String>>();

    private static LinkedHashMap<String, HashMap<String, ArrayList<String>>> moreInfo =
            new LinkedHashMap<String, HashMap<String, ArrayList<String>>>();

    private static RepsInfo instance = new RepsInfo();

    static ArrayList<String> arr = new ArrayList<String>();

    static String data = "";
     static String repName = "";

    private RepsInfo() {}

    public static String getValue(String k1, String k2) {
//        Log.v("RepsInfo: ", map.get(k1).get(k2) + "");
        return map.get(k1).get(k2);
    }

    public static HashMap<String, String> getAllInfo(String repr_name) {
        return map.get(repr_name);
    }

    public static boolean containsKeyMoreInfo(String repr_name) {
        return moreInfo.containsKey(repr_name);
    }

    public static void addKey(String rep_name) {
        HashMap<String, String> pair = new HashMap<String, String> ();
        map.put(rep_name, pair);
    }

    public static void addKeyToInfo(String rep_name) {
//        System.out.println("RepInfo, rep name is: " + rep_name);
        HashMap<String, ArrayList<String>> pair = new HashMap<String, ArrayList<String>> ();
        moreInfo.put(rep_name, pair);
    }

    public static void add(String rep_name, String key, String value) {
        HashMap<String, String> pair = map.get(rep_name);
        pair.put(key, value);
    }

    public static void addToMoreInfo(String rep_name, String key, ArrayList<String> lst) {
        HashMap<String, ArrayList<String>> pair = moreInfo.get(rep_name);
        pair.put(key, lst);
    }

    public static void printMap(String repr_name) {
        HashMap<String, String> info = map.get(repr_name);
//        System.out.println("repr_name is: " + repr_name);
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " --> " + value);
        }
    }

    public static void printLst(String repr_name) {
        HashMap<String, ArrayList<String>> info = moreInfo.get(repr_name);
//        System.out.println("repr_name is: " + repr_name);
        for (Map.Entry<String, ArrayList<String>> entry : info.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(repr_name + ": " + key + " --> " + value);
        }
    }

    public static int getSize() {
        return map.size();
    }

    public static int getSizeMoreInfo() {
        return moreInfo.size();
    }

    public static HashMap<String, HashMap<String, String>> getMap() {
        return map;
    }

    public static ArrayList<String> getValueForInfo(String k1, String k2) {
        return moreInfo.get(k1).get(k2);
    }

    public static HashMap<String, HashMap<String, ArrayList<String>>> getMoreInfo() {
        return moreInfo;
    }

    public static void clearAll() {
        moreInfo.clear();
        map.clear();
    }

}
