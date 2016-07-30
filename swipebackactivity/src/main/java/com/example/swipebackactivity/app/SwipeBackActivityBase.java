package com.example.swipebackactivity.app;

import com.example.swipebackactivity.SwipeBackLayout;


public interface SwipeBackActivityBase {
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    void scrollToFinishActivity();

}
