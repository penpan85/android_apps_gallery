package com.u17.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.u17.binder.BaseComicListBinder;
import com.u17.binder.ComicListBinder;
import com.u17.binder.CommenListBinder;
import com.u17.binder.RankListBinder;
import com.u17.binder.UpdateListBinder;
import com.u17.net.model.ComicListDatas;
import com.u17.net.model.ComicListItem;
import com.u17.net.utils.DataTypeUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ComicListAdapter extends BaseAdapter {
	
	private final static String TAG = ComicListAdapter.class.getSimpleName();
//	private List<ComicListItem> comicList;
	private ComicListDatas data;
	public final static long DAY_TIME = 24*60*60*1000L; 
	private String keyword = null;
	private boolean isDebug=true;
	
	private ComicListBinder comicListBinder;
	public ComicListAdapter(Context context,ImageLoader imageStrategy
			,int type) {
		this.data = new ComicListDatas();
		if(type == BaseComicListBinder.COMIC_LIST_TYPE_RANKING){
			comicListBinder=new RankListBinder(context, keyword, imageStrategy,type);
		}else if(type == BaseComicListBinder.COMIC_LIST_TYPE_UPDATE){
			comicListBinder=new UpdateListBinder(context, keyword, imageStrategy,type);
		}else
			comicListBinder=new CommenListBinder(context, keyword, imageStrategy,type);
		
		
	}

	public ComicListAdapter(Context context,ImageLoader imageStrategy
			,int type,String sortName) {
		this.data = new ComicListDatas();
		if(type == BaseComicListBinder.COMIC_LIST_TYPE_RANKING){
			comicListBinder=new RankListBinder(context, keyword, imageStrategy,type,sortName);
		}else if(type == BaseComicListBinder.COMIC_LIST_TYPE_UPDATE){
			comicListBinder=new UpdateListBinder(context, keyword, imageStrategy,type);
		}else
			comicListBinder=new CommenListBinder(context, keyword, imageStrategy,type);
		
		
	}
	public void setData(ComicListDatas data) {
		clearData();
		this.data.getListItems().addAll(data.getListItems());
		notifyDataSetChanged();
	}
	
	public void destroy(){
		if(!DataTypeUtils.isEmpty(data)){
			data.clear();
			data=null;
		}
	}
	public void clearData(){
		this.data = new ComicListDatas();
	}
	public ComicListDatas getDatas(){
		return data;
	}
	public void addData(List<ComicListItem> list,int pageNo) {
		data.getListItems().addAll(list);
		data.setCurPageNo(pageNo);
		notifyDataSetChanged();
	}
	
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public int getCount() {
		if (data == null)
			return 0;
		return data.getListItems().size();
	}

	@Override
	public ComicListItem getItem(int position) {
		return data.getListItems().get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.getListItems().get(position).getComicId();
	}

	@Override
	public View getView(int i, View view, ViewGroup parent) {
		ComicListItem item = data.getListItems().get(i);
		return comicListBinder.bind(view, item,i);
	}
}
