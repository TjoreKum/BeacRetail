package com.example.beacretail;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseHelper extends SQLiteOpenHelper{
	    private static String DB_PATH = "/data/data/com.example.beacretail/databases/";
	    private static String DB_NAME = "BeaconInfo.db";
	    private SQLiteDatabase myDataBase; 
	    private final Context myContext;
	    public DatabaseHelper(Context context) {
	    	super(context, DB_NAME, null, 1);
	        this.myContext = context;
	    }	
	    public void createDataBase() throws IOException{
	    	boolean dbExist = checkDataBase();
	    	if(dbExist){
	    	}else{
	        	this.getReadableDatabase();
	        	try {
	    			copyDataBase();
	    		} catch (IOException e) {
	        		throw new Error("Error copying database");
	        	}
	    	}
	 
	    }
	    private boolean checkDataBase(){
	 
	    	SQLiteDatabase checkDB = null;
	    	try{
	    		String myPath = DB_PATH + DB_NAME;
	    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    	}catch(SQLiteException e){
	    	}
	    	if(checkDB != null){
	    		/*Cursor resultSet=checkDB.rawQuery("Select * from Beacon_Info where ibeaconUUID = 'vc3q'",null);
	    		resultSet.moveToFirst();
    	        String uniqueName = "";
    	        while(resultSet.isAfterLast() == false){
    	        	uniqueName = uniqueName + "\n" +(resultSet.getString(resultSet.getColumnIndex("UNIQUE_ID")));
    	            resultSet.moveToNext();
    	         }*/
	    		checkDB.close();
	    	}
	    	return checkDB != null ? true : false;
	    }
	 
	    private void copyDataBase() throws IOException{
	 
	    	InputStream myInput = myContext.getAssets().open(DB_NAME);
	    	String outFileName = DB_PATH + DB_NAME;
	    	OutputStream myOutput = new FileOutputStream(outFileName);
	 
	    	byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = myInput.read(buffer))>0){
	    		myOutput.write(buffer, 0, length);
	    	}
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
	    }
	 
	    public void openDataBase() throws SQLException{
	        String myPath = DB_PATH + DB_NAME;
	    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    }
	 
	    public SQLiteDatabase getMyDataBase() {
			return myDataBase;
		}

		@Override
		public synchronized void close() {
	    	    if(myDataBase != null)
	    		    myDataBase.close();
	    	    super.close();
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	 
	}