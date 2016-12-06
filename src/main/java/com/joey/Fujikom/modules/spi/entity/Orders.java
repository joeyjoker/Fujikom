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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joey.Fujikom.common.persistence.BaseEntity;

/**
 * 订单Entity
 * @author JoeyHuang
 * @version 2016-03-08
 */
@Entity
@Table(name = "Fujikom_orders")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Orders extends BaseEntity<Orders> {

	
	private static final long serialVersionUID = 1L;
	private Long ordersId;// 订单ID
	private String ordersNumber;//订单号
	private String ordersFlag;	// 订单标记
	private String ordersReceiverName;//收件人姓名
	private String ordersTelphone;	// 收件人联系电话
	private String ordersAddress;	// 收件人地址
	private String ordersZip;      //收件人邮编
	private String ordersPrice;// 订单价格
	private String ordersStatus;// 订单状态
	private Date ordersRequestDate;	// 订单请求时间
   	private Member member;
   	private String ordersCompany;
   	private String ordersConsultNumber;
   	private String ordersProductsNumber; //订单商品数量
   	private String ordersSize;   //订单大小
   	private Date ordersDeliveryDate;//订单寄送时间
   	private String ordersSenderName;//订单发送时间
   	private String ordersRejectPattern; //订单返品时间
   	private Date ordersUpdateDate; //订单更新时间
   	private String ordersBuliding;//建屋名
   	private String ordersLocation;//配送先
   	private String ordersSenderTelphone;//发送人电话
   	private String ordersSenderAddress; //发送人地址
   	private String ordersSenderZip;     //发送人邮编
   	private String ordersAccountFlag;   //结算标记
   	private String ordersExportFlag;    //csv导出标记
   	
   	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "ORDERS_ID")
	public Long getOrdersId() {
		return ordersId;
	}
	public void setOrdersId(Long ordersId) {
		this.ordersId = ordersId;
	}
	
	@Column(name = "ORDERS_NUMBER")
	public String getOrdersNumber() {
		return ordersNumber;
	}
	public void setOrdersNumber(String ordersNumber) {
		this.ordersNumber = ordersNumber;
	}
	@Column(name = "ORDERS_FLAG")
	public String getOrdersFlag() {
		return ordersFlag;
	}
	public void setOrdersFlag(String ordersFlag) {
		this.ordersFlag = ordersFlag;
	}
	
	@Column(name = "ORDERS_RECEIVER_NAME")
	public String getOrdersReceiverName() {
		return ordersReceiverName;
	}
	public void setOrdersReceiverName(String ordersReceiverName) {
		this.ordersReceiverName = ordersReceiverName;
	}
	
	@Column(name = "ORDERS_TELPHONE")
	public String getOrdersTelphone() {
		return ordersTelphone;
	}
	public void setOrdersTelphone(String ordersTelphone) {
		this.ordersTelphone = ordersTelphone;
	}

	@Column(name = "ORDERS_ZIP")
	public String getOrdersZip() {
		return ordersZip;
	}
	public void setOrdersZip(String ordersZip) {
		this.ordersZip = ordersZip;
	}
	@Column(name = "ORDERS_ADDRESS")
	public String getOrdersAddress() {
		return ordersAddress;
	}
	public void setOrdersAddress(String ordersAddress) {
		this.ordersAddress = ordersAddress;
	}
	
	@Column(name = "ORDERS_PRICE")
	public String getOrdersPrice() {
		return ordersPrice;
	}
	public void setOrdersPrice(String ordersPrice) {
		this.ordersPrice = ordersPrice;
	}
	
	@Column(name = "ORDERS_STATUS")
	public String getOrdersStatus() {
		return ordersStatus;
	}
	public void setOrdersStatus(String ordersStatus) {
		this.ordersStatus = ordersStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "ORDERS_REQUEST_DATE")
	public Date getOrdersRequestDate() {
		return ordersRequestDate;
	}
	public void setOrdersRequestDate(Date ordersRequestDate) {
		this.ordersRequestDate = ordersRequestDate;
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
	
	@Column(name = "ORDERS_COMPANY")
	public String getOrdersCompany() {
		return ordersCompany;
	}
	public void setOrdersCompany(String ordersCompany) {
		this.ordersCompany = ordersCompany;
	}
	
	@Column(name = "ORDERS_CONSULT_NUMBER")
	public String getOrdersConsultNumber() {
		return ordersConsultNumber;
	}
	public void setOrdersConsultNumber(String ordersConsultNumber) {
		this.ordersConsultNumber = ordersConsultNumber;
	}
	
	@Column(name = "ORDERS_PRODUCTS_NUMBER")
	public String getOrdersProductsNumber() {
		return ordersProductsNumber;
	}
	public void setOrdersProductsNumber(String ordersProductsNumber) {
		this.ordersProductsNumber = ordersProductsNumber;
	}
	
	@Column(name = "ORDERS_SIZE")
	public String getOrdersSize() {
		return ordersSize;
	}
	public void setOrdersSize(String ordersSize) {
		this.ordersSize = ordersSize;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "ORDERS_DELIVERY_DATE")
	public Date getOrdersDeliveryDate() {
		return ordersDeliveryDate;
	}
	public void setOrdersDeliveryDate(Date ordersDeliveryDate) {
		this.ordersDeliveryDate = ordersDeliveryDate;
	}
	
	@Column(name = "ORDERS_SENDER_NAME")
	public String getOrdersSenderName() {
		return ordersSenderName;
	}
	public void setOrdersSenderName(String ordersSenderName) {
		this.ordersSenderName = ordersSenderName;
	}
	
	@Column(name = "ORDERS_REJECT_PATTERN")
	public String getOrdersRejectPattern() {
		return ordersRejectPattern;
	}
	public void setOrdersRejectPattern(String ordersRejectPattern) {
		this.ordersRejectPattern = ordersRejectPattern;
	}
	
	@Column(name = "ORDERS_UPDATE_DATE")
	public Date getOrdersUpdateDate() {
		return ordersUpdateDate;
	}
	public void setOrdersUpdateDate(Date ordersUpdateDate) {
		this.ordersUpdateDate = ordersUpdateDate;
	}
	@Column(name = "ORDERS_BULIDING")
	public String getOrdersBuliding() {
		return ordersBuliding;
	}
	public void setOrdersBuliding(String ordersBuliding) {
		this.ordersBuliding = ordersBuliding;
	}
	
	@Column(name = "ORDERS_LOCATION")
	public String getOrdersLocation() {
		return ordersLocation;
	}
	public void setOrdersLocation(String ordersLocation) {
		this.ordersLocation = ordersLocation;
	}
	@Column(name = "ORDERS_SENDER_TELPHONE")
	public String getOrdersSenderTelphone() {
		return ordersSenderTelphone;
	}
	public void setOrdersSenderTelphone(String ordersSenderTelphone) {
		this.ordersSenderTelphone = ordersSenderTelphone;
	}
	
	@Column(name = "ORDERS_SENDER_ADDRESS")
	public String getOrdersSenderAddress() {
		return ordersSenderAddress;
	}
	public void setOrdersSenderAddress(String ordersSenderAddress) {
		this.ordersSenderAddress = ordersSenderAddress;
	}
	
	@Column(name = "ORDERS_SENDER_ZIP")
	public String getOrdersSenderZip() {
		return ordersSenderZip;
	}
	public void setOrdersSenderZip(String ordersSenderZip) {
		this.ordersSenderZip = ordersSenderZip;
	}
	
	@Column(name = "ORDERS_ACCOUNT_FLAG")
	public String getOrdersAccountFlag() {
		return ordersAccountFlag;
	}
	public void setOrdersAccountFlag(String ordersAccountFlag) {
		this.ordersAccountFlag = ordersAccountFlag;
	}
	
	@Column(name = "ORDERS_EXPORT_FLAG")
	public String getOrdersExportFlag() {
		return ordersExportFlag;
	}
	public void setOrdersExportFlag(String ordersExportFlag) {
		this.ordersExportFlag = ordersExportFlag;
	}
	
	
	
   	
   	
   

}
