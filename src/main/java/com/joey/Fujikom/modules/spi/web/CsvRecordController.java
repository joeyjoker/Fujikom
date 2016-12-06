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
import com.joey.Fujikom.modules.spi.entity.CsvRecord;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.service.CsvRecordService;
import com.joey.Fujikom.modules.spi.utils.ExportOrdersCsv;

/**
 * csv记录Controller
 * @author JoeyHuang
 * @version 2016-03-14
 */
@Controller
@RequestMapping(value = "${adminPath}/spi/csvrecord")
public class CsvRecordController extends BaseController {

	@Autowired
	private CsvRecordService csvRecordService;
	
	
	
	
	@ModelAttribute
	public CsvRecord get(@RequestParam(required=false) Long csvId) {
		if (csvId!=null){
			return csvRecordService.get(csvId);
		}else{
			return new CsvRecord();
		}
	}
	
	@RequiresPermissions("spi:csvrecord:view")
	@RequestMapping(value = {"list", ""})
	public String list(CsvRecord csvRecord, HttpServletRequest request, HttpServletResponse response, Model model) {	
		String rootPath = SpringContextHolder.getResourceRootRealPath();
		logger.info(rootPath);
        Page<CsvRecord> page = csvRecordService.find(new Page<CsvRecord>(request, response), csvRecord); 
        model.addAttribute("page", page);
		return "modules/spi/csvRecordList";
	}

	@RequiresPermissions("spi:csvrecord:edit")
	@RequestMapping(value = "download")
	public String form(CsvRecord csvRecord, Model model,HttpServletResponse response,RedirectAttributes redirectAttributes) {
		List<Orders> ordersList = csvRecordService.findCsvHistoryService(csvRecord);
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
		    return null; 

		} catch (IOException e) {
			addMessage(redirectAttributes,"CSVダウンロードが失敗しました：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/spi/csvrecord/?repage";
	}


}
