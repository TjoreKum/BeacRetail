package com.example.beacretail;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Coupon implements Parcelable{
	
	private int deptId;
	private String couponId;
	private String couponDesc;
	private String couponImg;
	private boolean selected;
	
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getCouponDesc() {
		return couponDesc;
	}
	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}
	public String getCouponImg() {
		return couponImg;
	}
	public void setCouponImg(String couponImg) {
		this.couponImg = couponImg;
	}
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public Coupon(int deptId, String couponId, String couponDesc,
			String couponImg) {
		super();
		this.deptId = deptId;
		this.couponId = couponId;
		this.couponDesc = couponDesc;
		this.couponImg = couponImg;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
