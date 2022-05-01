package com.example.assistivetouch.ads;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assistivetouch.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;

import java.lang.invoke.MethodType;


public class AddsClass extends AppCompatActivity {

    AdView adView;
    Activity activity;
    private MethodsInterface methodsInterface;
    private Sharedprefs sharedprefs;
    private InterstitialAd mInterstitialAd;


    public AddsClass(Activity activity, MethodsInterface methodsInterface) {
        this.activity = activity;
        this.methodsInterface = methodsInterface;
    }

    public AddsClass(Activity activity) {
        this.activity = activity;
    }

    //Banner ad code--------------------------------------------------------------------------------
//Banner ad code--------------------------------------------------------------------------------
    public void load_banner(final FrameLayout frameLayout) {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadBanner(frameLayout);
                }
            });
        } else {
            frameLayout.setVisibility(View.GONE);
        }
    }


    private void loadBanner(FrameLayout frameLayout) {
        adView = new AdView(activity);
        adView.setAdUnitId(AddIds.getBannerID());
        frameLayout.removeAllViews();
        frameLayout.addView(adView);

        AdSize adSize = getAdSize(frameLayout);
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize(FrameLayout frameLayout) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = frameLayout.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();

    }



    //----------------------------------------------------------------------------------------------

    public void load_Interstitial() {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity,AddIds.getInterstialId(), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    Log.i("TAG", "onAdLoaded");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.i("TAG", loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });

        }
    }
//
    public void intentFunctAdd(final Activity nextactivity, final String type) {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        Intent intent = new Intent(activity, nextactivity.getClass());
                        intent.putExtra("code",type);
                        activity.startActivity(intent);
                    }
                });
            } else {
                Intent intent = new Intent(activity, nextactivity.getClass());
                intent.putExtra("code",type);
                activity.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(activity, nextactivity.getClass());
            intent.putExtra("code",type);
            activity.startActivity(intent);
        }
    }

public void intentFunctAdd(final Activity nextactivity) {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        Intent intent = new Intent(activity, nextactivity.getClass());
                        activity.startActivity(intent);
                    }
                });
            } else {
                Intent intent = new Intent(activity, nextactivity.getClass());

                activity.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(activity, nextactivity.getClass());

            activity.startActivity(intent);
        }
    }
    public void intentFunctAdWithoutIntent() {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            }
        }
    }




    public void performFunctAdd() {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            } else {
                methodsInterface.perform_functions();
            }

            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when fullscreen content is dismissed.
                    Log.d("TAG", "The ad was dismissed.");
                    methodsInterface.perform_functions();
                }
            });
        } else {
            //todo
            methodsInterface.perform_functions();
        }
    }

    public void intentFunct(final Activity nextactivity, String type) {
        Intent intent = new Intent(activity, nextactivity.getClass());
        intent.putExtra("Type", type);
        activity.startActivity(intent);
    }

    public void methodFunct(MethodType methodType) {

    }

    //----------------------------------------------------------------------------------------------

    public void load_Native_Ad(final TemplateView templateView) {
        sharedprefs = new Sharedprefs(activity);
        if (!sharedprefs.showPreferences()) {
            AdLoader adLoader = new AdLoader.Builder(activity, AddIds.getNativeId())
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd NativeAd) {
                            ColorDrawable cd = new ColorDrawable(activity.getResources().getColor(R.color.purple_200));
                            NativeTemplateStyle styles = new
                                    NativeTemplateStyle.Builder().withMainBackgroundColor(cd).build();
                            templateView.setVisibility(View.VISIBLE);
                            templateView.setStyles(styles);
                            templateView.setNativeAd(NativeAd);

                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }
}
