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
 * FAQEntity
 * @author JoeyHuang
 * @version 2016-04-07
 */
@Entity
@Table(name = "Fujikom_faq")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Faq extends BaseEntity<Faq> {
	
	private static final long serialVersionUID = 1L;
	private Long faqId; //faqId
    private String faqContent; //faq内容
    private Member member; //用户
    private Date faqCreateDate;//faq创建时间
	
	
    @Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "FAQ_ID")
	public Long getFaqId() {
		return faqId;
	}
	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}
	
	@Column(name = "FAQ_CONTENT")
	public String getFaqContent() {
		return faqContent;
	}
	public void setFaqContent(String faqContent) {
		this.faqContent = faqContent;
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
	@Column(name = "FAQ_CREATE_DATE")
	public Date getFaqCreateDate() {
		return faqCreateDate;
	}
	public void setFaqCreateDate(Date faqCreateDate) {
		this.faqCreateDate = faqCreateDate;
	}
	
	
	
}


