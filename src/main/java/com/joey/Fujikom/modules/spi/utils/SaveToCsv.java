package com.joey.Fujikom.modules.spi.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.joey.Fujikom.common.utils.DateUtils;
import com.joey.Fujikom.modules.spi.entity.Product;

public class SaveToCsv {
	
	public static File putOutTaskToExcelFile(List<Product> products) {
		
		  BufferedWriter out = null;
		 
		  /*SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");*/
		  File csvFile = null;
		  String fileName = "在庫データ" + DateUtils.getDate("yyyyMMddHHmmss"); 
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
		     .write("syohin_code"+","+"sire_code"+","+"jan_code"+","+"syohin_name" + "," +"syohin_kbn"+","+ "toriatukai_kbn"+","+
		   "genka_tnk"+","+"baika_tnk"+","+"zaiko_su"+","+"location"+","+"kataban" + "," + "bikou");//换成你需要的表头
		   out.newLine();
		   Iterator<Product> resultIterator = products.iterator();
		   while (resultIterator.hasNext()) {
		   Product product = resultIterator.next();
		    
		    out.write(product.getProductBarcode()+","+product.getMember().getWarehouse().getWarehouseId().toString()+","+product.getProductBarcode()+","
		    +product.getProductName() + "," + "0"+","+"0"+","+"0"+","+"0"+","+"1"+","+product.getProductShelfNumber()+","+product.getMember().getMemberRealName()+","+product.getProductMemo());
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
