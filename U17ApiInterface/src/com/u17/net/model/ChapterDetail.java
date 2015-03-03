package com.u17.net.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ChapterDetail implements Serializable,Parcelable{
	private static final long serialVersionUID = 1408053901956898009L;
	private int chapterId;

	private List<Image> images = null;
	private int code;
	private String message;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(chapterId);
		
		dest.writeTypedList(images);
		
	}
	public static final Parcelable.Creator<ChapterDetail> CREATOR = new Creator<ChapterDetail>() {

		@Override
		public ChapterDetail createFromParcel(Parcel source) {
			ChapterDetail chapter = new ChapterDetail();
			chapter.setChapterId(source.readInt());
			List<Image> imageList = new ArrayList<Image>();
			source.readTypedList(imageList, Image.CREATOR);
			chapter.setImages(imageList);
			return chapter;
		}

		@Override
		public ChapterDetail[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ChapterDetail[size];
		}  
		
	};

	
}
