package com.u17.binder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.u17.R;
import com.u17.net.model.ComicListItem;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author YzY
 *
 */
public class UpdateListBinder extends ComicListBinder{
	private ImageSpan iClock;
	public UpdateListBinder(Context context, String keyWord ,ImageLoader imageFetcher, int type) {
			super(context, keyWord, imageFetcher, type);
			iClock = addImageSpan(R.drawable.icon_list_clock);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void bindDataToHolder(BaseComicListBinder.ViewHolder holder,ComicListItem t,int position) {
			super.bindDataToHolder(holder, t, position);  	
	  		String updateChapterName = t.getLatestUpdateChapter();
	  		if(TextUtils.isEmpty(updateChapterName)){
	  			holder.tv_line3.setVisibility(View.GONE);
	  		}else{
	  			holder.tv_line3.setVisibility(View.VISIBLE);
	  			holder.tv_line3.setText("更新到  "+updateChapterName);
	  			holder.tv_line3.setMaxLines(1);//低端手机很容易宽度到头，导致高度上第一行被盖住一半，所以单行显示...
	  		}
	  		holder.tv_line4.setTextColor(Color.rgb(169, 3, 3));
	  		holder.tv_line4.setText(getSpannableStr(t.getUpdateTips(),iClock));
	}
	
}



    
	
	
