package com.example.android.classlist.others;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.android.classlist.database.SignInDbSchema.SignInTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import static com.example.android.classlist.others.Other.Constants.*;

/**
 * Created by User on 4/28/2017.
 * Class contains methods for connection to database
 * POST posts message to url, processResults processes the post's results and
 * contentValues creates a key-value pair for insertion into SQLite database
 */

public class Post {

    private static TreeSet<String> results = new TreeSet<>();
    private static JSONArray mJSONArray;
    private static JSONObject sJSONObject = new JSONObject();

    private static TreeSet<String> getResults() {
        return results;
    }

    private static void setResults(String result) {
        results.add(result);
    }

    /**
     * Method to carry out POST requests to a given url and return response to request
     * @param url URL to send request to
     * @param message Message to be sent
     * @return JSON Object response to request
     */
    public static JSONObject POST(String url, Message message, Activity activity){
        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate(NAME, message.getName());
            jsonObject.accumulate(REG_NO, message.getReg_no());
            jsonObject.accumulate(DEPARTMENT, message.getDepartment());
            jsonObject.accumulate(YEAR, message.getYear());
            jsonObject.accumulate(TIME, message.getTime());
            jsonObject.accumulate(IMAGES, message.getImages());
            jsonObject.accumulate(PIC, message.getPic());
            jsonObject.accumulate(LATITUDE,message.getLatitude());
            jsonObject.accumulate(LONGITUDE, message.getLongitude());
            jsonObject.accumulate(ALTITUDE, message.getAltitude());
            jsonObject.accumulate(LAC, message.getLac());
            jsonObject.accumulate(CI, message.getCi());
            jsonObject.accumulate(PHONE, message.getPhone());
            jsonObject.accumulate(SUGGESTION,message.getMessage());
            jsonObject.accumulate(CHOICE, message.getChoice());

            VolleyHelperClass.getVolleyHelperClass(activity.getApplicationContext()).addToRequestQueue(new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            sJSONObject = response;
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Something went wrong");
                    error.printStackTrace();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-type", "application/json");
                    return headers;
                }
            });
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return sJSONObject;
    }

    public static String POST(String url, Message message){
        return POST(url, message, null).toString();
    }

    /**
     * Method that maps student's details to their respective columns
     * @param reg_no Student's registration number
     * @param name Student's name
     * @return content value containing column names and their values
     */
    public static ContentValues getContentValues(String reg_no, String name) {
        ContentValues values = new ContentValues();
        values.put(SignInTable.Cols.REG_NO, reg_no);
        values.put(SignInTable.Cols.NAME, name);
        return values;
    }

    /**
     * Method that carries out GET requests using Android Volley library
     * This methods assumes that a JSON Object is returned upon request
     * @param url URL to request values from
     * @param context Current context of app
     * @return TreeSet of the returned values
     */
    public static TreeSet<String> GET(String url, Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                try {
                    // parse fetched Json String to JSON Object
                    jsonObject = new JSONObject(response);
                    // store results to JSON Array
                    mJSONArray = jsonObject.getJSONArray(DEPARTMENT);
                    // pass JSON Array to retrieve content
                    getContent(mJSONArray);
                } catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(ERROR, "Something went wrong");
                error.printStackTrace();
            }
        });

        VolleyHelperClass.getVolleyHelperClass(context).addToRequestQueue(stringRequest);
        Log.d("RESULTS", getResults().toString());
        return getResults();
    }

    /**
     * Method to unpack contents of JSON Array to a TreeSet
     * @param jsonArray JSON Array to be unpacked
     */
    private static void getContent(JSONArray jsonArray){
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                // Add content to ArrayList
                setResults((String) jsonArray.get(i));
            } catch (JSONException ex) {
                Log.e(ERROR, "Something went wrong: " + ex.getMessage());
            }
        }
    }
}
