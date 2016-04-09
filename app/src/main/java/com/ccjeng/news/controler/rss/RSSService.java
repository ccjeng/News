package com.ccjeng.news.controler.rss;

import android.util.Log;

import com.ccjeng.news.parser.rss.CustomFeedParser;
import com.ccjeng.news.utils.Category;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class RSSService {

    private static final String TAG = "RSSService";

    public RSSService(){
    }

    public void requestRSS(final URL url, final IRSSCallback callback) throws IOException {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS);

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

                        RSSFeed rssFeed;

                        if (Category.customRSSFeed(url.toString())){

                            //Custom RSS Parser
                            CustomFeedParser feedParser = new CustomFeedParser();
                            rssFeed = feedParser.getFeeds(url.toString(), response.body().string().trim());

                            callback.onRSSReceived(rssFeed);

                        }  else {
                            //Standard RSS Parser
                            InputSource inputSource = new InputSource();
                            inputSource.setEncoding("ISO-8859-1");
                            inputSource.setCharacterStream(new StringReader(response.body().string().trim()));

                            try {

                                //RSS
                                SAXParserFactory spf = SAXParserFactory.newInstance();
                                SAXParser sp = spf.newSAXParser();
                                XMLReader xr = sp.getXMLReader();
                                RSSHandler mRSSHandler = new RSSHandler();

                                xr.setContentHandler(mRSSHandler);
                                xr.parse(inputSource);

                                rssFeed = mRSSHandler.getParsedData();
                                callback.onRSSReceived(rssFeed);


                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                                Log.d(TAG, "ParserConfigurationException error = " + e.toString());

                            } catch (SAXException e) {
                                e.printStackTrace();
                                Log.d(TAG, "SAXException error = " + e.toString());
                            }

                            Log.d(TAG, "onResponse");


                        }

                    } else {
                        Log.d(TAG, "response failed");
                        //responseError(context);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse error = " + e.toString());
                    //responseError(context);

                }

            }
        });

    }

    /*
    private void responseError(final NewsRSSList context){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setListView(null);
            }
        });

    }*/

    private InputStreamReader StreamReader(URL url, InputStream in) {

        InputStreamReader streamReader = null;
        // perform the synchronous parse
        try {
            if (url.toString().contains("stgloballink")
                    //  || url.toString().contains("nownews")
                    || url.toString().contains("hkheadline")
                //  || url.toString().contains("rthk.hk")
                //  || url.toString().contains("mingpao")
                    ) {
                streamReader = new InputStreamReader(in, "big5");
            } else {
                streamReader = new InputStreamReader(in, "utf-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return streamReader;
    }

}