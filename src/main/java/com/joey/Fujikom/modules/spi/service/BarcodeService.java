package com.joey.Fujikom.modules.spi.service;

import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.service.BaseService;
import com.joey.Fujikom.modules.spi.dao.BarcodeDao;
import com.joey.Fujikom.modules.spi.dao.BarcodeRequestDao;
import com.joey.Fujikom.modules.spi.entity.Barcode;
import com.joey.Fujikom.modules.spi.entity.BarcodeRequest;
import com.joey.Fujikom.modules.spi.utils.BarcodeGen;

/**
 * 条形码Service
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class BarcodeService extends BaseService {

	@Autowired
	private BarcodeRequestDao barcodeRequestDao;
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	
	public BarcodeRequest get(Long barcodeRequestId) {
		return barcodeRequestDao.get(barcodeRequestId);
	}
	
	public Page<BarcodeRequest> find(Page<BarcodeRequest> page, BarcodeRequest barcodeRequest) {
		DetachedCriteria dc = barcodeRequestDao.createDetachedCriteria();
		/*if (StringUtils.isNotEmpty(orders.getOrdersStatus())){
			dc.add(Restrictions.eq("ordersStatus", orders.getOrdersStatus()));
		}
		if (orders.getMember()!=null){
			if (StringUtils.isNotEmpty(orders.getMember().getMemberRealName())) {
				DetachedCriteria dca = dc.createAlias("member","a");
				dca.add(Restrictions.like("a.memberRealName", "%"+orders.getMember().getMemberRealName()+"%"));
			}
		}*/
		dc.add(Restrictions.eq("delFlag","0"));
		dc.addOrder(Order.desc("barcodeRequestDate"));
		return barcodeRequestDao.find(page, dc);
	}
	
	public Page<BarcodeRequest> findReturnOrders(Page<BarcodeRequest> page, BarcodeRequest barcodeRequest) {
		DetachedCriteria dc = barcodeRequestDao.createDetachedCriteria();
		/*if (StringUtils.isNotEmpty(project.getName())){
			dc.add(Restrictions.like("name", "%"+project.getName()+"%"));
		}*/
		/*dc.add(Restrictions.eq("ordersFlag", AppProductService.ORDERS_FLAG_RETUEN));*/
		dc.addOrder(Order.desc("barcodeRequestDate"));
		return barcodeRequestDao.find(page, dc);
	}
	
	@Transactional(readOnly = false)
	public String generateBarcodeService(BarcodeRequest barcodeRequest,HttpServletResponse response){
		HashSet<String> barcodeset = new HashSet<>();
		String barcodes = "";
		while (barcodeset.size()< Integer.parseInt(barcodeRequest.getBarcodeNumber())) {		
			String barcodeString = BarcodeGen.get();
			List<String> listBarcodeStrings = barcodeDao.getBarcodeValue();
			if (!listBarcodeStrings.contains(barcodeString)) {
				barcodeset.add(barcodeString);
				//将新创建的条形码保存到数据库
				Barcode barcode = new Barcode();
				barcode.setBarcodeValue(barcodeString);
				barcodeDao.saveEntity(barcode);		
				barcodes = barcodes+barcodeString+",";
//			    ZxingHandler.encode(barcodeString, 200, 50, barcodeString+".png",response);
//				BarcodeUtil.downloadBarcode(barcodeString, response, barcodeString+".png");
			}
		}
		//barcodeRequestDao.deleteById(barcodeRequest.getBarcodeRequestId());
		return barcodes;
	}
	
	


	
	/*@Transactional(readOnly = false)
	public void save(Orders orders) {
		ordersDao.saveEntity(orders);
	}*/
	
	@Transactional(readOnly = false)
	public void delete(String id) {
		barcodeRequestDao.deleteById(Long.parseLong(id));
	}
	
}
