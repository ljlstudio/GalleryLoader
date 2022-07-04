package com.lee.album.entity

import com.lee.album.entity.GalleryInfoEntity
import java.io.Serializable
import java.util.LinkedHashMap

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * desc   :
 */
class GalleryInfoEntity : Serializable {
    var imgPath: String? = null
        private set
    var imgName: String? = null
        private set
    var isSelected = false
    var displayName: String? = null
        private set
    var displayId: String? = null
        private set
    var imgWidth = 0
        private set
    var imgHeight = 0
        private set

    fun setImgWidth(imgWidth: Int): GalleryInfoEntity {
        this.imgWidth = imgWidth
        return this
    }

    fun setImgHeight(imgHeight: Int): GalleryInfoEntity {
        this.imgHeight = imgHeight
        return this
    }

    var size = LinkedHashMap<String, List<String>>()
    fun setDisplayName(displayName: String?): GalleryInfoEntity {
        this.displayName = displayName
        return this
    }

    fun setImgPath(imgPath: String?): GalleryInfoEntity {
        this.imgPath = imgPath
        return this
    }

    fun setImgName(imgName: String?): GalleryInfoEntity {
        this.imgName = imgName
        return this
    }

    override fun equals(obj: Any?): Boolean {
        return imgPath == (obj as GalleryInfoEntity?)!!.imgPath
    }

    fun setDisplayId(id: String?): GalleryInfoEntity {
        displayId = id
        return this
    }
}