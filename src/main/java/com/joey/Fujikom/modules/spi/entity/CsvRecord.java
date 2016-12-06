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
 * csv导出记录Entity
 * @author JoeyHuang
 * @version 2016-03-09
 */
@Entity
@Table(name = "Fujikom_csv")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CsvRecord extends BaseEntity<CsvRecord> {
	
	private static final long serialVersionUID = 1L;
	private Long csvId; //csv创建时间
	private String csvFileName; //csv文件名
	private Date csvCreateDate; //csv创建时间
	
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "CSV_ID")
	public Long getCsvId() {
		return csvId;
	}
	public void setCsvId(Long csvId) {
		this.csvId = csvId;
	}
	@Column(name = "CSV_FILE_NAME")
	public String getCsvFileName() {
		return csvFileName;
	}
	public void setCsvFileName(String csvFileName) {
		this.csvFileName = csvFileName;
	}
	
	@Column(name = "CSV_CREATE_DATE")
	public Date getCsvCreateDate() {
		return csvCreateDate;
	}
	public void setCsvCreateDate(Date csvCreateDate) {
		this.csvCreateDate = csvCreateDate;
	}
	
}


