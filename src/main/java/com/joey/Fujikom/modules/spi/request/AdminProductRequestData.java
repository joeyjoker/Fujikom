package com.joey.Fujikom.modules.spi.request;


public class AdminProductRequestData {
	private Long productId;// 商品ID
	private String productName;	// 商品名称
    private String memberId;	// 用户Id
	private String productMemo;	// 商品介绍
	private String productImageOne;// 商品图片1
	private String productImageTwo;// 商品图片2
	private String productImageThree;	// 商品图片3
	private String productImageFour;// 商品图片4
	private String productBarcode;   
    private String productImageOneType;
    private String productImageTwoType;
    private String productImageThreeType;
    private String productImageFourType;
    private String boxId;
    
    
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	
	public String getProductBarcode() {
		return productBarcode;
	}
	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}
	public String getProductImageOneType() {
		return productImageOneType;
	}
	public void setProductImageOneType(String productImageOneType) {
		this.productImageOneType = productImageOneType;
	}
	public String getProductImageTwoType() {
		return productImageTwoType;
	}
	public void setProductImageTwoType(String productImageTwoType) {
		this.productImageTwoType = productImageTwoType;
	}
	public String getProductImageThreeType() {
		return productImageThreeType;
	}
	public void setProductImageThreeType(String productImageThreeType) {
		this.productImageThreeType = productImageThreeType;
	}
	public String getProductImageFourType() {
		return productImageFourType;
	}
	public void setProductImageFourType(String productImageFourType) {
		this.productImageFourType = productImageFourType;
	}
	public String getBoxId() {
		return boxId;
	}
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	
	
    

}
