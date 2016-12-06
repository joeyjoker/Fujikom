package com.joey.Fujikom.modules.spi.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.joey.Fujikom.common.persistence.BaseDao;
import com.joey.Fujikom.common.persistence.Parameter;
import com.joey.Fujikom.modules.spi.entity.Product;
import com.joey.Fujikom.modules.spi.service.AppAdminService;

@Repository
public class AppProductDao  extends BaseDao<Product>{

	
	public List<String> getFacebookId(){
		return find("select faceBookId from Member");
	}
	
	public List<String> getTwitterId(){
		return find("select twitterId from Member");
	}
	
	/**
     * 获取发、退货商品列表
     * 
     * @param productNumber
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Product> findProducts(Long memberId,String del_flag,String product_status,String product_status2,Integer sinceId,Integer size){
		Query query = createQuery("from Product p where p.member.id = :p1 and p.delFlag = :p2 and (p.productStatus = :p3 or p.productStatus = :p4) order by p.productCreateDate desc,p.productId desc", new Parameter(memberId, del_flag,product_status,product_status2));
		query.setFirstResult(sinceId);
		query.setMaxResults(size);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> countProducts(Long memberId,String del_flag,String product_status,String product_status2){
		Query query  = createQuery("from Product p where p.member.id = :p1 and p.delFlag = :p2 and (p.productStatus = :p3 or p.productStatus = :p4) order by p.productCreateDate desc,p.productId desc", new Parameter(memberId, del_flag,product_status,product_status2));	
		return query.list();
	}
	
	/**
     * 获取商品列表
     * 
     * @param productNumber
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Product> findProcessingProductList(Long memberId,String del_flag,String product_status,Integer sinceId,Integer size){
		Query query = createQuery("from Product p where p.member.id = :p1 and p.delFlag = :p2 and p.productStatus = :p3 and p.orders.id is null order by p.productCreateDate desc,p.productId desc", new Parameter(memberId, del_flag,product_status));
		query.setFirstResult(sinceId);
		query.setMaxResults(size);
		return query.list();
	}
	
	/**
     * 获取仓库商品列表
     * 
     * @param productNumber
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Product> findProductList(Long memberId,String del_flag,String product_status1,String product_status2,String product_status3,Integer sinceId,Integer size){
		Query query = createQuery("from Product p where p.member.id = :p1 and p.delFlag = :p2 and (p.productStatus = :p3 or p.productStatus = :p4 or p.productStatus = :p5) order by p.productCreateDate desc,p.productId desc", new Parameter(memberId, del_flag,product_status1,product_status2,product_status3));
		query.setFirstResult(sinceId);
		query.setMaxResults(size);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Product> findAllProductList(Long memberId,String del_flag,String product_status1,String product_status2,String product_status3){
		Query query = createQuery("from Product p where p.member.id = :p1 and p.delFlag = :p2 and (p.productStatus = :p3 or p.productStatus = :p4 or p.productStatus = :p5) order by p.productCreateDate desc,p.productId desc", new Parameter(memberId, del_flag,product_status1,product_status2,product_status3));
		return query.list();
	}
	
	public Product getFromBarcode(String productBarcode){
		
		return getByHql("from Product where productBarcode = :p1", new Parameter(productBarcode));
	}
	
	public List<String> getProductBarcode(){
		return find("select productBarcode from Product");
	}
	
	public List<String> getBarcodeFromShelf(String productShelfNumber){
		return find("select productBarcode from Product where productShelfNumber = :p1 and productShelfStatus = :p2", new Parameter(productShelfNumber,AppAdminService.SHELF_STATUS_TMP));
	}
	
	public List<String> getBarcodeFromOrders(Long ordersId){
		return find("select productBarcode from Product p where p.orders.id = :p1",new Parameter(ordersId));
	}
	
	public List<Product> getFromBox(Long boxId){
		return find("from Product p where p.box.id = :p1",new Parameter(boxId));
	}
	
}
