package com.example.beacretail;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.beacretail.codegen.QRActivity;
import com.example.beacretail.codegen.QRCodegen;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class ClippedCoupons extends Activity {

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clipped_coupons);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		ListView clippedList = (ListView) findViewById(R.id.clippedCouponsList);
		BeacApp app = (BeacApp) getApplicationContext();

		ArrayList<Coupon> savedCoupons = app.getSavedCoupons();
		if (null != savedCoupons && (!savedCoupons.isEmpty())) {
			CouponDeleteAdapter adapter = new CouponDeleteAdapter(this, R.layout.coupondelete, savedCoupons);
			clippedList.setAdapter(adapter);

		} else {
			final TextView textView = (TextView) findViewById(R.id.textView1);
			textView.setText(textView.getText() + "No savings yet !!!!");
		}


		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clipped_coupons, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.QRActivity) {
			String userId = "KumaranMohan";
			String couponIds = "";
			BeacApp app = (BeacApp) getApplicationContext();

			ArrayList<Coupon> savedCoupons = app.getSavedCoupons();
			if (null != savedCoupons && (!savedCoupons.isEmpty())) {
				for (Coupon temp : savedCoupons) {
					if (!couponIds.equals("")) {
						couponIds = couponIds + ",";
						couponIds = couponIds.concat(temp.getCouponId());
					} else {
						couponIds = couponIds.concat(temp.getCouponId());
					}
				}
			}

			String urlString = "http://192.168.43.2:8080/CrunchifyRESTJerseyExample/crunchify/postUserCoupons?User="+userId+"&coupons="+couponIds;
			new CallAPI().execute(urlString);

			Intent sendQRIntent = new Intent(ClippedCoupons.this, QRActivity.class);
			startActivityForResult(sendQRIntent, 1);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client2.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"ClippedCoupons Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.example.beacretail/http/host/path")
		);
		AppIndex.AppIndexApi.start(client2, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"ClippedCoupons Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.example.beacretail/http/host/path")
		);
		AppIndex.AppIndexApi.end(client2, viewAction);
		client2.disconnect();
	}

	private class CallAPI extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String urlString=params[0]; // URL to call
			String resultToDisplay = "";
			InputStream in = null;
			// HTTP Get
			try {
				URL url = new URL(urlString);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				in = new BufferedInputStream(urlConnection.getInputStream());
			} catch (Exception e ) {
				System.out.println(e.getMessage());
				return e.getMessage();
			}
			return resultToDisplay;
		}
		protected void onPostExecute(String result) {
		}

	} // end CallAPI

}
