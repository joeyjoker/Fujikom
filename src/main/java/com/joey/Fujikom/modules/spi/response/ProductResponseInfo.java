package com.joey.Fujikom.modules.spi.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ProductResponseInfo {
	
	private Long productId;// 商品ID
	private String productName;	// 商品名称
	private String productSize;	// 商品size
	private String productMemo;	// 商品介绍
	private String productImageOne;// 商品图片1
	private String productImageTwo;// 商品图片2
	private String productImageThree;	// 商品图片3
	private String productImageFour;// 商品图片4
	private String productStatus;	// 商品状态
	private String productBarcode;   //商品条形码
   	private String productShelfNumber;//商品货架号
   	private Date productCreateDate;//商品创建时间
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSize() {
		return productSize;
	}
	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}
	public String getProductMemo() {
		return productMemo;
	}
	public void setProductMemo(String productMemo) {
		this.productMemo = productMemo;
	}
	public String getProductImageOne() {
		return productImageOne;
	}
	public void setProductImageOne(String productImageOne) {
		this.productImageOne = productImageOne;
	}
	public String getProductImageTwo() {
		return productImageTwo;
	}
	public void setProductImageTwo(String productImageTwo) {
		this.productImageTwo = productImageTwo;
	}
	public String getProductImageThree() {
		return productImageThree;
	}
	public void setProductImageThree(String productImageThree) {
		this.productImageThree = productImageThree;
	}
	public String getProductImageFour() {
		return productImageFour;
	}
	public void setProductImageFour(String productImageFour) {
		this.productImageFour = productImageFour;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	public String getProductBarcode() {
		return productBarcode;
	}
	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}
	public String getProductShelfNumber() {
		return productShelfNumber;
	}
	public void setProductShelfNumber(String productShelfNumber) {
		this.productShelfNumber = productShelfNumber;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getProductCreateDate() {
		return productCreateDate;
	}
	public void setProductCreateDate(Date productCreateDate) {
		this.productCreateDate = productCreateDate;
	}
   	
   	

}
