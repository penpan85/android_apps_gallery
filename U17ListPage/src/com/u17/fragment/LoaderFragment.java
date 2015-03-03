package com.u17.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.view.View.OnClickListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.u17.R;
import com.u17.adapter.ComicListAdapter;
import com.u17.binder.BaseComicListBinder;
import com.u17.net.model.ComicListDatas;
import com.u17.net.model.ComicListItem;
import com.u17.net.utils.DataTypeUtils;
import com.u17.widget.RefreshableProgressBar;
import com.u17.widget.StyledRefreshListView;
import com.u17.widget.StyledRefreshListView.OnLoadMoreListener;

public class LoaderFragment extends Fragment{
	public final static int PAGE_SIZE = 40;
	
	public final static int START_LOAD_MORE_OFFSET=10;
	
	private int curPageNo = 1;
	
	DisplayImageOptions options;
	
	private StyledRefreshListView refreshListView;
	
	private ComicListAdapter mComicAdapter;
	
	private RefreshableProgressBar progressBar;
	
	public String Tag=LoaderFragment.class.getSimpleName();
	
	private String cacheKey = null;
	
	private ComicListDataState mDataState;
	
	private Activity mActivity;
	
	private int pageType=-1;
	
	private int currentLoadDateNum = 0;
	
	private ImageLoader mImageLoader;
	// refresh
	private final static int MODE_REFRESH_CLEAR=0;
	// init from net
	private final static int MODE_INITFROMNET=1;
	// add more
	private final static int MODE_ADD_MORE=2;
	// init from cache
	private final static int MODE_INITFROMCACHE=7;
		
	private final static int STATE_PROGRESSBAR_LOADING=3;
		
	private final static int STATE_PROGRESSBAR_ERROR_NO_NET=4;
		
	private final static int STATE_PROGRESSBAR_ERROR_OTHERS=5;
		
	private final static int STATE_PROGRESSBAR_GONE=6;
		
	private final static int STATE_PROGRESSBAR_DATA_NULL=7;
		
	private int CURRENT_BIND_MODE= -1;//1表示为更新模式，0为下载模式
	
	private OnScrollListener onScrollListener=new OnScrollListener() {
		private int lastVisiableItem=-1;
		private int totalCount=-1;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState){
			if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING||
					scrollState==AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
				mImageLoader.pause();
            } else {
            	mImageLoader.resume();
            	int currLastVisiableItemOffsetEnd=totalCount-1-lastVisiableItem;
            	//ULog.record(Tag+" onScrollStateChanged", "now can load more:"+currLastVisiableItemOffsetEnd);
            	//System.out.println("test123 currLastVisiableItemOffsetEnd = "+currLastVisiableItemOffsetEnd+" mDataState.getIsLoadMore() = "+mDataState.getIsLoadMore()+" mDataState.getIsLoading() = "+mDataState.getIsLoading()+" mDataState.getIsOver() = "+mDataState.getIsOver());
            	if(currLastVisiableItemOffsetEnd<=START_LOAD_MORE_OFFSET
            			&&!mDataState.getIsLoadMore()
            			&&!mDataState.getIsLoading()
            			&&!mDataState.getIsOver()){
            		//ULog.record(Tag+" onScrollStateChanged", "now start load more:"+curPageNo);
            		curPageNo++;
					CURRENT_BIND_MODE=MODE_ADD_MORE;
					mDataState.setIsLoadMore(true);
					refreshListView.onMoreLoadStart();
					loadListData(curPageNo);
            	}
            }
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {	
			lastVisiableItem=firstVisibleItem+visibleItemCount-1;
			totalCount=totalItemCount;
		}
	};
	
	private OnClickListener onclickListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			CURRENT_BIND_MODE=MODE_REFRESH_CLEAR;
			mDataState.setIsLoading(false);
			mDataState.setIsLoadMore(false);
			ensureProgressBar(STATE_PROGRESSBAR_LOADING,"");
			loadListData(1);
		}
	};
	
	private OnItemClickListener onItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			if (mComicAdapter == null||position>=mComicAdapter.getCount()+1) {
				return;
			}
			ComicListItem item = mComicAdapter.getItem(position - 1);
			Toast.makeText(mActivity, "条目"+position+"被点击了", 2000).show();
		}
	};
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		options = new DisplayImageOptions.Builder() 
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		mActivity = activity;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mDataState==null){
			mDataState=new ComicListDataState();
		}
		pageType = BaseComicListBinder.COMIC_LIST_TYPE_CLASSIFY;
		mImageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		FrameLayout mainFLayout = new FrameLayout(mActivity);
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mainFLayout.setLayoutParams(flp);
		
		refreshListView = new StyledRefreshListView(mActivity);
		FrameLayout.LayoutParams flp1 = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//		refreshListView.setId(R.id.id_list);
	//	refreshListView.setDividerDrawable(new ColorDrawable(android.R.color.transparent));
		refreshListView.setScrollBarStyle(R.style.custom_list);
		if(mComicAdapter==null)
			mComicAdapter=new ComicListAdapter(mActivity, 
						ImageLoader.getInstance(), pageType,"");
		refreshListView.setAdapter(mComicAdapter);
		refreshListView.setDrawingCacheEnabled(false);
		refreshListView.setAnimationCacheEnabled(false);
		refreshListView.setShowIndicator(false);
		
		progressBar = new RefreshableProgressBar(mActivity);
//		FrameLayout.LayoutParams flp2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

		mainFLayout.addView(refreshListView, flp1);
		mainFLayout.addView(progressBar);
		return mainFLayout;
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {
		refreshListView.setOnRefreshListener(onRefreshListener);
		refreshListView.setOnScrollListener(onScrollListener);
		
		refreshListView.setOnLoadMoreListener(onLoadMoreListener);

		refreshListView.setOnItemClickListener(onItemClickListener);
		progressBar.setOnClickListener(onclickListener);
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(pageType!=BaseComicListBinder.COMIC_LIST_TYPE_SEARCH_RESULT){
			refreshListView.setMode(Mode.PULL_FROM_START);
		}else{
			refreshListView.setMode(Mode.DISABLED);
		}
		if(!mComicAdapter.isEmpty()){
			ensureProgressBar(STATE_PROGRESSBAR_GONE,"");
			refreshListView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * @param state
	 * 根据不同的状态确定页面progressbar的显示
	 * 第一次进入，加载缓存，加载网络数据，提示"正在加载中"
	 * 加载缓存成功，隐藏掉progressbar,显示数据
	 * 无网，progressbar提示无网，并提供点击 重试 功能
	 * 
	 * 此后加载数据，进度提示由listview的header完成，出错或者无网由土司提示
	 */
	public void ensureProgressBar(int state,String msg){
		//mComicLv.setVisibility(View.GONE);
		switch(state){
		case STATE_PROGRESSBAR_ERROR_NO_NET:
			refreshListView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.onLoadingError("无网络，请稍候重试");
			break;
		case STATE_PROGRESSBAR_ERROR_OTHERS:
			refreshListView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			if(pageType != BaseComicListBinder.COMIC_LIST_TYPE_SEARCH_RESULT)
				progressBar.onLoadingErrorWithNoRefresh(msg);
			else
				progressBar.onLoadingErrorWithNoRefresh("未找到相关资源");
			break;
		case STATE_PROGRESSBAR_LOADING:
			refreshListView.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			progressBar.reset();
			break;
		case STATE_PROGRESSBAR_GONE:
			progressBar.setVisibility(View.GONE);
			break;
		case STATE_PROGRESSBAR_DATA_NULL:
			progressBar.setVisibility(View.VISIBLE);
			refreshListView.setVisibility(View.INVISIBLE);
			if(pageType != BaseComicListBinder.COMIC_LIST_TYPE_SEARCH_RESULT)
				progressBar.onLoadingErrorWithNoRefresh(msg);
			else
				progressBar.onLoadingErrorWithNoRefresh("未找到相关资源");
			break;
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	private void loadListData(int pageIndex){
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
	public static class ComicListDataState
	{
		private AtomicBoolean isLoading=new AtomicBoolean(false);//当前是否正在加载数据
		private AtomicBoolean isLoadMore=new AtomicBoolean(false);//当前是否是加载更多
		private AtomicBoolean isOver=new AtomicBoolean(false);//是否已经加载完毕
		private AtomicBoolean isFirst=new AtomicBoolean(true);//是否是第一次加载页面
		private ComicListDatas mComicListDatas;
		public ComicListDataState(){
			mComicListDatas=new ComicListDatas();
		}
		public void clear()
		{
			this.mComicListDatas.getListItems().clear();
		}
		public boolean isComicListNull(){
			return mComicListDatas == null || mComicListDatas.getListItems()==null||mComicListDatas.getListItems().size()==0;
		}
		public boolean getIsLoading() {
			return isLoading.get();
		}
		public void setIsLoading(boolean isLoading) {
			this.isLoading.set(isLoading);
		}
		
		public int getPageNo(){
			return mComicListDatas.getCurPageNo();
		}
		
		public boolean getIsLoadMore() {
			return isLoadMore.get();
		}
		public void setIsLoadMore(boolean isLoadMore) {
			this.isLoadMore.set(isLoadMore);
		}
		
		public ArrayList<ComicListItem> subCacheList(){
			if(mComicListDatas==null||DataTypeUtils.isEmpty(mComicListDatas.getListItems())){
				return null;
			}
			ArrayList<ComicListItem> items=new ArrayList<ComicListItem>();
			Iterator<ComicListItem> it=mComicListDatas.getListItems().iterator();
			ComicListItem item=null;
			int count=0;
			while(it.hasNext()){
				item=it.next();
				if(item==null){
					continue;
				}if(count>10){
					break;
				}
				items.add(item);
				count++;
			}
			return items;
		}
		
		public boolean getIsFirst() {
			return isFirst.get();
		}
		public void setIsFirst(boolean isFirst) {
			this.isFirst.set(isFirst);
		}
		public boolean getIsOver() {
			return isOver.get();
		}
		public void setIsOver(boolean isOver) {
			this.isOver.set(isOver);
		}
		public ComicListDatas getDatas() {
		
			return mComicListDatas;
		}
		public void setDatas(ComicListDatas datas) {
			this.mComicListDatas = datas;
		}
		
		public void addDatas(List<ComicListItem> listItems){
			this.mComicListDatas.getListItems().addAll(listItems);
		}

	}
	
	private OnLoadMoreListener onLoadMoreListener=new OnLoadMoreListener() {
		@Override
		public void onLoadMore() {
			/*if(!mDataState.getIsLoadMore()&&!mDataState.getIsLoading()&&!mDataState.getIsOver()){
				curPageNo++;
				CURRENT_BIND_MODE=MODE_ADD_MORE;
				mDataState.setIsLoadMore(true);
				refreshListView.onMoreLoadStart();
				if(pageType==BaseComicListBinder.COMIC_LIST_TYPE_SEARCH_RESULT)
					loadSearchResultData(curPageNo);
				else
					loadListData(curPageNo);
				//if(isDebug){ULog.record(TAG+"  OnMoreListener", curPageNo+"");}
			}*/
			currentLoadDateNum++;
			if(currentLoadDateNum >= 900)
				currentLoadDateNum = 900;
		}
		
		@Override
		public void onClickRetry() {
			if(mDataState.getIsLoading()){
				return;
			}
			mDataState.getDatas().getCurPageNo();
			loadListData(curPageNo);
		}
	};
	private OnRefreshListener<ListView>  onRefreshListener=new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			if (mComicAdapter == null||mDataState.getIsLoading()) {
				return;
			}
			String label = DateUtils.formatDateTime(mActivity,
					System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);

			// Update the LastUpdatedLabel
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			CURRENT_BIND_MODE=MODE_REFRESH_CLEAR;
			loadListData(1);
		}

	};
	
}
