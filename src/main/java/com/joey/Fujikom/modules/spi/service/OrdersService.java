package com.joey.Fujikom.modules.spi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.service.BaseService;
import com.joey.Fujikom.common.utils.StringUtils;
import com.joey.Fujikom.modules.spi.dao.AppBoxDao;
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.dao.CsvHistoryDao;
import com.joey.Fujikom.modules.spi.dao.CsvRecordDao;
import com.joey.Fujikom.modules.spi.dao.OrdersDao;
import com.joey.Fujikom.modules.spi.entity.Box;
import com.joey.Fujikom.modules.spi.entity.CsvHistory;
import com.joey.Fujikom.modules.spi.entity.CsvRecord;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.entity.Product;

/**
 * 订单Service
 * 
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class OrdersService extends BaseService {

	@Autowired
	private OrdersDao ordersDao;

	@Autowired
	private AppProductDao productDao;
	
	@Autowired
	private AppBoxDao boxDao;
	
	@Autowired
	private CsvHistoryDao csvHistoryDao;
	
	@Autowired
	private CsvRecordDao csvRecordDao;
	
	private static String ACCOUNT_STATUS_FINISHED = "0";

	public Orders get(Long ordersId) {
		return ordersDao.get(ordersId);
	}

	public Page<Orders> findSendOrders(Page<Orders> page, Orders orders) {
		DetachedCriteria dc = ordersDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(orders.getOrdersStatus())) {
			dc.add(Restrictions.eq("ordersStatus", orders.getOrdersStatus()));
		}
		if (orders.getMember() != null) {
			if (StringUtils.isNotEmpty(orders.getMember().getMemberRealName())) {
				DetachedCriteria dca = dc.createAlias("member", "a");
				dca.add(Restrictions.like("a.memberRealName", "%"
						+ orders.getMember().getMemberRealName() + "%"));
			}
		}
		dc.add(Restrictions
				.eq("ordersFlag", AppProductService.ORDERS_FLAG_SEND));
		dc.addOrder(Order.desc("ordersUpdateDate"));
		return ordersDao.find(page, dc);
	}

	public Page<Orders> findReturnOrders(Page<Orders> page, Orders orders) {
		DetachedCriteria dc = ordersDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(orders.getOrdersStatus())) {
			dc.add(Restrictions.eq("ordersStatus", orders.getOrdersStatus()));
		}
		if (orders.getMember() != null) {
			if (StringUtils.isNotEmpty(orders.getMember().getMemberRealName())) {
				DetachedCriteria dca = dc.createAlias("member", "a");
				dca.add(Restrictions.like("a.memberRealName", "%"
						+ orders.getMember().getMemberRealName() + "%"));
			}
		}
		dc.add(Restrictions.eq("ordersFlag",
				AppProductService.ORDERS_FLAG_RETUEN));
		dc.addOrder(Order.desc("ordersUpdateDate"));
		return ordersDao.find(page, dc);
	}

	public Page<Orders> findRejectOrders(Page<Orders> page, Orders orders) {
		DetachedCriteria dc = ordersDao.createDetachedCriteria();
		
		 if (StringUtils.isNotEmpty(orders.getOrdersStatus())){
			 dc.add(Restrictions.eq("ordersStatus", orders.getOrdersStatus())); }
		 /*if (orders.getMember()!=null){ if
		 * (StringUtils.isNotEmpty(orders.getMember().getMemberRealName())) {
		 * DetachedCriteria dca = dc.createAlias("member","a");
		 * dca.add(Restrictions.like("a.memberRealName",
		 * "%"+orders.getMember().getMemberRealName()+"%")); } }
		 */
		dc.add(Restrictions.eq("ordersFlag",
				AppProductService.ORDERS_FLAG_REJECT));
		dc.addOrder(Order.desc("ordersUpdateDate"));
		return ordersDao.find(page, dc);
	}

	@Transactional(readOnly = false)
	public void save(Orders orders) {
		orders.setOrdersUpdateDate(new Date());
		ordersDao.saveEntity(orders);
	}

	@Transactional(readOnly = false)
	public void deliveryService(Orders orders, String productStatus) {

		List<Product> products = new ArrayList<Product>();
		DetachedCriteria dc = productDao.createDetachedCriteria();
		dc.add(Restrictions.eq("orders", orders));
		products = productDao.find(dc);
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			product.setProductStatus(productStatus);
			product.setProductDeliveryDate(new Date());
			productDao.saveEntity(product);

		}
		orders.setOrdersDeliveryDate(new Date());
		orders.setOrdersUpdateDate(new Date());
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_SEND);
		this.save(orders);
	}

	@Transactional(readOnly = false)
	public void accountService(Orders orders) {

		orders.setOrdersAccountFlag(ACCOUNT_STATUS_FINISHED);
		ordersDao.saveEntity(orders);
	}

	public List<Orders> UnsendOrdersService() {

		DetachedCriteria dc = ordersDao.createDetachedCriteria();
		dc.add(Restrictions
				.eq("ordersFlag", AppProductService.ORDERS_FLAG_SEND));
		dc.add(Restrictions.eq("ordersExportFlag", AppProductService.ORDERS_EXPORT_DEFAULT));
		dc.add(Restrictions.eq("ordersStatus",
				AppProductService.ORDERS_STATUS_DEFAULT));
		List<Orders> ordersList = ordersDao.find(dc);
		return ordersList;

	}

	public List<Orders> UnreturnOrdersService() {

		DetachedCriteria dc = ordersDao.createDetachedCriteria();
		dc.add(Restrictions.eq("ordersFlag",
				AppProductService.ORDERS_FLAG_RETUEN));
		dc.add(Restrictions.eq("ordersExportFlag", AppProductService.ORDERS_EXPORT_DEFAULT));
		dc.add(Restrictions.eq("ordersStatus",
				AppProductService.ORDERS_STATUS_DEFAULT));
		List<Orders> ordersList = ordersDao.find(dc);
		return ordersList;

	}

	@Transactional(readOnly = false)
	public void csvDeliveryService(Orders orders, String[] data) {

		List<Product> products = new ArrayList<Product>();

		DetachedCriteria dc = productDao.createDetachedCriteria();
		dc.add(Restrictions.eq("orders", orders));
		products = productDao.find(dc);
		for (int j = 0; j < products.size(); j++) {
			Product product = products.get(j);
			product.setProductStatus(AppProductService.PRODUCT_STATUS_SEND);
			product.setProductDeliveryDate(new Date());

		}
		orders.setOrdersDeliveryDate(new Date());
		orders.setOrdersUpdateDate(new Date());
		orders.setOrdersCompany(data[1]);
		orders.setOrdersConsultNumber(data[2]);
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_SEND);
		ordersDao.saveEntity(orders);

	}
	
	
	/**
     * 管理页面处理返品请求
     * 
     * @param orders
     * @return
     */
	@Transactional(readOnly = false)
	public void rejectProcessService(Orders orders){
		
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_SEND);
		ordersDao.saveEntity(orders);
		List<Product> products = new ArrayList<>();
		DetachedCriteria dc = productDao.createDetachedCriteria();
		dc.add(Restrictions.eq("orders", orders));
		products = productDao.find(dc);
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			Orders orders2 = new Orders();
			product.setProductStatus(AppProductService.PRODUCT_STATUS_PROCESSING);
			product.setOrders(orders2);
			product.setProductShelfNumber(null);
			product.setProductShelfStatus(null);
			productDao.saveEntity(product);

		}
		
		for (int i = 0; i < products.size(); i++) {
			if (!products.get(i).getBox().getBoxStatus().equals(AppProductService.BOX_STATUS_CHECKED)) {
				Box box = products.get(i).getBox();
				List<Product> pList = new ArrayList<>();
				DetachedCriteria dc2 = productDao.createDetachedCriteria();
				dc2.add(Restrictions.eq("box", box));
				pList = productDao.find(dc2);
				List<String> flagList = new ArrayList<>();
				for (int j = 0; j < pList.size(); j++) {
					if (pList.get(i).getProductStatus().equals(AppProductService.PRODUCT_STATUS_PROCESSING)) {
						flagList.add("true");
						
					}else {
						flagList.add("false");
					}
				}
				
				if (!flagList.contains("false")) {
					box.setBoxStatus(AppProductService.BOX_STATUS_CHECKED);
					boxDao.saveEntity(box);
				}
			}
		}
	}
	
	
	@Transactional(readOnly = false)
	public void csvExportFlagService(List<Orders> ordersList){
	
		//更改导出状态
		for (Orders  o : ordersList) {
			o.setOrdersExportFlag(AppProductService.ORDERS_EXPORT_DONE);
			ordersDao.saveEntity(o);
		}
		
	
	}
	
	@Transactional(readOnly = false)
	public CsvRecord csvExportRecordService(String fileName){
	

		CsvRecord csvRecord = new CsvRecord();
		csvRecord.setCsvFileName(fileName);
		csvRecord.setCsvCreateDate(new Date());
        csvRecordDao.saveEntity(csvRecord);
		return csvRecord;
	
	}
	
	@Transactional(readOnly = false)
	public void csvExportHistoryService(CsvRecord csvRecord,List<Orders> ordersList){
	

        for (Orders o : ordersList) {
			CsvHistory csvHistory = new CsvHistory();
			csvHistory.setOrdersNumber(o.getOrdersNumber());
			csvHistory.setOrdersReceiverName(o.getOrdersReceiverName());
			csvHistory.setOrdersTelphone(o.getOrdersTelphone());
			csvHistory.setOrdersAddress(o.getOrdersAddress());
			csvHistory.setOrdersPrice(o.getOrdersPrice());
			csvHistory.setOrdersSenderName(o.getOrdersSenderName());
			csvHistory.setOrdersSenderTelphone(o.getOrdersSenderTelphone());
			csvHistory.setOrdersSenderAddress(o.getOrdersSenderAddress());
			csvHistory.setOrdersSenderZip(o.getOrdersSenderZip());
			csvHistory.setOrdersProductsNumber(o.getOrdersProductsNumber());
			csvHistory.setOrdersRequestDate(o.getOrdersRequestDate());
			csvHistory.setCsvRecord(csvRecord);
			csvHistoryDao.saveEntity(csvHistory);
		}
		
	
	}
	/*
	 * @Transactional(readOnly = false) public void delete(String id) {
	 * ordersDao.deleteById(id); }
	 */

}
