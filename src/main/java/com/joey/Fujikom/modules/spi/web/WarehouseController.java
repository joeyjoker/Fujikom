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
import com.joey.Fujikom.modules.spi.entity.Warehouse;
import com.joey.Fujikom.modules.spi.service.WarehouseService;

/**
 * 发货订单Controller
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/warehouse")
public class WarehouseController extends BaseController {

	@Autowired
	private WarehouseService warehouseService;
	
	@Autowired
	private AppProductDao productDao;
	
	
	@ModelAttribute
	public Warehouse get(@RequestParam(required=false) Long warehouseId) {
		if (warehouseId!=null){
			return warehouseService.get(warehouseId);
		}else{
			return new Warehouse();
		}
	}
	
	@RequiresPermissions("spi:warehouse:view")
	@RequestMapping(value = {"list", ""})
	public String list(Warehouse warehouse, HttpServletRequest request, HttpServletResponse response, Model model) {	
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
        Page<Warehouse> page = warehouseService.find(new Page<Warehouse>(request, response), warehouse); 
        model.addAttribute("page", page);
		return "modules/spi/warehouseList";
	}

	@RequiresPermissions("spi:warehouse:view")
	@RequestMapping(value = "form")
	public String form(Warehouse warehouse, Model model) {
		model.addAttribute("warehouse", warehouse);
		return "modules/spi/warehouseForm";
	}
	
	
	@RequiresPermissions("spi:warehouse:edit")
	@RequestMapping(value = "save")
	public String save(Warehouse warehouse, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, warehouse)){
			return form(warehouse, model);
		}
		warehouseService.save(warehouse);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:"+Global.getAdminPath()+"/spi/warehouse/?repage";
	}
	
	
	/*@RequiresPermissions("prj:project:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		projectService.delete(id);
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:"+Global.getAdminPath()+"/prj/project/?repage";
	}*/

}
