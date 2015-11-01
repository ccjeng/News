package com.ccjeng.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        ListView v = new ListView(getActivity());
        v.setLayoutParams(params);
        v.setLayoutParams(params);
        //v.setGravity(Gravity.CENTER);
        v.setBackgroundResource(R.color.windowBackground);
        //v.setText("Tab " + (position + 1));


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

        v.setAdapter(new ArrayAdapter<String>
			(getActivity(),android.R.layout.simple_list_item_1 , newsSource));

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goIntent(tabName, position, newsSource[position]);
                Log.d(TAG, "TAB " + position);
            }
        });

        fl.addView(v);
        return fl;
    }


    private void goIntent(String tabName, int itemnumber, String itemname) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewsCategory.class);
        Bundle bundle = new Bundle();
        /*if (Debug.On) {
            Log.d(TAG, "goIntent-itemnumber: " + Integer.toString(itemnumber));
            Log.d(TAG, "goIntent-itemname: " + itemname.toString());
            Log.d(TAG, "goIntent-tab: " + newsSourceIntent.toString());
        }
        */
        bundle.putString("SourceTab", tabName);
        bundle.putString("SourceNum", Integer.toString(itemnumber));
        bundle.putString("SourceName", itemname);

        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
    }
}