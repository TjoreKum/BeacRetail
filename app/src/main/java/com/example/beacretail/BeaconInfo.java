package com.example.beacretail;

import java.io.Serializable;

public class BeaconInfo implements Serializable{
	private String departmentName;
	private int departmentId;

	public BeaconInfo(String departmentName) {
		this.departmentName = departmentName;
	}

	private String ibeaconUUID;
	private String ibeaconName;
	private int ibeaconMajorID;
	private int ibeaconMinorID;
	private String departmentImageLoc;
	private String ibeaconUniqueId;
	private int ibeaconFirmware;
	private int ibeaconbattery;
	private double ibeaconRSSI;

	public BeaconInfo() {
		super();
	}

	public String getIbeaconUniqueId() {
		return ibeaconUniqueId;
	}
	public int getIbeaconFirmware() {
		return ibeaconFirmware;
	}
	public void setIbeaconFirmware(int ibeaconFirmware) {
		this.ibeaconFirmware = ibeaconFirmware;
	}
	public int getIbeaconbattery() {
		return ibeaconbattery;
	}
	public void setIbeaconbattery(int ibeaconbattery) {
		this.ibeaconbattery = ibeaconbattery;
	}
	public double getIbeaconRSSI() {
		return ibeaconRSSI;
	}
	public void setIbeaconRSSI(double ibeaconRSSI) {
		this.ibeaconRSSI = ibeaconRSSI;
	}
	public void setIbeaconUniqueId(String ibeaconUniqueId) {
		this.ibeaconUniqueId = ibeaconUniqueId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	public String getIbeaconUUID() {
		return ibeaconUUID;
	}
	public void setIbeaconUUID(String ibeaconUUID) {
		this.ibeaconUUID = ibeaconUUID;
	}

	public String getDepartmentImageLoc() {
		return departmentImageLoc;
	}
	public void setDepartmentImageLoc(String departmentImageLoc) {
		this.departmentImageLoc = departmentImageLoc;
	}
	public int getIbeaconMajorID() {
		return ibeaconMajorID;
	}
	public void setIbeaconMajorID(int ibeaconMajorID) {
		this.ibeaconMajorID = ibeaconMajorID;
	}
	public int getIbeaconMinorID() {
		return ibeaconMinorID;
	}
	public void setIbeaconMinorID(int ibeaconMinorID) {
		this.ibeaconMinorID = ibeaconMinorID;
	}
	public String getIbeaconName() {
		return ibeaconName;
	}
	public void setIbeaconName(String ibeaconName) {
		this.ibeaconName = ibeaconName;
	}
	public BeaconInfo(String ibeaconUUID, String ibeaconName,
			int ibeaconMajorID, int ibeaconMinorID,
			String ibeaconUniqueId, int ibeaconFirmware, int ibeaconbattery,
			double ibeaconRSSI) {
		super();
		this.ibeaconUUID = ibeaconUUID;
		this.ibeaconName = ibeaconName;
		this.ibeaconMajorID = ibeaconMajorID;
		this.ibeaconMinorID = ibeaconMinorID;
		this.ibeaconUniqueId = ibeaconUniqueId;
		this.ibeaconFirmware = ibeaconFirmware;
		this.ibeaconbattery = ibeaconbattery;
		this.ibeaconRSSI = ibeaconRSSI;
	}
}
