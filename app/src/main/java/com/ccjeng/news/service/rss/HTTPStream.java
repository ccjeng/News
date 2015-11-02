package com.ccjeng.news.service.rss;

import java.io.IOException;
import java.io.InputStream;
import android.os.StrictMode;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HTTPStream {

	  public InputStream getInputStreamFromUrl(String url) throws IOException {


		  if (android.os.Build.VERSION.SDK_INT > 9) {
			  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			  StrictMode.setThreadPolicy(policy);
		  }

				 OkHttpClient client = new OkHttpClient();
				 Request request = new Request.Builder()
						 .url(url)
						 .build();

				 Response response = client.newCall(request).execute();

	            return response.body().byteStream();

	   }
}
