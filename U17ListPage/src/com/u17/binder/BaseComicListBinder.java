package com.u17.binder;

import java.util.Dictionary;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.u17.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseComicListBinder<T> implements ViewBinder<T>{
	public static final int FADING_TIME=200;
//	public Drawable signDrawable;//图片压缩
//	public Drawable onlyDrawable;//图片压缩
	public ImageLoader mImageFetcher;
	private String TAG=BaseComicListBinder.class.getSimpleName();
	public int color;
	public String keyword;
	public Context context;
	public static final int COMIC_LIST_TYPE_UPDATE=0;
	public static final int COMIC_LIST_TYPE_RANKING=1;
	public static final int COMIC_LIST_TYPE_SPECIAL=2;
	public static final int COMIC_LIST_TYPE_SEARCH_RESULT=3;
	public static final int COMIC_LIST_TYPE_COMICINFOTASK=4;
	public static final int COMIC_LIST_TYPE_CLASSIFY=5;
	public static final int COMIC_LIST_TYPE_FAVORITE=6;
	public int viewType=COMIC_LIST_TYPE_CLASSIFY;
	public BaseComicListBinder(Context context,String keyWord,ImageLoader imageFetcher
			,int type){
		this.context=context;
		this.keyword=keyword;
		this.mImageFetcher=imageFetcher;
//		signDrawable=context.getResources().getDrawable(R.drawable.ic_sign);//图片压缩
//		onlyDrawable=context.getResources().getDrawable(R.drawable.ic_only);//图片压缩
		this.color=context.getResources().getColor(R.color.red);
		this.viewType=type;
	}
	@Override
	public View bind(View view, T t,int position) {
		ViewHolder viewHolder=null;
		if (view == null) {
			view =(RelativeLayout)View.inflate(context, R.layout.list_item_comic_list, null);
			viewHolder=new ViewHolder();
			//cover
			viewHolder.iv_cover = (ImageView) view.findViewById(R.id.id_comic_iv_cover);
			//cover label
			viewHolder.tv_level = (TextView) view.findViewById(R.id.id_comic_tv_level);
			//title
			viewHolder.tv_title = (TextView) view.findViewById(R.id.id_comic_tv_title);
			// sychronized iv
			//line 2 text
			viewHolder.tv_line2 = (TextView) view.findViewById(R.id.id_comic_tv_line2);
		
			//line 3 text
			viewHolder.tv_line3 = (TextView) view.findViewById(R.id.id_comic_tv_line3);
			//line 4 text
			viewHolder.tv_line4 = (TextView) view.findViewById(R.id.id_comic_tv_line4);
			// dujia or qianyue container
			viewHolder.tv_dujia = (TextView) view.findViewById(R.id.id_comic_tv_dujia);
			viewHolder.tv_qianyue = (TextView) view.findViewById(R.id.id_comic_tv_qianyue);
			viewHolder.tv_number = (TextView) view.findViewById(R.id.id_comic_tv_right_number);
			//menu button
			confirmItemVisibility(viewHolder);
			view.setTag(viewHolder);
			//ULog.record(TAG+" create viewHolder", "imageview:"+viewHolder.iv_cover.hashCode()+",pos"+position);
		} else {
			viewHolder= (ViewHolder) view.getTag();
			//ULog.record(TAG+" get tag", "view:"+view.hashCode()+",pos"+position);
		}
		setListenerToButton(viewHolder,t);
		bindDataToHolder(viewHolder,t,position);
		if(position % 2 == 0){
			view.setBackgroundResource(R.drawable.bg_chapter_light_selector);
		}else{
			view.setBackgroundResource(R.drawable.bg_chapter_dark_selector);
		}
		return view;
	}
	private void confirmItemVisibility(ViewHolder viewHolder){
		switch(viewType){
		case COMIC_LIST_TYPE_UPDATE:
			viewHolder.tv_level .setVisibility(View.GONE);
			viewHolder.tv_number.setVisibility(View.GONE);
			break;
		case COMIC_LIST_TYPE_RANKING:
			break;
		case COMIC_LIST_TYPE_CLASSIFY:
			viewHolder.tv_level.setVisibility(View.GONE);
			viewHolder.tv_number.setVisibility(View.GONE);
			break;
			
		case COMIC_LIST_TYPE_SPECIAL:
		case COMIC_LIST_TYPE_SEARCH_RESULT:
			viewHolder.tv_level .setVisibility(View.GONE);
			viewHolder.tv_number.setVisibility(View.GONE);
			break;
		case COMIC_LIST_TYPE_COMICINFOTASK:
			viewHolder.tv_level .setVisibility(View.GONE);
			viewHolder.tv_qianyue.setVisibility(View.GONE);
			viewHolder.tv_dujia.setVisibility(View.GONE);
			viewHolder.tv_line4.setVisibility(View.GONE);
			viewHolder.iv_cloud.setVisibility(View.GONE);
			break;
		case COMIC_LIST_TYPE_FAVORITE:
			viewHolder.tv_level .setVisibility(View.GONE);
			viewHolder.tv_dujia.setVisibility(View.GONE);
			viewHolder.tv_qianyue.setVisibility(View.VISIBLE);
			viewHolder.tv_line3.setVisibility(View.GONE);
			viewHolder.tv_line4.setVisibility(View.GONE);
			viewHolder.iv_cloud.setVisibility(View.VISIBLE);
			break;
	}
	}
	public void bindDataToHolder(ViewHolder holder,T t,int position){};

	public void setListenerToButton(ViewHolder holder, T t){};
	
	static class ViewHolder{
		public ImageView iv_cover;
		public FrameLayout rl_option;
		public TextView tv_level;
		public TextView tv_title;
		public TextView tv_line2;
		public TextView tv_line3;
		public TextView tv_line4;
		public TextView tv_dujia;
		public TextView tv_qianyue;
		public TextView tv_number;
		public ImageView iv_options;
		public ImageView iv_cloud;	
	} 
}
