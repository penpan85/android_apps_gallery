package com.u17.binder;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.u17.AnimateFirstDisplayListener;
import com.u17.App;
import com.u17.R;
import com.u17.net.model.ComicListItem;
import com.u17.net.utils.ContextUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @author YzY
 *
 */
public class ComicListBinder extends BaseComicListBinder<ComicListItem>{

	protected String[] tags;
	protected int length;
	protected String tagStr;
	protected int accreditDisplayCode;
	protected ImageSpan iAuthor;
	protected ImageSpan iClassify;
	protected ImageSpan iHand;
	protected SpannableString lineTwo;
	private SpannableString line2;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private String[] sortNameTag = {"人气排行榜","新作排行榜","点击排行榜"};
	private boolean isPager = false;
	
	public ComicListBinder(Context context, String keyWord,	ImageLoader imageFetcher, int type) {
			super(context, keyWord, imageFetcher, type);
			iAuthor = addImageSpan(R.drawable.icon_list_author_green);
			iClassify = addImageSpan(R.drawable.icon_list_classify_green);
			iHand = addImageSpan(R.drawable.icon_list_hand_green);
	}
	
	public ComicListBinder(Context context, String keyWord,	ImageLoader imageFetcher, int type,String sortName) {
		super(context, keyWord, imageFetcher, type);
		iAuthor = addImageSpan(R.drawable.icon_list_author_green);
		iClassify = addImageSpan(R.drawable.icon_list_classify_green);
		iHand = addImageSpan(R.drawable.icon_list_hand_green);
		if(type==COMIC_LIST_TYPE_RANKING){
			if(!TextUtils.isEmpty(sortName)){
				for(String str:sortNameTag){
					if(str.equals(sortName)){
						isPager=true;
						break;
					}
				}
			}
		}
}
	@SuppressWarnings("deprecation")
	@Override
	public void bindDataToHolder(BaseComicListBinder.ViewHolder holder,ComicListItem t,int position) {
//		  	holder.tv_title.setText(String.format(context.getString(R.string.comic_title),
//		  			t.getName(),ContextUtil.getUpdateState(t.getSeries_status()) ));
		    if(t == null){
		    	return;
		    }
		  	holder.tv_title.setText(t.getName());
		  	holder.tv_title.append(ContextUtil.getUpdateState(t.getSeries_status()));		//（完，连载，暂停更新）包括 搜索结果漫画列表 分类漫画列表 排行榜漫画列表 更新漫画列表
		  	//"动作"，"搞笑"
			tagStr=getTagStr(t);
			accreditDisplayCode = t.getAccreditDisplayCode();
			if(isPager){			//人气&新作排行榜 隐藏公共的人气图标（手指）与数据
				line2 = generateSecondLineTwo(checkStr(t.getNickname()),checkStr(tagStr));
				if(line2 != null){
					holder.tv_line2.setText(line2);
				}else
					holder.tv_line2.setText("");
			}else{
				lineTwo=generateSecondLine(checkStr(t.getNickname()),checkStr(t.getClick_total())
						,checkStr(tagStr));
			}
			
			if(lineTwo==null && position == -1){
				holder.tv_line2.setText("");
			}else
				holder.tv_line2.setText(lineTwo);
			//0代表签约，1代表独家，2代表不显示,3,代表既是签约又是独家
			switch (accreditDisplayCode) {
			case App.QIANYUE:
				holder.tv_dujia.setVisibility(View.GONE);
				holder.tv_qianyue.setVisibility(View.VISIBLE);
				break;
			case App.DUJIA:
				holder.tv_dujia.setVisibility(View.VISIBLE);
				holder.tv_qianyue.setVisibility(View.GONE);
				break;
			case App.NOT_DUJIA_QIANYUE:
				holder.tv_dujia.setVisibility(View.GONE);
				holder.tv_qianyue.setVisibility(View.GONE);
				break;
			case App.DUJIA_AND_QIANYUE:
				holder.tv_dujia.setVisibility(View.VISIBLE);
				holder.tv_qianyue.setVisibility(View.VISIBLE); 
				break;

			default:
				holder.tv_dujia.setVisibility(View.GONE);
				holder.tv_qianyue.setVisibility(View.GONE);
				break;
			}
			mImageFetcher.displayImage(t.getCover(), holder.iv_cover);
		}
		private String getTagStr(ComicListItem item){
			if(item==null){
				return "";
			}
			tags = item.getTags();
			String result="";
			if(tags!=null){
				length=tags.length;
				for(int i=0;i<length;i++){
//				result+=tags[i]+"\t\t";
					result+=tags[i]+" ";
				}
			}
		return result;
	}

	protected SpannableString generateSecondLine(String author,String clickNum,String tag){
		lineTwo=new SpannableString("  "+author+"   "+clickNum+"   "+tag);
		int currIndex=0;
		lineTwo.setSpan(iAuthor, 0, ++currIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		currIndex+=author.length()+2;
		lineTwo.setSpan(iHand,currIndex,++currIndex,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		currIndex+=clickNum.length()+2;
		lineTwo.setSpan(iClassify,currIndex,++currIndex,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return lineTwo;
	}
	
	protected SpannableString generateSecondLineTwo(String author,String tag){
		lineTwo=new SpannableString("  "+author+"   "+tag);
		int currIndex=0;
		lineTwo.setSpan(iAuthor, 0, ++currIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		currIndex+=author.length()+2;
		lineTwo.setSpan(iClassify,currIndex,++currIndex,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return lineTwo;
	}
	
	protected SpannableString getSpannableStr(String str,ImageSpan span){
		lineTwo=new SpannableString("  "+str);
		int currIndex=0;
		lineTwo.setSpan(span, 0, ++currIndex, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		return lineTwo;
	}
	
	private String checkStr(String str){
		if(TextUtils.isEmpty(str)){
			return "";
		}
		return str;
	}
	
	protected ImageSpan addImageSpan(int drawableId) {
		Drawable d = context.getResources().getDrawable(drawableId);
		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
		ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
		return span;
	}

}
	
