package com.ccjeng.news.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ccjeng.news.R;
import com.ccjeng.news.controler.rss.RSSFeed;
import com.ccjeng.news.utils.ScreenUtil;
import com.ccjeng.news.view.NewsRSSList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CustomViewHolder> {
	private static final String TAG = "NewsListAdapter";
	private final NewsRSSList context;
	private RSSFeed items = null;
	
	public NewsListAdapter(NewsRSSList context) {
		  this.context = context;
	 }

	public void setData(RSSFeed list) {
		this.items = list;
		notifyDataSetChanged();
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
		customViewHolder.titleView.setText(Html.fromHtml(items.getItem(i).getTitle()));

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
			RequestOptions options = new RequestOptions();
			options.centerCrop();

			Glide.with(context)
					.load(items.getItem(i).getImg())
					.apply(options)
					.thumbnail( 0.1f )
					.transition(withCrossFade())
					.into(customViewHolder.imageView)
					;
		}

		//runEnterAnimation(customViewHolder.itemView, i);
	}

	@Override
	public int getItemCount() {
		return (null != items ? items.getItemCount() : 0);
	}

	public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            view.setOnClickListener(this);
		}

        // Handles the row item being clicked
        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            context.showDetail(position, items);
        }
	}

	private void runEnterAnimation(View view, int position) {
		view.setTranslationY(ScreenUtil.getScreenHight(context));
		view.animate()
				.translationY(0)
				.setStartDelay(100 * (position % 5))
				.setInterpolator(new DecelerateInterpolator(3.f))
				.setDuration(700)
				.start();
	}
}