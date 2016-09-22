package com.ccjeng.news.utils;

import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

/**
 * Created by andycheng on 2016/3/24.
 */
public class UI {

    public static void showErrorSnackBar(CoordinatorLayout container, int message) {

        Snackbar snackbar = Snackbar
                .make(container, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.RED);

        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.show();

    }
}
