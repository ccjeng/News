package com.ccjeng.news.controler.web;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsHandler";
    //private static final int NOHTTP_WHAT_WEB = 0x002;

    private IWebCallback callback;
    private String url;
    private Context context;
    private String charset;


    public NewsHandler(Context context, IWebCallback callback, String url) {
        this.context = context;
        this.callback = callback;
        this.url = url;
    }
    public void getNewsContent(String charset) {

        this.charset = charset;

        VolleyStringRequest req = new VolleyStringRequest(url, charset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onWebContentReceived(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onWebContentError(error.getMessage());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(req);

    }

}
