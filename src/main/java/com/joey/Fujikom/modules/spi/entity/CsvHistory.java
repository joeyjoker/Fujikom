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
 * csv导出历史记录Entity
 * @author JoeyHuang
 * @version 2016-03-09
 */
@Entity
@Table(name = "Fujikom_csv_history")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsvHistory extends BaseEntity<CsvHistory> {
	
	private static final long serialVersionUID = 1L;
	private Long csvHistoryId;
	private CsvRecord csvRecord;
	private String ordersNumber;
	private String ordersReceiverName;//收件人姓名
	private String ordersTelphone;	// 收件人联系电话
	private String ordersAddress;	// 收件人地址
	private String ordersPrice;// 订单价格
	private String ordersSenderName;
	private String ordersSenderTelphone;//发送人电话
   	private String ordersSenderAddress; //发送人地址
   	private String ordersSenderZip;     //发送人邮编
   	private String ordersProductsNumber; //订单商品数量
   	private Date ordersRequestDate; //订单创建时间
   	
   	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "CSV_HISTORY_ID")
	public Long getCsvHistoryId() {
		return csvHistoryId;
	}
	public void setCsvHistoryId(Long csvHistoryId) {
		this.csvHistoryId = csvHistoryId;
	}
	
	@ManyToOne
	@JoinColumn(name="CSV_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public CsvRecord getCsvRecord() {
		return csvRecord;
	}
	public void setCsvRecord(CsvRecord csvRecord) {
		this.csvRecord = csvRecord;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_NUMBER")
	public String getOrdersNumber() {
		return ordersNumber;
	}
	public void setOrdersNumber(String ordersNumber) {
		this.ordersNumber = ordersNumber;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_RECEIVER_NAME")
	public String getOrdersReceiverName() {
		return ordersReceiverName;
	}
	public void setOrdersReceiverName(String ordersReceiverName) {
		this.ordersReceiverName = ordersReceiverName;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_TELPHONE")
	public String getOrdersTelphone() {
		return ordersTelphone;
	}
	public void setOrdersTelphone(String ordersTelphone) {
		this.ordersTelphone = ordersTelphone;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_ADDRESS")
	public String getOrdersAddress() {
		return ordersAddress;
	}
	public void setOrdersAddress(String ordersAddress) {
		this.ordersAddress = ordersAddress;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_PRICE")
	public String getOrdersPrice() {
		return ordersPrice;
	}
	public void setOrdersPrice(String ordersPrice) {
		this.ordersPrice = ordersPrice;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_SENDER_NAME")
	public String getOrdersSenderName() {
		return ordersSenderName;
	}
	public void setOrdersSenderName(String ordersSenderName) {
		this.ordersSenderName = ordersSenderName;
	}
	@Column(name = "CSV_HISTORY_ORDERS_SENDER_TELPHONE")
	public String getOrdersSenderTelphone() {
		return ordersSenderTelphone;
	}
	public void setOrdersSenderTelphone(String ordersSenderTelphone) {
		this.ordersSenderTelphone = ordersSenderTelphone;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_SENDER_ADDRESS")
	public String getOrdersSenderAddress() {
		return ordersSenderAddress;
	}
	public void setOrdersSenderAddress(String ordersSenderAddress) {
		this.ordersSenderAddress = ordersSenderAddress;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_SENDER_ZIP")
	public String getOrdersSenderZip() {
		return ordersSenderZip;
	}
	public void setOrdersSenderZip(String ordersSenderZip) {
		this.ordersSenderZip = ordersSenderZip;
	}
	
	@Column(name = "CSV_HISTORY_ORDERS_PRODUCT_NUMBER")
	public String getOrdersProductsNumber() {
		return ordersProductsNumber;
	}
	public void setOrdersProductsNumber(String ordersProductsNumber) {
		this.ordersProductsNumber = ordersProductsNumber;
	}
	@Column(name = "CSV_HISTORY_ORDERS_REQUEST_DATE")
	public Date getOrdersRequestDate() {
		return ordersRequestDate;
	}
	public void setOrdersRequestDate(Date ordersRequestDate) {
		this.ordersRequestDate = ordersRequestDate;
	}	// 订单请求时间
	
	
	
	
	
}


