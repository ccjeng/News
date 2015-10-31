package com.ccjeng.news.rss;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccjeng.news.NewsRSSList;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andycheng on 2015/10/31.
 */
public class RssRequest {

    private static final String TAG = "RssRequest";

    private RequestQueue queue;
    private String encoding;
    private RSSItem rssItem;
    private List<RSSItem> listItem;

    private String getEncoding(String url){

        if (url.contains("stgloballink")
                || url.contains("nownews")
                || url.contains("hkheadline")
                || url.contains("rthk.hk")
                || url.contains("mingpao")
                ) {
            return "BIG5";
        } else {
            return "UTF-8";
        }
    }

    public void getFeed(final NewsRSSList context, String url) {

        listItem = new ArrayList<RSSItem>();

        encoding = getEncoding(url);
        queue = Volley.newRequestQueue(context);

        StringRequest request1 = new StringRequest(Request.Method.GET
                , url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    InputStream is = new ByteArrayInputStream(response.getBytes(encoding));
                    Feed feed = EarlParser.parseOrThrow(is, 0);
                    //Log.i(TAG, "Processing feed: " + feed.getTitle());

                    for (Item item : feed.getItems()) {

                        String title = item.getTitle();
                        //Log.i(TAG, "Item title: " + (title == null ? "N/A" : title));

                        rssItem = new RSSItem();
                        rssItem.setLink(item.getLink());
                        rssItem.setTitle(item.getTitle());
                        rssItem.setDescription(item.getDescription());
                        rssItem.setPubDate(item.getPublicationDate().toString());

                        listItem.add(rssItem);
                    }

                    context.setListView(listItem);


                } catch (Exception e) {
                    Log.e(TAG, "onResponse error: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        });

        //request.setPriority(Request.Priority.HIGH);
        queue.add(request1);


    }
}
