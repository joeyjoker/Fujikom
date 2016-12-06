package com.joey.Fujikom.modules.spi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.joey.Fujikom.common.persistence.BaseEntity;

/**
 * 商品Entity
 * @author JoeyHuang
 * @version 2016-03-02
 */
@Entity
@Table(name = "Fujikom_product")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product extends BaseEntity<Product> {

	
	private static final long serialVersionUID = 1L;
	private Long productId;// 商品ID
	private String productName;	// 商品名称
    private Member member;	// 用户
	private String productSize;	// 商品size
	private String productMemo;	// 商品介绍
	private String productImageOne;// 商品图片1
	private String productImageTwo;// 商品图片2
	private String productImageThree;	// 商品图片3
	private String productImageFour;// 商品图片4
	
	private String productStatus;	// 商品状态
	
	private String productBarcode;   //商品条形码
    private String delFlag;	// 逻辑删除标记
   	private Box box;	// 箱子
   	private String productShelfNumber;//商品货架号
   	private String productShelfStatus;
   	private Orders orders; //商品对应订单实体
   	private Date productCreateDate;//商品创建时间
   	private Date productInstockDate;//商品入库时间 
   	private Date productDeliveryDate;//商品寄送时间
   	private Date productCostDate; //商品结算时间
   	
   	

	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "PRODUCT_ID")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "PRODUCT_NAME")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	@ManyToOne
	@JoinColumn(name="MEMBER_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Column(name = "PRODUCT_SIZE")
	public String getProductSize() {
		return productSize;
	}

	public void setProductSize(String productSize) {
		this.productSize = productSize;
	}

	@Column(name = "PRODUCT_MEMO")
	public String getProductMemo() {
		return productMemo;
	}

	public void setProductMemo(String productMemo) {
		this.productMemo = productMemo;
	}

	@Column(name = "PRODUCT_IMAGE_ONE")
	public String getProductImageOne() {
		return productImageOne;
	}

	public void setProductImageOne(String productImageOne) {
		this.productImageOne = productImageOne;
	}

	@Column(name = "PRODUCT_IMAGE_TWO")
	public String getProductImageTwo() {
		return productImageTwo;
	}

	public void setProductImageTwo(String productImageTwo) {
		this.productImageTwo = productImageTwo;
	}

	@Column(name = "PRODUCT_IMAGE_THREE")
	public String getProductImageThree() {
		return productImageThree;
	}

	public void setProductImageThree(String productImageThree) {
		this.productImageThree = productImageThree;
	}

	@Column(name = "PRODUCT_IMAGE_FOUR")
	public String getProductImageFour() {
		return productImageFour;
	}

	public void setProductImageFour(String productImageFour) {
		this.productImageFour = productImageFour;
	}
	
	

	@Column(name = "PRODUCT_STATUS")
	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	

	@Column(name = "PRODUCT_BARCODE")
	public String getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(String productBarcode) {
		this.productBarcode = productBarcode;
	}

	@Column(name = "DEL_FLAG")
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@ManyToOne
	@JoinColumn(name="BOX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	@Column(name = "PRODUCT_SHELF_NUMBER")
	public String getProductShelfNumber() {
		return productShelfNumber;
	}

	public void setProductShelfNumber(String productShelfNumber) {
		this.productShelfNumber = productShelfNumber;
	}

	@ManyToOne
	@JoinColumn(name="ORDERS_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	@Column(name = "PRODUCT_CREATE_DATE")
	public Date getProductCreateDate() {
		return productCreateDate;
	}

	public void setProductCreateDate(Date productCreateDate) {
		this.productCreateDate = productCreateDate;
	}

	@Column(name = "PRODUCT_INSTOCK_DATE")
	public Date getProductInstockDate() {
		return productInstockDate;
	}

	public void setProductInstockDate(Date productInstockDate) {
		this.productInstockDate = productInstockDate;
	}

	@Column(name = "PRODUCT_SHELF_STATUS")
	public String getProductShelfStatus() {
		return productShelfStatus;
	}

	public void setProductShelfStatus(String productShelfStatus) {
		this.productShelfStatus = productShelfStatus;
	}
	
	@Column(name = "PRODUCT_DELIVERY_DATE")
	public Date getProductDeliveryDate() {
		return productDeliveryDate;
	}

	public void setProductDeliveryDate(Date productDeliveryDate) {
		this.productDeliveryDate = productDeliveryDate;
	}

	@Column(name = "PRODUCT_COST_DATE")
	public Date getProductCostDate() {
		return productCostDate;
	}

	public void setProductCostDate(Date productCostDate) {
		this.productCostDate = productCostDate;
	}
	
   
	
	
}


