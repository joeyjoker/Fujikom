package com.joey.Fujikom.modules.spi.service;

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
import com.joey.Fujikom.modules.spi.dao.ProductDao;
import com.joey.Fujikom.modules.spi.entity.Product;

/**
 * 商品Service
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class ProductService extends BaseService {

	@Autowired
	private ProductDao productDao;
	
	
	public Product get(Long productId) {
		return productDao.get(productId);
	}
	
	public Page<Product> find(Page<Product> page, Product product) {
		DetachedCriteria dc = productDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(product.getProductStatus())){
			if (product.getProductStatus().equals(AppProductService.PRODUCT_STATUS_PROCESSING)) {
				dc.add(Restrictions.or(Restrictions.eq("productStatus", AppProductService.PRODUCT_STATUS_PROCESSING), 
						Restrictions.eq("productStatus",  AppProductService.PRODUCT_STATUS_UNSEND)));
			}else {
				
				dc.add(Restrictions.eq("productStatus", product.getProductStatus()));
			}
		}
		if (product.getMember()!=null){
			if (StringUtils.isNotEmpty(product.getMember().getMemberRealName())) {
				DetachedCriteria dca = dc.createAlias("member","a");
				dca.add(Restrictions.like("a.memberRealName", "%"+product.getMember().getMemberRealName()+"%"));
			}
		}
		if (StringUtils.isNotEmpty(product.getProductBarcode())) {
			dc.add(Restrictions.like("productBarcode", product.getProductBarcode()));
		}
		if (StringUtils.isNotBlank(product.getProductName())) {
			dc.add(Restrictions.like("productName", "%"+product.getProductName()+"%"));
		}
		if (product.getOrders()!=null) {
			dc.add(Restrictions.eq("orders", product.getOrders()));
		}
		dc.addOrder(Order.desc("productCreateDate"));
		return productDao.find(page, dc);
	}
	
	public List<Product> findCsv(Product product){
		DetachedCriteria dc = productDao.createDetachedCriteria();
		
	    dc.add(Restrictions.eq("productStatus", AppProductService.PRODUCT_STATUS_INSTOCK));
		
		if (product.getMember()!=null){
			if (StringUtils.isNotEmpty(product.getMember().getMemberRealName())) {
				DetachedCriteria dca = dc.createAlias("member","a");
				dca.add(Restrictions.like("a.memberRealName", "%"+product.getMember().getMemberRealName()+"%"));
			}
		}
		if (StringUtils.isNotEmpty(product.getProductBarcode())) {
			dc.add(Restrictions.like("productBarcode", product.getProductBarcode()));
		}
		if (StringUtils.isNotBlank(product.getProductName())) {
			dc.add(Restrictions.like("productName", "%"+product.getProductName()+"%"));
		}
		dc.addOrder(Order.asc("productId"));
		return productDao.find(dc);
		
	}
	

	

	
	@Transactional(readOnly = false)
	public void save(Product product) {
		productDao.saveEntity(product);
	}
	
	/*@Transactional(readOnly = false)
	public void delete(String id) {
		ordersDao.deleteById(id);
	}*/
	
}
