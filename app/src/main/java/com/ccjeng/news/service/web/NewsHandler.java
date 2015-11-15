package com.ccjeng.news.service.web;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccjeng.news.R;
import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.parser.tw.AppleDaily;
import com.ccjeng.news.ui.NewsView;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsContent";


    public static void getNewsContent(final NewsView context, String url, String tab, int position) {

        final String mimeType = "text/html";
        final String encoding = "utf-8";

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        Log.d(TAG, "url = " + url);

        StringRequest req = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                INewsParser parser = new AppleDaily();

                try {
                    String newsContent = parser.parseHtml(response);

                    WebView webView = (WebView) context.findViewById(R.id.webView);
                    webView.loadDataWithBaseURL(null, newsContent, mimeType, encoding, "about:blank");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());
            }
        });

        mRequestQueue.add(req);
    }
}
