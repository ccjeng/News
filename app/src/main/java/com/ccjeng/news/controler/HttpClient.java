package com.ccjeng.news.controler;

import okhttp3.OkHttpClient;

/**
 * Created by andycheng on 2016/9/23.
 */

public class HttpClient {

    private static OkHttpClient client;

    private HttpClient(){
    }

    /* OkHttpClient Singleton
    * */
    public static OkHttpClient getHttpClient() {
        if (client == null) {
            synchronized (HttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

}
