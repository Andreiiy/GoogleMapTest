package com.atatar.googlemaptest.jsonparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {

    private HashMap<String,String> parseJsonObject(JSONObject object){
        //Initialize hash map
        HashMap<String,String> dataList = new HashMap<>();

        try {

            String name = object.getString("name");
            String address = object.getString("formatted_address");
           // String name = object.getString("1");
            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");
            //put all value in hash map
            dataList.put("name",name);
            dataList.put("lat",latitude);
            dataList.put("lng",longitude);
            dataList.put("address",address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){

        List<HashMap<String,String>> dataList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //Add data in hash map list
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public List<HashMap<String,String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseJsonArray(jsonArray);
    }
    }
