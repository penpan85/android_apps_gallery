/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gallery3d.data;

import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;

import com.test.android.gallery3d.R;
import com.android.gallery3d.app.GalleryApp;
import com.android.gallery3d.data.BucketHelper.BucketEntry;
import com.android.gallery3d.util.Future;
import com.android.gallery3d.util.FutureListener;
import com.android.gallery3d.util.MediaSetUtils;
import com.android.gallery3d.util.ThreadPool;
import com.android.gallery3d.util.ThreadPool.JobContext;

import java.util.ArrayList;
import java.util.Comparator;

// LocalAlbumSet lists all image or video albums in the local storage.
// The path should be "/local/image", "local/video" or "/local/all"
/**
 * @author pengpan
 * 本地相册集,包含许多个LocalAlbum或者LocalMergeAlbum
 */
public class LocalAlbumSet extends MediaSet
        implements FutureListener<ArrayList<MediaSet>> {
    @SuppressWarnings("unused")
    private static final String TAG = "LocalAlbumSet";

    
    public static final Path PATH_ALL = Path.fromString("/local/all");
    public static final Path PATH_IMAGE = Path.fromString("/local/image");
    public static final Path PATH_VIDEO = Path.fromString("/local/video");

    private static final Uri[] mWatchUris =
        {Images.Media.EXTERNAL_CONTENT_URI, Video.Media.EXTERNAL_CONTENT_URI};

    private final GalleryApp mApplication;
    // 是image类型还是 video类型
    private final int mType;
    private ArrayList<MediaSet> mAlbums = new ArrayList<MediaSet>();
    private final ChangeNotifier mNotifier;
    private final String mName;
    private final Handler mHandler;
    private boolean mIsLoading;

    private Future<ArrayList<MediaSet>> mLoadTask;
    private ArrayList<MediaSet> mLoadBuffer;

    public LocalAlbumSet(Path path, GalleryApp application) {
        super(path, nextVersionNumber());
        mApplication = application;
        mHandler = new Handler(application.getMainLooper());
        //默认path "/local/all",所以mType默认情况下会是"MEDIA_TYPE_ALL"
        mType = getTypeFromPath(path);
        mNotifier = new ChangeNotifier(this, mWatchUris, application);
        mName = application.getResources().getString(
                R.string.set_label_local_albums);
    }

    private static int getTypeFromPath(Path path) {
        String name[] = path.split();
        if (name.length < 2) {
            throw new IllegalArgumentException(path.toString());
        }
        return getTypeFromString(name[1]);
    }

    @Override
    public MediaSet getSubMediaSet(int index) {
        return mAlbums.get(index);
    }

    @Override
    public int getSubMediaSetCount() {
        return mAlbums.size();
    }

    @Override
    public String getName() {
        return mName;
    }

    private static int findBucket(BucketEntry entries[], int bucketId) {
        for (int i = 0, n = entries.length; i < n; ++i) {
            if (entries[i].bucketId == bucketId) return i;
        }
        return -1;
    }

    /**
     * @author pengpan
     * 相册数据加载器，获取此相册集下所有相册对应的MediaSet对象集合 
     */
    private class AlbumsLoader implements ThreadPool.Job<ArrayList<MediaSet>> {

        @Override
        @SuppressWarnings("unchecked")
        public ArrayList<MediaSet> run(JobContext jc) {
            // Note: it will be faster if we only select media_type and bucket_id.
            //       need to test the performance if that is worth
        	// 得到所有媒体文件的entry
            BucketEntry[] entries = BucketHelper.loadBucketEntries(
                    jc, mApplication.getContentResolver(), mType);

            if (jc.isCancelled()) return null;

            int offset = 0;
            // Move camera and download bucket to the front, while keeping the
            // order of others.
            // 让camera和download类型的媒体文件entry排到最前面，保持其他的顺序
            int index = findBucket(entries, MediaSetUtils.CAMERA_BUCKET_ID);
            if (index != -1) {
                circularShiftRight(entries, offset++, index);
            }
            index = findBucket(entries, MediaSetUtils.DOWNLOAD_BUCKET_ID);
            if (index != -1) {
                circularShiftRight(entries, offset++, index);
            }

            ArrayList<MediaSet> albums = new ArrayList<MediaSet>();
            DataManager dataManager = mApplication.getDataManager();
            for (BucketEntry entry : entries) {
            	// entry.bucketId:相当于相册的父路径的hashCode
            	// entry.bucketName:相当于相册对应的文件夹名称
            	// 比方当前相册album完整路径 /sdcard/download/album
            	// 那么bucketId即为"/sdcard/download"的hashCode,bucketName为album
            	// mPath默认下会传入 "/local/all"
                MediaSet album = getLocalAlbum(dataManager,
                        mType, mPath, entry.bucketId, entry.bucketName);
                albums.add(album);
            }
            return albums;
        }
    }

    /**
     * @param manager
     * @param type MEDIA_TYPE_IMAGE,MEDIA_TYPE_VIDEO,MEDIA_TYPE_ALL
     * @param parent 相册的path,如"/local/all"
     * @param id 相册的实际父路径的hashCode,例如 /sdcard/download
     * @param name 相册实际文件夹名称
     * @return
     */
    private MediaSet getLocalAlbum(
            DataManager manager, int type, Path parent, int id, String name) {
        synchronized (DataManager.LOCK) {
        	// 生成 相册目录对应的path,/local/all/jdfdjjf
            Path path = parent.getChild(id);
            // 得到path可能已经关联的MediaObject
            MediaObject object = manager.peekMediaObject(path);
            if (object != null) return (MediaSet) object;
            switch (type) {
                case MEDIA_TYPE_IMAGE:
                	// 默认会得到 path:/local/image/jkjf930,
                    return new LocalAlbum(path, mApplication, id, true, name);
                case MEDIA_TYPE_VIDEO:
                    return new LocalAlbum(path, mApplication, id, false, name);
                //默认传入的是/local/all,所以会执行这一句
                case MEDIA_TYPE_ALL:
                    Comparator<MediaItem> comp = DataManager.sDateTakenComparator;
                    return new LocalMergeAlbum(path, comp, new MediaSet[] {
                            getLocalAlbum(manager, MEDIA_TYPE_IMAGE, PATH_IMAGE, id, name),
                            getLocalAlbum(manager, MEDIA_TYPE_VIDEO, PATH_VIDEO, id, name)}, id);
            }
            throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    @Override
    public synchronized boolean isLoading() {
        return mIsLoading;
    }

    @Override
    // synchronized on this function for
    //   1. Prevent calling reload() concurrently.
    //   2. Prevent calling onFutureDone() and reload() concurrently
    // 页面进入resume后就会调用此reload()方法，
    // 第一次reload(),会执行AlbumsLoader加载各个相册的信息
    public synchronized long reload() {
    	//首次进入，isDirty()返回true
        if (mNotifier.isDirty()) {
            if (mLoadTask != null) mLoadTask.cancel();
            mIsLoading = true;
            // 执行完这一步，该相册集所有的LocalAlbum都被加载进了mAlbums
            mLoadTask = mApplication.getThreadPool().submit(new AlbumsLoader(), this);
        }
        //第一次reload()，不会执行这一段
        if (mLoadBuffer != null) {
            mAlbums = mLoadBuffer;
            mLoadBuffer = null;
            for (MediaSet album : mAlbums) {
                album.reload();
            }
            mDataVersion = nextVersionNumber();
        }
        // 第一次执行，mDataVersion会返回1
        return mDataVersion;
    }

    /* (non-Javadoc)
     * @see com.android.gallery3d.util.FutureListener#onFutureDone(com.android.gallery3d.util.Future)
     * AlbumsLoader被执行完毕后，会回调此方法最终得到所有的localAlbum
     */
    @Override
    public synchronized void onFutureDone(Future<ArrayList<MediaSet>> future) {
        if (mLoadTask != future) return; // ignore, wait for the latest task
        mLoadBuffer = future.get();
        mIsLoading = false;
        if (mLoadBuffer == null) mLoadBuffer = new ArrayList<MediaSet>();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
            	//通知albumSetDataLoader数据发生了变化
                notifyContentChanged();
            }
        });
    }

    // For debug only. Fake there is a ContentObserver.onChange() event.
    void fakeChange() {
        mNotifier.fakeChange();
    }

    // Circular shift the array range from a[i] to a[j] (inclusive). That is,
    // a[i] -> a[i+1] -> a[i+2] -> ... -> a[j], and a[j] -> a[i]
    private static <T> void circularShiftRight(T[] array, int i, int j) {
        T temp = array[j];
        for (int k = j; k > i; k--) {
            array[k] = array[k - 1];
        }
        array[i] = temp;
    }
}
