package com.lee.album;

import com.lee.album.inter.LoaderDataCallBack;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/514:23
 * Package: com.lee.album
 * desc   :
 */
public class AlbumLoaderBuilder {


    /**
     * 初始化时，设置默认值
     */
    public void AlbumLoaderBuilder() {
        setPageSize(50);
        setShowLastModified(false);
    }


    /**
     * 分页加载个数
     */
    private int pageSize;

    /**
     * 是否显示最近项目
     */
    private boolean showLastModified;

    /**
     * 数据请求成功回调
     */
    private LoaderDataCallBack callBack;

    /**
     * 是否分页
     */
    private boolean shouldLoadPaging;

    public boolean isShouldLoadPaging() {
        return shouldLoadPaging;
    }

    public AlbumLoaderBuilder setShouldLoadPaging(boolean shouldLoadPaging) {
        this.shouldLoadPaging = shouldLoadPaging;
        return this;
    }

    /**
     * 数据回调
     *
     * @return
     */
    public LoaderDataCallBack getCallBack() {
        return callBack;
    }

    public AlbumLoaderBuilder setCallBack(LoaderDataCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public boolean isShowLastModified() {
        return showLastModified;
    }

    public AlbumLoaderBuilder setShowLastModified(boolean showLastModified) {
        this.showLastModified = showLastModified;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public AlbumLoaderBuilder setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }
}