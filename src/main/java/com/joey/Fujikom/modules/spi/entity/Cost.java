package com.joey.Fujikom.modules.spi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
 * 结算Entity
 * @author JoeyHuang
 * @version 2016-04-19
 */
@Entity
@Table(name = "Fujikom_cost")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cost extends BaseEntity<Cost> {
	
	private static final long serialVersionUID = 1L;
	private Long costId;// 结算Id
    private Member member;	// 用户
    private String costValue;// 费用值
    private String costType;// 结算类型
    private String costFlag;// 结算标记
    private Orders orders;
    
    @Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "COST_ID")
    public Long getCostId() {
		return costId;
	}
	public void setCostId(Long costId) {
		this.costId = costId;
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
	
	@Column(name = "COST_VALUE")
	public String getCostValue() {
		return costValue;
	}
	public void setCostValue(String costValue) {
		this.costValue = costValue;
	}
	@Column(name = "COST_TYPE")
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	@Column(name = "COST_FLAG")
	public String getCostFlag() {
		return costFlag;
	}
	public void setCostFlag(String costFlag) {
		this.costFlag = costFlag;
	}
	@OneToOne
    @JoinColumn(name="ORDERS_ID") 
	public Orders getOrders() {
		return orders;
	}
	public void setOrders(Orders orders) {
		this.orders = orders;
	}
	
	
	
	
	
	
}


