/**
 * There are <a href="https://github.com/thinkgem/Fujikom">Fujikom</a> code generation
 */
package com.joey.Fujikom.modules.spi.web;

import java.util.ArrayList;
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
import com.joey.Fujikom.modules.spi.entity.BarcodeRequest;
import com.joey.Fujikom.modules.spi.service.BarcodeService;

/**
 * 申请条形码Controller
 * @author JoeyHuang
 * @version 2016-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/barcode")
public class BarcodeController extends BaseController {

	@Autowired
	private BarcodeService barcodeService;
	
	
	
	
	@ModelAttribute
	public BarcodeRequest get(@RequestParam(required=false) Long barcodeRequestId) {
		if (barcodeRequestId!=null){
			return barcodeService.get(barcodeRequestId);
		}else{
			return new BarcodeRequest();
		}
	}
	
	@RequiresPermissions("spi:barcode:view")
	@RequestMapping(value = {"list", ""})
	public String list(BarcodeRequest barcodeRequest, HttpServletRequest request, HttpServletResponse response, Model model) {	
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
        Page<BarcodeRequest> page = barcodeService.find(new Page<BarcodeRequest>(request, response), barcodeRequest); 
        model.addAttribute("page", page);
		return "modules/spi/barcodeList";
	}

//	@RequiresPermissions("spi:sendorders:view")
	@RequestMapping(value = "generate")
	public String generate(BarcodeRequest barcodeRequest, Model model,HttpServletResponse response,RedirectAttributes redirectAttributes) {
//		addMessage(redirectAttributes, "条形码生成成功");
		try {
			String barcodeValue = barcodeService.generateBarcodeService(barcodeRequest,response);
			List<String> barcodeValueList= new ArrayList<String>();
			String[] barcodes = barcodeValue.split(",");
			for (int i = 0; i < barcodes.length; i++) {
				barcodeValueList.add(barcodes[i]);
			}
			model.addAttribute("barcodeRequest", barcodeRequest).addAttribute("ordersBarcodeValue", barcodeValue).addAttribute("barcodeValueList", barcodeValueList);
			addMessage(redirectAttributes, "条形码生成成功");
			return "modules/spi/barcodeGenerate";
		} catch (Exception e) {
			addMessage(redirectAttributes, "失败" + e.getMessage());
		}
		
		return "redirect:"+Global.getAdminPath()+"/spi/barcode/?repage";
	}
	
	
/*
	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = "save")
	public String save(Orders orders, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, orders)){
			return form(orders, model);
		}
		ordersService.save(orders);
		addMessage(redirectAttributes, "订单发送成功");
		return "redirect:"+Global.getAdminPath()+"/spi/sendorders/?repage";
	}
	
	@RequiresPermissions("spi:sendorders:edit")
	@RequestMapping(value = "delivery")
	public String delivery(Orders orders, Model model) {
		List<Product> products = new ArrayList<Product>();
		DetachedCriteria dc = productDao.createDetachedCriteria();
		dc.add(Restrictions.eq("orders", orders));
		products = productDao.find(dc);
		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			product.setProductStatus(AppProductService.PRODUCT_STATUS_SEND);
		}
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_SEND);
		model.addAttribute("orders", orders);
		return "modules/spi/sendOrdersInfo";
	}*/
	
	@RequiresPermissions("spi:barcode:edit")
	@RequestMapping(value = "delete")
	public String delete(String barcodeRequestId, RedirectAttributes redirectAttributes) {
		barcodeService.delete(barcodeRequestId);
		addMessage(redirectAttributes, "削除に成功しました");
		return "redirect:"+Global.getAdminPath()+"/spi/barcode/?repage";
	}

}
