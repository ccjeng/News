package com.ccjeng.news.controler.rss;

import android.util.Log;

import com.ccjeng.news.parser.rss.CustomFeedParser;
import com.ccjeng.news.utils.Category;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RSSService {

    private static final String TAG = "RSSService";
    private URL mUrl;
    private IRSSCallback mCallback;

    public RSSService(URL url, IRSSCallback callback){
        this.mUrl = url;
        this.mCallback = callback;
    }

    public void requestRSS() throws IOException {

        OkHttpClient client = new OkHttpClient();


        final Request request = new Request.Builder()
                .url(mUrl.toString())
                .build();

        /*
        if (mUrl.toString().contains("www.am730.com.hk")) {
          //  request.setUserAgent("Custom user agent");
            request = new Request.Builder()
                    .url(mUrl.toString())
                    .header("User-Agent", "Custom user agent")
                    .build();
        } else {
            request = new Request.Builder()
                    .url(mUrl.toString())
                    .build();
        }*/


        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallback.onRSSFailed(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, response.toString());
                    //throw new IOException("Unexpected code " + response);
                }

                try {

                    RSSFeed rssFeed;

                    if (Category.customRSSFeed(mUrl.toString())){

                        //Custom RSS Parser
                        CustomFeedParser feedParser = new CustomFeedParser();
                        rssFeed = feedParser.getFeeds(mUrl.toString(), response.body().string().trim());

                        mCallback.onRSSReceived(rssFeed);

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
                            mCallback.onRSSReceived(rssFeed);


                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                            Log.d(TAG, "ParserConfigurationException error = " + e.toString());

                        } catch (SAXException e) {
                            e.printStackTrace();
                            Log.d(TAG, "SAXException error = " + e.toString());
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


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