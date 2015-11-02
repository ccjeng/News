package com.ccjeng.news.service.rss;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//import android.util.Log;

public class RSSHandler extends DefaultHandler {

    private boolean in_item = false;
    private boolean in_title = false;
    private boolean in_link = false;
    private boolean in_desc = false;
    private boolean in_date = false;
    private RSSFeed feed;
    private RSSItem item;
    private String title = "";
    private StringBuffer buf = new StringBuffer();

    public RSSFeed getParsedData() {
        return feed;
    }

    public String getRssTitle() {
        return title;
    }

    @Override
    public void startDocument() throws SAXException {
        feed = new RSSFeed();
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {
        if (localName.equals("item")) {
            this.in_item = true;
            item = new RSSItem();
        } else if (localName.equals("title")) {
            if (this.in_item) {
                this.in_title = true;
            }
        } else if (localName.equals("link")) {
            if (this.in_item) {
                this.in_link = true;
            }
        } else if (localName.equals("description")) {
            if (this.in_item) {
                this.in_desc = true;
            }
        } else if (localName.equals("pubDate")) {
            if (this.in_item) {
                this.in_date = true;
            }
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {
        if (localName.equals("item")) {
            this.in_item = false;
            feed.addItem(item);
        } else if (localName.equals("title")) {
            if (this.in_item) {
                item.setTitle(buf.toString().trim());
                buf.setLength(0);
                this.in_title = false;
            }
        } else if (localName.equals("link")) {
            if (this.in_item) {
                item.setLink(buf.toString().trim());
                buf.setLength(0);
                this.in_link = false;
            }
        } else if (localName.equals("description")) {
            if (in_item) {
                item.setDescription(buf.toString().trim());
                buf.setLength(0);
                this.in_desc = false;
            }
        } else if (localName.equals("pubDate")) {
            if (in_item) {
                item.setPubDate(buf.toString().trim());
                buf.setLength(0);
                this.in_date = false;
            }
        } else buf.setLength(0);
        /*
	    else if (localName.equals("guid"))
	    {
	    	buf.setLength(0);
	    }*/
    }

    @Override
    public void characters(char ch[], int start, int length) {
        if (this.in_item) {
            buf.append(ch, start, length);
        }
    }
}
