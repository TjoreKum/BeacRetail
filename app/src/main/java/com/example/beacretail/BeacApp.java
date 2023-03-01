package com.example.beacretail;

import java.util.ArrayList;

import android.app.Application;

public class BeacApp extends Application {

	private ArrayList<Coupon> savedCoupons;

	public ArrayList<Coupon> getSavedCoupons() {
		return savedCoupons;
	}

	public void setSavedCoupons(ArrayList<Coupon> savedCoupons) {
		this.savedCoupons = savedCoupons;
	}
	
	
}
