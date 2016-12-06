package com.joey.Fujikom.modules.spi.entity;

import javax.persistence.CascadeType;
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
 * 用户Entity
 * @author JoeyHuang
 * @version 2016-03-02
 */
@Entity
@Table(name = "Fujikom_member")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Member extends BaseEntity<Member> {
	
	private static final long serialVersionUID = 1L;
	private Long memberId;// 用户ID
	private String memberName;	// 用户登录名
    private String memberPassword;	// 用户登录密码
	private String memberZip;	// 用户邮编
	private String memberAddress;	// 用户地址
	private String memberTelphone;// 用户联系电话
	private String memberRealName;// 用户真实姓名
	private String faceBookId;	// 用户facebook登录Id
	private String twitterId;// 用户twitter登录Id
	private Warehouse warehouse; //仓库
	private String memberSendName;//寄送名
	private String memberBuliding;//建屋名
	private String memberCreditFlag;//信用卡绑定标记
	
	@Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "MEMBER_NAME")
	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Column(name = "MEMBER_PASSWORD")
	public String getMemberPassword() {
		return memberPassword;
	}

	public void setMemberPassword(String memberPassword) {
		this.memberPassword = memberPassword;
	}

	@Column(name = "MEMBER_ZIP")
	public String getMemberZip() {
		return memberZip;
	}

	public void setMemberZip(String memberZip) {
		this.memberZip = memberZip;
	}

	@Column(name = "MEMBER_ADDRESS")
	public String getMemberAddress() {
		return memberAddress;
	}

	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}

	@Column(name = "MEMBER_TELPHONE")
	public String getMemberTelphone() {
		return memberTelphone;
	}

	public void setMemberTelphone(String memberTelphone) {
		this.memberTelphone = memberTelphone;
	}

	@Column(name = "MEMBER_REAL_NAME")
	public String getMemberRealName() {
		return memberRealName;
	}

	public void setMemberRealName(String memberRealName) {
		this.memberRealName = memberRealName;
	}

	@Column(name = "FACEBOOK_ID")
	public String getFaceBookId() {
		return faceBookId;
	}

	public void setFaceBookId(String faceBookId) {
		this.faceBookId = faceBookId;
	}

	@Column(name = "TWITTER_ID")
	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE})
	@JoinColumn(name="WAREHOUSE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Column(name = "MEMBER_SEND_NAME")
	public String getMemberSendName() {
		return memberSendName;
	}

	public void setMemberSendName(String memberSendName) {
		this.memberSendName = memberSendName;
	}

	@Column(name = "MEMBER_BULIDING")
	public String getMemberBuliding() {
		return memberBuliding;
	}

	public void setMemberBuliding(String memberBuliding) {
		this.memberBuliding = memberBuliding;
	}

	@Column(name = "MEMBER_CREDIT_FLAG")
	public String getMemberCreditFlag() {
		return memberCreditFlag;
	}
	
	public void setMemberCreditFlag(String memberCreditFlag) {
		this.memberCreditFlag = memberCreditFlag;
	}

	
   
	
}


