package com.ccjeng.news.service.rss;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccjeng.news.ui.NewsRSSList;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
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
    private String mUrl;

    private String getEncoding(String url){

        String returnValue = "UTF-8";
        if (url.contains("stgloballink")
               // || url.contains("nownews")
                || url.contains("hkheadline")
                || url.contains("rthk.hk")
                || url.contains("mingpao")
                ) {
            returnValue = "BIG5";
        } else {
            returnValue = "UTF-8";
        }

        return returnValue;
    }

    public void getFeedOkhttp(final NewsRSSList context, String url) {

        listItem = new ArrayList<RSSItem>();
        mUrl = url;
        encoding = getEncoding(url);

        RSSFeed rssFeed = null;
        try {
            final URL uurl = new URL(mUrl);
            rssFeed = XPPHandler.parseRSS(uurl);

            context.setListView(rssFeed);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    /*
    public void getFeed(final NewsRSSList context, String url) {

        listItem = new ArrayList<RSSItem>();
        mUrl = url;

        encoding = getEncoding(url);
        queue = Volley.newRequestQueue(context);

        StringRequest request1 = new StringRequest(Request.Method.GET
                , url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    if (mUrl.contains("udn")
                            || mUrl.contains("yam")
                            || mUrl.contains("orientaldaily")) {
                        response = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"), encoding);
                    }

                    InputStream is = new ByteArrayInputStream(response.getBytes(encoding));
                    Feed feed = EarlParser.parseOrThrow(is, 0);
                    //Log.d(TAG, "Processing feed: " + feed.getTitle());

                    String strTitle, strDescr;
                    Boolean itemIgnore = false;
                    for (Item item : feed.getItems()) {

                        strTitle = item.getTitle();
                        if (strTitle != null) {
                            strTitle = strTitle.trim();
                        }
                        //Log.i(TAG, "Item title: " + (title == null ? "N/A" : title));

                        rssItem = new RSSItem();
                        rssItem.setLink(item.getLink());
                        rssItem.setTitle(strTitle);

                        //rssItem.setDescription(item.getDescription());
                        //rssItem.setPubDate(item.getPublicationDate().toString());

                        //Skip ADs in RSS item
                        if (mUrl.contains("appledaily")) {
                            itemIgnore = false;
                            strDescr = item.getDescription();
                            if (strDescr != null) {
                                if (strDescr.contains("廣編特輯")
                                        || strDescr.contains("專題企劃")) {
                                    itemIgnore = true;
                                }
                            }
                        }

                        if (!itemIgnore) {
                            listItem.add(rssItem);
                        }
                    }

                    context.setListView(listItem);


                } catch (Exception e) {
                    Log.e(TAG, "RSS Parse Error: " + e.toString());
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
    */
}
