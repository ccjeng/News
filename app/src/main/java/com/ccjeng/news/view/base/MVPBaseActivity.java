package com.ccjeng.news.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.ccjeng.news.presenter.base.BasePresenter;
import com.google.android.gms.analytics.GoogleAnalytics;

import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;


/**
 * Created by andycheng on 2016/9/5.
 */
public abstract class MVPBaseActivity<V, T extends BasePresenter<V>> extends SwipeBackActivity {
        protected T mPresenter;


    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSwipeBackLayout().setEdgeOrientation(SwipeBackLayout.EDGE_LEFT);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract T createPresenter();

}
