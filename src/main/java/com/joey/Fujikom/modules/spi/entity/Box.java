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
 * 箱子Entity
 * @author JoeyHuang
 * @version 2016-03-02
 */
@Entity
@Table(name = "Fujikom_box")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Box extends BaseEntity<Box> {
	
	private static final long serialVersionUID = 1L;
	private Long boxId;// 箱子ID
	private Member member;	// 用户
    private String boxAdminNumber;	// 箱子管理番号
	private String boxFlag;	// 全权委托/普通
	private String boxStatus;	// 箱子状态
	private String boxProductNumber; //箱子中商品数
	private Date boxCreateDate; //箱子创建时间
	private String boxDelFlag; //箱子删除标记
	private String boxBarcodeHistory; //条码url历史记录（web用户）
	
	
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "BOX_ID")
	public Long getBoxId() {
		return boxId;
	}
	public void setBoxId(Long boxId) {
		this.boxId = boxId;
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
	
	@Column(name = "BOX_ADMIN_NUMBER")
	public String getBoxAdminNumber() {
		return boxAdminNumber;
	}
	public void setBoxAdminNumber(String boxAdminNumber) {
		this.boxAdminNumber = boxAdminNumber;
	}
	
	@Column(name = "BOX_FLAG")
	public String getBoxFlag() {
		return boxFlag;
	}
	public void setBoxFlag(String boxFlag) {
		this.boxFlag = boxFlag;
	}
	
	@Column(name = "BOX_STATUS")
	public String getBoxStatus() {
		return boxStatus;
	}
	public void setBoxStatus(String boxStatus) {
		this.boxStatus = boxStatus;
	}
	
	@Column(name = "BOX_PRODUCT_NUMBER")
	public String getBoxProductNumber() {
		return boxProductNumber;
	}
	public void setBoxProductNumber(String boxProductNumber) {
		this.boxProductNumber = boxProductNumber;
	}
	@Column(name = "BOX_CREATE_DATE")
	public Date getBoxCreateDate() {
		return boxCreateDate;
	}
	public void setBoxCreateDate(Date boxCreateDate) {
		this.boxCreateDate = boxCreateDate;
	}
	@Column(name = "BOX_DEL_FLAG")
	public String getBoxDelFlag() {
		return boxDelFlag;
	}
	public void setBoxDelFlag(String boxDelFlag) {
		this.boxDelFlag = boxDelFlag;
	}
	
	@Column(name = "BOX_BARCODE_HISTORY")
	public String getBoxBarcodeHistory() {
		return boxBarcodeHistory;
	}
	public void setBoxBarcodeHistory(String boxBarcodeHistory) {
		this.boxBarcodeHistory = boxBarcodeHistory;
	}
	

	

	
	
	
   
	
}


