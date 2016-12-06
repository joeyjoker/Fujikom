package com.joey.Fujikom.modules.spi.app;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.service.WebMemberService;

/**
 * AppMemberController
 *
 * @author JoeyHuang
 * @version 2016-3-3
 */
@Controller
@RequestMapping(value = "/spi/web/member")
public class WebMemberController {

	private static Logger logger = LoggerFactory
			.getLogger(WebMemberController.class);

	@Autowired
	private WebMemberService webMemberService;

	/**
	 * web用户登录接口
	 * 
	 * @param thirdId
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "memberlogin", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse memberLogin(
			@RequestParam("memberName") String memberName,
			@RequestParam("memberPassword") String memberPassword,
			HttpServletRequest request) {

		try {

			return webMemberService.memberLogin(memberName, memberPassword,
					request);
		} catch (Exception e) {
			logger.error("AppMemberController.memberLogin error", e);
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.SYSTEM_ERROR,
					ResultConstant.SYSTEM_ERROR_MSG));
		}
	}

	/**
	 * web用户第三方登录接口
	 * 
	 * @param thirdId
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "thirdlogin", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse thirdLogin(@RequestParam("thirdId") String thirdId,
			@RequestParam("category") String category,
			HttpServletRequest request) {
		try {

			return webMemberService.thirdLogin(thirdId, category, request);

		} catch (Exception e) {

			logger.error("Member thirdLogin Error", e);
			return new BaseResponse(false,new ErrorResponse(
					ResultConstant.SYSTEM_ERROR,
					ResultConstant.SYSTEM_ERROR_MSG));
		}

	}
	

	/**
     * web用户第三方注册接口
     * 
     * @param memberName
     * @param memberPassword
     * @return
     */
	@RequestMapping(value = "thirdregister", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse loginRegister(@RequestParam("thirdId") String thirdId,
			@RequestParam("category") String category,
			@RequestParam("memberZip") String memberZip,
			@RequestParam("memberAddress") String memberAddress,
			@RequestParam("memberTelphone") String memberTelphone,
			@RequestParam("memberSendName") String memberSendName,
			@RequestParam("memberRealName") String memberRealName,
			@RequestParam("memberBuliding") String memberBuliding,HttpServletRequest request) {
		try {
			Member member = new Member();
			member.setMemberName(thirdId);
			member.setMemberZip(memberZip);
			member.setMemberAddress(memberAddress);
			member.setMemberTelphone(memberTelphone);
			member.setMemberSendName(memberSendName);
			member.setMemberRealName(memberRealName);
			member.setMemberBuliding(memberBuliding);
			return webMemberService.thirdRegister(thirdId, category, member,request);
			
		} catch (Exception e) {
			logger.error("Member third Register Error ", e);
			return new BaseResponse(false);
		}

	}

	/**
	 * web用户注册接口
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse register(@RequestParam("memberName") String memberName,
			@RequestParam("memberPassword") String memberPassword,
			@RequestParam("memberZip") String memberZip,
			@RequestParam("memberAddress") String memberAddress,
			@RequestParam("memberTelphone") String memberTelphone,
			@RequestParam("memberSendName") String memberSendName,
			@RequestParam("memberRealName") String memberRealName,
			@RequestParam("memberBuliding") String memberBuliding,
			HttpServletRequest request) {
		try {
			Member member = new Member();
			member.setMemberName(memberName);
			member.setMemberPassword(memberPassword);
			member.setMemberZip(memberZip);
			member.setMemberAddress(memberAddress);
			member.setMemberTelphone(memberTelphone);
			member.setMemberSendName(memberSendName);
			member.setMemberRealName(memberRealName);
			member.setMemberBuliding(memberBuliding);
			return webMemberService.memberRegister(member, request);

		} catch (Exception e) {
			logger.error("Member Register Error ", e);
			return new BaseResponse(false,new ErrorResponse(
					ResultConstant.SYSTEM_ERROR,
					ResultConstant.SYSTEM_ERROR_MSG));
		}

	}

	/**
	 * web获取用户信息
	 * 
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "getmemberinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getMemberInfo(HttpServletRequest request) {
		try {
			if (request.getSession() != null) {
				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {

					return webMemberService.getMemberInfo(memberId);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
				
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}
		} catch (Exception e) {
			logger.error("Get MemberInfo Error", e);
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.SYSTEM_ERROR,
					ResultConstant.SYSTEM_ERROR_MSG));
		}

	}

	/**
	 * web修改用户信息
	 * 
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "editmemberinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editMemberInfo(
			@RequestParam("memberZip") String memberZip,
			@RequestParam("memberAddress") String memberAddress,
			@RequestParam("memberTelphone") String memberTelphone,
			@RequestParam("memberSendName") String memberSendName,
			@RequestParam("memberRealName") String memberRealName,
			@RequestParam("memberBuliding") String memberBuliding,
			HttpServletRequest request) {
		try {
			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {

					return webMemberService.editMemberInfo(memberId, memberZip,
							memberTelphone, memberAddress, memberSendName,
							memberRealName, memberBuliding);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}
		} catch (Exception e) {
			logger.error("Edit MemberInfo Error", e);
			return new BaseResponse(false);
		}

	}

	/**
	 * web发送Faq
	 * 
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "sendfaq", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse sendFaq(@RequestParam("memberId") String memberId,
			@RequestParam("faqContent") String faqContent) {
		try {
			return webMemberService.insertFaqService(memberId, faqContent);
		} catch (Exception e) {
			logger.error("Send faq Error", e);
			return new BaseResponse(false);
		}
	}

}
