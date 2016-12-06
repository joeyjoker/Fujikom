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
import com.joey.Fujikom.modules.spi.dao.ProductDao;
import com.joey.Fujikom.modules.spi.entity.Product;
import com.joey.Fujikom.modules.spi.service.ProductService;
import com.joey.Fujikom.modules.spi.utils.SaveToCsv;

/**
 * web商品管理Controller
 * 
 * @author JoeyHuang
 * @version 2016-03-10
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/product")
public class ProductController extends BaseController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductDao productDao;

	@ModelAttribute
	public Product get(@RequestParam(required = false) Long productId) {
		if (productId != null) {
			return productService.get(productId);
		} else {
			return new Product();
		}
	}

	@RequiresPermissions("spi:product:view")
	@RequestMapping(value = { "list", "" })
	public String list(Product product, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
		Page<Product> page = productService.find(new Page<Product>(request,
				response), product);
		model.addAttribute("page", page);
		return "modules/spi/productList";
	}

	@RequiresPermissions("spi:product:view")
	@RequestMapping(value = "form")
	public String form(Product product, Model model) {
		model.addAttribute("product", product);
		return "modules/spi/productForm";
	}

	@RequiresPermissions("spi:product:edit")
	@RequestMapping(value = "save")
	public String save(Product product, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, product)) {
			return form(product, model);
		}
		productService.save(product);
		addMessage(redirectAttributes, "保存に成功しました");
		return "redirect:" + Global.getAdminPath() + "/spi/product/?repage";
	}

	@RequiresPermissions("spi:product:edit")
	@RequestMapping(value = "export")
	public String exportCsv(Product product, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) {
		List<Product> producs = productService.findCsv(product);
		File tempFile = SaveToCsv.putOutTaskToExcelFile(producs);
		String filenamedownload = tempFile.toString();
		String filenamedisplay = tempFile.getName();
		try {
			filenamedisplay = URLEncoder.encode(filenamedisplay, "UTF-8");

			response.addHeader("Content-Disposition", "attachment;filename="
					+ filenamedisplay);
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
			return null;
		} catch (IOException e) {
			addMessage(redirectAttributes, "导出CSV失败！失败信息：" + e.getMessage());
		}

		return "redirect:" + Global.getAdminPath() + "/spi/product/?repage";
	}

	/*
	 * @RequiresPermissions("spi:sendorders:edit")
	 * 
	 * @RequestMapping(value = "delivery") public String delivery(Orders orders,
	 * Model model) { List<Product> products = new ArrayList<Product>();
	 * DetachedCriteria dc = productDao.createDetachedCriteria();
	 * dc.add(Restrictions.eq("orders", orders)); products =
	 * productDao.find(dc); for (int i = 0; i < products.size(); i++) { Product
	 * product = products.get(i);
	 * product.setProductStatus(AppProductService.PRODUCT_STATUS_SEND); }
	 * orders.setOrdersStatus(AppProductService.ORDERS_STATUS_SEND);
	 * model.addAttribute("orders", orders); return
	 * "modules/spi/sendOrdersInfo"; }
	 */

	/*
	 * @RequiresPermissions("prj:project:edit")
	 * 
	 * @RequestMapping(value = "delete") public String delete(String id,
	 * RedirectAttributes redirectAttributes) { projectService.delete(id);
	 * addMessage(redirectAttributes, "删除项目成功"); return
	 * "redirect:"+Global.getAdminPath()+"/prj/project/?repage"; }
	 */

}
