package com.joey.Fujikom.modules.spi.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.service.BaseService;
import com.joey.Fujikom.modules.spi.dao.ExpenseDao;
import com.joey.Fujikom.modules.spi.entity.Expense;

/**
 * 费用Service
 * @author JoeyHuang
 * @version 2016-04-19
 */
@Component
@Transactional(readOnly = true)
public class ExpenseService extends BaseService {

	@Autowired
	private ExpenseDao expenseDao;
	
		
	public Expense get(Long expenseId) {
		return expenseDao.get(expenseId);
	}
	
	public Page<Expense> find(Page<Expense> page, Expense expense) {
		DetachedCriteria dc = expenseDao.createDetachedCriteria();
		/*if (StringUtils.isNotEmpty(orders.getOrdersStatus())){
			dc.add(Restrictions.eq("ordersStatus", orders.getOrdersStatus()));
		}*/
	
		/*dc.add(Restrictions.eq("ordersFlag", AppProductService.ORDERS_FLAG_SEND));*/
		dc.addOrder(Order.asc("expenseId"));
		return expenseDao.find(page, dc);
	}
	
	
	

	
	@Transactional(readOnly = false)
	public void save(Expense expense) {
		expenseDao.saveEntity(expense);
	}
	
	
	
	/*@Transactional(readOnly = false)
	public void delete(String id) {
		ordersDao.deleteById(id);
	}*/
	
}
