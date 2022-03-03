package com.lee.album.inter;

import androidx.annotation.IntDef;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/514:19
 * Package: com.lee.album.inter
 * desc   :
 */
@IntDef(flag = true, value = {
        LoaderType.LOADER_ALL,
        LoaderType.LOADER_TITLE
})
public @interface LoaderType {
    int LOADER_ALL = 1;//加载全部
    int LOADER_TITLE = 2;//根据标题id
}
