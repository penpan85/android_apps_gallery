package com.u17.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.u17.R;

public class StyledRefreshListView extends PullToRefreshListView{

	private OnLoadMoreListener onLoadMoreListener;
	private LinearLayout bottomLoadingLayout;
	private ProgressBar progressBar;
	private TextView tvInfo;
	public StyledRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public StyledRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public StyledRefreshListView(
			Context context,
			Mode mode,
			AnimationStyle style) {
		super(context, mode, style);
		init(context);
	}

	public StyledRefreshListView(Context context,
			Mode mode) {
		super(context, mode);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater inflater=LayoutInflater.from(context);
		bottomLoadingLayout=(LinearLayout) inflater.inflate(R.layout.ui_pulltofresh_footer, null);
		progressBar=(ProgressBar) bottomLoadingLayout.findViewById(R.id.id_footer_progress);
		tvInfo=(TextView) bottomLoadingLayout.findViewById(R.id.id_tv_loadmore_text);
		setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if(onLoadMoreListener!=null){
					onLoadMoreListener.onLoadMore();
				}
			}
		});
	}

	public void setOnLoadMoreListener(OnLoadMoreListener listener){
		this.onLoadMoreListener=listener;
	}
	

	
	/**
	 * @user: pengpan
	 * @time: 2014-6-6,下午2:10:34
	 * @Title: onMoreLoadError 
	 * @Description: TODO void
	 * @version
	 */
	public void onMoreLoadError(){
		bottomLoadingLayout.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		tvInfo.setVisibility(View.VISIBLE);
		tvInfo.setText("加载失败，点击重试");
		tvInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onLoadMoreListener!=null){
					onLoadMoreListener.onClickRetry();
				}
			}
		});
	}
	
	/**
	 * @user: pengpan
	 * @time: 2014-6-6,上午11:23:31
	 * @Title: onMoreLoadStart 
	 * @Description: when scroll to bottom state start,we use this to add bottom view
	 * @version
	 */
	public void onMoreLoadStart(){
		bottomLoadingLayout.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		tvInfo.setVisibility(View.VISIBLE);
		tvInfo.setText("正在加载中");
		tvInfo.setOnClickListener(null);
		//修改在漫画搜索页面多次点击搜索按钮漫画列表底部间隔变化的bug
		int count = getRefreshableView().getFooterViewsCount();
		if(count > 0){
			getRefreshableView().removeFooterView(bottomLoadingLayout);
		}
		getRefreshableView().addFooterView(bottomLoadingLayout);
		
	}
	
	
	/**
	 * @user: pengpan
	 * @time: 2014-6-6,上午11:28:15
	 * @Title: onNoMore 
	 * @Description: when no more data,we display a message to tell user
	 * @param mes void
	 * @version
	 */
	public void onNoMore(String mes){
		bottomLoadingLayout.setVisibility(View.VISIBLE);
		tvInfo.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		tvInfo.setText(mes);
		tvInfo.setOnClickListener(null);
		getRefreshableView().removeFooterView(bottomLoadingLayout);		//BUG #1856
	}
	
	public static interface OnLoadMoreListener{
		void onLoadMore();
		void onClickRetry();
	}
	public void hideFooter(){
		bottomLoadingLayout.setVisibility(View.GONE);
	}
}
