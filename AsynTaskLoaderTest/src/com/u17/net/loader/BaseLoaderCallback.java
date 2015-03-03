package com.u17.net.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * @author pengpan
 * 对loadercallback进行封装
 * @param <T>
 */
public class BaseLoaderCallback<T> implements LoaderManager.LoaderCallbacks<T> {

	private static String tag = "BaseLoader";
	private Set<String> mRunon = new HashSet<String>();
	private HashMap<String, Integer> mRunning = new HashMap<String, Integer>();
	private HashMap<Integer, String> mRunningReverse = new HashMap<Integer, String>();
	private int getNextFreeLoaderId(LoaderManager manager) {
		for (int i = 0; i < 2147483647; i++) {
			if ((!this.mRunningReverse.containsKey(Integer.valueOf(i)))
					&& (manager.getLoader(i) == null)) {
				return i;
			}
		}
		return -1;
	}

	// 从loadermanager中删除指定的加载器
	private void cancelTask(LoaderManager manager, String taskName) {
		if (mRunning.containsKey(taskName)) {
			int i = mRunning.get(taskName).intValue();
			manager.destroyLoader(i);
			mRunning.remove(i);
			mRunon.remove(taskName);
			mRunningReverse.remove(taskName);
		}
	}

	// 返回指定的加载是否被初始化
	public boolean isInitialnized(String taskName) {
		return mRunon.contains(taskName);
	}

	// 返回指定的加载器是否还在运行
	public boolean isRunning(String taskName) {
		return mRunning.containsKey(taskName);
	}

	// 遍历mRunning集合，看Loadmanager中是否存在这些id对应的loader,如果存在，就使其初始化，不然如果使用了已经初始化的loader，会导致加载器一启动就执行onLoaderReset
	//
	public void onActivityCreated(LoaderManager manager) {
		HashSet<String> shouldCancelLoaders = new HashSet<String>();
		Set<Entry<String, Integer>> mRunningSet = mRunning.entrySet();
		Iterator<Entry<String, Integer>> iterator = mRunningSet.iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			shouldCancelLoaders.add(entry.getKey());
			manager.destroyLoader(entry.getValue().intValue());
		}
		for (String taskName : shouldCancelLoaders) {
			cancelTask(manager, taskName);
		}
	}

	@Override
	public Loader<T> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	// 将每个完成的网络请求对应的loader执行销毁
	public void onLoadFinished(LoaderManager manager, Loader<T> loader) {
		String taskName = mRunningReverse.get(Integer.valueOf(loader.getId()));
		if (taskName != null) {
			mRunning.remove(taskName);
			mRunningReverse.remove(loader.getId());
		}
		if (manager != null && loader != null)
			manager.destroyLoader(loader.getId());

	}

	@Override
	public void onLoadFinished(Loader<T> arg0, T arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<T> arg0) {
		// TODO Auto-generated method stub

	}

	// 开始执行任务，将网络加载任务的id存放进入三个集合，初始化加载器
	public void startTask(LoaderManager paramLoaderManager, String paramString,
			Bundle paramBundle) {
		if (!this.mRunning.containsKey(paramString)) {
			int i = getNextFreeLoaderId(paramLoaderManager);
			this.mRunon.add(paramString);
			this.mRunning.put(paramString, Integer.valueOf(i));
			this.mRunningReverse.put(Integer.valueOf(i), paramString);
			paramLoaderManager.initLoader(i, paramBundle, this).forceLoad();
		}
	}

}
