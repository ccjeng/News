package com.ccjeng.news.controler.rss;

import android.util.Log;

import com.ccjeng.news.utils.DateTimeFormater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class RSSItem {

	private static final String TAG = "RSSItem";
	public static final String TITLE = "title";
	public static final String PUBDATE = "pubDate"; 
	
	private String title = null;
	private String link = null;
	private String pubDate = null;
	private String description = null;
	private String category = null;
	private String img = null;
	
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
		return DateTimeFormater.parse(pubDate);
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {

		int maxDescrLength = 100;
		Document doc = Jsoup.parse(description);
		Element image = doc.select("img").first();
		String descr = doc.text().trim();

		if (descr.length()>0) {
			descr = descr.replace("-- Delivered by Feed43 service","");
			if (descr.length() > maxDescrLength) {
				descr = descr.substring(0, maxDescrLength);
			}
		}

		String imgURL = "";
		if (image != null) {
			imgURL = image.absUrl("src");
			//Log.d(TAG, imgURL);
			if (imgURL.contains("rsscna") || imgURL.contains("cnaFirstNews")) {
				setImg("");
			} else {
				setImg(image.absUrl("src"));
			}
		} else {
			setImg("");
		}

		//Log.d(TAG, descr);


		this.description = descr;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

	public String toString()
	{
		// limit how much text we display
		/*if (title.length() > 42)
		{
			return title.substring(0, 42) + "...";
		}*/

		return getTitle();
	}
}
