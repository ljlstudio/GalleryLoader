package com.lee.album.entity;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * desc   :
 */
public class GalleryInfoEntity implements Serializable {

    private String imgPath;
    private String imgName;
    private boolean selected;
    private String displayName;
    private String displayId;
    private int imgWidth;
    private int imgHeight;

    public int getImgWidth() {
        return imgWidth;
    }

    public GalleryInfoEntity setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
        return this;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public GalleryInfoEntity setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
        return this;
    }

    private LinkedHashMap<String, List<String>>size=new LinkedHashMap<>();

    public LinkedHashMap<String, List<String>> getSize() {
        return size;
    }

    public void setSize(LinkedHashMap<String, List<String>> size) {
        this.size = size;
    }






    public String getDisplayId() {
        return displayId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GalleryInfoEntity setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getImgPath() {
        return imgPath;
    }

    public GalleryInfoEntity setImgPath(String imgPath) {
        this.imgPath = imgPath;
        return this;
    }

    public String getImgName() {
        return imgName;
    }

    public GalleryInfoEntity setImgName(String imgName) {
        this.imgName = imgName;
        return this;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return this.imgPath.equals(((GalleryInfoEntity) obj).imgPath);
    }

    public GalleryInfoEntity setDisplayId(String id) {
        this.displayId = id;
        return this;
    }

}