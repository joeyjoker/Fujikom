package com.joey.Fujikom.modules.spi.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.joey.Fujikom.modules.spi.request.AdminProductRequestData;
import com.joey.Fujikom.modules.spi.request.InstockResquestData;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.service.AppAdminService;
import com.joey.Fujikom.modules.spi.service.AppProductService;



/**
 * 工厂APP接口
 *
 * @author JoeyHuang
 * @version 2016-3-07
 */
@Controller
@RequestMapping(value = "/spi/admin")
public class AppAdminController {

	private static Logger logger = LoggerFactory
			.getLogger(AppAdminController.class);
	
	@Autowired
	private AppAdminService appAdminService;
	
	

	
	
	/**
     * 工厂App商品搜索（商品添加）
     * 
     * @param searchCondition
     * @return
     */
	@RequestMapping(value="searchloginproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse searchBoxes(@RequestParam("searchCondition") String searchCondition,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size){
		
		try {
			return appAdminService.searchBoxesService(searchCondition, AppProductService.BOX_STATUS_DEFAULT,sinceId,size);
		} catch (Exception e) {
			logger.error("Search Boxes error(add)", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App商品搜索（商品检查）
     * 
     * @param searchCondition
     * @return
     */
	@RequestMapping(value="searchcheckproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse checkBoxes(@RequestParam("searchCondition") String searchCondition,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size){
		
		try {
			if (sinceId.equals("-1")) {
				return appAdminService.searchFromBarcodeService(searchCondition);//根据商品条码搜索
			}else {
				
				return appAdminService.searchUncheckBoxesService(searchCondition, AppProductService.BOX_STATUS_UNCHECKED,AppProductService.BOX_STATUS_CHECKED,sinceId,size);
			}
		} catch (Exception e) {
			logger.error("Search Boxes error(check)", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App箱子搜索（除未登录状态）
     * 
     * @param searchCondition
     * @return
     */
	@RequestMapping(value="searchboxes", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse searchAllBoxes(@RequestParam("searchCondition") String searchCondition,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size){
		
		try {
			return appAdminService.searchAllBoxesService(searchCondition, AppProductService.BOX_STATUS_DEFAULT,sinceId,size);
		} catch (Exception e) {
			logger.error("Search Boxes error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App商品登录
     * 
     * @param products
     * @return
     */
	@RequestMapping(value="productregister", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse adminProductRegister(@RequestBody List<AdminProductRequestData> products){
		
		try {
			return appAdminService.trustLoginProduct(products);
		} catch (Exception e) {
			logger.error("Login products error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App商品登录2
     * 
     * @param products
     * @return
     */
	@RequestMapping(value="productlogin", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse adminProductLogin(@RequestParam(value="productImageOne", required=false) MultipartFile productImageOne,
			@RequestParam(value="productImageTwo", required=false) MultipartFile productImageTwo,
			@RequestParam(value="productImageThree", required=false) MultipartFile productImageThree,
			@RequestParam(value="productImageFour", required=false) MultipartFile productImageFour,
			@RequestParam String productName,
			@RequestParam String productMemo,
			@RequestParam String productBarcode,
			@RequestParam String memberId,
			@RequestParam String boxId,
			@RequestParam String productNumber,
			@RequestParam String index){
		
		try {
			return appAdminService.trustRegisterProduct(productImageOne, productImageTwo, productImageThree, 
					productImageFour,productName, productMemo, productBarcode, memberId, boxId, productNumber,index);
		} catch (Exception e) {
			logger.error("Login products error", e);
			return new BaseResponse(false,index);
		}
	}
	
	/**
     * 工厂App商品登录3(base64)
     * 
     * @param products
     * @return
     */
	@RequestMapping(value="productlogin2", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse trustProductLogin(@RequestParam(value="productImageOne", required=false) String productImageOne,
			@RequestParam(value="productImageTwo", required=false) String productImageTwo,
			@RequestParam(value="productImageThree", required=false) String productImageThree,
			@RequestParam(value="productImageFour", required=false) String productImageFour,
			@RequestParam(value="productImageOneType", required=false) String productImageOneType,
			@RequestParam(value="productImageTwoType", required=false) String productImageTwoType,
			@RequestParam(value="productImageThreeType", required=false) String productImageThreeType,
			@RequestParam(value="productImageFourType", required=false) String productImageFourType,
			@RequestParam String productName,
			@RequestParam String productMemo,
			@RequestParam String productBarcode,
			@RequestParam String memberId,
			@RequestParam String boxId,
			@RequestParam String productNumber,
			@RequestParam String index){
		
		try {
			return appAdminService.trustRegisterProductService(productImageOne, productImageTwo, productImageThree, 
					productImageFour,productImageOneType,productImageTwoType,productImageThreeType,productImageFourType,
					productName, productMemo, productBarcode, memberId, boxId, productNumber,index);
		} catch (Exception e) {
			logger.error("Login products error", e);
			return new BaseResponse(false,index);
		}
	}
	
	/**
     * 工厂App发送生成条形码请求
     * 
     * @param barcodeNumber
     * @return
     */
	@RequestMapping(value="requestbarcode", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse barcodeRequest(@RequestParam("barcodeNumber") String barcodeNumber){
		
		try {
			return appAdminService.barcodeRequestService(barcodeNumber);
		} catch (Exception e) {
			logger.error("Search Boxes error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App检查商品
     * 
     * @param boxId
     * @return
     */
	@RequestMapping(value="productcheck", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse productCheck(@RequestParam("boxId") String boxId){
		
		try {
			return appAdminService.productCheckService(boxId);
		} catch (Exception e) {
			logger.error("Check Products error)", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App商品入库(临时货架)
     * 
     * @param boxId
     * @return
     */
	@RequestMapping(value="teminstock", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse temInstock(@RequestBody List<InstockResquestData> instockData){
		
		try {
			return appAdminService.instockTemService(instockData);
		} catch (Exception e) {
			logger.error("temInstock error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App商品入库(检查货架)
     * 
     * @param boxId
     * @return
     */
	@RequestMapping(value="confirmshelf", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse confirmShelf(@RequestParam("shelfBarcode") String shelfBarcode){
		
		try {
			return appAdminService.confirmShelfService(shelfBarcode);
		} catch (Exception e) {
			logger.error("confirmshelf error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App商品入库(正式货架)
     * 
     * @param boxId
     * @return
     */
	@RequestMapping(value="formalinstock", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse formalInstock(@RequestBody List<String> barcodeList){
		
		try {
			return appAdminService.instockFormalService(barcodeList);
		} catch (Exception e) {
			logger.error("formalinstock error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App返品订单搜索
     * 
     * @param searchCondition
     * @return
     */
	@RequestMapping(value="searchorders", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse searchRejectOrders(@RequestParam("searchCondition") String searchCondition,
			@RequestParam("sinceId") String sinceId,
			@RequestParam("size") String size){
		
		try {
			return appAdminService.searchRejectOrdersService(searchCondition,AppProductService.ORDERS_STATUS_SEND,AppProductService.ORDERS_FLAG_SEND,sinceId,size);
		} catch (Exception e) {
			logger.error("Search orders error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
     * 工厂App返品订单商品条码
     * 
     * @param searchCondition
     * @return
     */
	@RequestMapping(value="searchbarcode", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse searchRejectOrdersBarcode(@RequestParam("ordersId") String ordersId){
		
		try {
			return appAdminService.rejectOrdersInfoService(ordersId);
		} catch (Exception e) {
			logger.error("Search barcode error", e);
			return new BaseResponse(false);
		}
	}
	
	/**
	 * 工厂App发送返品请求
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@RequestMapping(value="rejectrequest", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse rejectOrdersRequest(@RequestParam("ordersId") String ordersId,
			@RequestParam("ordersRejectPattern") String ordersRejectPattern,
			@RequestParam("ordersPrice") String ordersPrice){
		
		try {
			return appAdminService.rejectRequestService(ordersId,ordersRejectPattern,ordersPrice);
		} catch (Exception e) {
			logger.error("Search barcode error", e);
			return new BaseResponse(false);
		}
	}
}
