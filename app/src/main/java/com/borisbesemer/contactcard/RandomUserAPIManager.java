package com.borisbesemer.contactcard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by borisbesemer on 07-10-15.
 * Based on code by Diederich Kroeske
 */
public class RandomUserAPIManager extends AsyncTask<String, Void, String>  {

    // Call back
    private OnRandomUserAvailable listener = null;

    // Static's
    private static final String TAG = "RandomUserTask";
    private static final String urlString = "https://randomuser.me/api/";

    // Constructor, set listener
    public RandomUserAPIManager(OnRandomUserAvailable listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        InputStream inputStream = null;
        int responsCode = -1;

        String response = "";

        for(String url : params) {
            Log.i(TAG, url);
        }

        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();

            if (!(urlConnection instanceof HttpURLConnection)) {
                // Url
                return null;
            }

            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setAllowUserInteraction(false);
            httpConnection.setInstanceFollowRedirects(true);
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            responsCode = httpConnection.getResponseCode();

            if (responsCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpConnection.getInputStream();
                response = getStringFromInputStream(inputStream);
                //Log.i(TAG, response);
            }
        } catch (MalformedURLException e) {
            Log.e("TAG", e.getLocalizedMessage());
            return null;
        } catch (IOException e) {
            Log.e("TAG", e.getLocalizedMessage());
            return null;
        }

        return response;
    }


    protected void onProgressUpdate(Integer... progress) {
        Log.i(TAG, progress.toString());
    }

    protected void onPostExecute(String response) {

        //Log.i(TAG, response);

        // parse JSON and inform caller
        JSONObject jsonObject;

        try {
            // Top level json object
            jsonObject = new JSONObject(response);

            // Get all users and start looping
            JSONArray users = jsonObject.getJSONArray("results");
            for(int idx = 0; idx < users.length(); idx++) {
                // array level objects and get user
                JSONObject array = users.getJSONObject(idx);
                JSONObject user = array.getJSONObject("user");

                // Get title, first and last name
                JSONObject name = user.getJSONObject("name");
                String title = name.getString("title");
                String firstName = name.getString("first");
                String lastName = name.getString("last");
                Log.i(TAG, title + " " + firstName + ", " + lastName);

                String email = user.getString("email");

                // Get image url
                JSONObject picture = user.getJSONObject("picture");

                String imageurl = picture.getString("large");

                // Try downloading the

                // Create new Person object
                Person p = new Person();
                p.first = firstName;
                p.last = lastName;
                p.title = title;
                p.email = email;
                p.imageUrl = imageurl;

                // call back with new person data
                listener.onRandomUserAvailable(p);

            }
        } catch( JSONException ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }


    }


    //
    // convert InputStream to String
    //
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    // Call back interface
    public interface OnRandomUserAvailable {
        void onRandomUserAvailable(Person person);
    }
}
