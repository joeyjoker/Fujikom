package com.joey.Fujikom.modules.spi.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import com.joey.Fujikom.common.utils.DateUtils;
import com.joey.Fujikom.modules.spi.entity.Orders;

public class ExportOrdersCsv {
	
	public static File putOutTaskToExcelFile(List<Orders> ordersList) {
		
		  BufferedWriter out = null;
		  SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:MM:ss");
		  File csvFile = null;
		  String fileName = "出荷指示データ" + DateUtils.getDate("yyyyMMddHHmmss"); 
		  try {
			  csvFile = File.createTempFile(fileName,".csv");
		//生成一个csv临时文件
		   csvFile.deleteOnExit();
		  } catch (IOException e1) {
		   e1.printStackTrace();
		  }
		
		  try {
		   out = new BufferedWriter(new FileWriter(csvFile));
		   out
		     .write("店舗伝票番号" + "," +"受注日"+","+ "受注名" + "," + "受注電話番号" + "," + "受注住所"
		       + ","+"発送郵便番号"+","+"発送先住所"+","+"発送先名"+","+"発送電話番号"+","+"商品計"+","+"合計金額"+","
		       +"受注メールアドレス"+","+"支払方法"+","+"発送方法"+","+"商品コード"+","+"商品価格"+","+"受注数量"+"出荷済フラグ");//换成你需要的表头
		   out.newLine();
 
		   Iterator<Orders> resultIterator = ordersList.iterator();
		   while (resultIterator.hasNext()) {
		   Orders orders = resultIterator.next();
		   out.write(orders.getOrdersNumber() + "," +df.format(orders.getOrdersRequestDate())+","+ orders.getOrdersReceiverName() + ","+ orders.getOrdersTelphone() + "," + 
		    		orders.getOrdersAddress()+","+orders.getOrdersSenderZip()+","+orders.getOrdersSenderAddress()+","+orders.getOrdersSenderName()
		    		+","+orders.getOrdersSenderTelphone()+","+orders.getOrdersProductsNumber()+","+orders.getOrdersPrice()+","+""+","+""+","+""+","+""+","+""+","+""+","+"");
		   
		   out.newLine();
		   }
		   out.flush();
		  } catch (IOException e) {
		   e.printStackTrace();
		  } finally {
		   if (out != null) {
		    try {
		     out.close();
		    } catch (IOException e) {
		     e.printStackTrace();
		    }
		   }
		  }
		  return csvFile;
		 }
	


}
