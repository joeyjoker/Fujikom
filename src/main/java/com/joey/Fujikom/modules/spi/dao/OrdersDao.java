package com.joey.Fujikom.modules.spi.dao;

import org.springframework.stereotype.Repository;

import com.joey.Fujikom.common.persistence.BaseDao;
import com.joey.Fujikom.common.persistence.Parameter;
import com.joey.Fujikom.modules.spi.entity.Orders;

@Repository
public class OrdersDao  extends BaseDao<Orders>{

	
	public Orders getOrdersFromNumber(String ordersNumber){
		
		return getByHql("from Orders o where o.ordersNumber = :p1",new Parameter(ordersNumber));
	}
	


}
