package com.ccjeng.news.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by andycheng on 2015/12/24.
 */
public class DateTimeFormater {
    private static final String TAG = "DateTimeFormater";
    public static String parse(String datetime){

        String strDT = "";
        if(datetime == null){
            return "";
        }

        //todo format error: 1. ETToday Thu,24 Dec 2015 12:51:00  +0800 2. format error: 新頭殼 hide

        //String dateFromatFrom = "EEE, dd MMM yyyy HH:mm:ss zzz";
        String dateFromatFrom = "EEE, dd MMM yyyy HH:mm:ss";
        String dateFromatTo = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdfFrom = new SimpleDateFormat(dateFromatFrom, Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat(dateFromatTo, Locale.US);

        //sdf.setLenient(false);

        try {
            //if not valid, it will throw ParseException
            Date date = sdfFrom.parse(datetime);
            strDT = sdfTo.format(date);
            Log.d(TAG, datetime +"--" +strDT);
            return strDT;

        } catch (ParseException e) {
            Log.d(TAG, "ParseException=" + e.toString());
            //        e.printStackTrace();
            return datetime;
        }

        //return strDT;
    }
}
