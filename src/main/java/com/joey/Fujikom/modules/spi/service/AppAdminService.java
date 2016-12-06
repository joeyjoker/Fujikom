package com.joey.Fujikom.modules.spi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.joey.Fujikom.common.mapper.BeanMapper;
import com.joey.Fujikom.common.utils.StringUtils;
import com.joey.Fujikom.modules.spi.dao.AppBoxDao;
import com.joey.Fujikom.modules.spi.dao.AppMemberDao;
import com.joey.Fujikom.modules.spi.dao.AppOrdersDao;
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.dao.BarcodeRequestDao;
import com.joey.Fujikom.modules.spi.entity.BarcodeRequest;
import com.joey.Fujikom.modules.spi.entity.Box;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.entity.Product;
import com.joey.Fujikom.modules.spi.request.AdminProductRequestData;
import com.joey.Fujikom.modules.spi.request.InstockResquestData;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.ConfirmResponseData;
import com.joey.Fujikom.modules.spi.utils.FileS3Utils;

/**
 * AppAdminService
 * 
 * @author JoeyHuang
 * @version 2016-3-09
 */
@Service
@Transactional()
public class AppAdminService {

	@Autowired
	private AppBoxDao appBoxDao;

	@Autowired
	private BarcodeRequestDao barcodeRequestDao;

	@Autowired
	private AppProductDao appProductDao;

	@Autowired
	private AppMemberDao appMemberDao;

	@Autowired
	private AppOrdersDao appOrdersDao;

	public static String SHELF_STATUS_TMP = "1";
	public static String SHELF_STATUS_FORMAL = "0";

	/**
	 * 工厂App查询
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse searchBoxesService(String searchCondition,
			String boxStatus, String sinceId, String size) {

		List<Box> boxList = new ArrayList<>();
		/*List<Box> boxFindByRealNameList = appBoxDao.findByRealName(
				searchCondition, boxStatus, Integer.parseInt(sinceId),
				Integer.parseInt(size));

		List<Box> boxFindByTelphoneList = appBoxDao.findByTelphone(
				searchCondition, boxStatus, Integer.parseInt(sinceId),
				Integer.parseInt(size));
		List<Box> boxFindByAdminNumberList = appBoxDao.findByAdminNumber(
				searchCondition, boxStatus, Integer.parseInt(sinceId),
				Integer.parseInt(size));
		if (boxFindByRealNameList.size() > 0) {
			for (int i = 0; i < boxFindByRealNameList.size(); i++) {
				boxList.add(boxFindByRealNameList.get(i));
			}
		}
		if (boxFindByTelphoneList.size() > 0) {
			for (int i = 0; i < boxFindByTelphoneList.size(); i++) {
				boxList.add(boxFindByTelphoneList.get(i));
			}
		}
		if (boxFindByAdminNumberList.size() > 0) {
			for (int i = 0; i < boxFindByAdminNumberList.size(); i++) {
				boxList.add(boxFindByAdminNumberList.get(i));
			}
		}*/
		boxList = appBoxDao.searchBoxes(searchCondition, boxStatus, Integer.parseInt(sinceId), Integer.parseInt(size));
		return new BaseResponse(true, boxList, "success");

	}

	/**
	 * App全权委托注册商品
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public BaseResponse trustLoginProduct(List<AdminProductRequestData> products)
			throws Exception {

		List<String> productBarcode = appProductDao.getProductBarcode();
		Boolean flag = true;
		for (int i = 0; i < products.size(); i++) {
			if (productBarcode.contains(products.get(i).getProductBarcode())) {
				flag = false;
				break;
			}
		}
		if (flag) {

			Date createDate = new Date();
			Base64 base64 = new Base64();
			Box box = appBoxDao.get(Long.parseLong(products.get(0).getBoxId()));
			Member member = appMemberDao.get(Long.parseLong(products.get(0)
					.getMemberId()));
			for (int i = 0; i < products.size(); i++) {
				Product product = new Product();
				BeanMapper.copy(products.get(i), product);
				if (product.getProductImageOne() != null
						&& product.getProductImageOne() != ""
						&& StringUtils.isNotBlank(products.get(i)
								.getProductImageOneType())) {
					byte[] b = base64.decode(product.getProductImageOne());
					product.setProductImageOne(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageOneType()));
				}
				if (product.getProductImageTwo() != null
						&& product.getProductImageTwo() != ""
						&& StringUtils.isNotBlank(products.get(i)
								.getProductImageTwoType())) {
					byte[] b = base64.decode(product.getProductImageTwo());
					product.setProductImageTwo(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageTwoType()));
				}
				if (product.getProductImageThree() != null
						&& product.getProductImageThree() != ""
						&& StringUtils.isNotBlank(products.get(i)
								.getProductImageThreeType())) {
					byte[] b = base64.decode(product.getProductImageThree());
					product.setProductImageThree(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageThreeType()));
				}
				if (product.getProductImageFour() != null
						&& product.getProductImageFour() != ""
						&& StringUtils.isNotBlank(products.get(i)
								.getProductImageFourType())) {
					byte[] b = base64.decode(product.getProductImageFour());
					product.setProductImageFour(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageFourType()));
				}
				product.setProductName(products.get(i).getProductName());
				product.setProductSize("default");
				product.setProductStatus(AppProductService.PRODUCT_STATUS_PROCESSING);
				product.setDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
				product.setMember(member);
				product.setBox(box);
				product.setProductCreateDate(createDate);
				product.setProductBarcode(products.get(i).getProductBarcode());
				appProductDao.saveEntity(product);

			}

			box.setBoxProductNumber(String.valueOf(products.size()));
			box.setBoxStatus(AppProductService.BOX_STATUS_CHECKED);
			appBoxDao.saveEntity(box);
			return new BaseResponse(true, "success");
		} else {
			return new BaseResponse(false, "条形码已存在");
		}
	}

	/**
	 * App全权委托注册商品2(multipartfile)
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public BaseResponse trustRegisterProduct(MultipartFile productImageOne,
			MultipartFile productImageTwo, MultipartFile productImageThree,
			MultipartFile productImageFour, String productName,
			String productMemo, String productBarcode, String memberId,
			String boxId, String productNumber, String index) throws Exception {
		Boolean flag = true;

		List<String> barcodeList = appProductDao.getProductBarcode();
		if (barcodeList.contains(productBarcode)) {
			flag = false;
		}
		if (flag) {
			Box box = appBoxDao.get(Long.parseLong(boxId));
			Member member = appMemberDao.get(Long.parseLong(memberId));
			Product product = new Product();
			if (productImageOne != null) {

				String filename1 = productImageOne.getOriginalFilename();
				String type1 = filename1
						.substring(filename1.lastIndexOf(".") + 1);
				product.setProductImageOne(FileS3Utils.fileUploadToS3(
						productImageOne, type1));
			}
			if (productImageTwo != null) {

				String filename2 = productImageOne.getOriginalFilename();
				String type2 = filename2
						.substring(filename2.lastIndexOf(".") + 1);
				product.setProductImageTwo(FileS3Utils.fileUploadToS3(
						productImageTwo, type2));
			}
			if (productImageThree != null) {

				String filename3 = productImageOne.getOriginalFilename();
				String type3 = filename3
						.substring(filename3.lastIndexOf(".") + 1);
				product.setProductImageThree(FileS3Utils.fileUploadToS3(
						productImageThree, type3));
			}
			if (productImageFour != null) {

				String filename4 = productImageOne.getOriginalFilename();
				String type4 = filename4
						.substring(filename4.lastIndexOf(".") + 1);
				product.setProductImageFour(FileS3Utils.fileUploadToS3(
						productImageFour, type4));
			}
			product.setProductName(productName);
			product.setProductMemo(productMemo);
			product.setProductBarcode(productBarcode);
			product.setMember(member);
			product.setBox(box);
			product.setProductCreateDate(new Date());
			product.setProductSize("");
			product.setProductStatus(AppProductService.PRODUCT_STATUS_PROCESSING);
			product.setDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
			appProductDao.saveEntity(product);

			DetachedCriteria dc = appProductDao.createDetachedCriteria();
			dc.add(Restrictions.eq("box", box));
			List<Product> products = appProductDao.find(dc);
			if (products.size() == Integer.parseInt(productNumber)) {
				box.setBoxProductNumber(String.valueOf(productNumber));
				box.setBoxStatus(AppProductService.BOX_STATUS_CHECKED);
				appBoxDao.saveEntity(box);

			}
			return new BaseResponse(true, index);
		} else {
			return new BaseResponse(false, "条形码已存在");
		}

	}
	
	/**
	 * App全权委托注册商品3(Base64)
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public BaseResponse trustRegisterProductService(String productImageOne,
			String productImageTwo, String productImageThree,
			String productImageFour, String productImageOneType,
			String productImageTwoType, String productImageThreeType,
			String productImageFourType,String productName,
			String productMemo, String productBarcode, String memberId,
			String boxId, String productNumber, String index) throws Exception {
		Boolean flag = true;

		List<String> barcodeList = appProductDao.getProductBarcode();
		Base64 base64 = new Base64();
		if (barcodeList.contains(productBarcode)) {
			flag = false;
		}
		if (flag) {
			Box box = appBoxDao.get(Long.parseLong(boxId));
			Member member = appMemberDao.get(Long.parseLong(memberId));
			Product product = new Product();
			if (StringUtils.check(productImageOne)&&StringUtils.check(productImageOneType)) {

				byte[] b = base64.decode(productImageOne);
				product.setProductImageOne(FileS3Utils.ImageUpload(b,
						productImageOneType));
			}
			if (StringUtils.check(productImageTwo)&&StringUtils.check(productImageTwoType)) {

				byte[] b = base64.decode(productImageTwo);
				product.setProductImageTwo(FileS3Utils.ImageUpload(b,
						productImageTwoType));
			}
			if (StringUtils.check(productImageThree)&&StringUtils.check(productImageThreeType)) {

				byte[] b = base64.decode(productImageThree);
				product.setProductImageThree(FileS3Utils.ImageUpload(b,
						productImageThreeType));
			}
			if (StringUtils.check(productImageFour)&&StringUtils.check(productImageFourType)) {

				byte[] b = base64.decode(productImageFour);
				product.setProductImageFour(FileS3Utils.ImageUpload(b,
						productImageFourType));
			}
			product.setProductName(productName);
			product.setProductMemo(productMemo);
			product.setProductBarcode(productBarcode);
			product.setMember(member);
			product.setBox(box);
			product.setProductCreateDate(new Date());
			product.setProductSize("");
			product.setProductStatus(AppProductService.PRODUCT_STATUS_PROCESSING);
			product.setDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
			appProductDao.saveEntity(product);

			DetachedCriteria dc = appProductDao.createDetachedCriteria();
			dc.add(Restrictions.eq("box", box));
			List<Product> products = appProductDao.find(dc);
			if (products.size() == Integer.parseInt(productNumber)) {
				box.setBoxProductNumber(String.valueOf(productNumber));
				box.setBoxStatus(AppProductService.BOX_STATUS_CHECKED);
				appBoxDao.saveEntity(box);

			}
			return new BaseResponse(true, index);
		} else {
			return new BaseResponse(false, "条形码已存在");
		}

	}

	/**
	 * 工厂App请求条形码
	 * 
	 * @param barcodeNumber
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse barcodeRequestService(String barcodeNumber) {

		BarcodeRequest br = new BarcodeRequest();
		Date requestDate = new Date();
		br.setBarcodeNumber(barcodeNumber);
		br.setBarcodeRequestDate(requestDate);
		br.setDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
		barcodeRequestDao.saveEntity(br);
		return new BaseResponse(true, "success");
	}

	/**
	 * 工厂App检查商品
	 * 
	 * @param boxId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse productCheckService(String boxId) {

		Box box = appBoxDao.get(Long.parseLong(boxId));
		box.setBoxStatus(AppProductService.BOX_STATUS_CHECKED);
		appBoxDao.saveEntity(box);
		List<Product> products = appProductDao
				.getFromBox(Long.parseLong(boxId));
		for (int i = 0; i < products.size(); i++) {
			Product p = products.get(i);
			p.setProductStatus(AppProductService.PRODUCT_STATUS_PROCESSING);
			appProductDao.saveEntity(p);
		}

		return new BaseResponse(true, "success");
	}

	/**
	 * 工厂App商品入库(临时)
	 * 
	 * @param instockData
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse instockTemService(List<InstockResquestData> instockData) {

		List<InstockResquestData> res = new ArrayList<>();
		for (int i = 0; i < instockData.size(); i++) {
			Product p = appProductDao.getFromBarcode(instockData.get(i)
					.getProductBarcode());
			if (p != null) {

				if (p.getProductStatus().equals("P")) {

					productInstockTemService(p, instockData.get(i));

				} else if (p.getProductStatus().equals("S")) {
					Orders orders = p.getOrders();
					if (orders != null) {
						if (orders.getOrdersFlag().equals(
								AppProductService.ORDERS_FLAG_REJECT)
								&& orders.getOrdersStatus().equals(
										AppProductService.ORDERS_STATUS_SEND)) {
							productInstockTemService(p, instockData.get(i));
						} else {
							res.add(instockData.get(i));
						}
					} else {
						res.add(instockData.get(i));
					}
				} else {
					res.add(instockData.get(i));
				}

			}
		}
		return new BaseResponse(true, res, "未成功入库" + res.size() + "条数据");
	}

	@Transactional(readOnly = false)
	public void productInstockTemService(Product product,
			InstockResquestData data) {

		product.setProductSize(data.getProductSize());
		product.setProductShelfNumber(data.getProductShelf());
		product.setProductShelfStatus(SHELF_STATUS_TMP);
		appProductDao.saveEntity(product);
	}

	/**
	 * 工厂App确认货架号
	 * 
	 * @param instockData
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse confirmShelfService(String shelfBarcode) {
		ConfirmResponseData confirmdata = new ConfirmResponseData();
		List<String> barcodeList = appProductDao
				.getBarcodeFromShelf(shelfBarcode);
		confirmdata.setBarcodeList(barcodeList);
		Product product = appProductDao.getFromBarcode(barcodeList.get(0));
		if (product != null) {
			String size = product.getProductSize();
			confirmdata.setSize(size);
		}
		return new BaseResponse(true, confirmdata, "success");
	}

	/**
	 * 工厂App商品入库(正式)
	 * 
	 * @param instockData
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse instockFormalService(List<String> barcodeList) {
		List<Product> productList = new ArrayList<>();// 入库商品集合
		for (int i = 0; i < barcodeList.size(); i++) {
			Product product = appProductDao.getFromBarcode(barcodeList.get(i));
			if (product.getProductShelfStatus().equals(SHELF_STATUS_TMP)) {
				Date date = new Date();
				product.setProductShelfStatus(SHELF_STATUS_FORMAL);
				product.setProductInstockDate(date);
				product.setProductCostDate(date);
				product.setProductStatus(AppProductService.PRODUCT_STATUS_INSTOCK);
				appProductDao.saveEntity(product);
				productList.add(product);
			}
		}
		// 检查箱子中商品是否全部入库
		for (int i = 0; i < productList.size(); i++) {
			Product product = productList.get(i);
			Box box = product.getBox();
			if (box.getBoxStatus().equals(AppProductService.BOX_STATUS_CHECKED)) {

				// 获取箱子中所有商品
				DetachedCriteria dc = appProductDao.createDetachedCriteria();
				dc.add(Restrictions.eq("box", box));
				List<Product> products = appProductDao.find(dc);
				// list为记录商品是否入库
				List<String> list = new ArrayList<>();
				for (int j = 0; j < products.size(); j++) {

					if (products.get(j).getProductStatus()
							.equals(AppProductService.PRODUCT_STATUS_INSTOCK)) {
						list.add("true");
					} else {
						list.add("false");
					}

				}
				// 如果箱子中商品全部入库则将箱子状态更改为入库
				if (!list.contains("false")) {
					box.setBoxStatus(AppProductService.BOX_STATUS_INSTCOKED);
					appBoxDao.saveEntity(box);
				}
			}

		}
		return new BaseResponse(true, "success");
	}

	/**
	 * 工厂App查询(箱子状态为未检查和已检查)
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse searchUncheckBoxesService(String searchCondition,
			String boxStatus1, String boxStatus2, String sinceId, String size) {

		List<Box> boxList = new ArrayList<>();
		List<Box> boxFindByRealNameList = appBoxDao.UncheckFindByRealName(
				searchCondition, boxStatus1, boxStatus2,
				Integer.parseInt(sinceId), Integer.parseInt(size));
		List<Box> boxFindByTelphoneList = appBoxDao.UncheckFindByTelphone(
				searchCondition, boxStatus1, boxStatus2,
				Integer.parseInt(sinceId), Integer.parseInt(size));
		List<Box> boxFindByAdminNumberList = appBoxDao
				.UncheckFindByAdminNumber(searchCondition, boxStatus1,
						boxStatus2, Integer.parseInt(sinceId),
						Integer.parseInt(size));
		if (boxFindByRealNameList.size() > 0) {
			for (int i = 0; i < boxFindByRealNameList.size(); i++) {
				boxList.add(boxFindByRealNameList.get(i));
			}
		}
		if (boxFindByTelphoneList.size() > 0) {
			for (int i = 0; i < boxFindByTelphoneList.size(); i++) {
				boxList.add(boxFindByTelphoneList.get(i));
			}
		}
		if (boxFindByAdminNumberList.size() > 0) {
			for (int i = 0; i < boxFindByAdminNumberList.size(); i++) {
				boxList.add(boxFindByAdminNumberList.get(i));
			}
		}
		return new BaseResponse(true, boxList, "success");

	}

	/**
	 * 工厂App查询(箱子状态除未登录)
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse searchAllBoxesService(String searchCondition,
			String boxStatus, String sinceId, String size) {

		List<Box> boxList = new ArrayList<>();
		if (!searchCondition.equals("")) {

			List<Box> boxFindByRealNameList = appBoxDao.allFindByRealName(
					searchCondition, boxStatus, Integer.parseInt(sinceId),
					Integer.parseInt(size));
			List<Box> boxFindByTelphoneList = appBoxDao.allFindByTelphone(
					searchCondition, boxStatus, Integer.parseInt(sinceId),
					Integer.parseInt(size));
			List<Box> boxFindByAdminNumberList = appBoxDao
					.allFindByAdminNumber(searchCondition, boxStatus,
							Integer.parseInt(sinceId), Integer.parseInt(size));
			if (boxFindByRealNameList.size() > 0) {
				for (int i = 0; i < boxFindByRealNameList.size(); i++) {
					boxList.add(boxFindByRealNameList.get(i));
				}
			}
			if (boxFindByTelphoneList.size() > 0) {
				for (int i = 0; i < boxFindByTelphoneList.size(); i++) {
					boxList.add(boxFindByTelphoneList.get(i));
				}
			}
			if (boxFindByAdminNumberList.size() > 0) {
				for (int i = 0; i < boxFindByAdminNumberList.size(); i++) {
					boxList.add(boxFindByAdminNumberList.get(i));
				}
			}
		} else {
			boxList = appBoxDao.allFind(boxStatus, Integer.parseInt(sinceId),
					Integer.parseInt(size));
		}
		return new BaseResponse(true, boxList, "success");

	}

	/**
	 * 工厂App返品订单查询
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse searchRejectOrdersService(String searchCondition,
			String ordersStatus, String ordersFlag, String sinceId, String size) {

		List<Orders> ordersList = new ArrayList<>();
		ordersList = appOrdersDao.findRejectOrders(searchCondition,
				ordersStatus, ordersFlag, Integer.parseInt(sinceId),
				Integer.parseInt(size));
		return new BaseResponse(true, ordersList, "success");

	}

	/**
	 * 工厂App订单中商品条码查询
	 * 
	 * @param ordersId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse rejectOrdersInfoService(String ordersId) {

		List<String> barcodeList = appProductDao.getBarcodeFromOrders(Long
				.parseLong(ordersId));
		return new BaseResponse(true, barcodeList, "success");

	}

	/**
	 * 工厂App发送返品请求
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse rejectRequestService(String ordersId,
			String ordersRejectPattern, String ordersPrice) {
		Orders orders = appOrdersDao.get(Long.parseLong(ordersId));
		orders.setOrdersRejectPattern(ordersRejectPattern);
		orders.setOrdersDeliveryDate(new Date());
		orders.setOrdersRequestDate(new Date());
		orders.setOrdersUpdateDate(new Date());
		orders.setOrdersFlag(AppProductService.ORDERS_FLAG_REJECT);
		orders.setOrdersAccountFlag(AppProductService.ORDERS_ACCOUNT_DEFAULT);
		orders.setOrdersExportFlag(AppProductService.ORDERS_EXPORT_DEFAULT);
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_DEFAULT);
		orders.setOrdersPrice(ordersPrice);
		appOrdersDao.saveEntity(orders);
		return new BaseResponse(true, "success");
	}

	/**
	 * 工厂App根据条形码查询箱子
	 * 
	 * @param memberName
	 * @param memberPassword
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse searchFromBarcodeService(String productBarcode) {
		Product product = appProductDao.getFromBarcode(productBarcode);
		if (product != null) {
			List<Box> boxList = new ArrayList<>();
			if (product.getBox().getBoxStatus()
					.equals(AppProductService.BOX_STATUS_CHECKED)
					|| product.getBox().getBoxStatus()
							.equals(AppProductService.BOX_STATUS_UNCHECKED)) {

				boxList.add(product.getBox());
			}
			return new BaseResponse(true, boxList, "success");
		} else {
			return new BaseResponse(true,"not exist");
		}
	}

}
