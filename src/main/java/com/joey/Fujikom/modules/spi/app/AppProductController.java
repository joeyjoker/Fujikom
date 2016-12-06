package com.joey.Fujikom.modules.spi.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.joey.Fujikom.modules.spi.dao.MemberDao;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.request.OrdersRequestData;
import com.joey.Fujikom.modules.spi.request.ProductRequestData;
import com.joey.Fujikom.modules.spi.response.BarcodeView;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.service.AppMemberService;
import com.joey.Fujikom.modules.spi.service.AppProductService;



/**
 * AppProductController
 *
 * @author JoeyHuang
 * @version 2016-3-07
 */
@Controller
@RequestMapping(value = "/spi/product")
public class AppProductController {

	private static Logger logger = LoggerFactory
			.getLogger(AppProductController.class);
	
	@Autowired
	private AppMemberService appmemberService;
	
	@Autowired
	private AppProductService appProductService;
	
	@Autowired
	private MemberDao memberdao;

	
	
	/**
     * App全权委托
     * 
     * @param thirdId
     * @param category
     * @return
     */
	@RequestMapping(value="entrust", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse entrustProduct(@RequestParam("memberId") String memberId,
			@RequestParam("productNumber") String productNumber){
		
		try {
			return appProductService.entrustProduct(memberId, productNumber);
		} catch (Exception e) {
			logger.error("AppMemberController.memberLogin error", e);
			return new BaseResponse(false,"fail");
		}
	}
	
	/**
     * App获取全权委托列表
     * 
     * @param 
     * @param 
     * @return
     */
	@RequestMapping(value="entrustlist", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse entrustList(@RequestParam("memberId") String memberId,
					@RequestParam("sinceId") String sinceId,
					@RequestParam("size") String size){
		
		try {
			return appProductService.entrustListService(memberId, sinceId,size);
		} catch (Exception e) {
			logger.error("get entrustList error", e);
			return new BaseResponse(false,"fail");
		}
	}
	
	/**
     * App删除箱子
     * 
     * @param 
     * @param 
     * @return
     */
	@RequestMapping(value="deletebox", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse deleteBox(@RequestParam("boxId") String boxId){
		try {
			return appProductService.delBoxService(boxId);
		} catch (Exception e) {
			logger.error("delete entrustbox error", e);
			return new BaseResponse(false,"fail");
		}
	}
	
	/**
     * App普通方案生成条形码
     * 
     * @param productNumber
     * @return
     */
	@RequestMapping(value = "genbarcode", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse genBarcode(@RequestParam("productNumber") String productNumber,
			                       @RequestParam("memberId") String memberId) {
		try {

			return appProductService.barcodeGenService(productNumber,memberId);

		} catch (Exception e) {

			logger.error("generdate barcode Error", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App普通方案生成条形码
     * 
     * @param productNumber
     * @return
     */
	@RequestMapping(value = "barcodeview", method = RequestMethod.GET)
	public String barcodeView(@RequestParam("barcodeValue") String  barcodeValue,@RequestParam("memberId") String memberId, HttpServletRequest request,HttpServletResponse response, Model model) {
		Member member = memberdao.get(Long.parseLong(memberId));
		List<BarcodeView> barcodeViewList = appProductService.barcodeViewService(barcodeValue);
		model.addAttribute("barcodeViewList", barcodeViewList).addAttribute("barcodeValue", barcodeValue).addAttribute("member", member);
		return "modules/spi/barcodeView";
	}
	
	
	/**
     * App普通方案商品注册接口
     * 
     * @param products
     * @return
     */
	@RequestMapping(value = "productregister", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse productRegister(@RequestBody List<ProductRequestData> products) {
		try {
			
			return appProductService.normalLoginProduct(products);
			
		} catch (Exception e) {
			logger.error("Normal Product Register Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App获取处理中商品列表接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "getprocessproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getProcessProduct(@RequestParam("memberId") String memberId,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size) {
		try {
			
			return appProductService.getProcessingProducts(memberId,Integer.parseInt(sinceId),Integer.parseInt(size));
			
		} catch (Exception e) {
			logger.error("get Process Products Error", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App获取未检查箱子列表接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "getuncheckboxes", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getUncheckBoxes(@RequestParam("memberId") String memberId,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size) {
		try {
			
			return appProductService.getUncheckBoxes(memberId,Integer.parseInt(sinceId),Integer.parseInt(size));
			
		} catch (Exception e) {
			logger.error("get Uncheck Boxes Error", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App获取仓库商品列表接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "getinstockproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getInstockProduct(@RequestParam("memberId") String memberId,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size) {
		try {
			
			return appProductService.getInstockProducts(memberId,Integer.parseInt(sinceId),Integer.parseInt(size));
			
		} catch (Exception e) {
			logger.error("get Instock Products Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App获取商品信息接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "getproductinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getProductInfo(@RequestParam("productId") String productId) {
		try {
			
			return appProductService.getProductInfoService(productId);
			
		} catch (Exception e) {
			logger.error("get Instock Products Error ", e);
			return new BaseResponse(false);
		}

	}
	

	/**
     * App修改商品信息
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "editproductinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editProductInfo(@RequestParam("productId") String productId,
			@RequestParam("productName") String productName,
			@RequestParam("productMemo") String productMemo) {
		try {
			
			return appProductService.editProductInfoService(productId, productName, productMemo);
			
		} catch (Exception e) {
			logger.error("Update Product info Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App删除商品信息
     * 
     * @param productId
     * @return
     */
	@RequestMapping(value = "deleteproduct", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editProductInfo(@RequestParam("productId") String productId) {
		try {
			
			return appProductService.deleteProductService(productId);
			
		} catch (Exception e) {
			logger.error("delete Product  Error ", e);
			return new BaseResponse(false,"delete Product  Error ");
		}

	}
	
	/**
     * App获取发货商品列表
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "getsendproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getSendProducts(@RequestParam("memberId") String memberId,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size) {
		try {
			
			return appProductService.getSendProductsService(memberId,Integer.parseInt(sinceId),Integer.parseInt(size));
			
		} catch (Exception e) {
			logger.error("Get Send Products  Error ", e);
			return new BaseResponse(false,"delete Product  Error ");
		}

	}
	
	/**
     * App用户发货请求接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "shipordersrequest", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse shipOrdersRequest(@RequestBody OrdersRequestData ordersData) {
		try {
			
			return appProductService.shipRuquestService(ordersData);
			
		} catch (Exception e) {
			logger.error("ship Orders Data  Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App用户取回请求接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "returnordersrequest", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse returnOrdersRequest(@RequestBody OrdersRequestData ordersData) {
		try {
			
			return appProductService.returnProductService(ordersData);
			
		} catch (Exception e) {
			logger.error("retur orders  Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App用户寄送费计算接口
     * 
     * @param memberId
     * @return
     */
	@RequestMapping(value = "deliverycal", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse deliveryCost(@RequestParam("sizes") String sizes) {
		try {
			List<String> sizeList = new ArrayList<String>();
			String[] sizeStrings = sizes.split(",");
			for (String str : sizeStrings) {
				sizeList.add(str);
			}
			return appProductService.deliveryCostService(sizeList);
		} catch (Exception e) {
			logger.error("ship Orders Data  Error ", e);
			return new BaseResponse(false);
		}

	}
	
	/**
     * App用户搜索商品接口
     *
     * @param searchString
     * @param memberId
     * @param sinceId
     * @param size
     * @return
     */
	@RequestMapping(value = "searchproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse searchProducts(@RequestParam("searchString")String searchString,
			@RequestParam("memberId") String memberId,
		    @RequestParam("sinceId") String sinceId,
		    @RequestParam("size") String size){
		try {
			
			return appProductService.searchService(searchString, memberId,sinceId,size);
			
		} catch (Exception e) {
			logger.error("retur orders  Error ", e);
			return new BaseResponse(false);
		}

	}

	
	

	
	/*	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse login(@RequestBody List<Member> member) {

		try {
			return new BaseResponse(true,member);
		} catch (Exception e) {
			return new BaseResponse(false);
		}
	}
*/
}
