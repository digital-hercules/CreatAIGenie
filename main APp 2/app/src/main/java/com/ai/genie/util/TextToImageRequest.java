package com.ai.genie.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TextToImageRequest {

    private String BASE_URL = "";
    private static final String TAG = "TextToImage";

    public void makeTextToImageRequest(Activity activity, final String text, final String url, final int gridSize) {

        BASE_URL = url;

        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Generating Image....");
        dialog.show();
        dialog.setCancelable(false);

        RequestQueue queue = Volley.newRequestQueue(activity);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        // Process the image response
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            dialog.dismiss();
                            Log.d(TAG, "onResponse: " + response);
                            JSONObject jsonResponse = new JSONObject(response);
                            String outputUrl = jsonResponse.getString("output_url");
                            Intent intent = new Intent(activity, ImageResultActivity.class);
                            intent.putExtra("url", outputUrl);
                            activity.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error response
                Log.d(TAG, "onErrorResponse: " + error);
                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("text", text);
                params.put("grid_size", String.valueOf(gridSize));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("api-key", Utils.DEEPAI_API);
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}