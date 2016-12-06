package com.joey.Fujikom.modules.spi.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joey.Fujikom.common.config.Global;
import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.entity.CreditCard;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.request.MemberRequestData;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.service.AppMemberService;




/**
 * AppMemberController
 *
 * @author JoeyHuang
 * @version 2016-3-3
 */
@Controller
@RequestMapping(value = "/spi/member")
public class AppMemberController {

	private static Logger logger = LoggerFactory
			.getLogger(AppMemberController.class);
	
	@Autowired
	private AppMemberService appmemberService;
	
	
	
	/**
     * App用户信用卡绑定接口
     * 
     * @param thirdId
     * @param category
     * @return
	 * @throws Exception 
     */
	@RequestMapping(value="creditcard", method = RequestMethod.GET)
	public String memberCreditCard(@RequestParam("memberId") String memberId,Model model) throws Exception{
		CreditCard card = new CreditCard();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+9"));
		card.setPayMethod(Global.getPayMethod());
		card.setMerchantId(Global.getMerchantId());
		card.setServiceId(Global.getServiceId());
		card.setCustCode(memberId);
		card.setSpsCustNo(Global.getSpsCustNo());
		card.setTerminalType(Global.getTerminalType());
		card.setSuccessUrl(Global.getSuccessUrl());
		card.setCancelUrl(Global.getCancelUrl());
		card.setErrorUrl(Global.getErrorUrl());
		card.setPageconUrl(Global.getPageConUrl());
		card.setFree1(Global.getFree1());
		card.setFree2(Global.getFree2());
		card.setFree3(Global.getFree3());
		card.setRequestDate(sdf.format(date));
		card.setLimitSecond(Global.getLimitSecond());
		String sps = card.getPayMethod()+card.getMerchantId()+card.getServiceId()+card.getCustCode()+card.getSpsCustNo()+card.getTerminalType();
		sps = sps+card.getSuccessUrl()+card.getCancelUrl()+card.getErrorUrl()+card.getPageconUrl();
		sps=sps+card.getFree1()+card.getFree2()+card.getFree3()+card.getRequestDate()+card.getLimitSecond();
		sps=sps+"644da9995cac43695d6b3fcbc89787872fbc8b5c";
		card.setSpsHashcode(DigestUtils.sha1Hex(sps));
		model.addAttribute("card", card);
		return "modules/spi/registerCreditCard";
		
	}
	
	
	/**
     * App用户信用卡绑定接口
     * 
     * @param thirdId
     * @param category
     * @return
	 * @throws Exception 
     */
	@RequestMapping(value="cardcallback", method = RequestMethod.POST)
	public void memberCreditCard(@RequestParam(value="cust_code",required=false) String cust_code,
			@RequestParam(value="res_result",required=false) String res_result){
		System.out.println(res_result);
		if (res_result.equals("OK")) {
			System.out.println(cust_code);
			appmemberService.memberCreditService(Long.parseLong(cust_code));
		}else {
			System.out.println("fail");
		}
		
	}
	
	
	/**
     * App用户登录接口
     * 
     * @param thirdId
     * @param category
     * @return
     */
	@RequestMapping(value="memberlogin", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse memberLogin(@RequestParam("memberName") String memberName,
			@RequestParam("memberPassword") String memberPassword){
		
		try {
			return appmemberService.memberLogin(memberName, memberPassword);
		} catch (Exception e) {
			logger.error("AppMemberController.memberLogin error", e);
			return new BaseResponse(false,"ログインに失敗");
		}
	}
	
	/**
     * App用户第三方登录接口
     * 
     * @param thirdId
     * @param category
     * @return
     */
	@RequestMapping(value = "thirdlogin", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse thirdLogin(@RequestParam("thirdId") String thirdId,
			@RequestParam("category") String category) {
		try {

			
			return appmemberService.thirdLogin(thirdId, category);

		} catch (Exception e) {

			logger.error("Member thirdLogin Error", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App用户第三方注册接口
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
			@RequestParam("memberBuliding") String memberBuliding) {
		try {
			Member member = new Member();
			member.setMemberName(thirdId);
			member.setMemberZip(memberZip);
			member.setMemberAddress(memberAddress);
			member.setMemberTelphone(memberTelphone);
			member.setMemberSendName(memberSendName);
			member.setMemberRealName(memberRealName);
			member.setMemberBuliding(memberBuliding);
			return appmemberService.thirdRegister(thirdId, category, member);
			
		} catch (Exception e) {
			logger.error("Member third Register Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App用户注册接口
     * 
     * @param memberName
     * @param memberPassword
     * @return
     */
	@RequestMapping(value = "register", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse register(@RequestParam("memberName") String memberName,
			@RequestParam("memberPassword") String memberPassword,
			@RequestParam("memberZip")String memberZip,
			@RequestParam("memberAddress")String memberAddress,
			@RequestParam("memberTelphone") String memberTelphone,
			@RequestParam("memberSendName") String memberSendName,
			@RequestParam("memberRealName") String memberRealName,
			@RequestParam("memberBuliding") String memberBuliding) {
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
			return appmemberService.memberRegister(member);
			
		} catch (Exception e) {
			logger.error("Member Register Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App用户找回密码接口
     * 
     * @param memberName
     * @return
     */
	@RequestMapping(value = "findpsw", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse findPsw(@RequestParam("memberName") String memberName) {
		try {

			return appmemberService.findPsw(memberName);

		} catch (Exception e) {

			logger.error("Member thirdLogin Error", e);
			return new BaseResponse(false,new ErrorResponse(ResultConstant.SYSTEM_ERROR, ResultConstant.SYSTEM_ERROR_MSG));
			
		}

	}
	
	/**
     * App获取用户信息
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "getmemberinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getMemberInfo(@RequestParam("memberId") String memberId) {
		try {
			return appmemberService.getMemberInfo(Long.parseLong(memberId));
		} catch (Exception e) {
			logger.error("Get MemberInfo Error", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App修改用户信息
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "editmemberinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editMemberInfo(@RequestBody MemberRequestData memberRequestData) {
		try {
			return appmemberService.editMemberInfo(memberRequestData);
		} catch (Exception e) {
			logger.error("Edit MemberInfo Error", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App发送Faq
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "sendfaq", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse sendFaq(@RequestParam("memberId") String memberId,
			@RequestParam("faqContent") String faqContent){
		try {
			return appmemberService.insertFaqService(memberId, faqContent);
		} catch (Exception e) {
			logger.error("Send faq Error", e);
			return new BaseResponse(false);
		}
	}
	
}
