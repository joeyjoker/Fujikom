package com.joey.Fujikom.modules.spi.request;


public class MemberRequestData {
	
	private String memberId;// 用户ID
	private String memberZip;	// 用户邮编
	private String memberAddress;	// 用户地址
	private String memberTelphone;// 用户联系电话
	private String memberRealName;// 用户真实姓名
	private String memberSendName;
	private String memberBuliding;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public String getMemberSendName() {
		return memberSendName;
	}
	public void setMemberSendName(String memberSendName) {
		this.memberSendName = memberSendName;
	}
	public String getMemberBuliding() {
		return memberBuliding;
	}
	public void setMemberBuliding(String memberBuliding) {
		this.memberBuliding = memberBuliding;
	}
	
	

}
