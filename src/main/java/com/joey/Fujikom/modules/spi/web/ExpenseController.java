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
import com.joey.Fujikom.modules.spi.entity.Expense;
import com.joey.Fujikom.modules.spi.service.ExpenseService;

/**
 * 费用Controller
 * @author JoeyHuang
 * @version 2016-03-19
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/expense")
public class ExpenseController extends BaseController {

	@Autowired
	private ExpenseService expenseService;
	
	@ModelAttribute
	public Expense get(@RequestParam(required=false) Long expenseId) {
		if (expenseId!=null){
			return expenseService.get(expenseId);
		}else{
			return new Expense();
		}
	}
	
	@RequiresPermissions("spi:expense:view")
	@RequestMapping(value = {"list", ""})
	public String list(Expense expense, HttpServletRequest request, HttpServletResponse response, Model model) {	
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
        Page<Expense> page = expenseService.find(new Page<Expense>(request, response), expense); 
        model.addAttribute("page", page);
		return "modules/spi/expenseList";
	}

	@RequiresPermissions("spi:expense:view")
	@RequestMapping(value = "form")
	public String form(Expense expense, Model model) {
		model.addAttribute("expense", expense);
		return "modules/spi/expense";
	}
	
	
	@RequiresPermissions("spi:expense:edit")
	@RequestMapping(value = "save")
	public String save(Expense expense, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, expense)){
			return form(expense, model);
		}
		expenseService.save(expense);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:"+Global.getAdminPath()+"/spi/expense/?repage";
	}
	
	
	/*@RequiresPermissions("prj:project:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		projectService.delete(id);
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:"+Global.getAdminPath()+"/prj/project/?repage";
	}*/

}
