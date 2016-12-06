package com.joey.Fujikom.modules.spi.response;

import java.util.HashSet;

public class BarcodeResponse {
	
	private HashSet<String> barcodeSet;
	private String barcodeUrl;
	public HashSet<String> getBarcodeSet() {
		return barcodeSet;
	}
	public void setBarcodeSet(HashSet<String> barcodeSet) {
		this.barcodeSet = barcodeSet;
	}
	public String getBarcodeUrl() {
		return barcodeUrl;
	}
	public void setBarcodeUrl(String barcodeUrl) {
		this.barcodeUrl = barcodeUrl;
	} 
	

}
