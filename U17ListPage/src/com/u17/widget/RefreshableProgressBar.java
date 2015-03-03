package com.u17.widget;

import com.u17.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Package com.u17.phone.ui.widget
 * @user: pengpan
 * @time: 2014-6-4,下午3:22:56
 * @Description: to display a loading view or load error view,provide button to
 *               refresh
 * @version
 */
public class RefreshableProgressBar extends RelativeLayout {
	private FrameLayout loadingContainer;
	private TextView loadingInfoTv;
	private TextView loadingInfoTv2;
	private ImageView freshBtn;
	private View.OnClickListener clickListner;
	private ProgressBar progressBar;
	private String strLoading;
	private Context context;
	private LayoutInflater inflater;

	public RefreshableProgressBar(Context context) {
		this(context, null);
		this.context = context;
		inflater = LayoutInflater.from(context);
		strLoading = "加载中";
		this.context = context;
		init();
		reset();
	}

	public void setOnClickListener(View.OnClickListener listener) {
		this.clickListner = listener;
	}

	public RefreshableProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	private void init() {
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rlp.addRule(CENTER_HORIZONTAL, TRUE);
		rlp.addRule(BELOW, R.id.id_fresh_fl);
		rlp.setMargins(0, 5, 0, 0);
		setGravity(Gravity.CENTER);
		addView(addFramelayout());
		addView(addTextView(), rlp);
		addView(addTextView2(), rlp);
		// addView(addTextView());
		// addView(addTextView2());
	}

	private FrameLayout addFramelayout() {
		loadingContainer = new FrameLayout(context);
		loadingContainer.setId(R.id.id_fresh_fl);
		RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pl.addRule(CENTER_HORIZONTAL, TRUE);
		loadingContainer.setLayoutParams(pl);
		loadingContainer.addView(addImageView());
		loadingContainer.addView(addProgressBar());
		return loadingContainer;
	}

	private ImageView addImageView() {
		freshBtn = new ImageView(context);
		freshBtn.setId(R.id.id_fresh_bt);
		ViewGroup.LayoutParams pl = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		freshBtn.setLayoutParams(pl);
		freshBtn.setScaleType(ScaleType.FIT_XY);
		freshBtn.setImageResource(R.drawable.icon_content_refresh_change);
		freshBtn.setVisibility(View.VISIBLE);
		freshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				if (clickListner != null) {
					clickListner.onClick(RefreshableProgressBar.this);
				}
			}
		});
		return freshBtn;
	}

	private ProgressBar addProgressBar() {
		progressBar = (ProgressBar) inflater.inflate(R.layout.ui_progressbar,
				null);
		FrameLayout.LayoutParams pl = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		progressBar.setLayoutParams(pl);
		progressBar.setId(R.id.id_progress);
		progressBar.setVisibility(View.GONE);
		return progressBar;
	}

	// <TextView
	// android:id="@+id/id_tv_loading_info"
	// style="@style/ww"
	// android:layout_centerHorizontal="true"
	// android:layout_below="@id/id_fresh_fl"
	// android:layout_marginTop="@dimen/list_item_download_seg_margin"
	// android:text="加载中..."
	// android:visibility="visible" />
	private TextView addTextView() {
		loadingInfoTv = new TextView(context);
		loadingInfoTv.setId(R.id.id_tv_loading_info);
		RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pl.addRule(CENTER_HORIZONTAL, TRUE);
		pl.addRule(BELOW, R.id.id_fresh_fl);
		pl.setMargins(0, 5, 0, 0);
		loadingInfoTv.setLayoutParams(pl);
		loadingInfoTv.setCompoundDrawablePadding(18);
		loadingInfoTv.setText("加载中...");
		loadingInfoTv.setVisibility(View.VISIBLE);
		return loadingInfoTv;
	}

	private TextView addTextView2() {
		loadingInfoTv2 = new TextView(context);
		loadingInfoTv2.setId(R.id.id_tv_loading_info_other);
		RelativeLayout.LayoutParams pl = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		pl.addRule(CENTER_HORIZONTAL, TRUE);
		pl.setMargins(0, 5, 0, 0);
		pl.addRule(BELOW, R.id.id_tv_loading_info);
		loadingInfoTv2.setLayoutParams(pl);
		loadingInfoTv2.setText("info2");
		loadingInfoTv2.setVisibility(View.VISIBLE);
		loadingInfoTv.setTextSize(13);
		return loadingInfoTv2;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		loadingContainer = (FrameLayout) findViewById(R.id.id_fresh_fl);
		loadingInfoTv = (TextView) findViewById(R.id.id_tv_loading_info);
		loadingInfoTv2 = (TextView) findViewById(R.id.id_tv_loading_info_other);
		progressBar = (ProgressBar) findViewById(R.id.id_progress);
		freshBtn = (ImageView) findViewById(R.id.id_fresh_bt);
		freshBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				if (clickListner != null) {
					clickListner.onClick(RefreshableProgressBar.this);
				}
			}
		});
		reset();
	}

	/**
	 * @user: pengpan
	 * @time: 2014-6-4,下午3:13:26
	 * @Title: reset
	 * @Description: reset the state of the view,the bar is loading state
	 * @version
	 */
	public void reset() {
		loadingInfoTv.setVisibility(View.VISIBLE);
		loadingInfoTv.setText(strLoading);
		progressBar.setVisibility(View.VISIBLE);
		loadingInfoTv2.setVisibility(View.GONE);
		freshBtn.setVisibility(View.GONE);
	}

	/**
	 * @user: pengpan
	 * @time: 2014-6-4,下午3:13:26
	 * @Title: reset
	 * @Description: reset the state of the view,the bar is loading state
	 * @version
	 */
	public void reset(String mes) {
		loadingInfoTv.setVisibility(View.VISIBLE);
		loadingInfoTv.setText(mes);
		progressBar.setVisibility(View.VISIBLE);
		loadingInfoTv2.setVisibility(View.GONE);
		freshBtn.setVisibility(View.GONE);
	}

	/**
	 * @user: pengpan
	 * @time: 2014-6-4,下午3:34:27
	 * @Title: onLoadingError
	 * @Description:when get a error ,call this method
	 * @param errorMes
	 *            void
	 * @version
	 */
	public void onLoadingError(String errorMes) {
		loadingInfoTv.setVisibility(View.VISIBLE);
		loadingInfoTv.setText(errorMes);
		progressBar.setVisibility(View.GONE);
		loadingInfoTv2.setVisibility(View.GONE);
		freshBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * @user: pengpan
	 * @time: 2014-6-4,下午3:34:27
	 * @Title: onLoadingError
	 * @Description:when get a error ,call this method
	 * @param errorMes
	 *            void
	 * @version
	 */
	public void onLoadingErrorWithNoRefresh(String errorMes) {
		loadingInfoTv.setVisibility(View.VISIBLE);
		loadingInfoTv.setText(errorMes);
		progressBar.setVisibility(View.GONE);
		loadingInfoTv2.setVisibility(View.GONE);
		freshBtn.setVisibility(View.GONE);
	}

	/**
	 * @adduser wangchao
	 * @param errorMes
	 *            加载数据失败页面显示文字
	 * @param id加载数据失败页面显示的图片id
	 */
	public void onLoadingErrorWithNoRefresh(String errorMes, int id) {
		loadingInfoTv.setVisibility(View.VISIBLE);

		Drawable drawable = getResources().getDrawable(id);
		if (drawable != null) {
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			loadingInfoTv.setCompoundDrawables(null, drawable, null, null);
		}
		loadingInfoTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		loadingInfoTv.setText(errorMes);
		progressBar.setVisibility(View.GONE);
		loadingInfoTv2.setVisibility(View.GONE);
		freshBtn.setVisibility(View.GONE);
	}

	/**
	 * @adduser wangchao
	 * @param errorMes
	 *            加载数据失败页面显示文字
	 * @param drawable加载数据失败页面显示的图片的drawable
	 */
	public void onLoadingErrorWithNoRefresh(String errorMes, Drawable drawable) {
		loadingInfoTv.setVisibility(View.VISIBLE);
		if (drawable != null) {
			// / 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			loadingInfoTv.setCompoundDrawables(null, drawable, null, null);
		}
		loadingInfoTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		loadingInfoTv.setText(errorMes);
		progressBar.setVisibility(View.GONE);
		loadingInfoTv2.setVisibility(View.GONE);
		freshBtn.setVisibility(View.GONE);
	}

}
