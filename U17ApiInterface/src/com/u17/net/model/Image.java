package com.u17.net.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Serializable,Parcelable{
	private static final long serialVersionUID = -2734094120127284548L;
	private int imageId;
	private int width;
	private int height;
	private int totalTucao=0;
	private String url="";
	private String fastUrl="";
	private String balanceUrl="";
	private String mobileUrl="";
	private String svolUrl="";
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getTotalTucao() {
		return totalTucao;
	}
	public void setTotalTucao(int totalTucao) {
		this.totalTucao = totalTucao;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getFastUrl() {
		return fastUrl;
	}
	public void setFastUrl(String fastUrl) {
		this.fastUrl = fastUrl;
	}
	public String getBalanceUrl() {
		return balanceUrl;
	}
	public void setBalanceUrl(String balanceUrl) {
		this.balanceUrl = balanceUrl;
	}
	
	public String getMobileUrl() {
		return mobileUrl;
	}
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}
	
	public String getSvolUrl() {
		return svolUrl;
	}
	public void setSvolUrl(String svolUrl) {
		this.svolUrl = svolUrl;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(imageId);
		dest.writeInt(totalTucao);
		dest.writeInt(height);
		dest.writeInt(width);
		dest.writeString(url);
		dest.writeString(fastUrl);
		dest.writeString(balanceUrl);
		dest.writeString(mobileUrl);
		dest.writeString(svolUrl);
		
	}
	public static final Parcelable.Creator<Image> CREATOR = new Creator<Image>() {

		@Override
		public Image createFromParcel(Parcel source) {
			Image image = new Image();
			image.setImageId(source.readInt());
			image.setTotalTucao(source.readInt());
			image.setHeight(source.readInt());
			image.setWidth(source.readInt());
			image.setUrl(source.readString());
			image.setFastUrl(source.readString());
			image.setBalanceUrl(source.readString());
			image.setMobileUrl(source.readString());
			image.setSvolUrl(source.readString());
			return image;
		}

		@Override
		public Image[] newArray(int size) {
			return new Image[size];
		}  
		
	};

	@Override
	public String toString() {
		return "Image [imageId=" + imageId + ", width=" + width + ", height="
				+ height + ", totalTucao=" + totalTucao + ", url=" + url
				+ ", fastUrl=" + fastUrl + ", balanceUrl=" + balanceUrl + "]";
	}
	
	
}
