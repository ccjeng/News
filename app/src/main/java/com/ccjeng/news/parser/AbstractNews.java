package com.ccjeng.news.parser;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/28.
 */
public abstract class AbstractNews {
    public abstract String parseHtml(final String link, String content) throws IOException;

    public Boolean isEmptyContent() {
        return false;
    }

    protected String cleaner(String rs) {
        Whitelist wlist = null;
        try {
            wlist = Whitelist.basicWithImages();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Jsoup.clean(rs, wlist);

    }

}
