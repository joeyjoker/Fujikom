package com.joey.Fujikom.modules.spi.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.joey.Fujikom.common.persistence.BaseDao;
import com.joey.Fujikom.common.persistence.Parameter;
import com.joey.Fujikom.modules.spi.entity.Orders;

@Repository
public class AppOrdersDao  extends BaseDao<Orders>{

	
	@SuppressWarnings("unchecked")
	public List<Orders> findRejectOrders(String searchString,String ordersStatus,String ordersFlag,Integer sinceId,Integer size){
		Query query =  createQuery("from Orders o where (o.ordersSenderName like :p1 or o.ordersTelphone = :p2)and o.ordersStatus = :p3 and o.ordersFlag = :p4 order by o.ordersRequestDate desc", new Parameter("%"+searchString+"%",searchString,ordersStatus,ordersFlag));
		query.setFirstResult(sinceId);
		query.setMaxResults(size);
		return query.list();
	}
	
	
	


}
