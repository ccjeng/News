package com.ccjeng.news.controler.web;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.ccjeng.news.utils.Category;

import java.io.UnsupportedEncodingException;

/**
 * Created by andycheng on 2015/11/19.
 */
public class VolleyStringRequest extends StringRequest {

    private static final String TAG = "VolleyStringRequest";

    private String charset = null;

    public VolleyStringRequest(String url, String charset, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.charset = charset;
    }


    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        String str = null;
        try {

            Log.d(TAG, "content-type=" + response.headers.get("content - type"));
            Log.d(TAG, "header=" + HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "charset=" + charset);

            if(charset != null) {
                str = new String(response.data, charset);
            } else {
                str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = new String(response.data);
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
    }

    /**
     * @return the Parse Charset Encoding
     */
    public String getCharset() {
        return charset;
    }

    /**
     * set the Parse Charset Encoding
     * @param charset
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }
}
