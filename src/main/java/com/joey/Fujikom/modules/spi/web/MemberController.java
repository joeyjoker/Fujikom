/**
 * There are <a href="https://github.com/thinkgem/Fujikom">Fujikom</a> code generation
 */
package com.joey.Fujikom.modules.spi.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joey.Fujikom.common.config.Global;
import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.utils.SpringContextHolder;
import com.joey.Fujikom.common.web.BaseController;
import com.joey.Fujikom.modules.spi.dao.MemberDao;
import com.joey.Fujikom.modules.spi.dao.WarehouseDao;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.entity.Warehouse;
import com.joey.Fujikom.modules.spi.service.MemberService;

/**
 * web用户管理Controller
 * 
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/member")
public class MemberController extends BaseController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private WarehouseDao warehouseDao; 

	@ModelAttribute
	public Member get(@RequestParam(required = false) Long memberId) {
		if (memberId != null) {
			return memberService.get(memberId);
		} else {
			return new Member();
		}
	}

	@RequiresPermissions("spi:member:view")
	@RequestMapping(value = { "list", "" })
	public String list(Member member, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
		Page<Member> page = memberService.find(new Page<Member>(request,
				response), member);
		model.addAttribute("page", page);
		return "modules/spi/memberList";
	}

	@RequiresPermissions("spi:member:view")
	@RequestMapping(value = "form")
	public String form(Member member, Model model) {
		List<Warehouse> warehouseList = warehouseDao.findAll();
		model.addAttribute("member", member).addAttribute("warehouseList", warehouseList);
		return "modules/spi/memberForm";
	}

	@RequiresPermissions("spi:member:edit")
	@RequestMapping(value = "save")
	public String save(Member member, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, member)) {
			return form(member, model);
		}
		
		memberService.save(member);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:" + Global.getAdminPath() + "/spi/member/?repage";
	}


}
