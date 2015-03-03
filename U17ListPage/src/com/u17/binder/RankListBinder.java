package com.u17.binder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.u17.R;
import com.u17.net.model.ComicListItem;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author YzY
 *
 */
public class RankListBinder extends ComicListBinder{

	private ImageSpan iFire;
	private SparseIntArray resArray=new SparseIntArray();
	public RankListBinder(Context context, String keyWord,	ImageLoader imageFetcher, int type) {
			super(context, keyWord, imageFetcher, type);
			iFire = addImageSpan(R.drawable.icon_list_fire);
			resArray.append(0, R.drawable.icon_list_crown_golden);
			resArray.append(1, R.drawable.icon_list_crown_silver);
			resArray.append(2, R.drawable.icon_list_crown_cropper);
	}
	
	public RankListBinder(Context context, String keyWord,	ImageLoader imageFetcher, int type,String sortName) {
		super(context, keyWord, imageFetcher, type, sortName);
		iFire = addImageSpan(R.drawable.icon_list_fire);
		resArray.append(0, R.drawable.icon_list_crown_golden);
		resArray.append(1, R.drawable.icon_list_crown_silver);
		resArray.append(2, R.drawable.icon_list_crown_cropper);
}
	@SuppressWarnings("deprecation")
	@Override
	public void bindDataToHolder(BaseComicListBinder.ViewHolder holder,ComicListItem t,int position) {
		super.bindDataToHolder(holder, t, position);
		
		if (position < 3){
			holder.tv_level.setVisibility(View.VISIBLE);
			holder.tv_level.setBackgroundResource(resArray.get(position));
		}
		else{
			holder.tv_level.setVisibility(View.GONE);
			
		}
		holder.tv_number.setVisibility(View.VISIBLE);
		holder.tv_number.setText(position+1+"");
		holder.tv_dujia.setVisibility(View.GONE);
		holder.tv_qianyue.setVisibility(View.GONE);
		holder.tv_line3.setVisibility(View.GONE);
		holder.tv_line4.setText(getSpannableStr(t.getExtraValue(), iFire));
		holder.tv_line4.setTextColor(Color.rgb(253, 95, 25));
		holder.tv_line2.setTextColor(Color.rgb(161, 162, 163));
		}
	

}
	


	
