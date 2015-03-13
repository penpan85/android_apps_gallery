package com.u17.adapter;

import java.util.ArrayList;
import java.util.List;

import com.u17.BaseLoaderCallback;
import com.u17.ListDataLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author pengpan
 *
 * @param <T>
 * 分页加载适配器
 * 
 * 将loader内置于adapter中，实现分页加载
 * 
 * 加载器生命周期控制要与fragment或者activity实现联动
 * 	  void onActivityCreated()
 *    初始化加载，实例化加载器对象,检查数据是否为null,为避免进入fragment时asyntaskloader已经创建
 *    ，造成onLoaderFinished被调用，执行一次检查loader，销毁已经存在的loader动作
 *    
 *    void loadDataIfNull()
 *    可以在onResume或者loadDataIfNull中调用，用来加载首批数据
 *    
 * 	      提供重新加载和继续加载以及加载异常的控制,
 *    重新加载需要将页码数置为1，restart加载器,
 */
public abstract class PaginatedListAdapter<T> extends BaseAdapter{

	private List<T> mDatas = new ArrayList<T>();
	
	private String mLoaderName = ""; 
	
	public static final int PAGE_SIZE = 10;
	
	private int mCurrPageIndex = 0;
	
	private LoaderManager mLoaderManager ;
	
	private Context mContext;
	
	private BaseLoaderCallback<T> mBaseLoaderCallback = new BaseLoaderCallback<T>(){
		public Loader<T> onCreateLoader(int arg0, Bundle arg1) {
			return getLoader(arg0,arg1);
		};
		
		public void onLoadFinished(Loader<T> arg0, T arg1) {
			
		};
		
		public void onLoaderReset(Loader<T> arg0) {
			
		};
		
	};
	
	public abstract Loader<T> getLoader (int id,Bundle arg);
	public abstract String getLoaderName ();
	public PaginatedListAdapter(LoaderManager lm , Context context) {
		this.mLoaderManager = lm;
		this.mContext = context;
	}
	
	public void onActivityCreated() {
		mBaseLoaderCallback.onActivityCreated(mLoaderManager);
	}
	
	public void loadDataIfNull() {
		if (mDatas == null || mDatas.size() == 0) {
			mCurrPageIndex = 1;
			Bundle bundle = ListDataLoader.newArgs(mCurrPageIndex);
			mBaseLoaderCallback.startTask(mLoaderManager, getLoaderName(), bundle);
		}
	}
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
	
}
