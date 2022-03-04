package com.lee.album.inter;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/2514:43
 * Package: com.lee.album.inter
 * desc   :
 */
public interface OnGalleryListener {
    /**
     * 点击图片
     *
     * @param path
     * @param position
     */
    void clickGallery(String path, int position);


    /**
     * 抽屉布局状态
     *
     * @param isOpen
     * @param fromUser
     */
    void bottomSheetState(boolean isOpen, boolean fromUser);


    /**
     * 点击了损坏的图片
     *
     * @param path
     */
    void clickBadPicture(String path,int position);
}
