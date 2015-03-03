package com.u17.binder;

import android.view.View;

/**
 * @Package com.u17.phone.ui.adapter.binder 
 * @user: pengpan
 * @time: 2014-5-8,上午10:46:59
 * @Description: TODO 进行view的数据绑定
 * @version
 */
public interface ViewBinder<T> {

    View bind(View view, T t ,int position);
}
