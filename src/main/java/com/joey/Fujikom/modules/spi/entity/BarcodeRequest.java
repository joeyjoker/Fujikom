package com.joey.Fujikom.modules.spi.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.joey.Fujikom.common.persistence.BaseEntity;

/**
 * 条形码请求Entity
 * @author JoeyHuang
 * @version 2016-03-09
 */
@Entity
@Table(name = "Fujikom_barcode_request")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BarcodeRequest extends BaseEntity<BarcodeRequest> {
	
	private static final long serialVersionUID = 1L;
	private Long barcodeRequestId;// 条形码请求ID
    private String barcodeNumber;	// 条形码请求数量
    private Date barcodeRequestDate;//条形码请求时间
    private String delFlag;//逻辑删除标记
	
	
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "BARCODE_REQUEST_ID")
	public Long getBarcodeRequestId() {
		return barcodeRequestId;
	}
	public void setBarcodeRequestId(Long barcodeRequestId) {
		this.barcodeRequestId = barcodeRequestId;
	}

	@Column(name = "BARCODE_NUMBER")
	public String getBarcodeNumber() {
		return barcodeNumber;
	}
	public void setBarcodeNumber(String barcodeNumber) {
		this.barcodeNumber = barcodeNumber;
	}
	
	@Column(name = "BARCODE_REQUEST_DATE")
	public Date getBarcodeRequestDate() {
		return barcodeRequestDate;
	}
	public void setBarcodeRequestDate(Date barcodeRequestDate) {
		this.barcodeRequestDate = barcodeRequestDate;
	}
	
	@Column(name = "DEL_FLAG")
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
	
}


