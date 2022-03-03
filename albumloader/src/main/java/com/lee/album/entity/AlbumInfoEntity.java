package com.lee.album.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2021/2/2410:49
 * Package: com.shyz.picture.entity
 * desc   : 相册分类
 */
public class AlbumInfoEntity implements Serializable {
    private List<GalleryInfoEntity> galleryInfo;
    private String albumName;
    private String dirPath;
    private boolean isAll;
    private Integer sort;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public boolean isAll() {
        return isAll;
    }

    public AlbumInfoEntity setAll(boolean all) {
        isAll = all;
        return this;
    }

    public List<GalleryInfoEntity> getGalleryInfo() {
        return galleryInfo;
    }

    public AlbumInfoEntity setGalleryInfo(List<GalleryInfoEntity> galleryInfo) {
        this.galleryInfo = galleryInfo;
        return this;
    }


    public String getAlbumName() {
        return albumName;
    }


    public String getDirPath() {
        return dirPath;
    }

    public AlbumInfoEntity setDirPath(String dirPath) {
        this.dirPath = dirPath;
        return this;
    }

    private String currentName;

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public AlbumInfoEntity setAlbumName(String name) {
        this.albumName = name;
        return this;
    }


}