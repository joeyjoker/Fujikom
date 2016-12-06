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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joey.Fujikom.common.config.Global;
import com.joey.Fujikom.common.persistence.Page;
import com.joey.Fujikom.common.utils.SpringContextHolder;
import com.joey.Fujikom.common.web.BaseController;
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.dao.OrdersDao;
import com.joey.Fujikom.modules.spi.entity.CsvRecord;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.entity.Product;
import com.joey.Fujikom.modules.spi.service.OrdersService;
import com.joey.Fujikom.modules.spi.service.ProductService;
import com.joey.Fujikom.modules.spi.utils.CsvUtils;
import com.joey.Fujikom.modules.spi.utils.ExportOrdersCsv;

/**
 * 发货订单Controller
 * 
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/sendorders")
public class SendOrdersController extends BaseController {

	@Autowired
	private OrdersService ordersService;

	@Autowired
	private ProductService productService;

	@Autowired
	private AppProductDao productDao;

	@Autowired
	private OrdersDao ordersdao;

	@ModelAttribute
	public Orders get(@RequestParam(required = false) Long ordersId) {
		if (ordersId != null) {
			return ordersService.get(ordersId);
		} else {
			return new Orders();
		}
	}

	@RequiresPermissions("spi:sendorders:view")
	@RequestMapping(value = { "list", "" })
	public String list(Orders orders, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
		Page<Orders> page = ordersService.findSendOrders(new Page<Orders>(
				request, response), orders);
		List<Orders> ordersList = ordersService.UnsendOrdersService();
		model.addAttribute("page", page).addAttribute("ordersList", ordersList);
		return "modules/spi/sendOrdersList";
	}

	@RequiresPermissions("spi:sendorders:view")
	@RequestMapping(value = "form")
	public String form(Orders orders, Model model) {
		model.addAttribute("orders", orders);
		return "modules/spi/sendOrdersForm";
	}

	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = "account")
	public String deliery(Orders orders, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)) {
			return form(orders, model);
		}
		ordersService.accountService(orders);
		addMessage(redirectAttributes, "決済に成功しました");
		return "redirect:" + Global.getAdminPath() + "/spi/sendorders/?repage";
	}

	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = "settle")
	public String settle(Orders orders, Model model) {

		model.addAttribute("orders", orders);
		return "modules/spi/sendOrdersInfo";
	}

	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = "save")
	public String save(Orders orders, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)) {
			return form(orders, model);
		}
		ordersService.save(orders);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:" + Global.getAdminPath() + "/spi/sendorders/?repage";
	}

	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = { "productlist" })
	public String productList(Orders orders, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
		Product product = new Product();
		product.setOrders(orders);
		Page<Product> page = productService.find(new Page<Product>(request,
				response), product);
		model.addAttribute("page", page).addAttribute("ordersNumber",
				orders.getOrdersNumber());
		return "modules/spi/ordersProductList";
	}

	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = "exportcsv")
	public String exportCsv(Orders orders, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		List<Orders> ordersList = ordersService.UnsendOrdersService();
		try {
			if (ordersList.size() != 0) {

				File tempFile = ExportOrdersCsv
						.putOutTaskToExcelFile(ordersList);
				String filenamedownload = tempFile.toString();
				String filenamedisplay = tempFile.getName();

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
				// 更改导出状态
				ordersService.csvExportFlagService(ordersList);
				CsvRecord csvRecord = ordersService
						.csvExportRecordService(tempFile.getName());
				ordersService.csvExportHistoryService(csvRecord, ordersList);

				/*return null;*/

			}
			if (ordersList.size() == 0) {

				addMessage(redirectAttributes,
						"CSVダウンロードが失敗しました：インポートできる注文情報がありません。");
			}
		} catch (IOException e) {
			addMessage(redirectAttributes, "CSVダウンロードが失敗しました：" + e.getMessage());
		}

		return "redirect:" + Global.getAdminPath() + "/spi/sendorders/?repage";
	}

	@RequestMapping(value = { "productform" })
	public String productForm(Long productId, Model model) {
		Product product = productService.get(productId);
		model.addAttribute("product", product);
		return "modules/spi/ordersProductForm";
	}

	@RequestMapping(value = "importcsv")
	public String importCsv(MultipartFile file, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {

			String filename = file.getOriginalFilename();
			String filetype = filename.substring(filename.lastIndexOf(".") + 1);
			Integer successNumber = 0;
			if (filetype.equals("csv")) {

				File convFile = new File(file.getOriginalFilename());
				file.transferTo(convFile);
				List<String> list = CsvUtils.importCsv(convFile);
				for (int i = 1; i < list.size(); i++) {
					String[] data = list.get(i).split(",");
					Orders orders = ordersdao.getOrdersFromNumber(data[0]);
					if (orders != null) {
						ordersService.csvDeliveryService(orders, data);
						successNumber++;
					}
				}
			}
			if (filetype.equals("csv") && successNumber > 0) {
				addMessage(redirectAttributes, "インポートに成功");
			} else if (!filetype.equals("csv")) {
				addMessage(redirectAttributes, "インポートが失敗しました：csvファイルを選択してください");
			} else {
				addMessage(redirectAttributes, "インポートが失敗しました:一致する受注番号はありません");
			}
		} catch (Exception e) {
			addMessage(redirectAttributes, "インポートが失敗しました：" + e.getMessage());
		}

		return "redirect:" + Global.getAdminPath() + "/spi/sendorders/?repage";
	}

	/*
	 * @RequiresPermissions("prj:project:edit")
	 * 
	 * @RequestMapping(value = "delete") public String delete(String id,
	 * RedirectAttributes redirectAttributes) { projectService.delete(id);
	 * addMessage(redirectAttributes, "删除项目成功"); return
	 * "redirect:"+Global.getAdminPath()+"/prj/project/?repage"; }
	 */

}
