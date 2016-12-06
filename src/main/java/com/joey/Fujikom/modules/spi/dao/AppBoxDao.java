package com.joey.Fujikom.modules.spi.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.joey.Fujikom.common.persistence.BaseDao;
import com.joey.Fujikom.common.persistence.Parameter;
import com.joey.Fujikom.modules.spi.entity.Box;
import com.joey.Fujikom.modules.spi.response.PageEntity;
import com.joey.Fujikom.modules.spi.service.AppProductService;


@Repository
public class AppBoxDao extends BaseDao<Box> {

    @SuppressWarnings("unchecked")
    public List<Box> searchBoxes(String searchCondition, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where (b.member.memberRealName like :p1 or b.member.memberTelphone = :p2 or b.boxAdminNumber = :p3) and b.boxStatus = :p4 order by b.boxCreateDate desc", new Parameter("%" + searchCondition + "%", searchCondition, searchCondition, boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Box> findByRealName(String memberRealName, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.member.memberRealName like :p1 and b.boxStatus = :p2", new Parameter("%" + memberRealName + "%", boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Box> findByTelphone(String memberTelphone, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.member.memberTelphone = :p1 and b.boxStatus = :p2", new Parameter(memberTelphone, boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }


    @SuppressWarnings("unchecked")
    public List<Box> findByAdminNumber(String boxAdminNumber, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.boxAdminNumber = :p1 and b.boxStatus = :p2", new Parameter(boxAdminNumber, boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }


    @SuppressWarnings("unchecked")
    public List<Box> UncheckFindByRealName(String memberRealName, String boxStatus1, String boxStatus2, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.member.memberRealName like :p1 and (b.boxStatus = :p2 or b.boxStatus = :p3)", new Parameter("%" + memberRealName + "%", boxStatus1, boxStatus2));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Box> UncheckFindByTelphone(String memberTelphone, String boxStatus1, String boxStatus2, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.member.memberTelphone = :p1 and (b.boxStatus = :p2 or b.boxStatus = :p3)", new Parameter(memberTelphone, boxStatus1, boxStatus2));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Box> UncheckFindByAdminNumber(String boxAdminNumber, String boxStatus1, String boxStatus2, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.boxAdminNumber = :p1 and (b.boxStatus = :p2 or b.boxStatus = :p3)", new Parameter(boxAdminNumber, boxStatus1, boxStatus2));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Box> allFindByRealName(String memberRealName, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.member.memberRealName like :p1 and b.boxStatus != :p2 ", new Parameter("%" + memberRealName + "%", boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Box> allFindByTelphone(String memberTelphone, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.member.memberTelphone = :p1 and b.boxStatus != :p2 ", new Parameter(memberTelphone, boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Box> allFindByAdminNumber(String boxAdminNumber, String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.boxAdminNumber = :p1 and b.boxStatus != :p2 ", new Parameter(boxAdminNumber, boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Box> allFind(String boxStatus, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where  b.boxStatus != :p1 ", new Parameter(boxStatus));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();

    }

    @SuppressWarnings("unchecked")
    public List<Box> findUncheckBoxes(Long memberId, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.boxStatus = :p1 and b.member.memberId = :p2 and b.boxDelFlag = :p3 order by boxCreateDate desc", new Parameter(AppProductService.BOX_STATUS_UNCHECKED, memberId, AppProductService.PRODUCT_DEL_DEFAULT));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }


    @SuppressWarnings("unchecked")
    public List<Box> findEntrustBoxes(Long memberId, Integer sinceId, Integer size) {
        Query query = createQuery("from Box b where b.boxFlag = :p1 and b.boxDelFlag = :p2 and b.member.id = :p3 order by boxCreateDate desc", new Parameter(AppProductService.BOX_FLAG_ENTRUST, AppProductService.PRODUCT_DEL_DEFAULT, memberId));
        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
	public List<Box> countEntrustBoxes(Long memberId) {
        Query query = createQuery("from Box b where b.boxFlag = :p1 and b.boxDelFlag = :p2 and b.member.id = :p3 order by boxCreateDate desc", new Parameter(AppProductService.BOX_FLAG_ENTRUST, AppProductService.PRODUCT_DEL_DEFAULT, memberId));
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public PageEntity<Box> webFindUncheckBoxes(Long memberId, Integer sinceId, Integer size) {
        PageEntity<Box> pageEntity = new PageEntity<>();
        Query countQuery = createQuery("select count(*) from Box b where b.boxFlag = :p1 and b.boxDelFlag = :p2 and b.member.id = :p3 order by boxCreateDate desc", new Parameter(AppProductService.BOX_FLAG_ENTRUST, AppProductService.PRODUCT_DEL_DEFAULT, memberId));
        List<Object> list = countQuery.list();
        pageEntity.setMaxSize(Integer.valueOf(list.get(0).toString()));
        Query query = createQuery("from Box b where b.boxFlag = :p1 and b.boxDelFlag = :p2 and b.member.id = :p3 order by boxCreateDate desc", new Parameter(AppProductService.BOX_FLAG_ENTRUST, AppProductService.PRODUCT_DEL_DEFAULT, memberId));

        query.setFirstResult(sinceId);
        query.setMaxResults(size);
        pageEntity.setList(query.list());
        return pageEntity;
    }

    public long countUncheckBoxes(Long memberId) {
        DetachedCriteria dc = createDetachedCriteria();
        dc.add(Restrictions.eq("boxFlag", AppProductService.BOX_FLAG_ENTRUST));
        dc.add(Restrictions.eq("boxDelFlag", AppProductService.PRODUCT_DEL_DEFAULT));
        dc.createAlias("member", "member");
        dc.add(Restrictions.eq("member.id", memberId));
        return count(dc);
    }


    public List<Box> findUncheckBoxesForWeb(Long memberId, Integer startIndex, Integer pageSize) {
        DetachedCriteria dc = createDetachedCriteria();
        dc.add(Restrictions.eq("boxFlag", AppProductService.BOX_FLAG_ENTRUST));
        dc.add(Restrictions.eq("boxDelFlag", AppProductService.PRODUCT_DEL_DEFAULT));
        dc.createAlias("member", "member");
        dc.add(Restrictions.eq("member.id", memberId));

        return findWithLimit(dc, startIndex, pageSize);
    }

}
