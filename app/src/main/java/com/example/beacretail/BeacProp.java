package com.example.beacretail;

import java.io.Serializable;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BeacProp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beac_prop);
		  ActionBar actionBar = getActionBar();
	        actionBar.setDisplayShowHomeEnabled(false);
	        actionBar.setDisplayShowTitleEnabled(false);
		String deviceTxt;
		
		Serializable dataRcv = getIntent().getSerializableExtra("BeaconProperties");
		if (dataRcv!=null)
		{
			BeaconInfo bd = (BeaconInfo) dataRcv;
			deviceTxt = "Device Name:" + bd.getIbeaconName() + "\n" +
    	    		"UUID:" + bd.getIbeaconUUID()+ "\n" +
    	    		"Device Unique Id:" + bd.getIbeaconUniqueId() + "\n" +
    	    		"Major:" + bd.getIbeaconMajorID()+ "\n" +
    	    		"Minor:" + bd.getIbeaconMinorID()+ "\n" +
    	    		"Firmware Version:" + bd.getIbeaconFirmware()+ "\n" +
    	    		"RSSI:" + bd.getIbeaconRSSI() + "\n" +
    	    		"Battery Power:" + bd.getIbeaconbattery()
    	    		;
    	    TextView text = (TextView)findViewById(R.id.beacPropText);
    		text.setText(deviceTxt);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.beac_prop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}
}
