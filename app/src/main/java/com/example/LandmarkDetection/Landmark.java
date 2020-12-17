package com.example.LandmarkDetection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Landmark implements Serializable {
    static final String TAG = "DHARMIK";
    static int AMOUNTOFLANDMARKS = 0;


    private transient JSONObject jsonObjectL;
    private transient JSONObject jsonObjectP;
    private String landmarkName;
    private ArrayList<String> URLs = new ArrayList<>();
    private ArrayList<String> photographers = new ArrayList<>();
    private ArrayList<String> icons = new ArrayList<>();


    public Landmark(String landmarkName){
        AMOUNTOFLANDMARKS++;
        Log.d(TAG, "AMOUNTOFLANDMARK: "+AMOUNTOFLANDMARKS);
        this.landmarkName = landmarkName;
        photographers.add(null);
        icons.add(null);
        new AsyncThread().execute(landmarkName, "landscape", "1");
        new AsyncThread().execute(landmarkName, "portrait", "5");
    }

    public String getURLsAtPos(int pos) {
        return URLs.get(pos);
    }
    public String getPhotographersAtPos(int pos) {
        return photographers.get(pos);
    }
    public String getIconsAtPos(int pos) {
        return icons.get(pos);
    }
    public String getLandmarkName() {
        return landmarkName;
    }

    public class AsyncThread extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String ore = strings[1];
            String amount = strings[2];
            try {
                    URL mainUrl = new URL("https://api.unsplash.com/search/photos/?" +
                            "query=" + landmarkName +
                            "&pages=1" +
                            "&per_page=" + amount +
                            "&orientation=" + ore +
                            "&client_id=bxXKKj5Ucgz-G5VZMNvXxheizMZMRKL9WIpXq2D6VCg");
                    Log.d(TAG, ""+ore+": " + mainUrl.toString());
                    URLConnection urlConnection = mainUrl.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = br.readLine()) != null)
                        response.append(inputLine);
                    br.close();
                    if (ore.equals("landscape")) {
                        jsonObjectL = new JSONObject(response.toString());
                        URLs.add(0, jsonObjectL
                                .getJSONArray("results")
                                .getJSONObject(0)
                                .getJSONObject("urls")
                                .getString("regular"));
                    } else {
                        jsonObjectP = new JSONObject(response.toString());
                        for (int i = 1; i <= Integer.parseInt(amount); i++) {
                            URLs.add(i, jsonObjectP
                                    .getJSONArray("results")
                                    .getJSONObject(i - 1)
                                    .getJSONObject("urls")
                                    .getString("regular"));
                            photographers.add(i, jsonObjectP
                                    .getJSONArray("results")
                                    .getJSONObject(i - 1)
                                    .getJSONObject("user")
                                    .getString("name"));
                            icons.add(i, jsonObjectP
                                    .getJSONArray("results")
                                    .getJSONObject(i - 1)
                                    .getJSONObject("user")
                                    .getJSONObject("profile_image")
                                    .getString("large"));
                        }
                    }

                } catch (Exception e) {
                Log.d(TAG, "Error While getting API: " + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "AsynThread: Task Finish");
        }
    }

}

