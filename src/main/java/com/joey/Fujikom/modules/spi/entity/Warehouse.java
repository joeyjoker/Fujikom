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
 * 仓库Entity
 * @author JoeyHuang
 * @version 2016-03-07
 */
@Entity
@Table(name = "Fujikom_warehouse")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Warehouse extends BaseEntity<Warehouse> {
	
	private static final long serialVersionUID = 1L;
	private Long warehouseId;// 仓库ID
    private String warehouseName;	// 仓库名
    private String warehouseAddress; //仓库地址
    private String warehouseRemarks; //仓库备注
    private String warehouseZip; //仓库邮编
    private String warehouseTelphone; //仓库电话
	
	
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "WAREHOUSE_ID")
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	

	
	@Column(name = "WAREHOUSE_NAME")
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	
	@Column(name = "WAREHOUSE_ADDRESS")
	public String getWarehouseAddress() {
		return warehouseAddress;
	}
	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	
	@Column(name = "WAREHOUSE_REMARKS")
	public String getWarehouseRemarks() {
		return warehouseRemarks;
	}
	public void setWarehouseRemarks(String warehouseRemarks) {
		this.warehouseRemarks = warehouseRemarks;
	}
	
	@Column(name = "WAREHOUSE_ZIP")
	public String getWarehouseZip() {
		return warehouseZip;
	}
	public void setWarehouseZip(String warehouseZip) {
		this.warehouseZip = warehouseZip;
	}
	
	@Column(name = "WAREHOUSE_TELPHONE")
	public String getWarehouseTelphone() {
		return warehouseTelphone;
	}
	public void setWarehouseTelphone(String warehouseTelphone) {
		this.warehouseTelphone = warehouseTelphone;
	}
	
	
	
}


