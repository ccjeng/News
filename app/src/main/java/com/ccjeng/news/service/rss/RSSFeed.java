package com.ccjeng.news.service.rss;

import java.util.ArrayList;
import java.util.List;

public class RSSFeed {

	private String title = null; //news title
	private String pubdate = null;
	public String getPubdate() {
		return pubdate;
	}

	public void setPubdate(String date) {
		this.pubdate = date;
	}

	private int itemCount; //count
	private List<RSSItem> itemList; //all items
	
	
	public RSSFeed() {
		itemList = new ArrayList<RSSItem>();
	}
	
	/**
	 * Add one RSSItem into RSSFeed
	 * @param item
	 * @return
	 */
	public int addItem(RSSItem item) {
		itemList.add(item);
		itemCount ++;
		return itemCount;
	}

	public void removeItem(RSSItem item) {
		itemList.remove(item);
	}

	public RSSItem getItem(int location) {
		return itemList.get(location);
	}
	
	/**
	 * Return all items
	 * @return
	 */
	public List<RSSItem> getAllItems() {
		return itemList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public int getItemCount() {
		return itemCount;
	}
}
