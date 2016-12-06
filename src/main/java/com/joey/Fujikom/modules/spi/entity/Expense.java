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
 * 费用Entity
 * @author JoeyHuang
 * @version 2016-04-19
 */
@Entity
@Table(name = "Fujikom_expense")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Expense extends BaseEntity<Expense> {
	
	private static final long serialVersionUID = 1L;
	private Long expenseId;// 费用Id
    private String expenseSize;	// 费用size
    private String expenseEntrust;// 全权委托费
    private String expenseInstock;// 入库费
    private String expenseStorage;// 保管费
    private String expenseDelivery;// 配送费
    private String expenseReject; // 退货费
    private String expenseRejectHandling;//退货手续费
    
	
    @Id
	@GeneratedValue(generator = "identity")
	@GenericGenerator(name = "identity", strategy = "identity")
	@Column(name = "EXPENSE_ID")
	public Long getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}
	
	@Column(name = "EXPENSE_SIZE")
	public String getExpenseSize() {
		return expenseSize;
	}

	public void setExpenseSize(String expenseSize) {
		this.expenseSize = expenseSize;
	}
	@Column(name = "EXPENSE_ENTRUST")
	public String getExpenseEntrust() {
		return expenseEntrust;
	}
	public void setExpenseEntrust(String expenseEntrust) {
		this.expenseEntrust = expenseEntrust;
	}
	@Column(name = "EXPENSE_INSTOCK")
	public String getExpenseInstock() {
		return expenseInstock;
	}
	public void setExpenseInstock(String expenseInstock) {
		this.expenseInstock = expenseInstock;
	}
	
	@Column(name = "EXPENSE_STORAGE")
	public String getExpenseStorage() {
		return expenseStorage;
	}
	public void setExpenseStorage(String expenseStorage) {
		this.expenseStorage = expenseStorage;
	}
	@Column(name = "EXPENSE_DELIVERY")
	public String getExpenseDelivery() {
		return expenseDelivery;
	}
	public void setExpenseDelivery(String expenseDelivery) {
		this.expenseDelivery = expenseDelivery;
	}
	@Column(name = "EXPENSE_REJECT")
	public String getExpenseReject() {
		return expenseReject;
	}
	public void setExpenseReject(String expenseReject) {
		this.expenseReject = expenseReject;
	}
	@Column(name = "EXPENSE_REJECT_HANDLING")
	public String getExpenseRejectHandling() {
		return expenseRejectHandling;
	}
	public void setExpenseRejectHandling(String expenseRejectHandling) {
		this.expenseRejectHandling = expenseRejectHandling;
	}
	
	
	
	
}


