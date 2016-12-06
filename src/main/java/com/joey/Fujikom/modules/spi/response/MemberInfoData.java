package com.joey.Fujikom.modules.spi.response;

import org.springframework.stereotype.Component;

@Component
public class MemberInfoData {
	
	
	private Long memberId;// 用户ID
	private String memberName;	// 用户登录名
    private String memberPassword;	// 用户登录密码
	private String memberZip;	// 用户邮编
	private String memberAddress;	// 用户地址
	private String memberTelphone;// 用户联系电话
	private String memberRealName;// 用户真实姓名
	private String memberSendName;//用户配送姓名
	private String memberBuliding;
	private String warehouseAddress;
	private String warehouseZip;
	private String warehouseTelphone;
	private String warehouseName;
	private String memberCreditFlag;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	public String getMemberZip() {
		return memberZip;
	}

	public void setMemberZip(String memberZip) {
		this.memberZip = memberZip;
	}

	public String getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}

	public String getMemberTelphone() {
		return memberTelphone;
	}

	public void setMemberTelphone(String memberTelphone) {
		this.memberTelphone = memberTelphone;
	}

	public String getMemberRealName() {
		return memberRealName;
	}

	public void setMemberRealName(String memberRealName) {
		this.memberRealName = memberRealName;
	}

	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	public String getMemberSendName() {
		return memberSendName;
	}

	public void setMemberSendName(String memberSendName) {
		this.memberSendName = memberSendName;
	}

	public String getWarehouseZip() {
		return warehouseZip;
	}

	public void setWarehouseZip(String warehouseZip) {
		this.warehouseZip = warehouseZip;
	}

	public String getWarehouseTelphone() {
		return warehouseTelphone;
	}

	public void setWarehouseTelphone(String warehouseTelphone) {
		this.warehouseTelphone = warehouseTelphone;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getMemberBuliding() {
		return memberBuliding;
	}

	public void setMemberBuliding(String memberBuliding) {
		this.memberBuliding = memberBuliding;
	}

	public String getMemberCreditFlag() {
		return memberCreditFlag;
	}

	public void setMemberCreditFlag(String memberCreditFlag) {
		this.memberCreditFlag = memberCreditFlag;
	}

	
	
}
