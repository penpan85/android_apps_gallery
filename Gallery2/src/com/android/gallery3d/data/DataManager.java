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

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.android.gallery3d.app.GalleryApp;
import com.android.gallery3d.app.StitchingChangeListener;
import com.android.gallery3d.common.Utils;
import com.android.gallery3d.data.MediaObject.PanoramaSupportCallback;
import com.android.gallery3d.data.MediaSet.ItemConsumer;
import com.android.gallery3d.data.MediaSource.PathId;
import com.android.gallery3d.picasasource.PicasaSource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.WeakHashMap;

// DataManager manages all media sets and media items in the system.
// 数据管理器管理所有的媒体集合以及所有的媒体条目
// Each MediaSet and MediaItem has a unique 64 bits id. The most significant
// 32 bits represents its parent, and the least significant 32 bits represents
// the self id. For MediaSet the self id is is globally unique, but for
// MediaItem it's unique only relative to its parent.
// 每个媒体集合和媒体条目有一个唯一的64位的id，头32位表示某个mediaset的父set,后面32位表示它自身的id，
// 每个mediaSet的id都是全局唯一的，但对于mediaItem来讲，只是相对于其父级mediaSet,id是唯一的
// To make sure the id is the same when the MediaSet is re-created, a child key
// is provided to obtainSetId() to make sure the same self id will be used as
// when the parent and key are the same. A sequence of child keys is called a
// path. And it's used to identify a specific media set even if the process is
// killed and re-created, so child keys should be stable identifiers.
// 为了确保当mediaSet被重新创建时其id是相同的，一个子对象提供了obtainSetId来提供子对象的Id来确保当父对象id未发生
// 变化时，子对象的id也不发生变化，子对象的key的序列其实是一个路径，不管进程是否被重新创建都用来标示特定mediaSet，
// 所以子对象的key具有稳定的唯一性

/**
 * @author pengpan
 * 数据源管理类
 */
public class DataManager implements StitchingChangeListener {
	//包含图片
    public static final int INCLUDE_IMAGE = 1;
    //包含视频
    public static final int INCLUDE_VIDEO = 2;
    //包含图片和视频
    public static final int INCLUDE_ALL = INCLUDE_IMAGE | INCLUDE_VIDEO;
    //只包含本地所有媒体类型
    public static final int INCLUDE_LOCAL_ONLY = 4;
    
    public static final int INCLUDE_LOCAL_IMAGE_ONLY =
            INCLUDE_LOCAL_ONLY | INCLUDE_IMAGE;
    public static final int INCLUDE_LOCAL_VIDEO_ONLY =
            INCLUDE_LOCAL_ONLY | INCLUDE_VIDEO;
    public static final int INCLUDE_LOCAL_ALL_ONLY =
            INCLUDE_LOCAL_ONLY | INCLUDE_IMAGE | INCLUDE_VIDEO;

    // Any one who would like to access data should require this lock
    // to prevent concurrency issue.
    public static final Object LOCK = new Object();

    // 构造出自身的对象
    public static DataManager from(Context context) {
        GalleryApp app = (GalleryApp) context.getApplicationContext();
        return app.getDataManager();
    }

    private static final String TAG = "DataManager";

    // This is the path for the media set seen by the user at top level.
    private static final String TOP_SET_PATH = "/combo/{/local/all,/picasa/all}";

    private static final String TOP_IMAGE_SET_PATH = "/combo/{/local/image,/picasa/image}";

    private static final String TOP_VIDEO_SET_PATH =
            "/combo/{/local/video,/picasa/video}";

    private static final String TOP_LOCAL_SET_PATH = "/local/all";

    private static final String TOP_LOCAL_IMAGE_SET_PATH = "/local/image";

    private static final String TOP_LOCAL_VIDEO_SET_PATH = "/local/video";

    public static final Comparator<MediaItem> sDateTakenComparator =
            new DateTakenComparator();

    private static class DateTakenComparator implements Comparator<MediaItem> {
        @Override
        public int compare(MediaItem item1, MediaItem item2) {
            return -Utils.compare(item1.getDateInMs(), item2.getDateInMs());
        }
    }

    private final Handler mDefaultMainHandler;

    private GalleryApp mApplication;
    private int mActiveCount = 0;

    //缓存所有的内容观察者对象
    private HashMap<Uri, NotifyBroker> mNotifierMap =
            new HashMap<Uri, NotifyBroker>();


    private HashMap<String, MediaSource> mSourceMap =
            new LinkedHashMap<String, MediaSource>();

    public DataManager(GalleryApp application) {
        mApplication = application;
        mDefaultMainHandler = new Handler(application.getMainLooper());
    }

    //初始化各种数据源
    public synchronized void initializeSourceMap() {
        if (!mSourceMap.isEmpty()) return;

        // the order matters, the UriSource must come last
        addSource(new LocalSource(mApplication));
        addSource(new PicasaSource(mApplication));
        addSource(new ComboSource(mApplication));
        addSource(new ClusterSource(mApplication));
        addSource(new FilterSource(mApplication));
        addSource(new SecureSource(mApplication));
        addSource(new UriSource(mApplication));
        addSource(new SnailSource(mApplication));

        if (mActiveCount > 0) {
            for (MediaSource source : mSourceMap.values()) {
                source.resume();
            }
        }
    }

    public String getTopSetPath(int typeBits) {

        switch (typeBits) {
            case INCLUDE_IMAGE: return TOP_IMAGE_SET_PATH;
            case INCLUDE_VIDEO: return TOP_VIDEO_SET_PATH;
            case INCLUDE_ALL: return TOP_SET_PATH;
            case INCLUDE_LOCAL_IMAGE_ONLY: return TOP_LOCAL_IMAGE_SET_PATH;
            case INCLUDE_LOCAL_VIDEO_ONLY: return TOP_LOCAL_VIDEO_SET_PATH;
            case INCLUDE_LOCAL_ALL_ONLY: return TOP_LOCAL_SET_PATH;
            default: throw new IllegalArgumentException();
        }
    }

    // open for debug
    void addSource(MediaSource source) {
        if (source == null) return;
        mSourceMap.put(source.getPrefix(), source);
    }

    // A common usage of this method is:
    // synchronized (DataManager.LOCK) {
    //     MediaObject object = peekMediaObject(path);
    //     if (object == null) {
    //         object = createMediaObject(...);
    //     }
    // }
    public MediaObject peekMediaObject(Path path) {
        return path.getObject();
    }

    /**
     * @param path
     * @return
     * 根据path得到相应的MediaObject对象
     * /combo/test/{/local/all,/picasa/all}对应的Prefix是"combo"
     * dataManager通过dataSource管理指定的mediaObject
     */
    public MediaObject getMediaObject(Path path) {
        synchronized (LOCK) {
            MediaObject obj = path.getObject();
            if (obj != null) return obj;
            // 先得到mediaObject对应的数据源
            // 每种source其实都对应一种prefix,localSource对应"local",
            // comboSource对应"combo"
            // 并且各类source下的mediaItem对应的path第一个segment必然是prefix
            MediaSource source = mSourceMap.get(path.getPrefix());
            if (source == null) {
                Log.w(TAG, "cannot find media source for path: " + path);
                return null;
            }

            try {
            	// 默认情况下
                MediaObject object = source.createMediaObject(path);
                if (object == null) {
                    Log.w(TAG, "cannot create media object: " + path);
                }
                return object;
            } catch (Throwable t) {
                Log.w(TAG, "exception in creating media object: " + path, t);
                return null;
            }
        }
    }

    public MediaObject getMediaObject(String s) {
        return getMediaObject(Path.fromString(s));
    }

    public MediaSet getMediaSet(Path path) {
        return (MediaSet) getMediaObject(path);
    }

    public MediaSet getMediaSet(String s) {
        return (MediaSet) getMediaObject(s);
    }

    /**
     * @param segment
     * @return 数据源uri某个segment对应的mediaSet[]
     * 默认传入值为{set1, set2, ...}，得到
     */
    public MediaSet[] getMediaSetsFromString(String segment) {
    	//传入参数{set1, set2, ...}，转换为一个数组，每个元素对应"set1","set2"
        String[] seq = Path.splitSequence(segment);
        int n = seq.length;
        MediaSet[] sets = new MediaSet[n];
        for (int i = 0; i < n; i++) {
            sets[i] = getMediaSet(seq[i]);
        }
        return sets;
    }

    // Maps a list of Paths to MediaItems, and invoke consumer.consume()
    // for each MediaItem (may not be in the same order as the input list).
    // An index number is also passed to consumer.consume() to identify
    // the original position in the input list of the corresponding Path (plus
    // startIndex).
    public void mapMediaItems(ArrayList<Path> list, ItemConsumer consumer,
            int startIndex) {
        HashMap<String, ArrayList<PathId>> map =
                new HashMap<String, ArrayList<PathId>>();

        // Group the path by the prefix.
        int n = list.size();
        for (int i = 0; i < n; i++) {
            Path path = list.get(i);
            String prefix = path.getPrefix();
            ArrayList<PathId> group = map.get(prefix);
            if (group == null) {
                group = new ArrayList<PathId>();
                map.put(prefix, group);
            }
            group.add(new PathId(path, i + startIndex));
        }

        // For each group, ask the corresponding media source to map it.
        for (Entry<String, ArrayList<PathId>> entry : map.entrySet()) {
            String prefix = entry.getKey();
            MediaSource source = mSourceMap.get(prefix);
            source.mapMediaItems(entry.getValue(), consumer);
        }
    }

    // The following methods forward the request to the proper object.
    public int getSupportedOperations(Path path) {
        return getMediaObject(path).getSupportedOperations();
    }

    public void getPanoramaSupport(Path path, PanoramaSupportCallback callback) {
        getMediaObject(path).getPanoramaSupport(callback);
    }

    public void delete(Path path) {
        getMediaObject(path).delete();
    }

    public void rotate(Path path, int degrees) {
        getMediaObject(path).rotate(degrees);
    }

    public Uri getContentUri(Path path) {
        return getMediaObject(path).getContentUri();
    }

    public int getMediaType(Path path) {
        return getMediaObject(path).getMediaType();
    }

    public Path findPathByUri(Uri uri, String type) {
        if (uri == null) return null;
        for (MediaSource source : mSourceMap.values()) {
            Path path = source.findPathByUri(uri, type);
            if (path != null) return path;
        }
        return null;
    }

    public Path getDefaultSetOf(Path item) {
        MediaSource source = mSourceMap.get(item.getPrefix());
        return source == null ? null : source.getDefaultSetOf(item);
    }

    // Returns number of bytes used by cached pictures currently downloaded.
    public long getTotalUsedCacheSize() {
        long sum = 0;
        for (MediaSource source : mSourceMap.values()) {
            sum += source.getTotalUsedCacheSize();
        }
        return sum;
    }

    // Returns number of bytes used by cached pictures if all pending
    // downloads and removals are completed.
    public long getTotalTargetCacheSize() {
        long sum = 0;
        for (MediaSource source : mSourceMap.values()) {
            sum += source.getTotalTargetCacheSize();
        }
        return sum;
    }
    
    //倘若uri对应的数据内容发生了变化，那么会先通知broker,broker再通知notifier
    public void registerChangeNotifier(Uri uri, ChangeNotifier notifier) {
    	//直接的内容观察者
        NotifyBroker broker = null;
        synchronized (mNotifierMap) {
            broker = mNotifierMap.get(uri);
            if (broker == null) {
                broker = new NotifyBroker(mDefaultMainHandler);
                mApplication.getContentResolver()
                        .registerContentObserver(uri, true, broker);
                mNotifierMap.put(uri, broker);
            }
        }
        broker.registerNotifier(notifier);
    }

    public void resume() {
        if (++mActiveCount == 1) {
            for (MediaSource source : mSourceMap.values()) {
                source.resume();
            }
        }
    }

    public void pause() {
        if (--mActiveCount == 0) {
            for (MediaSource source : mSourceMap.values()) {
                source.pause();
            }
        }
    }

    private static class NotifyBroker extends ContentObserver {
        private WeakHashMap<ChangeNotifier, Object> mNotifiers =
                new WeakHashMap<ChangeNotifier, Object>();

        public NotifyBroker(Handler handler) {
            super(handler);
        }

        public synchronized void registerNotifier(ChangeNotifier notifier) {
            mNotifiers.put(notifier, null);
        }

        @Override
        public synchronized void onChange(boolean selfChange) {
            for(ChangeNotifier notifier : mNotifiers.keySet()) {
                notifier.onChange(selfChange);
            }
        }
    }

    @Override
    public void onStitchingQueued(Uri uri) {
        // Do nothing.
    }

    @Override
    public void onStitchingResult(Uri uri) {
        Path path = findPathByUri(uri, null);
        if (path != null) {
            MediaObject mediaObject = getMediaObject(path);
            if (mediaObject != null) {
                mediaObject.clearCachedPanoramaSupport();
            }
        }
    }

    @Override
    public void onStitchingProgress(Uri uri, int progress) {
        // Do nothing.
    }
}
