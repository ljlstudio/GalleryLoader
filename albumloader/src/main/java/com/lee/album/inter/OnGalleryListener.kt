package com.lee.album.inter

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/2514:43
 * Package: com.lee.album.inter
 * desc   :
 */
interface OnGalleryListener {
    /**
     * 点击图片
     *
     * @param path
     * @param position
     */
    fun clickGallery(path: String?, position: Int)

    /**
     * 抽屉布局状态
     *
     * @param isOpen
     * @param fromUser
     */
    fun bottomSheetState(isOpen: Boolean, fromUser: Boolean)

    /**
     * 点击了损坏的图片
     *
     * @param path
     */
    fun clickBadPicture(path: String?, position: Int)
}