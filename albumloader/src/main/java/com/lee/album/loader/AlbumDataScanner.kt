package com.lee.album.loader

import android.content.Context
import android.database.Cursor
import com.lee.album.loader.MediaPickerParam
import com.lee.album.loader.AlbumDataScanner.AlbumDataReceiver
import io.reactivex.rxjava3.disposables.Disposable
import com.lee.album.loader.AlbumDataScanner
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.lee.album.loader.AlbumDataLoader
import io.reactivex.rxjava3.schedulers.Schedulers
import com.lee.album.entity.AlbumData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.ArrayList

class AlbumDataScanner(
    private val mContext: Context?,
    manager: LoaderManager,
    pickerParam: MediaPickerParam
) : LoaderManager.LoaderCallbacks<Cursor> {
    private var mLoaderManager: LoaderManager?
    private val mPickerParam: MediaPickerParam
    private var mAlbumDataReceiver: AlbumDataReceiver? = null
    private var mPause: Boolean


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when {
            mPickerParam.showImageOnly() -> {
                AlbumDataLoader.getImageLoader(mContext)
            }
            mPickerParam.showVideoOnly() -> {
                AlbumDataLoader.getVideoLoader(mContext)
            }
            else -> {
                AlbumDataLoader.getAllLoader(mContext)
            }
        }
    }

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {


        //第一种写法
        job = GlobalScope.launch(Dispatchers.IO) {

            flow<Cursor> {
                emit(data)
            }.map {
                scanAllAlbumData(it)
            }.collect {
                mAlbumDataReceiver?.onAlbumDataObserve(it)
            }
        }



//        //第二种写法，不在协程内，不可取消
//        runBlocking {
//            flow<Cursor> {
//
//            }.map {
//
//            }.flowOn(Dispatchers.IO)
//                .collect {
//
//                }
//        }
//

    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAlbumDataReceiver?.onAlbumDataReset()

    }

    fun resume() {
        if (hasRunningLoaders() == true) {
            mPause = false
            return
        }
        mPause = false
        loadAlbumData()
    }

    fun pause() {
        mPause = true
        job?.cancel()
        mLoaderManager?.destroyLoader(loaderId)

    }

    fun destroy() {
        mLoaderManager?.destroyLoader(loaderId)
        mLoaderManager = null
        mAlbumDataReceiver = null
    }

    private fun loadAlbumData() {
        mLoaderManager?.initLoader(loaderId, null, this)

    }

    private fun hasRunningLoaders(): Boolean? {
        mLoaderManager?.let {
            return mLoaderManager?.hasRunningLoaders()
        }
        return false

    }

    private fun isCursorEnable(cursor: Cursor?): Boolean {
        return cursor != null && !cursor.isClosed
    }

    private fun scanAllAlbumData(cursor: Cursor): List<AlbumData?> {
        val albumDataList: MutableList<AlbumData?> = ArrayList()
        while (isCursorEnable(cursor) && cursor.moveToNext()) {
            val albumData = AlbumData.valueOf(cursor, mContext)
            albumDataList.add(albumData)
        }
        return albumDataList
    }

    interface AlbumDataReceiver {
        fun onAlbumDataObserve(albumDataList: List<AlbumData?>?)
        fun onAlbumDataReset()
    }

    fun setAlbumDataReceiver(receiver: AlbumDataReceiver?) {
        mAlbumDataReceiver = receiver
    }

    companion object {
        private const val loaderId: Int = 1
    }

    init {
        mLoaderManager = manager
        mPickerParam = pickerParam
        mPause = false
    }
}