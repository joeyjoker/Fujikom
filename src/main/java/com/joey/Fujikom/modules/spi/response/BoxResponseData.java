package com.joey.Fujikom.modules.spi.response;

import java.util.Date;

public class BoxResponseData {
	
	private String boxAdminNumber;	
	private String boxProductNumber;
	private Date boxCreateDate;
	private String boxBarcodeHistory;
	public String getBoxAdminNumber() {
		return boxAdminNumber;
	}
	public void setBoxAdminNumber(String boxAdminNumber) {
		this.boxAdminNumber = boxAdminNumber;
	}
	public String getBoxProductNumber() {
		return boxProductNumber;
	}
	public void setBoxProductNumber(String boxProductNumber) {
		this.boxProductNumber = boxProductNumber;
	}
	public Date getBoxCreateDate() {
		return boxCreateDate;
	}
	public void setBoxCreateDate(Date boxCreateDate) {
		this.boxCreateDate = boxCreateDate;
	}
	public String getBoxBarcodeHistory() {
		return boxBarcodeHistory;
	}
	public void setBoxBarcodeHistory(String boxBarcodeHistory) {
		this.boxBarcodeHistory = boxBarcodeHistory;
	}
	
	

}
