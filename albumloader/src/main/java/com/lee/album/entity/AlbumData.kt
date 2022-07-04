package com.lee.album.entity

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import com.lee.album.loader.AlbumDataLoader
import androidx.documentfile.provider.DocumentFile

/**
 * 相册数据对象
 */
class AlbumData : Parcelable {
    val id: String

    /**
     * 获取封面路径
     *
     * @return
     */
    val coverUri: Uri
//            return "所有图片";
//        }
    /**
     * 获取显示名称
     *
     * @return
     */
    private val displayName: String?

    /**
     * 获取相册数量
     *
     * @return
     */
    var count: Long
        private set

    //全部图片、最近图片、收藏、个人相片、微信、淘宝、QQ、截图、壁纸、其他；
    var albumName: String? = null
    var sort: Int? = null
    var isSelected = false

    constructor(id: String, coverUri: Uri, displayName: String?, count: Long) {
        this.id = id
        this.coverUri = coverUri
        this.displayName = displayName
        this.count = count
        albumName = this.displayName
    }

    private constructor(source: Parcel) {
        id = source.readString()
        coverUri = source.readParcelable(Uri::class.java.classLoader)
        displayName = source.readString()
        count = source.readLong()
        albumName = displayName
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeParcelable(coverUri, 0)
        dest.writeString(displayName)
        dest.writeLong(count)
    }

    /**
     * 加入拍摄item
     */
    fun addCaptureCount() {
        count++
    }

    val isAll: Boolean
        get() = ALBUM_ID_ALL == id
    val isEmpty: Boolean
        get() = count == 0L

    override fun toString(): String {
        return "AlbumData{" +
                "mId='" + id + '\'' +
                ", mCoverPath='" + coverUri + '\'' +
                ", mDisplayName='" + displayName + '\'' +
                ", mCount=" + count +
                '}'
    }

    companion object {
        @SuppressLint("ParcelCreator")
        val CREATOR: Creator<AlbumData?> = object : Creator<AlbumData?> {
            override fun createFromParcel(source: Parcel): AlbumData {
                return AlbumData(source)
            }

            override fun newArray(size: Int): Array<AlbumData?> {
                return arrayOfNulls(size)
            }


        }

        const val ALBUM_ID_ALL = "-1"
        const val ALBUM_NAME_ALL = "全部图片"
        fun valueOf(cursor: Cursor, mContext: Context?): AlbumData {
            val index = cursor.getColumnIndex(AlbumDataLoader.COLUMN_URI)
            val count = cursor.getColumnIndex(AlbumDataLoader.COLUMN_COUNT)
            val uri = if (index >= 0) cursor.getString(index) else null
            var bucket_display_name = cursor.getString(cursor.getColumnIndex("bucket_display_name"))
            if (bucket_display_name == null && mContext != null) {
                bucket_display_name = "0"
            }
            return AlbumData(
                cursor.getString(cursor.getColumnIndex("bucket_id")),
                Uri.parse(uri ?: ""),
                bucket_display_name,
                if (count > 0) cursor.getLong(count) else 0
            )
        }

        fun getFileRealNameFromUri(context: Context?, fileUri: Uri?): String? {
            if (context == null || fileUri == null) return null
            val documentFile = DocumentFile.fromSingleUri(context, fileUri) ?: return null
            return documentFile.name
        }
    }




}