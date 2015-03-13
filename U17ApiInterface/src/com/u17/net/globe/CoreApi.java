package com.u17.net.globe;

import android.content.Context;

import com.u17.net.VisitResult;
import com.u17.net.utils.HttpUtils;

public class CoreApi {
	public static VisitResult getComicList (Context context, int pageIndex) {
		String url = UriGenerator.getInstance(context).getMangaListUrl(pageIndex);
		VisitResult
		return HttpUtils.loadDataFromUrl(url, visitor, postData, maxRetryTime, context, isUseShortTimeOut, u17Request)
	}
}
