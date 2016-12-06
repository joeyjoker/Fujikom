package com.joey.Fujikom.modules.spi.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.service.BaseService;
import com.joey.Fujikom.modules.spi.dao.MemberDao;
import com.joey.Fujikom.modules.spi.entity.Member;

/**
 * 用户Service
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Component
@Transactional(readOnly = true)
public class MemberService extends BaseService {

	@Autowired
	private MemberDao memberDao;
	
	
	public Member get(Long memberId) {
		return memberDao.get(memberId);
	}
	
	public Page<Member> find(Page<Member> page, Member member) {
		DetachedCriteria dc = memberDao.createDetachedCriteria();
		/*if (StringUtils.isNotEmpty(product.getProductStatus())){
			dc.add(Restrictions.eq("productStatus", product.getProductStatus()));
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
		}*/
		
		dc.addOrder(Order.asc("memberId"));
		return memberDao.find(page, dc);
	}
	
	
	

	

	
	@Transactional(readOnly = false)
	public void save(Member member) {
		memberDao.clear();
		memberDao.saveEntity(member);
	}
	
	/*@Transactional(readOnly = false)
	public void delete(String id) {
		ordersDao.deleteById(id);
	}*/
	
}
