package com.ccjeng.news.controler.web;

/**
 * Created by andycheng on 2015/11/19.
 */
public class VolleyStringRequest /*extends StringRequest*/ {

    /*
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

            if (BaseApplication.APPDEBUG) {
                Log.d(TAG, "content-type=" + response.headers.get("content - type"));
                Log.d(TAG, "header=" + HttpHeaderParser.parseCharset(response.headers));
                Log.d(TAG, "charset=" + charset);
            }

            if(!charset.equals("utf-8")) {
                str = new String(response.data, charset);
            } else {
                str = new String(response.data);
                //str = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = new String(response.data);
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
    */
}
