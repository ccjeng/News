package com.ccjeng.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccjeng.news.R;

/**
 * Created by andycheng on 2015/10/31.
 */
public class NewsCategoryAdapter extends RecyclerView.Adapter<NewsCategoryAdapter.CustomViewHolder>{

    private String[] mArrayString;
    private Context mContext;

    public NewsCategoryAdapter(Context context, String[] arrayString) {
        this.mArrayString = arrayString;
        this.mContext = context;
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
        customViewHolder.textView.setText(mArrayString[i]);
    }

    @Override
    public int getItemCount() {
        return (null != mArrayString ? mArrayString.length : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.icon);
            this.textView = (TextView) view.findViewById(R.id.row);
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("NewsCategoryAdapter", "当前点击的位置：" + getPosition());
                }
            });*/
        }

    }
}
