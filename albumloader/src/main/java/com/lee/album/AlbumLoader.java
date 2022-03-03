package com.lee.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;

import com.lee.album.entity.AlbumData;
import com.lee.album.entity.AlbumInfoEntity;
import com.lee.album.entity.GalleryInfoEntity;
import com.lee.album.inter.LoaderType;
import com.lee.album.loader.AlbumDataScanner;
import com.lee.album.loader.MediaPickerParam;
import com.lee.album.utils.Utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/418:54
 * Package: com.lee.album
 * desc   :
 */
public class AlbumLoader {


    private AlbumLoaderBuilder albumLoaderBuilder;
    private CopyOnWriteArrayList<GalleryInfoEntity> allData = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<AlbumInfoEntity> album = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<GalleryInfoEntity> latelyList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<GalleryInfoEntity> allList = new CopyOnWriteArrayList<>();
    private final Object lock = new Object();
    private Disposable disposable;
    int count = 0;
    private boolean isRunning = true;
    private static BitmapFactory.Options options;
    private AlbumDataScanner mAlbumDataScanner;
    private String selectionArgsName;

    private static Handler handler = new Handler();

    public AlbumLoader() {
        allData.clear();
        albumLoaderBuilder = new AlbumLoaderBuilder();
    }

    public void setAlbumLoaderBuilder(AlbumLoaderBuilder builder) {
        this.albumLoaderBuilder = builder;
    }


    /**
     * 加载分类列表数据
     *
     * @param context
     */
    public void loadClassyData(FragmentActivity context) {
        if (mAlbumDataScanner == null) {
            MediaPickerParam mediaPickerParam = new MediaPickerParam();
            mediaPickerParam.setShowImage(true);
            mediaPickerParam.setShowVideo(false);
            mAlbumDataScanner = new AlbumDataScanner(context, LoaderManager.getInstance(context), mediaPickerParam);
            mAlbumDataScanner.setAlbumDataReceiver(new AlbumDataScanner.AlbumDataReceiver() {
                @Override
                public void onAlbumDataObserve(List<AlbumData> albumDataList) {
                    try {
                        if (albumLoaderBuilder.getCallBack() != null) {
                            albumLoaderBuilder.getCallBack().loadClassyDataSuccess(albumDataList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAlbumDataReset() {

                }
            });
            mAlbumDataScanner.resume();
        }

    }

    /**
     * 加载某个分类数据
     */
    public void loadTitleListData(Context context, String selectionArgsName, String id) {
        release();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> {
            if (!TextUtils.isEmpty(selectionArgsName) && selectionArgsName.contains("全部图片")) {
                loadListData(context, selectionArgsName, LoaderType.LOADER_ALL, id);
            } else {
                loadListData(context, selectionArgsName, LoaderType.LOADER_TITLE, id);
            }
        }, 150);

    }


    /**
     * 加载所有数据
     *
     * @param context
     */
    public void loadAllListData(Context context) {

        loadListData(context, "", LoaderType.LOADER_ALL, "");
    }

    public void loadListMore(Context context, String selectionArgsName, String id, int loadType) {
        if (allData.size() < count) {
            loadListData(context, selectionArgsName, loadType, id);
        }
    }


    /**
     * 加载列表数据
     *
     * @param context
     * @param selectionArgsName
     * @param loadType
     * @param id
     */
    @SuppressLint("Range")
    private void loadListData(Context context, String selectionArgsName, int loadType, String id) {

        if (selectionArgsName != null && !selectionArgsName.equals(this.selectionArgsName)) {
            if (albumLoaderBuilder.getCallBack() != null) {
                albumLoaderBuilder.getCallBack().clearData();
            }
            allData.clear();
            isRunning = true;
        }
        this.selectionArgsName = selectionArgsName;
        album.clear();
        latelyList.clear();
        allList.clear();
        disposable = Observable.create((ObservableOnSubscribe<List<GalleryInfoEntity>>) emitter -> {

            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projImage = {MediaStore.Images.Media._ID
                    , MediaStore.Images.Media.DATA
                    , MediaStore.Images.Media.SIZE
                    , MediaStore.Images.Media.WIDTH
                    , MediaStore.Images.Media.HEIGHT
                    , MediaStore.Images.Media.DISPLAY_NAME};

            String[] selectionArgs = {selectionArgsName};
            Cursor mCursor;
            if (selectionArgsName.equals("0")) {
                selectionArgs = new String[]{id};
                String selection1 = MediaStore.Images.Media.BUCKET_ID + "=?";
                mCursor = context.getContentResolver().query(mImageUri,
                        projImage,
                        selection1,
                        selectionArgs,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");
            } else {
                //加载全部图片
                String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?";
                if (loadType == LoaderType.LOADER_ALL) {
                    mCursor = context.getContentResolver().query(mImageUri,
                            projImage,
                            MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                            new String[]{"image/jpeg", "image/png"},
                            MediaStore.Images.Media.DATE_MODIFIED + " desc");

                } else {
                    mCursor = context.getContentResolver().query(mImageUri,
                            projImage,
                            selection,
                            selectionArgs,
                            MediaStore.Images.Media.DATE_MODIFIED + " desc");

                }
            }


            int size = 0;
            File parentFile = null;
            if (mCursor != null) {
                count = mCursor.getCount();
                int size1 = allData.size() - 1;
                mCursor.moveToPosition(size1);
                while (mCursor.moveToNext() && isRunning && size < albumLoaderBuilder.getPageSize()) {
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

                    if (!isImageFile(path)) {
                        continue;
                    }
                    File file = new File(path);
                    //如果默认是相机,过滤掉别的图片，获取相机图片

                    String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

                    parentFile = new File(path).getParentFile(); // 获取该图片的父路径名


                    int width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                    int height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));

                    int rotation = Utils.readPictureDegree(path);
                    if (rotation == 90 || rotation == 270) {
                        width = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                        height = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
                    }


                    GalleryInfoEntity galleryInfoEntity = new GalleryInfoEntity()
                            .setImgName(displayName)
                            .setImgPath(path)
                            .setImgWidth(width)
                            .setImgHeight(height)
                            .setDisplayName(selectionArgsName)
                            .setDisplayId(id);


                    int pdf = 0; //判断此图片所属的相册是否已存在，pdf为0表示图片所属的相册还不存在
                    for (int i = 0; i < album.size(); i++) {
                        if ((album.get(i).getDirPath().toLowerCase().equals(parentFile.getAbsolutePath().toLowerCase()))) {
                            pdf = 1;
                            album.get(i).getGalleryInfo().add(galleryInfoEntity);
                        }

                    }
                    if (pdf == 0) {
                        CopyOnWriteArrayList<GalleryInfoEntity> list = new CopyOnWriteArrayList<>();
                        list.add(galleryInfoEntity);
                        album.add(new AlbumInfoEntity()
                                .setAlbumName(parentFile.getName())
                                .setDirPath(parentFile.getAbsolutePath())
                                .setGalleryInfo(list));

                    }

                    if (albumLoaderBuilder.isShowLastModified()) {

                    }

                    if (file.lastModified() >= Utils.getStateTime()) {
                        latelyList.add(galleryInfoEntity);
                    }

                    allList.add(galleryInfoEntity);
                    size++;

                }

                if (parentFile != null) {
                    album.add(0, new AlbumInfoEntity()
                            .setAlbumName("全部图片")
                            .setDirPath(parentFile.getAbsolutePath())
                            .setGalleryInfo(allList)
                            .setAll(true));

                    if (latelyList.size() > 0) {
                        album.add(new AlbumInfoEntity()
                                .setAlbumName("最近照片")
                                .setDirPath(parentFile.getAbsolutePath())
                                .setGalleryInfo(latelyList)
                                .setAll(true));
                    }
                }


                emitter.onNext(allList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tempGalleryData -> {
                    setData(tempGalleryData, context, selectionArgsName, id, loadType);
                }, throwable -> {


                });
    }

    /**
     * 设置数据
     *
     * @param galleryInfoEntityList
     * @param context
     * @param selectionArgsName
     * @param id
     * @param loadType
     */
    private void setData(List<GalleryInfoEntity> galleryInfoEntityList, Context context, String selectionArgsName, String id, int loadType) {
        synchronized (lock) {
            allData.addAll(galleryInfoEntityList);
            if (albumLoaderBuilder.getCallBack() != null && isRunning) {
                albumLoaderBuilder.getCallBack().loadListDataSuccess(galleryInfoEntityList, allData);
            }

            if (!albumLoaderBuilder.isLoadPaging() && isRunning) {
                loadListMore(context, selectionArgsName, id, loadType);
            }
        }

    }


    public void release() {
        selectionArgsName = "";
        isRunning = false;
        album.clear();
        latelyList.clear();
        allList.clear();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void resumeLoader() {
        if (mAlbumDataScanner != null) {
            mAlbumDataScanner.resume();
        }
    }

    public void pauseLoader() {
        if (mAlbumDataScanner != null) {
            mAlbumDataScanner.pause();
        }
        isRunning = false;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    /**
     * 销毁加载器
     */
    public void destroyLoader() {
        handler.removeCallbacksAndMessages(null);
        isRunning = false;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (mAlbumDataScanner != null) {
            mAlbumDataScanner.destroy();
        }
    }

    /**
     * 过滤坏掉的图片
     *
     * @param filePath
     * @return
     */
    public static boolean isImageFile(String filePath) {
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }


}