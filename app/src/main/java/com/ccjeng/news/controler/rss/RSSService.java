package com.ccjeng.news.controler.rss;

import android.util.Log;

import com.ccjeng.news.parser.rss.CustomFeedParser;
import com.ccjeng.news.utils.Category;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

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


public class RSSService {

    private static final String TAG = "RSSService";
    private static final int NOHTTP_WHAT_TEST = 0x001;
    private URL mUrl;
    private IRSSCallback mCallback;

    public RSSService(URL url, IRSSCallback callback){
        this.mUrl = url;
        this.mCallback = callback;
    }

    public void requestRSS() throws IOException {

        RequestQueue requestQueue = NoHttp.newRequestQueue();

        Request<String> request = NoHttp.createStringRequest(mUrl.toString(), RequestMethod.GET);
        requestQueue.add(NOHTTP_WHAT_TEST, request, onResponseListener);

    }


    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == NOHTTP_WHAT_TEST) {// 判断what是否是刚才指定的请求
                // 请求成功
                //String result = response.get();// 响应结果
                // 响应头
                //Headers headers = response.getHeaders();
                //headers.getResponseCode();// 响应码
                //response.getNetworkMillis();// 请求花费的时间

                try {

                        RSSFeed rssFeed;

                        if (Category.customRSSFeed(mUrl.toString())){

                            //Custom RSS Parser
                            CustomFeedParser feedParser = new CustomFeedParser();
                            rssFeed = feedParser.getFeeds(mUrl.toString(), response.get().trim());

                            mCallback.onRSSReceived(rssFeed);

                        }  else {
                            //Standard RSS Parser
                            InputSource inputSource = new InputSource();
                            inputSource.setEncoding("ISO-8859-1");
                            inputSource.setCharacterStream(new StringReader(response.get().trim()));

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

                            Log.d(TAG, "onResponse =" + response.get());


                        }


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse");
                    //responseError(context);

                }
            }
        }


        @Override
        public void onStart(int what) {

        }

        @Override
        public void onFinish(int what) {

        }


        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };

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