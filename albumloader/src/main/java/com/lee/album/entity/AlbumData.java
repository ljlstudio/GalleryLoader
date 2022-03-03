package com.lee.album.entity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.documentfile.provider.DocumentFile;

import com.lee.album.loader.AlbumDataLoader;


/**
 * 相册数据对象
 */
public class AlbumData implements Parcelable {

    public static final Creator<AlbumData> CREATOR = new Creator<AlbumData>() {
        @Override
        public AlbumData createFromParcel(Parcel source) {
            return new AlbumData(source);
        }

        @Override
        public AlbumData[] newArray(int size) {
            return new AlbumData[size];
        }
    };

    public static final String ALBUM_ID_ALL = "-1";
    public static final String ALBUM_NAME_ALL = "全部图片";

    private final String mId;
    private final Uri mCoverPath;
    private final String mDisplayName;
    private long mCount;
    private String albumName;
    private Integer sort;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public AlbumData(String id, Uri coverUri, String displayName, long count) {
        mId = id;
        mCoverPath = coverUri;
        mDisplayName = displayName;
        mCount = count;
        setAlbumName(mDisplayName);
    }

    private AlbumData(Parcel source) {
        mId = source.readString();
        mCoverPath = source.readParcelable(Uri.class.getClassLoader());
        mDisplayName = source.readString();
        mCount = source.readLong();
        setAlbumName(mDisplayName);
    }

    public static AlbumData valueOf(Cursor cursor, Context mContext) {
        int index = cursor.getColumnIndex(AlbumDataLoader.COLUMN_URI);
        int count = cursor.getColumnIndex(AlbumDataLoader.COLUMN_COUNT);
        String uri = index >= 0 ? cursor.getString(index) : null;



        String bucket_display_name = cursor.getString(cursor.getColumnIndex("bucket_display_name"));
        if (bucket_display_name == null && mContext != null) {
            bucket_display_name = "0";
        }
        return new AlbumData(cursor.getString(cursor.getColumnIndex("bucket_id")),
                Uri.parse(uri != null ? uri : ""),
                bucket_display_name,
                count > 0 ? cursor.getLong(count) : 0);
    }

    public static String getFileRealNameFromUri(Context context, Uri fileUri) {
        if (context == null || fileUri == null) return null;
        DocumentFile documentFile = DocumentFile.fromSingleUri(context, fileUri);
        if (documentFile == null) return null;
        return documentFile.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeParcelable(mCoverPath, 0);
        dest.writeString(mDisplayName);
        dest.writeLong(mCount);
    }

    public String getId() {
        return mId;
    }

    /**
     * 获取封面路径
     *
     * @return
     */
    public Uri getCoverUri() {
        return mCoverPath;
    }

    public Integer getSort() {
        return sort;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    //全部图片、最近图片、收藏、个人相片、微信、淘宝、QQ、截图、壁纸、其他；
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取显示名称
     *
     * @return
     */
    public String getDisplayName() {
//        if (isAll()) {
//            return "所有图片";
//        }
        return mDisplayName;
    }

    /**
     * 获取相册数量
     *
     * @return
     */
    public long getCount() {
        return mCount;
    }

    /**
     * 加入拍摄item
     */
    public void addCaptureCount() {
        mCount++;
    }

    public boolean isAll() {
        return ALBUM_ID_ALL.equals(mId);
    }

    public boolean isEmpty() {
        return mCount == 0;
    }

    @Override
    public String toString() {
        return "AlbumData{" +
                "mId='" + mId + '\'' +
                ", mCoverPath='" + mCoverPath + '\'' +
                ", mDisplayName='" + mDisplayName + '\'' +
                ", mCount=" + mCount +
                '}';
    }
}
