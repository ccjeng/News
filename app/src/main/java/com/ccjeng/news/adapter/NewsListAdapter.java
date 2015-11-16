package com.ccjeng.news.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccjeng.news.R;
import com.ccjeng.news.controler.rss.RSSFeed;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CustomViewHolder> {
	private final Activity context;
	private final RSSFeed items;
	
	public NewsListAdapter(Activity context, RSSFeed list) {
		  this.context = context;
		  this.items = list;
	 }

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rsslist_item, parent, false);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

		//Setting text view title
		customViewHolder.textView.setText(items.getItem(i).getTitle());
	}

	@Override
	public int getItemCount() {
		return (null != items ? items.getItemCount() : 0);
	}

	public class CustomViewHolder extends RecyclerView.ViewHolder {
		protected ImageView imageView;
		protected TextView textView;

		public CustomViewHolder(View view) {
			super(view);
			this.imageView = (ImageView) view.findViewById(R.id.icon);
			this.textView = (TextView) view.findViewById(R.id.row);
		}
	}

}