package com.ccjeng.news.service.rss;

public class RSSItem {

	public static final String TITLE = "title";
	public static final String PUBDATE = "pubDate"; 
	
	private String title = null;
	private String link = null;
	private String pubDate = null;
	private String description = null;
	private String category = null;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getPubDate() {
		//return DateUtil.getSimpleDateFormat(pubDate);
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String toString()
	{
		// limit how much text we display
		/*if (title.length() > 42)
		{
			return title.substring(0, 42) + "...";
		}*/
		return title;
	}
}
