package com.lee.album.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;


import com.lee.album.entity.AlbumData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AlbumDataScanner implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ALBUM_LOADER_ID = 1;
    private final Context mContext;
    private LoaderManager mLoaderManager;
    private final MediaPickerParam mPickerParam;
    private AlbumDataReceiver mAlbumDataReceiver;
    private boolean mPause;
    private Disposable mDisposable;

    public AlbumDataScanner(@NonNull Context context, @NonNull LoaderManager manager, @NonNull MediaPickerParam pickerParam) {
        mContext = context;
        mLoaderManager = manager;
        mPickerParam = pickerParam;
        mPause = false;
    }

    private int getLoaderId() {
        return ALBUM_LOADER_ID;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (mPickerParam.showImageOnly()) {
            return AlbumDataLoader.getImageLoader(mContext);
        } else if (mPickerParam.showVideoOnly()) {
            return AlbumDataLoader.getVideoLoader(mContext);
        } else {
            return AlbumDataLoader.getAllLoader(mContext);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (mDisposable == null) {
            mDisposable = Observable.just(0)
                    .subscribeOn(Schedulers.io())
                    .map(integer -> scanAllAlbumData(data))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(albumDataList -> {
                        mDisposable = null;
                        if (mAlbumDataReceiver != null) {
                            mAlbumDataReceiver.onAlbumDataObserve(albumDataList);
                        }
                    });
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (mAlbumDataReceiver != null) {
            mAlbumDataReceiver.onAlbumDataReset();
        }
    }

    public void resume() {
        if (hasRunningLoaders()) {
            mPause = false;
            return;
        }
        mPause = false;
        loadAlbumData();
    }

    public void pause() {
        mPause = true;
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(getLoaderId());
        }
    }

    public void destroy() {
        if (mLoaderManager != null) {
            mLoaderManager.destroyLoader(getLoaderId());
            mLoaderManager = null;
        }
        mAlbumDataReceiver = null;
    }

    public void loadAlbumData() {
        if (mLoaderManager != null) {
            mLoaderManager.initLoader(getLoaderId(), null, this);
        }
    }

    private boolean hasRunningLoaders() {
        if (mLoaderManager != null) {
            return mLoaderManager.hasRunningLoaders();
        }
        return false;
    }

    private boolean isCursorEnable(Cursor cursor) {
        return cursor != null && !cursor.isClosed();
    }

    private List<AlbumData> scanAllAlbumData(@NonNull Cursor cursor) {
        List<AlbumData> albumDataList = new ArrayList<>();
        while (isCursorEnable(cursor) && cursor.moveToNext()) {
            AlbumData albumData = AlbumData.valueOf(cursor,mContext);
            albumDataList.add(albumData);
        }
        return albumDataList;
    }

    public interface AlbumDataReceiver {

        void onAlbumDataObserve(List<AlbumData> albumDataList);

        void onAlbumDataReset();
    }


    public void setAlbumDataReceiver(AlbumDataReceiver receiver) {
        mAlbumDataReceiver = receiver;
    }

}
