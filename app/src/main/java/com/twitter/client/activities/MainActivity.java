package com.twitter.client.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.twitter.client.R;
import com.twitter.client.Utils.Statics;
import com.twitter.client.sharedpref.SharedPreferenceManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.twitter.client.Utils.Statics.CONSUMER_KEY;
import static com.twitter.client.Utils.Statics.CONSUMER_SECRET;

/**
 * @author MohamedSaleh on 24/6/2017
 */
public class MainActivity extends AppCompatActivity {

    private TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseText = (TextView) findViewById(R.id.responseText);

        new CallThread().execute();

        //------------------------------------------------------------------------------------------
        // URL encode the consumer key and secret

//        Authenticated auth = jsonToAuthenticated(rawAuthorization);

        //------------------------------------------------------------------------------------------



    }

    private class CallThread extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String urlApiKey = null;
            String urlApiSecret = null;
            try {
                urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
                urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            // Concatenate the encoded consumer key, a colon character, and the
            // encoded consumer secret
            String combined = urlApiKey + ":" + urlApiSecret;

            // Base64 encode the string
            String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Statics.API_URL);
            httpPost.setHeader("Authorization", "Basic " + base64Encoded);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            try {
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String responseString = response.toString();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String url = "https://api.twitter.com/1.1/followers/list.json?user_id=" +
                    SharedPreferenceManager.getInstance().getLong(Statics.USER_ID_KEY, 0);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            responseText.setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            responseText.setText("It is failed with : " + error.getMessage());

                        }
                    });
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(jsObjRequest);
        }
    }
}
