package com.ccjeng.news.utils;

import android.content.Context;

import com.ccjeng.news.R;
import com.ccjeng.news.parser.INewsParser;
import com.ccjeng.news.parser.Standard;
import com.ccjeng.news.parser.tw.AppleDaily;
import com.ccjeng.news.parser.tw.ChinaTimes;
import com.ccjeng.news.parser.tw.UDN;
import com.ccjeng.news.parser.tw.Yahoo;
import com.ccjeng.news.parser.tw.YamNews;

/**
 * Created by andycheng on 2015/11/14.
 */
public class Category {

    private Context context;

    public Category(Context context) {
        this.context = context;
    }

    public String[] getCategory(String tab, int position) {
        String[] category = null;

        if (tab.equals("TW")) {
            switch (position) {
                case 0:
                    category = context.getResources().getStringArray(R.array.newscatsYahoo);
                    break;
                case 1:
                    category = context.getResources().getStringArray(R.array.newscatsUDN);
                    break;
                case 2:
                    category = context.getResources().getStringArray(R.array.newscatsYam);
                    break;
                case 3:
                    category = context.getResources().getStringArray(R.array.newscatsChinaTimes);
                    break;
                case 4:
                    category = context.getResources().getStringArray(R.array.newscatsStorm);
                    break;
                case 5:
                    category = context.getResources().getStringArray(R.array.newscatsCommercial);
                    break;
                case 6:
                    category = context.getResources().getStringArray(R.array.newscatsEttoday);
                    break;
                case 7:
                    category = context.getResources().getStringArray(R.array.newscatsCNYes);
                    break;
                case 8:
                    category = context.getResources().getStringArray(R.array.newscatsNewsTalk);
                    break;
                case 9:
                    category = context.getResources().getStringArray(R.array.newscatsLibertyTimes);
                    break;
                case 10:
                    category = context.getResources().getStringArray(R.array.newscatsAppDaily);
                    break;
            }
        } else if (tab.equals("HK")) {
            switch (position) {
                case 0:
                    category = context.getResources().getStringArray(R.array.newscatsHKAppleDaily);
                    break;
                case 1:
                    category = context.getResources().getStringArray(R.array.newscatsHKOrientalDaily);
                    break;
                case 2:
                    category = context.getResources().getStringArray(R.array.newscatsHKYahoo);
                    break;
                case 3:
                    category = context.getResources().getStringArray(R.array.newscatsHKMingRT);
                    break;
                case 4:
                    category = context.getResources().getStringArray(R.array.newscatsHKMing);
                    break;
                case 5:
                    category = context.getResources().getStringArray(R.array.newscatsHKEJ);
                    break;
                case 6:
                    category = context.getResources().getStringArray(R.array.newscatsHKMetro);
                    break;
                case 7:
                    category = context.getResources().getStringArray(R.array.newscatsHKsun);
                    break;
                case 8:
                    category = context.getResources().getStringArray(R.array.newscatsHKam730);
                    break;
                case 9:
                    category = context.getResources().getStringArray(R.array.newscatsHKheadline);
                    break;
            }
        }

        return category;
    }

    public String[] getFeedURL(String tab, int position) {
        String[] feedURL = null;

        if (tab.equals("TW")) {
            switch (position) {
                case 0:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsYahoo);
                    break;
                case 1:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsUDN);
                    break;
                case 2:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsYam);
                    break;
                case 3:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsChinaTimes);
                    break;
                case 4:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsStorm);
                    break;
                case 5:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsCommercial);
                    break;
                case 6:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsEttoday);
                    break;
                case 7:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsCNYes);
                    break;
                case 8:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsNewsTalk);
                    break;
                case 9:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsLibertyTimes);
                    break;
                case 10:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsAppDaily);
                    break;
            }
        } else if (tab.equals("HK")) {
            switch (position) {
                case 0:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKAppleDaily);
                    break;
                case 1:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKOrientalDaily);
                    break;
                case 2:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKYahoo);
                    break;
                case 3:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKMingRT);
                    break;
                case 4:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKMing);
                    break;
                case 5:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKEJ);
                    break;
                case 6:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKMetro);
                    break;
                case 7:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKsun);
                    break;
                case 8:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKam730);
                    break;
                case 9:
                    feedURL = context.getResources().getStringArray(R.array.newsfeedsHKheadline);
                    break;
            }
        }


        return feedURL;
    }

    public INewsParser getNewsParser(String tab, int position) {
        INewsParser parser = null;

        if (tab.equals("TW")) {
            switch (position) {
                case 0:
                    parser = new Yahoo();
                    break;
                case 1:
                    parser = new UDN();
                    break;
                case 2:
                    parser = new YamNews();
                    break;
                case 3:
                    parser = new ChinaTimes();
                    break;
                case 4:
                    parser = new Standard();
                    break;
                case 5:
                    parser = new ChinaTimes();
                    break;
                case 6:
                    parser = new Standard();
                    break;
                case 7:
                    parser = new Standard();
                    break;
                case 8:
                    parser = new Standard();
                    break;
                case 9:
                    parser = new Standard();
                    break;
                case 10:
                    parser = new AppleDaily();
                    break;
            }
        } else if (tab.equals("HK")) {
            switch (position) {
                case 0:
                    parser = new Standard();
                    break;
                case 1:
                    parser = new Standard();
                    break;
                case 2:
                    parser = new Standard();
                    break;
                case 3:
                    parser = new Standard();
                    break;
                case 4:
                    parser = new Standard();
                    break;
                case 5:
                    parser = new Standard();
                    break;
                case 6:
                    parser = new Standard();
                    break;
                case 7:
                    parser = new Standard();
                    break;
                case 8:
                    parser = new Standard();
                    break;
                case 9:
                    parser = new Standard();
                    break;
            }
        }

        return parser;
    }


    public static String getEncoding(String tab, int position) {
        String encoding = "utf-8";

        if (tab.equals("TW")) {
            switch (position) {
                case 0://Yahoo
                case 1://UDN
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10://Apple Daily
                    encoding = "utf-8";
                    break;
            }
        } else if (tab.equals("HK")) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    encoding = "utf-8";
                    break;
            }
        }

        return encoding;
    }
}
