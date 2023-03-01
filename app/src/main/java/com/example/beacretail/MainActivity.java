package com.example.beacretail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;

import android.app.FragmentManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.beacretail.codegen.Utils;
import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.EddystoneScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.IBeaconScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.ScanContext;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.DeviceProfile;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.EventType;
import com.kontakt.sdk.android.ble.discovery.eddystone.EddystoneDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.ibeacon.IBeaconAdvertisingPacket;
import com.kontakt.sdk.android.ble.discovery.ibeacon.IBeaconDeviceEvent;
import com.kontakt.sdk.android.ble.filter.eddystone.EddystoneFilters;
import com.kontakt.sdk.android.ble.filter.ibeacon.IBeaconFilter;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.rssi.RssiCalculators;
import com.kontakt.sdk.android.ble.util.BluetoothUtils;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

public class MainActivity extends Activity implements ProximityManager.ProximityListener {
    private static final int REQUEST_CODE_ENABLE_BLUETOOTH = 1;
    private ProximityManager proximityManager;
    private ScanContext scanContext;
    //    List<BeaconInfo> beaconDetailList = new ArrayList<BeaconInfo>();
    BeaconInfo beaconInfo = new BeaconInfo();
    List<String> values = new ArrayList<String>();
    DatabaseHelper dbHelper =  new DatabaseHelper(this);
    //Menu handling
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    String[] deptArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        proximityManager = new ProximityManager(this);

        //Menu
        title = drawerTitle = getTitle();
        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();
        } catch (Exception ioe) {
            System.err.println(ioe.getStackTrace());
        }
        Cursor resultSet = dbHelper.getMyDataBase().rawQuery("Select * from Beacon_Info",null);
        resultSet.moveToFirst();
        String deptName = resultSet.getString(resultSet.getColumnIndex("departmentName"));
        deptArray = new String[] { "Health & Beauty", "Grocery", "Entertainment"};

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.drawerList);

        // Set shadow to drawer
//        drawerLayout.setDrawerShadow(R.drawable.drawershadow,
//                GravityCompat.START);

        // Set adapter to drawer
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, deptArray));

        // set up click listener on drawer
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // set app icon to behave as action to toggle navigation drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // ActionBarDrawerToggle ties together the the interactions the sliding
        // drawer and the app icon
        drawerToggle = new ActionBarDrawerToggle(this, // Host Activity
                drawerLayout, // layout container for navigation drawer
                R.drawable.menu_grey, // Application Icon
                R.string.drawer_open, // Open Drawer Description
                R.string.drawer_close) // Close Drawer Description
        {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        //Beac Scanner
        IBeaconScanContext iBeaconScanContext = new IBeaconScanContext.Builder()
                //.setEventTypes(EnumSet.of(EventType.DEVICE_DISCOVERED,
                //      EventType.DEVICE_LOST))
                .setRssiCalculator(RssiCalculators.DEFAULT)
                .setIBeaconFilters(Arrays.asList(
                        new IBeaconFilter() {
                            @Override
                            public boolean apply(IBeaconAdvertisingPacket iBeaconAdvertisingPacket) {
                                return iBeaconAdvertisingPacket.getDistance()<0.5;
                            }
                        }
                        //        IBeaconFilters.newMajorFilter(34)
                ))
                .build();

        EddystoneScanContext eddystoneScanContext = new EddystoneScanContext.Builder()
                .setEventTypes(EnumSet.of(EventType.SPACE_ENTERED,
                        EventType.SPACE_ABANDONED))
                .setUIDFilters(Arrays.asList(
                        EddystoneFilters.newUIDFilter(KontaktSDK.DEFAULT_KONTAKT_EDDYSTONE_NAMESPACE_ID, "000013")
                ))
                .setURLFilters(Arrays.asList(
                        EddystoneFilters.newURLFilter("http://Kontakt.io")
                ))
                .build();


        scanContext = new ScanContext.Builder()
                .setScanMode(ProximityManager.SCAN_MODE_BALANCED)
                .setIBeaconScanContext(iBeaconScanContext)
                .setEddystoneScanContext(eddystoneScanContext)
                .setActivityCheckConfiguration(ActivityCheckConfiguration.DEFAULT)
                .setForceScanConfiguration(ForceScanConfiguration.DEFAULT)
                .setScanPeriod(new ScanPeriod(TimeUnit.SECONDS.toMillis(10), 0))
                .build();
/*        ListView beaconList = (ListView) findViewById(R.id.beaconList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        beaconList.setAdapter(adapter);

        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent sendBeacIntent = new Intent(view.getContext(), CouponList.class);
                sendBeacIntent.putExtra("BeaconSelected", beaconDetailList.get(position));
                dbHelper.close();
                startActivityForResult(sendBeacIntent, 2);

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        MenuItem item = menu.findItem(R.id.coupons);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        BeacApp app = (BeacApp) getApplicationContext();;
        int count = 0;
        if(null != app.getSavedCoupons()){
            count = app.getSavedCoupons().size();
        }

        Utils.setBadgeCount(this, icon, count);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.coupons) {
            Intent sendBeacPropIntent = new Intent(MainActivity.this, ClippedCoupons.class);
            startActivityForResult(sendBeacPropIntent,1);
            return true;
        }
        if (id == R.id.login) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(loginIntent, 1);
            return true;
        }
        // Listen Toggle state of navigation Drawer
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        if (!BluetoothUtils.isBluetoothEnabled()) {
            final Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_CODE_ENABLE_BLUETOOTH);
        } else {
            initializeScan();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
        proximityManager.finishScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode) {

            case REQUEST_CODE_ENABLE_BLUETOOTH:

                if(resultCode == RESULT_OK) {
                    initializeScan();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onScanStart() {

    }

    // Click Event of navigation drawer item click
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
            System.out.println("!!!!!!!!!!!!!!!!!!!@@@@@@@@@@:::pos::"+position);
            Intent sendDeptIntent = new Intent(view.getContext(), CouponList.class);
            sendDeptIntent.putExtra("DepartmentInfo", position);
            startActivityForResult(sendDeptIntent, 2);
        }
    }

    private void selectItem(int position) {

        // Set Fragment text accordingly
        VersionFragment fragment = new VersionFragment();
        Bundle args = new Bundle();
        args.putString("name", deptArray[position]);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
      //  fragmentManager.beginTransaction()
              //  .replace(R.id.frameContainer, fragment).commit();

        // Update Title on action bar
        drawerList.setItemChecked(position, true);
        setTitle(deptArray[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    public void setTitle(CharSequence _title) {
        title = _title;
        getActionBar().setTitle(title);
    }

    @Override
    public void onEvent(BluetoothDeviceEvent event) {
        if (event.getDeviceProfile() == DeviceProfile.IBEACON) {
            final IBeaconDeviceEvent iBeaconDeviceEvent = (IBeaconDeviceEvent) event;
            // if(iBeaconDeviceEvent.getEventType()== EventType.DEVICE_DISCOVERED)

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String deviceTxt = "Start";
                    List<IBeaconDevice> deviceList = iBeaconDeviceEvent.getDeviceList();
                    RelativeLayout layout = (RelativeLayout)findViewById(R.id.container);
                    ImageButton button = (ImageButton)findViewById(R.id.imageButton);
                    Resources res = getResources();
                    if (deviceList != null && deviceList.size()>0) {
//                        beaconDetailList.clear();
                        values.clear();

                        try {
                            dbHelper.createDataBase();
                            dbHelper.openDataBase();
                        } catch (Exception ioe) {
                            System.err.println(ioe.getStackTrace());
                        }
                        for (int i = 0; i < 1; i++) {

                            String beacUniqId = deviceList.get(i).getUniqueId();
                            Cursor resultSet = dbHelper.getMyDataBase().rawQuery("Select * from Beacon_Info where ibeaconUniqueId = '" + beacUniqId+"'",null);
                            resultSet.moveToFirst();
                            String departmentName = resultSet.getString(resultSet.getColumnIndex("departmentName"));
                            int departmentId = resultSet.getInt(resultSet.getColumnIndex("departmentId"));
                            beaconInfo.setDepartmentId(departmentId);
                            beaconInfo.setDepartmentName(departmentName);
                            beaconInfo.setIbeaconUUID(deviceList.get(i).getProximityUUID().toString());
                            beaconInfo.setIbeaconName(deviceList.get(i).getName());
                            beaconInfo.setIbeaconMajorID(deviceList.get(i).getMajor());
                            beaconInfo.setIbeaconMinorID(deviceList.get(i).getMinor());
                            beaconInfo.setIbeaconUniqueId(deviceList.get(i).getUniqueId());
                            beaconInfo.setIbeaconFirmware(deviceList.get(i).getFirmwareVersion());
                            beaconInfo.setIbeaconbattery(deviceList.get(i).getBatteryPower());
                            beaconInfo.setIbeaconRSSI(deviceList.get(i).getRssi());
                            dbHelper.close();
                            button.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    // TODO Auto-generated method stub
                                    Intent sendBeacIntent = new Intent(view.getContext(), CouponList.class);
                                    sendBeacIntent.putExtra("BeaconSelected",beaconInfo);

                                    startActivityForResult(sendBeacIntent, 2);

                                }
                            });

                            if (departmentId == 1){
                                Drawable drawable = res.getDrawable(R.drawable.hnb);
                                layout.setBackground(drawable);
                                button.setVisibility(View.VISIBLE);
                            }
                            else if (departmentId == 2){
                                Drawable drawable = res.getDrawable(R.drawable.grocery);
                                layout.setBackground(drawable);
                                button.setVisibility(View.VISIBLE);
                            }
                            else if (departmentId == 3){
                                Drawable drawable = res.getDrawable(R.drawable.ent);
                                layout.setBackground(drawable);
                                button.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                    else if(deviceList==null || deviceList.size()==0)
                    {
                        button.setVisibility(View.INVISIBLE);
                        layout.setBackground(null);
                    }
                }
            });


        }
        else if( event.getDeviceProfile() == DeviceProfile.EDDYSTONE){
            final EddystoneDeviceEvent eddystoneDeviceEvent = (EddystoneDeviceEvent) event;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IEddystoneNamespace namespace = eddystoneDeviceEvent.getNamespace();
                    List<IEddystoneDevice> deviceList = eddystoneDeviceEvent.getDeviceList();
                }
            });
        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onScanStop() {
        //proximityManager.disconnect();
        //proximityManager = null;
        dbHelper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proximityManager.disconnect();
        proximityManager = null;
        dbHelper.close();
    }

    private void initializeScan() {
        proximityManager.initializeScan(scanContext, new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.attachListener(MainActivity.this);
            }

            @Override
            public void onConnectionFailure() {
                Toast.makeText(MainActivity.this.getApplicationContext(),"unexpected_error_connection",Toast.LENGTH_LONG);
            }
        });
    }
}
