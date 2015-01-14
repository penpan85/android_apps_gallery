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

import com.android.gallery3d.app.GalleryApp;

import java.util.concurrent.atomic.AtomicBoolean;

// This handles change notification for media sets.
public class ChangeNotifier {

    private MediaSet mMediaSet;
    private AtomicBoolean mContentDirty = new AtomicBoolean(true);

    public ChangeNotifier(MediaSet set, Uri uri, GalleryApp application) {
        mMediaSet = set;
        application.getDataManager().registerChangeNotifier(uri, this);
    }

    public ChangeNotifier(MediaSet set, Uri[] uris, GalleryApp application) {
        mMediaSet = set;
        for (int i = 0; i < uris.length; i++) {
            application.getDataManager().registerChangeNotifier(uris[i], this);
        }
    }

    // Returns the dirty flag and clear it.
    // 原始状态,mContentDirty 为true
    // isDirty()执行一次，mContentDirty为false
    // onchange一次, mContentDirty为true
    // isDirty()与onChange()各自连续执行都不能改变mContentDirty的状态
    public boolean isDirty() {
    	//compareAndSet函数，如果mContentDirty包裹的boolean型变量与expected参数值相同，则置为后面的参数update value,此处为"false"
    	//更新成功，返回true,否则返回false
        return mContentDirty.compareAndSet(true, false);
    }

    //伪造change
    public void fakeChange() {
        onChange(false);
    }

    protected void onChange(boolean selfChange) {
        if (mContentDirty.compareAndSet(false, true)) {
            mMediaSet.notifyContentChanged();
        }
    }
}