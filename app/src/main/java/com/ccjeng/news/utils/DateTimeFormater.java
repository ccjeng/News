package com.ccjeng.news.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

        //todo format error: format error: 新頭殼 hide
        datetime = datetime.replace(", ",",").replace("  +"," +"); //Fix ETToday format issue: Thu,24 Dec 2015 12:51:00

        //Storm
        //if(datetime.substring(datetime.length()-3, datetime.length()-2).equals(":")){
        //    datetime = datetime.trim() + " +0800";
        //};

        //String dateFromatFrom = "EEE, dd MMM yyyy HH:mm:ss zzz";
        String dateFromatFrom = "EEE,dd MMM yyyy HH:mm:ss zzz";
        String dateFromatTo = "yyyy-MM-dd HH:mm";
        String timeZone = "GMT+8";
        SimpleDateFormat sdfFrom = new SimpleDateFormat(dateFromatFrom, Locale.US);
        SimpleDateFormat sdfTo = new SimpleDateFormat(dateFromatTo, Locale.US);

        sdfFrom.setTimeZone(TimeZone.getTimeZone(timeZone));

        try {
            //if not valid, it will throw ParseException
            Date date = sdfFrom.parse(datetime);
            strDT = sdfTo.format(date);
            //Log.d(TAG, datetime +"--" +strDT);
            return strDT;

        } catch (ParseException e) {
            Log.d(TAG, "ParseException=" + e.toString());
            Log.d(TAG, datetime);

            //        e.printStackTrace();
            return datetime;
        }

        //return strDT;
    }
}
