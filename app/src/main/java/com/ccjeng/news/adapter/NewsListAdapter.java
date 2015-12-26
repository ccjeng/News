package com.ccjeng.news.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccjeng.news.R;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.squareup.picasso.Picasso;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CustomViewHolder> {
	private static final String TAG = "NewsListAdapter";
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
		customViewHolder.titleView.setText(items.getItem(i).getTitle());

		if (items.getItem(i).getPubDate().length()==0) {
			customViewHolder.dateView.setVisibility(View.GONE);
		} else {
			customViewHolder.dateView.setText(items.getItem(i).getPubDate());
		}

		if (items.getItem(i).getDescription().length()==0) {
			customViewHolder.descrView.setVisibility(View.GONE);
		} else {
			customViewHolder.descrView.setText(items.getItem(i).getDescription());
		}

		if (items.getItem(i).getImg().length()==0) {
			customViewHolder.imageView.setVisibility(View.GONE);
		} else {
			//Load image
			Picasso.with(context)
					.load(items.getItem(i).getImg())
					.resize(72,72)
					.centerInside()
					.into(customViewHolder.imageView);
		}
	}

	@Override
	public int getItemCount() {
		return (null != items ? items.getItemCount() : 0);
	}

	public class CustomViewHolder extends RecyclerView.ViewHolder {
		protected ImageView imageView;
		protected TextView titleView;
		protected TextView dateView;
		protected TextView descrView;


		public CustomViewHolder(View view) {
			super(view);
			this.imageView = (ImageView) view.findViewById(R.id.image);
			this.titleView = (TextView) view.findViewById(R.id.title);
			this.dateView = (TextView) view.findViewById(R.id.date);
			this.descrView = (TextView) view.findViewById(R.id.descr);

		}
	}

}