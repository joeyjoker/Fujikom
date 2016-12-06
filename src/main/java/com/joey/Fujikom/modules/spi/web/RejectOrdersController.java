/**
 * There are <a href="https://github.com/thinkgem/Fujikom">Fujikom</a> code generation
 */
package com.joey.Fujikom.modules.spi.web;

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
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.service.OrdersService;

/**
 * 退货订单Controller
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/rejectorders")
public class RejectOrdersController extends BaseController {

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private AppProductDao productDao;
	
	
	@ModelAttribute
	public Orders get(@RequestParam(required=false) Long ordersId) {
		if (ordersId!=null){
			return ordersService.get(ordersId);
		}else{
			return new Orders();
		}
	}
	
	@RequiresPermissions("spi:rejectorders:view")
	@RequestMapping(value = {"list", ""})
	public String list(Orders orders, HttpServletRequest request, HttpServletResponse response, Model model) {	
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
        Page<Orders> page = ordersService.findRejectOrders(new Page<Orders>(request, response), orders); 
        model.addAttribute("page", page);
		return "modules/spi/rejectOrdersList";
	}

	@RequiresPermissions("spi:rejectorders:view")
	@RequestMapping(value = "form")
	public String form(Orders orders, Model model) {
		model.addAttribute("orders", orders);
		return "modules/spi/rejectOrdersForm";
	}
	
	

	@RequiresPermissions("spi:rejectorders:edit")
	@RequestMapping(value = "reject")
	public String delivery(Orders orders, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)){
			return form(orders, model);
		}
		ordersService.rejectProcessService(orders);
		addMessage(redirectAttributes, "返品に成功しまし");
		return "redirect:"+Global.getAdminPath()+"/spi/rejectorders/?repage";
	}
	
	@RequiresPermissions("spi:rejectorders:edit")
	@RequestMapping(value = "settle")
	public String settle(Orders orders, Model model) {
		
		model.addAttribute("orders", orders);
		return "modules/spi/rejectOrdersInfo";
	}
	
	/*@RequiresPermissions("prj:project:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		projectService.delete(id);
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:"+Global.getAdminPath()+"/prj/project/?repage";
	}*/
	
	@RequiresPermissions("spi:rejectorders:edit")
	@RequestMapping(value = "save")
	public String save(Orders orders, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)){
			return form(orders, model);
		}
		ordersService.save(orders);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:"+Global.getAdminPath()+"/spi/rejectorders/?repage";
	}
	
	@RequiresPermissions("spi:rejectorders:edit")
	@RequestMapping(value = "account")
	public String deliery(Orders orders, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)) {
			return form(orders, model);
		}
		ordersService.accountService(orders);
		addMessage(redirectAttributes, "決済に成功しました");
		return "redirect:" + Global.getAdminPath() + "/spi/rejectorders/?repage";
	}
	

}
