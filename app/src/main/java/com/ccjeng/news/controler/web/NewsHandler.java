package com.ccjeng.news.controler.web;

import android.util.Log;

import com.ccjeng.news.controler.HttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsHandler";
    private String url;

    public NewsHandler( String url) {
        this.url = url;
    }

    public Observable<String> getNewsContent(final String charset) {

        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                final Request request = new Request.Builder()
                        .url(url)
                        .build();

                HttpClient.getHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) throws IOException {
                        if (response.isSuccessful()) {

                            String encodedResponse = parseNetworkResponse(charset, response.body());
                            subscriber.onNext(encodedResponse);
                        }
                        subscriber.onCompleted();

                    }
                });

            }
        });
    }


    private String parseNetworkResponse(String charset, okhttp3.ResponseBody response) {

        String parsedString = "";
        try {

            if (!charset.equals("utf-8")) {
                parsedString = new String(response.bytes(), charset);
            } else {
                parsedString = response.string();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            //Log.e(TAG, e.getMessage());
        }

        return parsedString;

    }

}
