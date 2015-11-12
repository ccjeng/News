package com.ccjeng.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import com.ccjeng.news.R;
import com.ccjeng.news.adapter.NewsCategoryAdapter;
import com.ccjeng.news.adapter.RecyclerItemClickListener;

/**
 * Created by andycheng on 2015/10/27.
 */
public class TabFragment extends Fragment {

    private static final String TAG = "TabFragment";
    private static final String ARG_POSITION = "position";

    private int position;
    private String[] newsSource = null;
    private String tabName;

    public static TabFragment newInstance(int position) {
        TabFragment f = new TabFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

        RecyclerView v = new RecyclerView(getActivity());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        v.setLayoutManager(llm);
        v.setHasFixedSize(true);


        switch (position) {
            case 0:
                newsSource = getResources().getStringArray(R.array.newsSourceTW);
                tabName = "TW";
                break;
            case 1:
                newsSource = getResources().getStringArray(R.array.newsSourceHK);
                tabName = "HK";
                break;
        }

        NewsCategoryAdapter adapter = new NewsCategoryAdapter(getActivity(), newsSource);
        v.setAdapter(adapter);

        v.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        goIntent(tabName, position, newsSource[position]);
                        Log.d(TAG, "TAB " + position);
                    }
                })
        );

        fl.addView(v);
        return fl;
    }


    private void goIntent(String tabName, int itemnumber, String itemname) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsCategory.class);

        Bundle bundle = new Bundle();
        bundle.putString("SourceTab", tabName);
        bundle.putString("SourceNum", Integer.toString(itemnumber));
        bundle.putString("SourceName", itemname);

        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }
}