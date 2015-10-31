package com.ccjeng.news.adapter;

import java.util.List;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccjeng.news.R;
import com.ccjeng.news.rss.RSSItem;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CustomViewHolder> {
	private final Activity context;
	private final List<RSSItem> items;
	
	public NewsListAdapter(Activity context, List<RSSItem> list) {
		  this.context = context;
		  this.items = list;
	 }

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, null);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {

		//Setting text view title
		customViewHolder.textView.setText(items.get(i).getTitle());
	}

	@Override
	public int getItemCount() {
		return (null != items ? items.size() : 0);
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