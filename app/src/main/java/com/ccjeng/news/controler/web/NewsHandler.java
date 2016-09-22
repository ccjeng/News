package com.ccjeng.news.controler.web;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsHandler";
    private String url;
    private Context context;

    public NewsHandler(Context context,  String url) {
        this.context = context;
        this.url = url;
    }
    public Observable<String> getNewsContent(final String charset) {

        return Observable.create(new Observable.OnSubscribe<String>(){
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                VolleyStringRequest req = new VolleyStringRequest(url, charset, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        subscriber.onError(error);
                    }
                });

                VolleySingleton.getInstance(context).addToRequestQueue(req);

            }
        });
    }

}
