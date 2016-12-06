/**
 * There are <a href="https://github.com/thinkgem/Fujikom">Fujikom</a> code generation
 */
package com.joey.Fujikom.modules.spi.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.entity.CsvRecord;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.service.AppProductService;
import com.joey.Fujikom.modules.spi.service.OrdersService;
import com.joey.Fujikom.modules.spi.utils.ExportOrdersCsv;

/**
 * 退货订单Controller
 * 
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/returnorders")
public class ReturnOrdersController extends BaseController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private AppProductDao productDao;

	@ModelAttribute
	public Orders get(@RequestParam(required = false) Long ordersId) {
		if (ordersId != null) {
			return ordersService.get(ordersId);
		} else {
			return new Orders();
		}
	}

	@RequiresPermissions("spi:returnorders:view")
	@RequestMapping(value = { "list", "" })
	public String list(Orders orders, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
		Page<Orders> page = ordersService.findReturnOrders(new Page<Orders>(
				request, response), orders);
		model.addAttribute("page", page);
		return "modules/spi/returnOrdersList";
	}

	@RequiresPermissions("spi:returnorders:view")
	@RequestMapping(value = "form")
	public String form(Orders orders, Model model) {
		model.addAttribute("orders", orders);
		return "modules/spi/returnOrdersForm";
	}

	@RequiresPermissions("spi:returnorders:edit")
	@RequestMapping(value = "delivery")
	public String delivery(Orders orders, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)) {
			return form(orders, model);
		}
		ordersService.deliveryService(orders,
				AppProductService.PRODUCT_STATUS_RETURN);
		addMessage(redirectAttributes, "引き取りに成功しました");
		return "redirect:" + Global.getAdminPath()
				+ "/spi/returnorders/?repage";
	}

	@RequiresPermissions("spi:returnorders:edit")
	@RequestMapping(value = "settle")
	public String settle(Orders orders, Model model) {

		model.addAttribute("orders", orders);
		return "modules/spi/returnOrdersInfo";
	}

	/*
	 * @RequiresPermissions("prj:project:edit")
	 * 
	 * @RequestMapping(value = "delete") public String delete(String id,
	 * RedirectAttributes redirectAttributes) { projectService.delete(id);
	 * addMessage(redirectAttributes, "删除项目成功"); return
	 * "redirect:"+Global.getAdminPath()+"/prj/project/?repage"; }
	 */

	@RequiresPermissions("spi:returnorders:edit")
	@RequestMapping(value = "save")
	public String save(Orders orders, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)) {
			return form(orders, model);
		}
		ordersService.save(orders);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:" + Global.getAdminPath()
				+ "/spi/returnorders/?repage";
	}

	@RequiresPermissions("spi:returnorders:edit")
	@RequestMapping(value = "exportcsv")
	public String exportCsv(Orders orders, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {

		List<Orders> ordersList = ordersService.UnreturnOrdersService();
		if (ordersList.size() == 0) {
			File tempFile = ExportOrdersCsv.putOutTaskToExcelFile(ordersList);
			String filenamedownload = tempFile.toString();
			String filenamedisplay = tempFile.getName();
			try {
				filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");

				response.addHeader("Content-Disposition",
						"attachment;filename=" + filenamedisplay);
				OutputStream output = null;
				FileInputStream fis = null;

				output = response.getOutputStream();
				fis = new FileInputStream(filenamedownload);
				byte[] b = new byte[1024];
				int i = 0;
				while ((i = fis.read(b)) > 0) {
					output.write(b, 0, i);
				}
				fis.close();
				output.flush();

				ordersService.csvExportFlagService(ordersList);
				CsvRecord csvRecord = ordersService.csvExportRecordService(filenamedisplay);
				ordersService.csvExportHistoryService(csvRecord, ordersList);
				
			    return null; 
			} catch (IOException e) {
				addMessage(redirectAttributes,
						"CSVダウンロードが失敗しました：" + e.getMessage());
			}
		} else {
			addMessage(redirectAttributes, "CSVダウンロードが失敗しました：");
		}
		return "redirect:" + Global.getAdminPath()
				+ "/spi/returnorders/?repage";
	}

	@RequiresPermissions("spi:returnorders:edit")
	@RequestMapping(value = "account")
	public String deliery(Orders orders, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)) {
			return form(orders, model);
		}
		ordersService.accountService(orders);
		addMessage(redirectAttributes, "決済に成功しました");
		return "redirect:" + Global.getAdminPath()
				+ "/spi/returnorders/?repage";
	}

}
