package com.joey.Fujikom.modules.spi.utils;

import java.util.Random;

public class BarcodeGen {

	
	public static String get() {
		String head = "49";
		String mid1 = "";
		String mid2 = "";
		int c1 = 0;
		int c2 = 0;
		Random random =  new Random();
		String barcodeString = "";
		mid1 = String.valueOf( 10000 + random.nextInt(90000));
		mid2 = String.valueOf( 10000 + random.nextInt(90000));
		barcodeString = head+mid1+mid2;
		for (int i = 0; i < 12; i+=2) {
			char c = barcodeString.charAt(i);
			int n = c-'0';
			c1+=n;
		}
		for (int i = 1; i < 12; i+=2) {
			char c = barcodeString.charAt(i);
			int n = c-'0';
			c2+=n;
		}
		int cc = c1+c2*3;
		int check = cc%10;
		check = (10-cc%10)%10;
		barcodeString = barcodeString+String.valueOf(check);
		return barcodeString;
	}
}
