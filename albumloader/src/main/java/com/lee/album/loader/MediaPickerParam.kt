package com.lee.album.loader

import java.io.Serializable

/**
 * 媒体扫描参数
 */
class MediaPickerParam : Serializable {
    // 是否显示拍照Item
    var isShowCapture = false

    // 是否显示图片Item
    var isShowImage = false

    // 是否显示视频Item
    var isShowVideo = false

    // 横向item的数量
    var spanCount = 0

    // 分割线大小
    var spaceSize = 0

    // 是否显示边沿分割线
    var isHasEdge = false
        private set

    // 是否自动销毁
    var isAutoDismiss = false

    /**
     * 重置为初始状态
     */
    private fun reset() {
        isShowCapture = true
        isShowImage = true
        isShowVideo = true
        spanCount = 4
        spaceSize = 4
        isHasEdge = true
        isAutoDismiss = false
    }

    fun setItemHasEdge(hasEdge: Boolean) {
        isHasEdge = hasEdge
    }

    /**
     * 仅显示图片
     * @return
     */
    fun showImageOnly(): Boolean {
        return isShowImage && !isShowVideo
    }

    /**
     * 仅显示视频
     * @return
     */
    fun showVideoOnly(): Boolean {
        return isShowVideo && !isShowImage
    }

    init {
        reset()
    }
}