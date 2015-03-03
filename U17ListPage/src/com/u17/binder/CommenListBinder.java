package com.u17.binder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.u17.net.model.ComicListItem;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author YzY
 *
 */
public class CommenListBinder extends ComicListBinder{

	public CommenListBinder(Context context, String keyWord,ImageLoader imageFetcher, int type) {
			super(context, keyWord, imageFetcher, type);
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void bindDataToHolder(BaseComicListBinder.ViewHolder holder,ComicListItem t,int position) {
		super.bindDataToHolder(holder, t, position);	
//		holder.tv_line2.setText("作者 "+t.getNickname());
		holder.tv_line4.setVisibility(View.GONE);
		holder.tv_line3.setText(t.getDescription());
		} 
	}
	
	
