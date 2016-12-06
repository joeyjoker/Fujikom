package com.joey.Fujikom.modules.spi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.service.BaseService;
import com.joey.Fujikom.common.utils.StringUtils;
import com.joey.Fujikom.modules.spi.dao.CsvHistoryDao;
import com.joey.Fujikom.modules.spi.dao.CsvRecordDao;
import com.joey.Fujikom.modules.spi.entity.CsvHistory;
import com.joey.Fujikom.modules.spi.entity.CsvRecord;
import com.joey.Fujikom.modules.spi.entity.Orders;

/**
 * csv记录Service
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class CsvRecordService extends BaseService {

	
	@Autowired
	private CsvRecordDao csvRecordDao;
	
	@Autowired
	private CsvHistoryDao csvHistoryDao;
	
	
	public CsvRecord get(Long csvId) {
		return csvRecordDao.get(csvId);
	}
	
	public Page<CsvRecord> find(Page<CsvRecord> page, CsvRecord csvRecord) {
		DetachedCriteria dc = csvRecordDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(csvRecord.getCsvFileName())){
			dc.add(Restrictions.eq("csvFileName", csvRecord.getCsvFileName()));
		}
		
		dc.addOrder(Order.desc("csvCreateDate"));
		return csvRecordDao.find(page, dc);
	}
	
	
	@Transactional(readOnly= false)
	public List<Orders> findCsvHistoryService(CsvRecord csvRecord){
		DetachedCriteria dc = csvHistoryDao.createDetachedCriteria();
		dc.add(Restrictions.eq("csvRecord", csvRecord));
		List<CsvHistory> list = csvHistoryDao.find(dc);
		
		 List<Orders> ordersList = list.parallelStream().map(p -> {
	            Orders data = new Orders();
	            data.setOrdersNumber(p.getOrdersNumber());
				data.setOrdersReceiverName(p.getOrdersReceiverName());
				data.setOrdersTelphone(p.getOrdersTelphone());
				data.setOrdersAddress(p.getOrdersAddress());
				data.setOrdersPrice(p.getOrdersPrice());
				data.setOrdersSenderName(p.getOrdersSenderName());
				data.setOrdersSenderTelphone(p.getOrdersSenderTelphone());
				data.setOrdersSenderAddress(p.getOrdersSenderAddress());
				data.setOrdersSenderZip(p.getOrdersSenderZip());
				data.setOrdersProductsNumber(p.getOrdersProductsNumber());
				data.setOrdersRequestDate(p.getOrdersRequestDate());
	            return data;
	        }).collect(Collectors.toList());

		return ordersList;
		
	
	}
	
	
}
