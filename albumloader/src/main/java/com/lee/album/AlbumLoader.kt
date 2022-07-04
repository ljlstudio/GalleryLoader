package com.lee.album

import com.lee.album.entity.GalleryInfoEntity
import com.lee.album.entity.AlbumInfoEntity
import com.lee.album.loader.AlbumDataScanner
import androidx.fragment.app.FragmentActivity
import com.lee.album.loader.MediaPickerParam
import com.lee.album.loader.AlbumDataScanner.AlbumDataReceiver
import com.lee.album.entity.AlbumData
import android.text.TextUtils
import com.lee.album.inter.LoaderType
import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.graphics.BitmapFactory
import android.os.Handler
import androidx.loader.app.LoaderManager
import com.lee.album.utils.Utils
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Author : 李嘉伦
 * e-mail : lijialun@angogo.cn
 * date   : 2022/1/418:54
 * Package: com.lee.album
 * desc   :
 */
open class AlbumLoader {
    private var albumLoaderBuilder: AlbumLoaderBuilder
    private val allData = CopyOnWriteArrayList<GalleryInfoEntity>()
    private val album = CopyOnWriteArrayList<AlbumInfoEntity>()
    private val latelyList = CopyOnWriteArrayList<GalleryInfoEntity>()
    private val allList = CopyOnWriteArrayList<GalleryInfoEntity>()
    private val lock = Any()
    private var count = 0
    private var isRunning = true
    private var mAlbumDataScanner: AlbumDataScanner? = null
    private var selectionArgsName: String? = null
    private var launch: Job? = null

    companion object {
        val TAG: String = AlbumLoader::class.java.simpleName
        private var options: BitmapFactory.Options? = null
        private val handler = Handler()

        /**
         * 过滤坏掉的图片
         *
         * @param filePath
         * @return
         */
        private fun isImageFile(filePath: String?): Boolean {
            if (options == null) {
                options = BitmapFactory.Options()
            }
            options!!.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            return options!!.outWidth != -1
        }
    }

    init {
        allData.clear()
        albumLoaderBuilder = AlbumLoaderBuilder()
    }

    fun setAlbumLoaderBuilder(builder: AlbumLoaderBuilder) {
        albumLoaderBuilder = builder
    }

    /**
     * 加载分类列表数据
     *
     * @param context
     */
    fun loadClassyData(context: FragmentActivity?) {
        if (mAlbumDataScanner == null) {
            val mediaPickerParam = MediaPickerParam()
            mediaPickerParam.isShowImage = true
            mediaPickerParam.isShowVideo = false
            context?.let {
                mAlbumDataScanner =
                    AlbumDataScanner(context, LoaderManager.getInstance(context), mediaPickerParam)
                mAlbumDataScanner?.setAlbumDataReceiver(object : AlbumDataReceiver {
                    override fun onAlbumDataObserve(albumDataList: List<AlbumData?>?) {
                        try {
                            albumLoaderBuilder.callBack?.loadClassyDataSuccess(albumDataList)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onAlbumDataReset() {}
                })
                mAlbumDataScanner!!.resume()
            }

        }
    }

    /**
     * 加载某个分类数据
     */
    fun loadTitleListData(context: Context, selectionArgsName: String, id: String) {
        release()
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            if (!TextUtils.isEmpty(selectionArgsName) && selectionArgsName.contains("全部图片")) {
                loadListData(context, selectionArgsName, LoaderType.LOADER_ALL, id)
            } else {
                loadListData(context, selectionArgsName, LoaderType.LOADER_TITLE, id)
            }
        }, 100)
    }

    /**
     * 加载所有数据
     *
     * @param context
     */
    fun loadAllListData(context: Context) {
        loadListData(context, "", LoaderType.LOADER_ALL, "")
    }

    private fun loadListMore(
        context: Context,
        selectionArgsName: String?,
        id: String,
        loadType: Int
    ) {
        if (allData.size < count) {
            loadListData(context, selectionArgsName, loadType, id)
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
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("Range")
    private fun loadListData(
        context: Context,
        selectionArgsName: String?,
        loadType: Int,
        id: String
    ) {
        if (selectionArgsName != null && selectionArgsName != this.selectionArgsName) {
            albumLoaderBuilder.callBack?.clearData()
            allData.clear()
            isRunning = true
        }
        this.selectionArgsName = selectionArgsName
        album.clear()
        latelyList.clear()
        allList.clear()


        launch = GlobalScope.launch(Dispatchers.IO) {

            val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projImage = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            var selectionArgs = arrayOf(selectionArgsName)
            val mCursor: Cursor?
            if (selectionArgsName == "0") {
                selectionArgs = arrayOf(id)
                val selection1 = MediaStore.Images.Media.BUCKET_ID + "=?"
                mCursor = context.contentResolver.query(
                    mImageUri,
                    projImage,
                    selection1,
                    selectionArgs,
                    MediaStore.Images.Media.DATE_MODIFIED + " desc"
                )
            } else {
                //加载全部图片
                val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?"
                mCursor = if (loadType == LoaderType.LOADER_ALL) {
                    context.contentResolver.query(
                        mImageUri,
                        projImage,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        arrayOf("image/jpeg", "image/png"),
                        MediaStore.Images.Media.DATE_MODIFIED + " desc"
                    )
                } else {
                    context.contentResolver.query(
                        mImageUri,
                        projImage,
                        selection,
                        selectionArgs,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc"
                    )
                }
            }
            var size = 0
            var parentFile: File? = null
            if (mCursor != null) {
                count = mCursor.count
                val size1 = allData.size - 1
                mCursor.moveToPosition(size1)

                while (mCursor.moveToNext() && isRunning && size < albumLoaderBuilder.pageSize) {
                    val path =
                        mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    if (!isImageFile(path)) {
                        continue
                    }
                    val file = File(path)
                    //如果默认是相机,过滤掉别的图片，获取相机图片
                    val displayName =
                        mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    parentFile = File(path).parentFile // 获取该图片的父路径名
                    var width =
                        mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                    var height =
                        mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                    val rotation = Utils.readPictureDegree(path)
                    if (rotation == 90 || rotation == 270) {
                        width =
                            mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
                        height =
                            mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
                    }
                    val galleryInfoEntity = GalleryInfoEntity()
                        .setImgName(displayName)
                        .setImgPath(path)
                        .setImgWidth(width)
                        .setImgHeight(height)
                        .setDisplayName(selectionArgsName)
                        .setDisplayId(id)
                    var pdf = 0 //判断此图片所属的相册是否已存在，pdf为0表示图片所属的相册还不存在
                    for (i in album.indices) {
                        if (album[i].dirPath.equals(parentFile.absolutePath, ignoreCase = true)) {
                            pdf = 1
                            album[i].galleryInfo
                            album[i].galleryInfo?.add(galleryInfoEntity)
                        }
                    }
                    if (pdf == 0) {
                        val list = CopyOnWriteArrayList<GalleryInfoEntity>()
                        list.add(galleryInfoEntity)
                        album.add(
                            AlbumInfoEntity()
                                .setAlbumName(parentFile.name)
                                .setDirPath(parentFile.absolutePath)
                                .setGalleryInfo(list)
                        )
                    }
                    if (albumLoaderBuilder.isShowLastModified) {
                    }
                    if (file.lastModified() >= Utils.stateTime) {
                        latelyList.add(galleryInfoEntity)
                    }
                    allList.add(galleryInfoEntity)
                    size++
                }
                if (parentFile != null) {
                    album.add(
                        0, AlbumInfoEntity()
                            .setAlbumName("全部图片")
                            .setDirPath(parentFile.absolutePath)
                            .setGalleryInfo(allList)
                            .setAll(true)
                    )
                    if (latelyList.size > 0) {
                        album.add(
                            AlbumInfoEntity()
                                .setAlbumName("最近照片")
                                .setDirPath(parentFile.absolutePath)
                                .setGalleryInfo(latelyList)
                                .setAll(true)
                        )
                    }
                }
                withContext(Dispatchers.Main) {

                    setData(
                        allList,
                        context,
                        selectionArgsName,
                        id,
                        loadType
                    )

                }
            }

        }
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
    private fun setData(
        galleryInfoEntityList: List<GalleryInfoEntity>?,
        context: Context,
        selectionArgsName: String?,
        id: String,
        loadType: Int
    ) {
        synchronized(lock) {
            allData.addAll(galleryInfoEntityList!!)
            if (isRunning) {
                albumLoaderBuilder.callBack?.loadListDataSuccess(galleryInfoEntityList, allData)
            }
            if (!albumLoaderBuilder.isShouldLoadPaging && isRunning) {
                loadListMore(context, selectionArgsName, id, loadType)
            }
        }
    }

    fun release() {
        selectionArgsName = ""
        isRunning = false
        album.clear()
        latelyList.clear()
        allList.clear()

        launch?.cancel()

    }

    fun resumeLoader() {
        if (mAlbumDataScanner != null) {
            mAlbumDataScanner!!.resume()
        }
    }

    fun pauseLoader() {
        if (mAlbumDataScanner != null) {
            mAlbumDataScanner!!.pause()
        }
        isRunning = false
        launch?.cancel()
    }

    /**
     * 销毁加载器
     */
    fun destroyLoader() {
        handler.removeCallbacksAndMessages(null)
        isRunning = false
        launch?.cancel()
        if (mAlbumDataScanner != null) {
            mAlbumDataScanner!!.destroy()
        }
    }


}