package com.joey.Fujikom.modules.spi.response;

import java.util.List;

public class ConfirmResponseData {
	
	private List<String> barcodeList;//货架上商品条码集合

	private String size;//货架大小

	public List<String> getBarcodeList() {
		return barcodeList;
	}

	public void setBarcodeList(List<String> barcodeList) {
		this.barcodeList = barcodeList;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	
}
