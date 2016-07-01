package com.ccjeng.news.controler.web;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;


/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsHandler";

    private IWebCallback callback;
    private String url;
    private Context context;


    public NewsHandler(IWebCallback callback, Context context, String url) {
        this.callback = callback;
        this.context = context;
        this.url = url;
    }
    public void getNewsContent(String charset) {

        VolleyStringRequest req = new VolleyStringRequest(url, charset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                callback.onWebContentReceived(response);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d(TAG, error.getMessage());

               // Crouton.makeText(context, R.string.data_error, Style.ALERT,
               //         (ViewGroup) context.findViewById(R.id.croutonview)).show();

                //Crouton.makeText(context, error.getMessage(), Style.ALERT,
                //               (ViewGroup) context.findViewById(R.id.croutonview)).show();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(req);


    }

}
