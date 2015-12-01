package com.ccjeng.news.utils;

/**
 * Created by andycheng on 2015/11/27.
 */
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ccjeng.news.News;
import com.vpadn.ads.VpadnAd;
import com.vpadn.ads.VpadnAdListener;
import com.vpadn.ads.VpadnAdRequest;
import com.vpadn.ads.VpadnAdRequest.VpadnErrorCode;
import com.vpadn.ads.VpadnAdSize;
import com.vpadn.ads.VpadnBanner;
import com.mopub.common.util.Views;
import com.mopub.mobileads.CustomEventBanner;
import static com.mopub.mobileads.MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR;
import java.util.Map;
import static com.mopub.mobileads.MoPubErrorCode.NETWORK_NO_FILL;


class MoPubVpadnBanner extends CustomEventBanner {
    /*
     * These keys are intended for MoPub internal use. Do not modify.
     */
    private static final String AD_UNIT_ID_KEY = News.AD_Vpon;


    private CustomEventBannerListener mBannerListener;

    private Activity activity;
    private VpadnBanner vponBanner;

    @Override
    protected void loadBanner(
            final Context context,
            final CustomEventBannerListener customEventBannerListener,
            final Map<String, Object> localExtras,
            final Map<String, String> serverExtras) {
        mBannerListener = customEventBannerListener;
        String adUnitId = null;

        //adUnitId = AD_UNIT_ID_KEY;

        if (serverExtras.containsKey(AD_UNIT_ID_KEY)) {
            adUnitId = serverExtras.get(AD_UNIT_ID_KEY);
        }

        if (adUnitId == null) {
            Log.e("VPADN", "adUnitId == null");
            mBannerListener.onBannerFailed(ADAPTER_CONFIGURATION_ERROR);
            return;
        } else {
            Log.d("VPADN", "adUnitId is " + adUnitId);
        }

        if (context instanceof Activity) {
            activity = (Activity) context;
        } else {
            Log.e("VPADN", "context instanceof Activity is FALSE");
            mBannerListener.onBannerFailed(ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        vponBanner = new VpadnBanner(activity, adUnitId, VpadnAdSize.SMART_BANNER, "TW");
        vponBanner.setAdListener(new AdViewListener());
        final VpadnAdRequest adRequest = new VpadnAdRequest();
        vponBanner.loadAd(adRequest);
    }

    @Override
    protected void onInvalidate() {
        Views.removeFromParent(vponBanner);
        if (vponBanner != null) {
            vponBanner.setAdListener(null);
            vponBanner.destroy();
        }
    }

    private class AdViewListener implements VpadnAdListener {

        @Override
        public void onVpadnDismissScreen(VpadnAd arg0) {
        }

        @Override
        public void onVpadnFailedToReceiveAd(VpadnAd arg0, VpadnErrorCode arg1) {
            Log.d("VPADN", "Vpon banner ad failed to load.");
            if (mBannerListener != null) {
                mBannerListener.onBannerFailed(NETWORK_NO_FILL);
            }
        }

        @Override
        public void onVpadnLeaveApplication(VpadnAd arg0) {
        }

        @Override
        public void onVpadnPresentScreen(VpadnAd arg0) {
            Log.d("VPADN", "Vpon banner ad clicked.");
            if (mBannerListener != null) {
                mBannerListener.onBannerClicked();
            }
        }

        @Override
        public void onVpadnReceiveAd(VpadnAd arg0) {
            Log.d("VPADN", "Vpon banner ad loaded successfully. Showing ad...");
            if (mBannerListener != null) {
                mBannerListener.onBannerLoaded(vponBanner);
            }

        }

    }

}
