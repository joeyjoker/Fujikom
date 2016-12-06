package com.joey.Fujikom.modules.spi.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.service.BaseService;
import com.joey.Fujikom.modules.spi.dao.WarehouseDao;
import com.joey.Fujikom.modules.spi.entity.Warehouse;

/**
 * 仓库Service
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class WarehouseService extends BaseService {

	@Autowired
	private WarehouseDao warehouseDao;
	
		
	public Warehouse get(Long warehouseId) {
		return warehouseDao.get(warehouseId);
	}
	
	public Page<Warehouse> find(Page<Warehouse> page, Warehouse warehouse) {
		DetachedCriteria dc = warehouseDao.createDetachedCriteria();
		/*if (StringUtils.isNotEmpty(orders.getOrdersStatus())){
			dc.add(Restrictions.eq("ordersStatus", orders.getOrdersStatus()));
		}*/
	
		/*dc.add(Restrictions.eq("ordersFlag", AppProductService.ORDERS_FLAG_SEND));*/
		dc.addOrder(Order.asc("warehouseId"));
		return warehouseDao.find(page, dc);
	}
	
	
	

	
	@Transactional(readOnly = false)
	public void save(Warehouse warehouse) {
		warehouseDao.saveEntity(warehouse);
	}
	
	
	
	/*@Transactional(readOnly = false)
	public void delete(String id) {
		ordersDao.deleteById(id);
	}*/
	
}
