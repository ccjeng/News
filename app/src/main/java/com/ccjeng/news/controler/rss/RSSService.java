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
import rx.Observable;
import rx.Subscriber;


public class RSSService {

    private static final String TAG = "RSSService";
    private OkHttpClient client;
    private String url;

    public RSSService(String url){
        this.url = url;
        client = new OkHttpClient();
    }

    public Observable<String> request() {

        return Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            subscriber.onNext(response.body().string());
                        }
                        subscriber.onCompleted();

                    }
                });

            }
        });
    }

    public RSSFeed parse(String response) {
        RSSFeed rssFeed = null;

        if (Category.customRSSFeed(url)){

            //Custom RSS Parser
            CustomFeedParser feedParser = new CustomFeedParser();
            rssFeed = feedParser.getFeeds(url, response.trim());

            return rssFeed;

        }  else {
            //Standard RSS Parser
            InputSource inputSource = new InputSource();
            inputSource.setEncoding("ISO-8859-1");
            inputSource.setCharacterStream(new StringReader(response.trim()));

            try {

                //RSS
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                RSSHandler mRSSHandler = new RSSHandler();

                xr.setContentHandler(mRSSHandler);
                xr.parse(inputSource);

                rssFeed = mRSSHandler.getParsedData();
                return rssFeed;


            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                Log.d(TAG, "ParserConfigurationException error = " + e.toString());
            } catch (SAXException e) {
                e.printStackTrace();
                Log.d(TAG, "SAXException error = " + e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "IOException error = " + e.toString());
            }

        }

        return rssFeed;
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