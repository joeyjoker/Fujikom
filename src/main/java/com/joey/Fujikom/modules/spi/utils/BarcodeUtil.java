package com.joey.Fujikom.modules.spi.utils;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import com.joey.Fujikom.common.utils.Encodes;



public class BarcodeUtil {
	
	public static void downloadBarcode(String barcodeValue,HttpServletResponse response,String filename){
		
		  try {
	            //Create the barcode bean
			    EAN13Bean bean = new EAN13Bean();
	            //Code39Bean bean = new Code39Bean();
	             
	            final int dpi = 150;
	             
	            //Configure the barcode generator
	            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar 
	                                                             //width exactly one pixel
	            //bean.setWideFactor(3);
	            bean.doQuietZone(false);
	            bean.setModuleWidth(0.33);
	            
	             
	            //Open output file
	            response.setContentType("application/x-download; charset=utf-8");
		        response.setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(filename));
		        OutputStream out = response.getOutputStream();
	            try {
	                //Set up the canvas provider for monochrome JPEG output 
	                BitmapCanvasProvider canvas = new BitmapCanvasProvider(
	                        out, "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
	             
	                //Generate the barcode
	                bean.generateBarcode(canvas,barcodeValue);
	             
	                //Signal end of generation
	                canvas.finish();
	            } finally {
	                out.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

}
