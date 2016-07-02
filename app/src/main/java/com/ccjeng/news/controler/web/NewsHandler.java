package com.ccjeng.news.controler.web;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsHandler";
    //private static final int NOHTTP_WHAT_WEB = 0x002;

    private IWebCallback callback;
    private String url;
    private Context context;
    private String charset;


    public NewsHandler(Context context, IWebCallback callback, String url) {
        this.context = context;
        this.callback = callback;
        this.url = url;
    }
    public void getNewsContent(String charset) {

        this.charset = charset;
/*
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.GET);
        requestQueue.add(NOHTTP_WHAT_WEB, request, onResponseListener);
*/

        VolleyStringRequest req = new VolleyStringRequest(url, charset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onWebContentReceived(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onWebContentError(error.getMessage());
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(req);

    }
/*
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == NOHTTP_WHAT_WEB) {

                String result = response.get();

                try {
                    if(!charset.equals("utf-8")) {
                        result = new String(response.getByteArray(), charset);
                    }
                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                }

                Log.d(TAG, "onSucceed");
                Log.d(TAG, result);

                callback.onWebContentReceived(result);
            }
        }

        @Override
        public void onStart(int what) {
            Log.d(TAG, "onStart");
        }


        @Override
        public void onFinish(int what) {
            Log.d(TAG, "onFinish");
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Log.d(TAG, "onFailed = " + responseCode + " - "+ exception.getMessage());
            callback.onWebContentError(exception.getMessage());
        }

    };
*/
}
