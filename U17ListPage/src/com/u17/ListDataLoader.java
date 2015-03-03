package com.u17;

import java.android.support.v4.app.LoaderManager;
import java.android.support.v4.app.LoaderManager.LoaderCallbacks;
import java.util.List;

import com.u17.net.model.ComicListItem;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

/**
 * @author pengpan
 * 普通的Loader
 * LoaderManager与activity关联，一个manager对应一个activity,activity和其中的fragment
 * 等由该activity对应的manager通过id来管理
 * 
 * loaderManager主要方法:
 *     
 * Ensures a loader is initialized and active.  If the loader doesn't
 * already exist, one is created and (if the activity/fragment is currently
 * started) starts the loader.  Otherwise the last created
 * loader is re-used.
 * 
 * <p>In either case, the given callback is associated with the loader, and
 * will be called as the loader state changes.  If at the point of call
 * the caller is in its started state, and the requested loader
 * already exists and has generated its data, then
 * callback {@link LoaderCallbacks#onLoadFinished} will
 * be called immediately (inside of this function), so you must be prepared
 * for this to happen.
 * 
 * 确认加载器被初始化并且是活跃的，如果不存在该指定Id的加载器则创建并且启动，否则重用上次创建的loader,
 * 另一方面，给定的callback是与loader关联的,当loader的加载状态发生变化时，callback对象将会被调用
 * ,如果调用者处于已经开始的状态，并且已经请求过的loader已经存在并生成了数据，那么callback的onLoadFinished
 * 会被立即调用，所以应当充分考虑到这样的情况
 *
 * @param id A unique identifier for this loader.  Can be whatever you want.
 * Identifiers are scoped to a particular LoaderManager instance.
 * 
 * 与loader对应的唯一标识符，可以是任意值，标识符局限于一个特定的loaderManager实例
 * 
 * @param args Optional arguments to supply to the loader at construction.
 * If a loader already exists (a new one does not need to be created), this
 * parameter will be ignored and the last arguments continue to be used.
 * 
 * loader加载所需要的参数，如果一个加载器已经存在，则不会被重新创建，此参数将会被忽略，加载器会使用上次创建
 * 时的参数
 * 
 * @param callback Interface the LoaderManager will call to report about
 * changes in the state of the loader.  Required.
 *
 * initLoader(int id,Bundle bundle,LoaderManager.loaderCallBack callBack)
 * 
 * 
 * 
 * 
 * 
 * Starts a new or restarts an existing {@link android.content.Loader} in
 * this manager, registers the callbacks to it,
 * and (if the activity/fragment is currently started) starts loading it.
 * If a loader with the same id has previously been
 * started it will automatically be destroyed when the new loader completes
 * its work. The callback will be delivered before the old loader
 * is destroyed.
 *
 * 启动一个新的或者重新启动loaderManager中存在的loader,并注册callback对象，当前可见的activity或者fragment
 * 将会立即开始利用它加载,如果相同id的loader之前已经被启动过，当新的loader完成加载任务后，旧的将会被销毁
 * ，在销毁之前，callback对象将会被投递新的loader的加载结果
 * 
 * 这个函数会做一些加载器的限制工作，如果相同Id的很多加载器已经被创建但还没有获取到数据，对该方法的调用会返回一个新的
 * loader但会一直到先前的loader完成加载后才会被启动，而且所有的旧的加载器的数据会被抛弃
 * 
 * 在调用该函数后，任何与参数中id相关联的loader将会被视作无效，调用者将会在旧的加载器被销毁前得到数据
 * 
 * loaderManager执行此函数，会试图从内存中得到加载器，若得到了旧的加载器，则执行abandon,
 * 重新创建加载器
 * 
 *
 * @param id A unique identifier for this loader.  Can be whatever you want.
 * Identifiers are scoped to a particular LoaderManager instance.
 * @param args Optional arguments to supply to the loader at construction.
 * @param callback Interface the LoaderManager will call to report about
 * changes in the state of the loader.  Required.
 * 
 * Loader<D> restartLoader(int id, Bundle args,LoaderManager.LoaderCallbacks<D> callback);
 *
 *
 *
 * Stops and removes the loader with the given ID.  If this loader
 * had previously reported data to the client through
 * {@link LoaderCallbacks#onLoadFinished(Loader, Object)}, a call
 * will be made to {@link LoaderCallbacks#onLoaderReset(Loader)}.
 * 
 * 停止并且删除给定Id对应的loader,如果loader之前已经通过 callback # onLoadFinished(loader,object)
 * 汇报过请求结果，那么callback # onLoaderReset(loader) 将会被调用
 * 
 *	public abstract void destroyLoader(int id);
 *
 * Return the Loader with the given id or null if no matching Loader
 * is found.
 * 
 *  返回给定id对应的loader对象
 * 
 *	public abstract <D> Loader<D> getLoader(int id);
 *
 */
public class ListDataLoader extends AsyncTaskLoader<List<ComicListItem>>{

	
	public ListDataLoader(Context context) {
		super(context);
	}
	
	@Override
	public void deliverResult(List<ComicListItem> data) {
		super.deliverResult(data);
	}
	
	 /**
     * Return whether this load has been started.  That is, its {@link #startLoading()}
     * has been called and no calls to {@link #stopLoading()} or
     * {@link #reset()} have yet been made.
     * 加载是否已经开始，
     * startLoading 已经被调用，
     * stopLoading 或者 reset还没被调用
     */
	@Override
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return super.isStarted();
	}
	
	/**
     * Return whether this loader has been abandoned.  In this state, the
     * loader <em>must not</em> report any new data, and <em>must</em> keep
     * its last reported data valid until it is finally reset.
     * 加载器是否被抛弃
     * 在此状态下，加载器肯定不会汇报任何新的数据，会保持最后一次的数据一直到被重置
     */
	@Override
	public boolean isAbandoned() {
		// TODO Auto-generated method stub
		return super.isAbandoned();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#isReset()
	 * 如果处于重置状态，加载器肯定至少启动过一次，或者其reset方法已经被调用
	 */
	@Override
	public boolean isReset() {
		// TODO Auto-generated method stub
		return super.isReset();
	}
	
	/*startLoading 该方法为final类型故不可在子类覆盖
	 * 启动一个异步的加载，如果加载完成，callback将会在主线程中被通知
	 * 如果先前已经完成过一次加载并仍然有效，加载结果将会被立即转给回调
	 * 加载器将会监控数据源，如果源发生了改变，加载器将会重新执行并投递结果，但是调用了stoploading将会终止这个过程
	 * 
	 * */
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#forceLoad()
	 * 强制进行异步加载，不同于startLoading,此函数将会忽略加载好的数据集，重新启动一个加载
	 * 一般来讲当加载器已经开始，isStarted()返回true时调用该函数
	 */
	@Override
	public void forceLoad() {
		super.forceLoad();
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#stopLoading()
	 * 停止更新的投递直到startLoading()又一次被调用
	 * 在这个节点，已经加载好的数据仍然会保持有效性
	 * 调用者仍然可以自由使用加载器最后一次完成加载的数据，但数据发生变化不会再被汇报，加载器仍然会监控
	 * 数据的变化，但在下次startLoading()被调用前不会再汇报数据
	 */
	@Override
	public void stopLoading() {
		super.stopLoading();
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#abandon()
	 * 由LoaderManager进行调用，一般不直接使用
	 * 告诉loader它已经被抛弃了,通常在reset前被调用,会保持当前已经加载好的数据但不会再汇报任何新的数据
	 * 
	 */
	@Override
	public void abandon() {
		// TODO Auto-generated method stub
		super.abandon();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.content.Loader#reset()
	 * 由LoaderManager的destroy方法进行调用，一般不直接使用
	 * 重置加载器，callback的onLoaderReset函数将会被调用，此时可以做对应的销毁工作
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
	}
	
	@Override
	public List<ComicListItem> loadInBackground() {
		
		return null;
	}

}
