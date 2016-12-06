package com.joey.Fujikom.modules.spi.entity;

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
 * 条形码Entity
 * @author JoeyHuang
 * @version 2016-03-07
 */
@Entity
@Table(name = "Fujikom_barcode")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Barcode extends BaseEntity<Barcode> {
	
	private static final long serialVersionUID = 1L;
	private Long barcodeId;// 条形码ID
    private String barcodeValue;	// 条形码值
	
	
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "BARCODE_ID")
	public Long getBarcodeId() {
		return barcodeId;
	}
	public void setBarcodeId(Long barcodeId) {
		this.barcodeId = barcodeId;
	}
	

	
	@Column(name = "BARCODE_VALUE")
	public String getBarcodeValue() {
		return barcodeValue;
	}
	public void setBarcodeValue(String barcodeValue) {
		this.barcodeValue = barcodeValue;
	}
	
	
}


