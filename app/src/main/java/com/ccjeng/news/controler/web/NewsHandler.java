package com.ccjeng.news.controler.web;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ccjeng.news.R;
import com.ccjeng.news.parser.AbstractNews;
import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.utils.Category;
import com.ccjeng.news.view.NewsView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by andycheng on 2015/11/15.
 */
public class NewsHandler {

    private static final String TAG = "NewsHandler";

    public static void getNewsContent(final NewsView context, final String url, final String tab, final int position) {

        final String mimeType = "text/html";
        final String charset = Category.getEncoding(tab, position);//"utf-8";

        Log.d(TAG, "charset = " + charset);
        Log.d(TAG, "url = " + url);

        final ProgressWheel progressWheel = (ProgressWheel) context.findViewById(R.id.progress_wheel);
        //final WebView webView = (WebView) context.findViewById(R.id.webView);

        context.webView.getSettings().setJavaScriptEnabled(true);

        progressWheel.setVisibility(View.VISIBLE);
        context.webView.setVisibility(View.GONE);

        VolleyStringRequest req = new VolleyStringRequest(url, charset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressWheel.setVisibility(View.GONE);
                context.webView.setVisibility(View.VISIBLE);

                Category cat = new Category(context);
                AbstractNews parser = cat.getNewsParser(tab, position);

                try {

                    String newsContent = parser.parseHtml(url, response);

                    if (parser.isEmptyContent()) {
                        //if parse result is empty, then show webview directly..
                        context.webView.loadUrl(url);

                        context.webView.setWebViewClient(new WebViewClient() {

                            @Override
                            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                super.onPageStarted(view, url, favicon);
                                progressWheel.setVisibility(View.VISIBLE);
                                context.webView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                progressWheel.setVisibility(View.GONE);
                                context.webView.setVisibility(View.VISIBLE);
                            }
                        });
                        context.webView.setWebChromeClient(new WebChromeClient() {

                            @Override
                            public void onProgressChanged(WebView view, int newProgress) {
                                super.onProgressChanged(view, newProgress);
                                progressWheel.setProgress((float) newProgress / 100);

                                if (newProgress > 90) {
                                    progressWheel.setVisibility(View.GONE);
                                    context.webView.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    } else {

                        context.webView.loadDataWithBaseURL(null, newsContent, mimeType, "utf-8", "about:blank");

                        //image fit screen
                        final String js;
                        js= "javascript:(function () { " +
                                " var w = " + getWidth(context) + ";" +
                                " for( var i = 0; i < document.images.length; i++ ) {" +
                                " var img = document.images[i]; " +
                                "   img.height = Math.round( img.height * ( w/img.width ) ); " +
                                "   img.width = w; " +
                                " }" +
                                " })();";

                        context.webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                context.webView.loadUrl(js);
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());

                Crouton.makeText(context, R.string.data_error, Style.ALERT,
                        (ViewGroup) context.findViewById(R.id.main)).show();
            }
        });

        VolleySingleton.getInstance(context).addToRequestQueue(req);
    }

    private static double getWidth(NewsView context) {
        int width = 0;
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Configuration config = context.getResources().getConfiguration();
        int vWidth = 0;
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
            width = dm.heightPixels;
        else
            width = dm.widthPixels;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            return width/3;
        } else{
            return width*2/3;
        }

    }
}
