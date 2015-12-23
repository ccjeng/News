package com.ccjeng.news.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ccjeng.news.R;

/**
 * Created by andycheng on 2015/12/23.
 */
public class PreferenceFrag extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
