package com.joey.Fujikom.modules.spi.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.mapper.BeanMapper;
import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.dao.AppFaqDao;
import com.joey.Fujikom.modules.spi.dao.AppMemberDao;
import com.joey.Fujikom.modules.spi.dao.WarehouseDao;
import com.joey.Fujikom.modules.spi.entity.Faq;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.entity.Warehouse;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.response.MemberInfoData;
import com.joey.Fujikom.modules.spi.response.MemberResponseData;
import com.joey.Fujikom.modules.spi.response.ThirdLoginResponse;


/**
 * AppMemberService
 *
 * @author JoeyHuang
 * @version 2016-3-04
 */
@Service
@Transactional()
public class WebMemberService {

    @Autowired
    private AppMemberDao appMemberDao;

    @Autowired
    private MemberResponseData memberResponseData;

    @Autowired
    private MemberInfoData memberInfoData;

    @Autowired
    private WarehouseDao warehousedao;

    @Autowired
    private AppFaqDao appFaqDao;

    /**
     * web用户登录
     *
     * @param memberName
     * @param memberPassword
     * @return
     */
    @Transactional(readOnly = false)
    public BaseResponse memberLogin(String memberName, String memberPassword, HttpServletRequest request) {

        Member member = appMemberDao.findByLoginName(memberName);
        if (member == null) {

            return new BaseResponse(false, new ErrorResponse(
                    ResultConstant.MEMBER_NOT_EXIST,
                    ResultConstant.MEMBER_NOT_EXIST_MSG));
        } else if (!member.getMemberPassword().equals(memberPassword)) {
            return new BaseResponse(false, new ErrorResponse(
                    ResultConstant.PASSWORD_ERROR,
                    ResultConstant.PASSWORD_ERROR_MSG));
        } else {
            String sessionId = request.getSession().getId();
            memberResponseData.setMemberId(member.getMemberId());
            memberResponseData.setSessionId(sessionId);
            request.getSession().setAttribute("memberId", member.getMemberId());
            return new BaseResponse(true, memberResponseData, "success");
        }

    }

    /**
     * web用户第三方登录
     *
     * @param thirdId
     * @param category
     * @return
     */
    @Transactional(readOnly = false)
    public BaseResponse thirdLogin(String thirdId, String category,HttpServletRequest request) {

    	ThirdLoginResponse data = new ThirdLoginResponse();
		Member m = new Member();
		DetachedCriteria dc = appMemberDao.createDetachedCriteria();
		switch (category) {
		case "facebook":

			dc = appMemberDao.createDetachedCriteria();
			dc.add(Restrictions.eq("faceBookId", thirdId));
			m = appMemberDao.findOne(dc);

			break;

		case "twitter":

			dc = appMemberDao.createDetachedCriteria();
			dc.add(Restrictions.eq("twitterId", thirdId));
			m = appMemberDao.findOne(dc);

			break;

		}

		if (m != null) {
			data.setLoginFlag("0"); // 第三方账号已存在
			data.setMemberId(m.getMemberId());
		} else {
			data.setLoginFlag("1"); // 第三方账号不存在
		}

		String sessionId = request.getSession().getId();
		data.setSessionId(sessionId);
		request.getSession().setAttribute("memberId", m.getMemberId());
		data.setMemberId(m.getMemberId());
		
		return new BaseResponse(true, data, "success");
        
    }
    
    /**
	 * web用户第三方注册
	 * 
	 * @param thirdId
	 * @param category
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse thirdRegister(String thirdId, String category,
			Member member,HttpServletRequest request) {

		Member m = appMemberDao.findByLoginName(thirdId);
		if (m == null) {

			List<Warehouse> waList = warehousedao.findAll();
			if (waList.size() > 0) {
				member.setWarehouse(waList.get(0));
			}

			switch (category) {
			case "facebook":

				member.setFaceBookId(thirdId);
				break;

			case "twitter":

				member.setTwitterId(thirdId);
				break;

			}
			member.setMemberCreditFlag(AppMemberService.MEMBER_CREDIT_DEFAULT);
			appMemberDao.saveEntity(member);
			String sessionId = request.getSession().getId();
			memberResponseData.setMemberId(member.getMemberId());
			memberResponseData.setSessionId(sessionId);
			return new BaseResponse(true, memberResponseData, "success");
		} else {
			return new BaseResponse(false, "ユーザー名はすでに使用されている");
		}

	}

    /**
     * web用户注册
     *
     * @param member
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    public BaseResponse memberRegister(Member member,HttpServletRequest request) {

        Member m = appMemberDao.findByLoginName(member.getMemberName());
        if (m == null) {
        	//给用户一个仓库初值
        	List<Warehouse> waList = warehousedao.findAll();
        	if (waList.size() > 0) {
        		member.setWarehouse(waList.get(0));
        	}
        	member.setMemberCreditFlag(AppMemberService.MEMBER_CREDIT_DEFAULT);
            appMemberDao.saveEntity(member);
            
            String sessionId = request.getSession().getId();
            memberResponseData.setSessionId(sessionId);
            memberResponseData.setMemberId(member.getMemberId());
            request.getSession().setAttribute("memberId", member.getMemberId());
            return new BaseResponse(true, memberResponseData, "success");
        } else {
            return new BaseResponse(false, new ErrorResponse(ResultConstant.MEMBER_NAME_EXIST,ResultConstant.MEMBER_NAME_EXIST_MSG));
        }


    }

    /**
     * web获取用户信息
     *
     * @param memberId
     * @param request
     * @return
     */
    @Transactional(readOnly = false)
    public BaseResponse getMemberInfo(Long memberId) {
        Member member = appMemberDao.get(memberId);
        if (member != null) {
            BeanMapper.copy(member, memberInfoData);
            memberInfoData.setWarehouseAddress(member.getWarehouse().getWarehouseAddress());
            memberInfoData.setWarehouseZip(member.getWarehouse().getWarehouseZip());
            memberInfoData.setWarehouseTelphone(member.getWarehouse().getWarehouseTelphone());
            memberInfoData.setWarehouseName(member.getWarehouse().getWarehouseName());
            return new BaseResponse(true, memberInfoData, "success");
        } else {
            return new BaseResponse(false, new ErrorResponse(ResultConstant.MEMBER_NOT_EXIST, ResultConstant.MEMBER_NOT_EXIST_MSG));
        }

    }

    @Transactional(readOnly = false)
    public BaseResponse editMemberInfo(Long memberId,String memberZip,String memberTelphone,String memberAddress,
    		             String memberSendName,String memberRealName,String memberBuliding) {
        Member member = appMemberDao.get(memberId);
        member.setMemberRealName(memberRealName);
        member.setMemberSendName(memberSendName);
        member.setMemberTelphone(memberTelphone);
        member.setMemberZip(memberZip);
        member.setMemberBuliding(memberBuliding);
        appMemberDao.saveEntity(member);
        return new BaseResponse(true,"success");


    }

    @Transactional(readOnly = false)
    public BaseResponse insertFaqService(String memberId, String faqContent) {
        Faq faq = new Faq();
        Member member = appMemberDao.get(Long.parseLong(memberId));
        faq.setMember(member);
        faq.setFaqContent(faqContent);
        faq.setFaqCreateDate(new Date());
        appFaqDao.saveEntity(faq);
        return new BaseResponse(true, "success");
    }


}
