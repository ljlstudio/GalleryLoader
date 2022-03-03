package com.lee.album.router;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/1014:07
 * Package: com.lee.album.router
 * desc   :
 */
public class GalleryEngine {
    private WeakReference<Activity> mWeakActivity;
    private WeakReference<Fragment> mWeakFragment;

    private GalleryEngine(Activity activity) {
        this(activity, null);
    }

    private GalleryEngine(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private GalleryEngine(Activity activity, Fragment fragment) {
        mWeakActivity = new WeakReference<>(activity);
        mWeakFragment = new WeakReference<>(fragment);
    }

    public static GalleryEngine from(Activity activity) {
        return new GalleryEngine(activity);
    }

    public static GalleryEngine from(Fragment fragment) {
        return new GalleryEngine(fragment);
    }


    public GalleryBuilder setGalleryBuilder(Context context) {
        return new GalleryBuilder(context,this);
    }

    public Activity getActivity() {
        return mWeakActivity.get();
    }

    public Fragment getFragment() {
        return mWeakFragment.get();
    }

} 