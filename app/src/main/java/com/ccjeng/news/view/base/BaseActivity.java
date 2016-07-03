package com.ccjeng.news.view.base;

import android.os.Bundle;

import com.example.swipebackactivity.app.SwipeBackActivity;

/**
 * Created by andycheng on 2016/6/17.
 */
public class BaseActivity  extends SwipeBackActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // getSwipeBackLayout().setEdgeSize(1000);

    }

}
