package com.example.mohamed.testvolley;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String URL_ADDRESS =
            "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=10";
    private static final String TAG = "my_tag";
    private RequestQueue mQueue;
    private StringRequest mStringRequest;
    private TextView mResponseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResponseTextView = findViewById(R.id.tv_response);

        mQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, URL_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        paringJsonResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mResponseTextView.setText("that did not work!");
            }
        });

        mStringRequest.setTag(TAG);
        mQueue.add(mStringRequest);

    }

    private void paringJsonResponse(String response) {
        try {
            JSONObject baseResponse = new JSONObject(response);
            JSONArray items = baseResponse.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject currentBook = items.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                mResponseTextView.append(title + "\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(TAG);
        }
    }
}
