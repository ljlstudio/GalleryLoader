package com.lee.album.entity

import com.lee.album.entity.GalleryInfoEntity
import com.lee.album.entity.AlbumInfoEntity
import java.io.Serializable

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/2/2410:49
 * Package: com.shyz.picture.entity
 * desc   : 相册分类
 */
class AlbumInfoEntity : Serializable {
    var galleryInfo: MutableList<GalleryInfoEntity>? = null
        private set
    var albumName: String? = null
        private set
    var dirPath: String? = null
        private set
    var isAll = false
        private set
    var sort: Int? = null
    var isSelected = false
    fun setAll(all: Boolean): AlbumInfoEntity {
        isAll = all
        return this
    }

    fun setGalleryInfo(galleryInfo: MutableList<GalleryInfoEntity>?): AlbumInfoEntity {
        this.galleryInfo = galleryInfo
        return this
    }

    fun setDirPath(dirPath: String?): AlbumInfoEntity {
        this.dirPath = dirPath
        return this
    }

    var currentName: String? = null
    fun setAlbumName(name: String?): AlbumInfoEntity {
        albumName = name
        return this
    }
}