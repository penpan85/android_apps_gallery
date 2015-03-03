package com.u17.net.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Chapter implements Parcelable{
	private static final long serialVersionUID = -2734094120127284548L;
	private String name ;
    private int imageTotal;
    private int chapterId;
    private int size ;
    
    private int buyed;
	private int is_view;
	private int type;
	
	private long time;
	private Double price;
	private long pass_time;
	private long release_time;
	//新添加字段,@time 2014年6月11日09:52:40
	private int read_state;// 0表示未读、1标示已读、2最近
	private int is_new;//0表示不是最新章节、1表示为最新章节
	private int image50Size;
	private int image05Size;
	private int imageMobileSize;
	private int imageSvolSize;

	public int getRead_state() {
		return read_state;
	}

	public void setRead_state(int read_state) {
		this.read_state = read_state;
	}

	public int getIs_new() {
		return is_new;
	}

	public void setIs_new(int is_new) {
		this.is_new = is_new;
	}


	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getImageTotal() {
		return imageTotal;
	}

	public void setImageTotal(int imageTotal) {
		this.imageTotal = imageTotal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
    public int getBuyed() {
		return buyed;
	}

	public void setBuyed(int buyed) {
		this.buyed = buyed;
	}

	public int getIs_view() {
		return is_view;
	}

	public void setIs_view(int is_view) {
		this.is_view = is_view;
	}
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getPass_time() {
		return pass_time;
	}

	public void setPass_time(long pass_time) {
		this.pass_time = pass_time;
	}

	public long getRelease_time() {
		return release_time;
	}

	public void setRelease_time(long release_time) {
		this.release_time = release_time;
	}
	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	
	public int getImage50Size() {
		return image50Size;
	}

	public void setImage50Size(int image50Size) {
		this.image50Size = image50Size;
	}

	public int getImage05Size() {
		return image05Size;
	}

	public void setImage05Size(int image05Size) {
		this.image05Size = image05Size;
	}

	public int getImageMobileSize() {
		return imageMobileSize;
	}

	public void setImageMobileSize(int imageMobileSize) {
		this.imageMobileSize = imageMobileSize;
	}

	public int getImageSvolSize() {
		return imageSvolSize;
	}

	public void setImageSvolSize(int imageSvolSize) {
		this.imageSvolSize = imageSvolSize;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(chapterId);
		dest.writeString(name);
		dest.writeInt(imageTotal);
		dest.writeInt(size);
		dest.writeInt(buyed);
		dest.writeInt(is_view);
		dest.writeLong(time);
		dest.writeDouble(price);
		dest.writeInt(type);
		dest.writeLong(pass_time);
		dest.writeLong(release_time);
		dest.writeInt(read_state);
		dest.writeInt(is_new);
		dest.writeInt(image05Size);
		dest.writeInt(image50Size);
		dest.writeInt(imageMobileSize);
		dest.writeInt(imageSvolSize);
		
	}
	public static final Parcelable.Creator<Chapter> CREATOR = new Creator<Chapter>() {

		@Override
		public Chapter createFromParcel(Parcel source) {
			Chapter chapter = new Chapter();
			chapter.setChapterId(source.readInt());
			chapter.setName(source.readString());
			chapter.setImageTotal(source.readInt());
			chapter.setSize(source.readInt());
			chapter.setBuyed(source.readInt());
			chapter.setIs_view(source.readInt());
			chapter.setTime(source.readLong());
			chapter.setPrice(source.readDouble());
			chapter.setType(source.readInt());
			chapter.setPass_time(source.readLong());
			chapter.setRelease_time(source.readLong());
			chapter.setRead_state(source.readInt());
			chapter.setIs_new(source.readInt());
			chapter.setImage05Size(source.readInt());
			chapter.setImage50Size(source.readInt());
			chapter.setImageMobileSize(source.readInt());
			chapter.setImageSvolSize(source.readInt());
			return chapter;
		}

		@Override
		public Chapter[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Chapter[size];
		}  
		
	};

	@Override
	public String toString() {
		return "Chapter [name=" + name + ", imageTotal=" + imageTotal
				+ ", chapterId=" + chapterId + ", size=" + size + ", buyed="
				+ buyed + ", is_view=" + is_view + ", type=" + type + ", time="
				+ time + ", price=" + price + ", pass_time=" + pass_time
				+ ", release_time=" + release_time + "]";
	}  
	

}
