package com.ccjeng.news.service.rss;

import android.util.Log;

import com.ccjeng.news.ui.NewsRSSList;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class RSSService {

    private static final String TAG = "RSSService";

    public void requestRSS(final NewsRSSList context, final URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "onFailure error = " + e.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {

                try {
                    if (response.isSuccessful()) {

                        InputStreamReader streamReader = StreamReader(url, response.body().byteStream());

                        try {
                            SAXParserFactory spf = SAXParserFactory.newInstance();
                            SAXParser sp = spf.newSAXParser();
                            XMLReader xr = sp.getXMLReader();
                            RSSHandler mRSSHandler = new RSSHandler();

                            xr.setContentHandler(mRSSHandler);
                            xr.parse(new InputSource(streamReader));

                            final RSSFeed rssFeed = mRSSHandler.getParsedData();

                            // Run view-related code back on the main thread
                            context.runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                              context.setListView(rssFeed);
                                                      }
                                                  });



                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                            Log.d(TAG, "ParserConfigurationException error = " + e.toString());

                        } catch (SAXException e) {
                            e.printStackTrace();
                            Log.d(TAG, "SAXException error = " + e.toString());

                        }

                        Log.d(TAG, "onResponse");
                    } else {
                        Log.d(TAG, "response failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse error = " + e.toString());
                }

            }
        });


    }


    private InputStreamReader StreamReader(URL url, InputStream in) {

        InputStreamReader streamReader = null;
        // perform the synchronous parse
        try {
            if (url.toString().contains("stgloballink")
                    || url.toString().contains("nownews")
                    || url.toString().contains("hkheadline")
                    || url.toString().contains("rthk.hk")
                    || url.toString().contains("mingpao")
                    ) {
                streamReader = new InputStreamReader(in, "big5");
            } else if (url.toString().contains("ent.163.com")
                    || url.toString().contains("tech.163.com/")
                    || url.toString().contains("money.163.com")) {
                streamReader = new InputStreamReader(in, "GB2312");
            } else {
                streamReader = new InputStreamReader(in, "utf-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return streamReader;
    }

}