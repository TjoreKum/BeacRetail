package com.example.beacretail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.beacretail.codegen.Utils;

public class CouponList extends Activity {

	ArrayList<Coupon> checkedList = new ArrayList<Coupon>();
	ArrayList<Coupon> couponList = new ArrayList<Coupon>();
	DatabaseHelper dbHelper =  new DatabaseHelper(this);
	BeaconInfo beaconData;
	int deptId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_list);
		  ActionBar actionBar = getActionBar();
	        actionBar.setDisplayShowHomeEnabled(false);
	        actionBar.setDisplayShowTitleEnabled(false);
		final ImageButton clipButton = (ImageButton)findViewById(R.id.clipButton);

		ListView listOffer = (ListView) findViewById(R.id.couponList);
		CouponAdapter adapter;

		deptId = getIntent().getIntExtra("DepartmentInfo", 99)+1;
		Serializable dataRcv = getIntent().getSerializableExtra("BeaconSelected");

		if (dataRcv!=null)
		{
			beaconData = (BeaconInfo) dataRcv;
			listOffer.setChoiceMode(listOffer.CHOICE_MODE_MULTIPLE);
			listOffer.setTextFilterEnabled(true);
			deptId = beaconData.getDepartmentId();
		}
			 try {
 	            dbHelper.openDataBase();
 	        } catch (Exception ioe) {
 	        	System.err.println(ioe.getStackTrace());
 	        }
		System.out.println("DEPT ID%%%%%%%%%%%%%%:"+deptId);
			 	Cursor resultSet = dbHelper.getMyDataBase().rawQuery("Select * from DeptCoupons where DepartmentId = '" + deptId +"'",null);
		    	resultSet.moveToFirst();
		    	 while (!resultSet.isAfterLast()) {
		    		 String couponId = resultSet.getString(resultSet.getColumnIndex("CouponId"));
		    		 String couponDesc = resultSet.getString(resultSet.getColumnIndex("CouponDescription"));
		    		 String couponImg = resultSet.getString(resultSet.getColumnIndex("ImagePath"));
		    		 couponList.add(new Coupon(deptId, couponId, couponDesc,couponImg));
		    		 System.out.println(couponDesc + couponId +couponImg);
		    		 resultSet.moveToNext();
		         }
		    	 
			dbHelper.close();
			BeacApp app = (BeacApp) getApplicationContext();
			ArrayList<Coupon> clippedCoupons = app.getSavedCoupons();
			if(null != clippedCoupons ){
			for (Coupon clipped : clippedCoupons)
			{
				for(Coupon current : couponList)
				{
				if(clipped.getCouponId().equals(current.getCouponId()))
				{
					current.setSelected(Boolean.TRUE);
					break;
				}
				}
			}
			}
			adapter = new CouponAdapter(this, R.layout.coupon_row, couponList);
			listOffer.setAdapter(adapter);

		
		clipButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*ListView listOffer = (ListView) findViewById(R.id.couponList);
				SparseBooleanArray chkdArray = listOffer.getCheckedItemPositions();	*/
				BeacApp app = (BeacApp) getApplicationContext();
				ArrayList<Coupon> savedCoupons = app.getSavedCoupons();
				if(null != savedCoupons ){
					Object[] savedCoupon = savedCoupons.toArray();
					for (Object coupon : savedCoupon) {
						if(deptId == ((Coupon)coupon).getDeptId())
						{
							savedCoupons.remove(coupon);
						}	
					} 
					

				}
				for (Coupon coupon : couponList) {
					if(coupon.isSelected())
					{
						checkedList.add(coupon);
					}
				}


				if(null != app.getSavedCoupons()){
		        app.getSavedCoupons().addAll(checkedList);
				}
				else{
					app.setSavedCoupons(checkedList);
				}
				Intent clipIntent = new Intent();
				clipIntent.putParcelableArrayListExtra("clipped", checkedList);
				setResult(RESULT_OK, clipIntent);
				finish();
				}
			});
		
//		cancelButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Intent clipIntent = new Intent();
//				setResult(RESULT_CANCELED, clipIntent);
//				finish();
//				}
//			});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coupon_list, menu);
		MenuItem settingsItem = menu.findItem(R.id.beacProp);
		// Get the notifications MenuItem and LayerDrawable (layer-list)
		if(beaconData==null){
			settingsItem.setVisible(false);
		}
		else{
			settingsItem.setVisible(true);
		}
		MenuItem item = menu.findItem(R.id.coupons);
		LayerDrawable icon = (LayerDrawable) item.getIcon();

		// Update LayerDrawable's BadgeDrawable
		BeacApp app = (BeacApp) getApplicationContext();;
		int count = 0;
		if(null != app.getSavedCoupons()){
			count = app.getSavedCoupons().size();
		}

		Utils.setBadgeCount(this, icon,count );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.beacProp) {
			Intent sendBeacPropIntent = new Intent(CouponList.this, BeacProp.class);
			sendBeacPropIntent.putExtra("BeaconProperties", beaconData);
			startActivityForResult(sendBeacPropIntent,1);		
			return true;
		}
		if (id == R.id.coupons) {
			Intent sendBeacPropIntent = new Intent(CouponList.this, ClippedCoupons.class);
			startActivityForResult(sendBeacPropIntent,1);		
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
