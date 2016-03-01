package com.cs160.joleary.catnip;

import java.util.HashMap;

/**
 * Created by jefftan on 2/22/16.
 */
public class DummyInfo {
    private String zipCode;
    private String[] committees = {"Intelligence", "Appropriations", "Health", "Foreign Trade",
            "Food", "Automobiles"};

    private HashMap<String, String> bills = new HashMap<String, String>() {{
        put("Abortion, 20 week ban", "5/13/15");
        put("Cybersecurity", "6/13/15");
        put("Debt Management", "7/13/15");
        put("Health care law, repeal", "8/13/15");
        put("Keystone XL pipeline", "9/13/15");
        put("Lead notification in drinking water", "10/13/15");
        put("Sanctuary cities bill", "11/13/15");
    }};

    private HashMap<String, String> reps_lst = new HashMap<String, String>() {{
        put("Dianne Feinstein", "Democrat");
        put("Kevin McCarthy", "Republican");
        put("Barbara Boxer", "Democrat");
    }};


    private HashMap<String, String> m1, m2, m3, m4, m5, m6;
    private HashMap<String, HashMap<String, String>> reps1, reps2;
    private HashMap<String, HashMap<String, HashMap<String, String>>> master_map;

    public DummyInfo(String zipCode) {
        this.zipCode = zipCode;
        reps1 = new HashMap<String, HashMap<String, String>>();
        reps2 = new HashMap<String, HashMap<String, String>>();
        master_map = new HashMap<String, HashMap<String, HashMap<String, String>>>();
        populateMap();
    }

    private void populateMap() {
        m1 = new HashMap<String, String>() {{
            put("name", "Dianne Feinstein");
            put("position", "Senator");
            put("email", "dfeinstein@gov.org");
            put("party", "Democrat");
            put("website", "www.dfeinstein.gov");
            put("last tweet", "i ate a sandwich");
            put("picture", "diannefeinstein");
            put("end_of_term", "2018");

        }};
        m2 = new HashMap<String, String>() {{
            put("name", "Kevin McCarthy");
            put("position", "Representative");
            put("party", "Republican");
            put("email", "kmccarthy@gov.org");
            put("website", "www.kmccarthy.gov");
            put("last tweet", "i like to eat pesto pasta!");
            put("picture", "johnmccarthy");
            put("end_of_term", "2020");

        }};
        m3 = new HashMap<String, String>() {{
            put("name", "Barbara Boxer");
            put("position", "Senator");
            put("party", "Democrat");
            put("email", "bboxer@gov.org");
            put("website", "www.bboxer.gov");
            put("last tweet", "i ate a panini for lunch!");
            put("picture", "barbaraboxer");
            put("end_of_term", "2018");
        }};

        m4 = new HashMap<String, String>() {{
            put("name", "Chuck Schumer");
            put("position", "Senator");
            put("party", "Democrat");
            put("email", "cschumer@gov.org");
            put("website", "www.schumer.gov");
            put("last tweet", "i ate a panini for lunch!");
            put("picture", "cs");
            put("end_of_term", "2018");
        }};

        m5 = new HashMap<String, String>() {{
            put("name", "Kristen Gillibrand");
            put("position", "Senator");
            put("party", "Democrat");
            put("email", "kgillibrand@gov.org");
            put("website", "www.kgillibrand.gov");
            put("last tweet", "i ate a panini for lunch!");
            put("picture", "kg");
            put("end_of_term", "2020");
        }};

        m6 = new HashMap<String, String>() {{
            put("name", "Lee Zeldin");
            put("position", "Representative");
            put("party", "Republican");
            put("email", "lzeldin@gov.org");
            put("website", "www.lzeldin.gov");
            put("last tweet", "i ate a panini for lunch!");
            put("picture", "lz");
            put("end_of_term", "2018");
        }};

        reps1.put("Dianne Feinstein", m1);
        reps1.put("Kevin McCarthy", m2);
        reps1.put("Barbara Boxer", m3);
        reps2.put("Chuck Schumer", m4);
        reps2.put("Kristen Gillibrand", m5);
        reps2.put("Lee Zeldin", m6);
    }


    public HashMap<String, HashMap<String, HashMap<String, String>>> getMap() {
        master_map.put(this.zipCode, reps1);
        master_map.put("10001", reps2);
        return master_map;
    }

   public  HashMap<String, String> getPersons(String name){
       if (name.equals("Dianne Feinstein")
           || name.equals("Kevin McCarthy")
           || name.equals("Barbara Boxer")) {
           return reps1.get(name);
       }
       return reps2.get(name);

   }
}