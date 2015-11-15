package com.ccjeng.news.parser;

import java.io.IOException;

/**
 * Created by andycheng on 2015/11/15.
 */


public interface INewsParser {
    public String parseHtml(String link) throws IOException;
}

