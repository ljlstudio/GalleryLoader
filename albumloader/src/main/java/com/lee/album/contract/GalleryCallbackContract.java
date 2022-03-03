package com.lee.album.contract;

import com.lee.album.entity.AlbumData;
import com.lee.album.entity.GalleryInfoEntity;
import com.lee.album.inter.LoaderDataCallBack;

import java.util.List;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/3/310:25
 * Package: com.lee.album.contract
 * desc   : 接口管理类,方便查询管理
 */
public class GalleryCallbackContract {


    public interface GalleryLoaderDataCallBack extends LoaderDataCallBack{
        @Override
        void loadClassyDataSuccess(List<AlbumData> list);

        @Override
        void loadListDataSuccess(List<GalleryInfoEntity> pageData, List<GalleryInfoEntity> currentAllData);

        @Override
        void clearData();
    }
} 