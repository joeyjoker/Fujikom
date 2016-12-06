package com.joey.Fujikom.modules.spi.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.config.Global;
import com.joey.Fujikom.common.mapper.BeanMapper;
import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.dao.AppFaqDao;
import com.joey.Fujikom.modules.spi.dao.AppMemberDao;
import com.joey.Fujikom.modules.spi.dao.WarehouseDao;
import com.joey.Fujikom.modules.spi.entity.Faq;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.entity.Warehouse;
import com.joey.Fujikom.modules.spi.request.MemberRequestData;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.response.MemberInfoData;
import com.joey.Fujikom.modules.spi.response.MemberResponseData;
import com.joey.Fujikom.modules.spi.response.ThirdLoginResponse;
import com.joey.Fujikom.modules.spi.utils.EmailUtil;
import com.joey.Fujikom.modules.spi.utils.SendEmailService;

/**
 * AppMemberService
 * 
 * @author JoeyHuang
 * @version 2016-3-04
 */
@Service
@Transactional()
public class AppMemberService {

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

	public static String MEMBER_CREDIT_DEFAULT = "1";// 未绑定
	public static String MEMBER_CREDIT_DONE = "0"; // 已绑定

	/**
	 * App用户登录
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse memberLogin(String memberName, String memberPassword) {

		Member member = appMemberDao.findByLoginName(memberName);
		if (member == null) {
			return new BaseResponse(false, "ユーザー名またはパスワードが間違っています");
		} else if (!member.getMemberPassword().equals(memberPassword)) {
			return new BaseResponse(false, "ユーザー名またはパスワードが間違っています");
		} else {
			memberResponseData.setMemberId(member.getMemberId());
			return new BaseResponse(true, memberResponseData, "success");
		}

	}

	/**
	 * App用户找回密码
	 * 
	 * @param memberName
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse findPsw(String memberName) {

		if (EmailUtil.isEmail(memberName)) {

			DetachedCriteria dc = appMemberDao.createDetachedCriteria();
			dc.add(Restrictions.eq("memberName", memberName));
			Member member = appMemberDao.findOne(dc);
			if (member != null) {

				String title = "Fujikom：IDとPWをお送りします。";
				String content = "下記が設定されたIDとPWです。" + "<br>" + "ユーザー名："
						+ member.getMemberName() + "<br>" + "パスワード："
						+ member.getMemberPassword();
				String emailFrom = Global.getMailFrom();
				SendEmailService.sendEmailByMemberEmail(memberName, emailFrom,
						title, content);
				return new BaseResponse(true, "success");
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.MEMBER_NOT_EXIST,
						ResultConstant.MEMBER_NOT_EXIST_MSG));

			}
		} else {
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.EMAIL_ERROR, ResultConstant.EMAIL_ERROR_MSG));
		}
	}

	/**
	 * App用户第三方登录
	 * 
	 * @param thirdId
	 * @param category
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse thirdLogin(String thirdId, String category) {

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

		return new BaseResponse(true, data, "success");
		/*
		 * Member member = new Member(); switch (category) { case "facebook":
		 * thirdIdList = appMemberDao.getFacebookId(); if
		 * (!thirdIdList.contains(thirdId)) { member.setMemberName(thirdId);
		 * member.setFaceBookId(thirdId); appMemberDao.saveEntity(member); }
		 * break;
		 * 
		 * case "twitter": thirdIdList = appMemberDao.getTwitterId(); if
		 * (!thirdIdList.contains(thirdId)) { member.setMemberName(thirdId);
		 * member.setTwitterId(thirdId); appMemberDao.saveEntity(member);
		 * 
		 * } break;
		 * 
		 * } DetachedCriteria dc = appMemberDao.createDetachedCriteria();
		 * dc.add(Restrictions.eq("memberName", thirdId)); Member m =
		 * appMemberDao.findOne(dc);
		 * 
		 * memberResponseData.setMemberId(m.getMemberId()); return new
		 * BaseResponse(true, memberResponseData, "success");
		 */
	}

	/**
	 * App用户第三方注册
	 * 
	 * @param thirdId
	 * @param category
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse thirdRegister(String thirdId, String category,
			Member member) {

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
			member.setMemberCreditFlag(MEMBER_CREDIT_DEFAULT);
			appMemberDao.saveEntity(member);
			memberResponseData.setMemberId(member.getMemberId());
			return new BaseResponse(true, memberResponseData, "success");
		} else {
			return new BaseResponse(false, "ユーザー名はすでに使用されている");
		}

	}

	@Transactional(readOnly = false)
	public BaseResponse memberRegister(Member member) {
		// 给用户一个仓库初值
		List<Warehouse> waList = warehousedao.findAll();
		if (waList.size() > 0) {
			member.setWarehouse(waList.get(0));
		}

		Member m = appMemberDao.findByLoginName(member.getMemberName());
		if (m == null) {
			member.setMemberCreditFlag(MEMBER_CREDIT_DEFAULT);
			appMemberDao.saveEntity(member);
			memberResponseData.setMemberId(member.getMemberId());
			return new BaseResponse(true, memberResponseData, "success");
		} else {
			return new BaseResponse(false, "ユーザー名はすでに使用されている");
		}

	}
	
	/**
	 * App用户信用卡绑定接口
	 * 
	 * @param thirdId
	 * @param category
	 * @return
	 */
	@Transactional(readOnly = false)
	public void memberCreditService(Long memberId){
		
		Member member = appMemberDao.get(memberId);
		member.setMemberCreditFlag(MEMBER_CREDIT_DONE);
		appMemberDao.saveEntity(member);
	}

	@Transactional(readOnly = false)
	public BaseResponse getMemberInfo(Long memberId) {
		Member member = appMemberDao.get(memberId);
		if (member != null) {
			BeanMapper.copy(member, memberInfoData);
			memberInfoData.setWarehouseAddress(member.getWarehouse()
					.getWarehouseAddress());
			memberInfoData.setWarehouseZip(member.getWarehouse()
					.getWarehouseZip());
			memberInfoData.setWarehouseTelphone(member.getWarehouse()
					.getWarehouseTelphone());
			memberInfoData.setWarehouseName(member.getWarehouse()
					.getWarehouseName());
			return new BaseResponse(true, memberInfoData, "success");
		} else {
			return new BaseResponse(false, "ユーザーは存在しない");
		}

	}

	@Transactional(readOnly = false)
	public BaseResponse editMemberInfo(MemberRequestData memberRequestData) {
		Member member = appMemberDao.get(Long.parseLong(memberRequestData
				.getMemberId()));

		member.setMemberRealName(memberRequestData.getMemberRealName());
		member.setMemberSendName(memberRequestData.getMemberSendName());
		member.setMemberTelphone(memberRequestData.getMemberTelphone());
		member.setMemberZip(memberRequestData.getMemberZip());
		member.setMemberBuliding(memberRequestData.getMemberBuliding());
		appMemberDao.saveEntity(member);
		memberResponseData.setMemberId(member.getMemberId());
		return new BaseResponse(true, memberResponseData, "success");

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
