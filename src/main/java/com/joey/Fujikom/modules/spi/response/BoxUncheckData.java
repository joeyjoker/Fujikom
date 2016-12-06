package com.joey.Fujikom.modules.spi.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BoxUncheckData {
	private Long boxId;// 用户ID
	private String boxAdminNumber; //
	private String boxStatus; // 用户地址
	private String boxProductNumber;
	private Date boxCreateDate;
	private List<String> barcodeList = new ArrayList<String>(); 

	public Long getBoxId() {
		return boxId;
	}

	public void setBoxId(Long boxId) {
		this.boxId = boxId;
	}

	public String getBoxAdminNumber() {
		return boxAdminNumber;
	}

	public void setBoxAdminNumber(String boxAdminNumber) {
		this.boxAdminNumber = boxAdminNumber;
	}

	public String getBoxStatus() {
		return boxStatus;
	}

	public void setBoxStatus(String boxStatus) {
		this.boxStatus = boxStatus;
	}

	public String getBoxProductNumber() {
		return boxProductNumber;
	}

	public void setBoxProductNumber(String boxProductNumber) {
		this.boxProductNumber = boxProductNumber;
	}

	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	public Date getBoxCreateDate() {
		return boxCreateDate;
	}

	public void setBoxCreateDate(Date boxCreateDate) {
		this.boxCreateDate = boxCreateDate;
	}

	public List<String> getBarcodeList() {
		return barcodeList;
	}

	public void setBarcodeList(List<String> barcodeList) {
		this.barcodeList = barcodeList;
	}
	

}
