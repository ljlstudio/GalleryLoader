package com.lee.album.inter

import androidx.annotation.IntDef
import com.lee.album.inter.LoaderType

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/514:19
 * Package: com.lee.album.inter
 * desc   :
 */
@IntDef(flag = true, value = [LoaderType.LOADER_ALL, LoaderType.LOADER_TITLE])
annotation class LoaderType {
    companion object {
        const val LOADER_ALL = 1 //加载全部
        const val LOADER_TITLE = 2 //根据标题id
    }
}