package com.lee.album.router

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.lee.album.router.GalleryBuilder
import com.lee.album.router.GalleryEngine
import java.lang.ref.WeakReference

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/1014:07
 * Package: com.lee.album.router
 * desc   :
 */
class GalleryEngine private constructor(activity: Activity?, fragment: Fragment? = null) {
    private val mWeakActivity: WeakReference<Activity?> = WeakReference(activity)
    private val mWeakFragment: WeakReference<Fragment?> = WeakReference(fragment)

    private constructor(fragment: Fragment) : this(fragment.activity, fragment) {}

    fun setGalleryBuilder(context: Context?): GalleryBuilder {
        return GalleryBuilder(context!!, this)
    }

    val activity: Activity?
        get() = mWeakActivity.get()
    val fragment: Fragment?
        get() = mWeakFragment.get()

    companion object {
        fun from(activity: Activity?): GalleryEngine {
            return GalleryEngine(activity)
        }

        fun from(fragment: Fragment): GalleryEngine {
            return GalleryEngine(fragment)
        }
    }

}