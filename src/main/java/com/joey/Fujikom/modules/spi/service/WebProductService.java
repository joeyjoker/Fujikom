package com.joey.Fujikom.modules.spi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.config.Global;
import com.joey.Fujikom.common.mapper.BeanMapper;
import com.joey.Fujikom.common.utils.StringUtils;
import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.dao.AppBoxDao;
import com.joey.Fujikom.modules.spi.dao.AppMemberDao;
import com.joey.Fujikom.modules.spi.dao.AppOrdersDao;
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.dao.BarcodeDao;
import com.joey.Fujikom.modules.spi.entity.Barcode;
import com.joey.Fujikom.modules.spi.entity.Box;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.entity.Product;
import com.joey.Fujikom.modules.spi.request.OrdersRequestData;
import com.joey.Fujikom.modules.spi.request.ProductRequestData;
import com.joey.Fujikom.modules.spi.response.BarcodeView;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.BoxResponseData;
import com.joey.Fujikom.modules.spi.response.BoxUncheckData;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.response.MemberInfoData;
import com.joey.Fujikom.modules.spi.response.MemberResponseData;
import com.joey.Fujikom.modules.spi.response.PageEntity;
import com.joey.Fujikom.modules.spi.response.ProductInstockData;
import com.joey.Fujikom.modules.spi.response.ProductResponseInfo;
import com.joey.Fujikom.modules.spi.response.ProductSendData;
import com.joey.Fujikom.modules.spi.utils.Caesar;
import com.joey.Fujikom.modules.spi.utils.FileS3Utils;
import com.joey.Fujikom.modules.spi.utils.BarcodeGen;

/**
 * AppProductService
 *
 * @author JoeyHuang
 * @version 2016-3-07
 */
@Service
@Transactional()
public class WebProductService {

	@Autowired
	private AppProductDao appProductDao;

	@Autowired
	private AppBoxDao appBoxDao;

	@Autowired
	private AppMemberDao appMemberDao;

	@Autowired
	private BarcodeDao barcodeDao;

	@Autowired
	private AppOrdersDao appOrdersDao;

	@Autowired
	private MemberResponseData memberResponseData;

	@Autowired
	private MemberInfoData memberInfoData;

	/**
	 * web全权委托
	 *
	 * @param memberId
	 * @param productNumber
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse entrustProduct(Long memberId, String productNumber) {
		Box box = new Box();
		Member member = new Member();
		member.setMemberId(memberId);
		box.setMember(member);
		box.setBoxProductNumber(productNumber);
		box.setBoxFlag(AppProductService.BOX_FLAG_ENTRUST);
		box.setBoxStatus(AppProductService.BOX_STATUS_DEFAULT);
		box.setBoxCreateDate(new Date());
		box.setBoxDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
		appBoxDao.saveEntity(box);
		String oldId = String.valueOf(box.getBoxId());
		// 创建箱子管理番号
		String strNumber = StringUtils.leftPad(oldId, 11, "0");
		Caesar caesar = new Caesar();
		String adminNumber = caesar.encode(32, strNumber);
		adminNumber = "2" + adminNumber + "8";
		// 保存箱子管理番号
		Box oldBox = appBoxDao.get(Long.parseLong(oldId));
		oldBox.setBoxAdminNumber(adminNumber);
		appBoxDao.saveEntity(oldBox);
		return new BaseResponse(true, adminNumber, "success");

	}

	/**
	 * web获取全权委托列表
	 *
	 * @param memberId
	 * @param pageIndex
	 * @return
	 */
	public BaseResponse entrustListService(Long memberId, Integer pageIndex) {
		
		Integer size = Integer.valueOf(Global.getPagesize());
		Integer startIndex = (pageIndex - 1) * size;

		long count = appBoxDao.countEntrustBoxes(memberId).size();

		List<Box> boxes = appBoxDao.findEntrustBoxes(memberId,
				startIndex, size);
		List<BoxUncheckData> boxdata = new ArrayList<>();
		for (int i = 0; i < boxes.size(); i++) {
			BoxUncheckData data = new BoxUncheckData();
			if (boxes.get(i) != null) {

				BeanMapper.copy(boxes.get(i), data);
				boxdata.add(data);
			}
		}
		
		PageEntity<BoxUncheckData> pageEntity = new PageEntity<BoxUncheckData>();
		pageEntity.setMaxSize((int) count);
		pageEntity.setList(boxdata);
		pageEntity.setMaxPage((int) ((count + size - 1) / size));
		return new BaseResponse(true, pageEntity, "success");
	}

	/**
	 * web删除全权委托箱子
	 *
	 * @param boxId
	 * @return
	 */
	public BaseResponse delBoxService(String boxId) {
		Box box = appBoxDao.get(Long.parseLong(boxId));
		if (box != null) {

			box.setBoxDelFlag(AppProductService.PRODUCT_DEL_TRUE);
			appBoxDao.saveEntity(box);
			return new BaseResponse(true, "success");
		} else {
			return new BaseResponse(false,new ErrorResponse(ResultConstant.BOX_NOT_EXIST,
					ResultConstant.BOX_NOT_EXIST_MSG));
		}
	}

	/**
	 * web普通方案生成条形码
	 *
	 * @param memberId
	 * @param productNumber
	 * @return
	 */
	@Transactional(readOnly = false)
	public String barcodeGenService(Integer productNumber, Long memberId) {

		HashSet<String> barcodeset = new HashSet<>();
		String barcodeValue = "";
		while (barcodeset.size() < productNumber) {
			String barcodeString = BarcodeGen.get();
			List<String> listBarcodeStrings = barcodeDao.getBarcodeValue();
			if (!listBarcodeStrings.contains(barcodeString)) {
				barcodeset.add(barcodeString);
				// 将新创建的条形码保存到数据库
				Barcode barcode = new Barcode();
				barcode.setBarcodeValue(barcodeString);
				barcodeDao.saveEntity(barcode);
				barcodeValue = barcodeValue + barcodeString + ",";
			}
		}

		return barcodeValue;
	}

	public List<BarcodeView> barcodeViewService(String barcodeValue) {

		String[] barcodeStrings = barcodeValue.split(",");
		List<BarcodeView> barcodeViewList = new ArrayList<>();
		for (int i = 0; i < barcodeStrings.length; i++) {
			BarcodeView barcodeView = new BarcodeView();
			Product product = appProductDao.getFromBarcode(barcodeStrings[i]);
			if (product != null) {
				barcodeView.setProductbarcode(product.getProductBarcode());
				barcodeView.setProductName(product.getProductName());
			}
			barcodeViewList.add(barcodeView);
		}
		return barcodeViewList;

	}

	/**
	 * web普通方案注册商品
	 *
	 * @param memberId
	 * @param products
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public BaseResponse normalLoginProduct(Long memberId,
			List<ProductRequestData> products) throws Exception {

		List<String> productBarcode = appProductDao.getProductBarcode();
		BoxResponseData data = new BoxResponseData();
		//检查条码是否存在
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
			// 生成条码
			String barcodeString = barcodeGenService(products.size(), memberId);
			String[] barcodeValue = barcodeString.split(",");
			Box box = new Box();
			Member member = new Member();
			member.setMemberId(memberId);
			box.setBoxFlag(AppProductService.BOX_FLAG_NORMAL);
			box.setBoxStatus(AppProductService.BOX_STATUS_UNCHECKED);
			box.setBoxProductNumber(String.valueOf(products.size()));
			box.setMember(member);
			box.setBoxCreateDate(new Date());
			box.setBoxDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
			box.setBoxBarcodeHistory(Global.getServerPath()
					+ "/spi/product/barcodeview?barcodeValue=" + barcodeString
					+ "&memberId=" + memberId);
			appBoxDao.saveEntity(box);
			String oldId = String.valueOf(box.getBoxId());
			// 创建箱子管理番号
			String strNumber = StringUtils.leftPad(oldId, 11, "0");
			Caesar caesar = new Caesar();
			String adminNumber = caesar.encode(32, strNumber);
			adminNumber = "2" + adminNumber + "8";
			// 保存箱子管理番号
			Box oldBox = appBoxDao.get(Long.parseLong(oldId));
			oldBox.setBoxAdminNumber(adminNumber);
			appBoxDao.saveEntity(oldBox);
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
				product.setProductBarcode(barcodeValue[i]);
				product.setProductName(products.get(i).getProductName());
				product.setProductSize("");
				product.setProductStatus(AppProductService.PRODUCT_STATUS_DELIVERY);
				product.setDelFlag(AppProductService.PRODUCT_DEL_DEFAULT);
				product.setMember(member);
				product.setBox(oldBox);
				product.setProductCreateDate(createDate);
				appProductDao.saveEntity(product);

				BeanMapper.copy(oldBox, data);

			}

			return new BaseResponse(true, data, "登録完了しました");
		} else {
			return new BaseResponse(false, "既に存在するバーコードです");
		}
	}

	/* *//**
	 * web获取处理中商品列表
	 *
	 * @param memberId
	 * @return
	 */
	/*
	 * @Transactional(readOnly = false) public BaseResponse
	 * getProcessingProducts(String memberId, Integer sinceId, Integer size) {
	 * 
	 * List<Product> products = appProductDao.findProcessingProductList(
	 * Long.parseLong(memberId), AppProductService.PRODUCT_DEL_DEFAULT,
	 * AppProductService.PRODUCT_STATUS_DELIVERY, sinceId, size);
	 * List<ProductProcessingData> productProcessDatas = new ArrayList<>(); for
	 * (int i = 0; i < products.size(); i++) { ProductProcessingData
	 * processingData = new ProductProcessingData();
	 * processingData.setBox(products.get(i).getBox());
	 * processingData.setProductId(products.get(i).getProductId());
	 * processingData.setProductBarcode(products.get(i) .getProductBarcode());
	 * processingData.setProductCreateDate(products.get(i)
	 * .getProductCreateDate());
	 * processingData.setProductImageFour(products.get(i)
	 * .getProductImageFour());
	 * processingData.setProductImageOne(products.get(i) .getProductImageOne());
	 * processingData.setProductImageTwo(products.get(i) .getProductImageTwo());
	 * processingData.setProductImageThree(products.get(i)
	 * .getProductImageThree());
	 * processingData.setProductMemo(products.get(i).getProductMemo());
	 * processingData.setProductName(products.get(i).getProductName());
	 * processingData.setProductSize(products.get(i).getProductSize());
	 * processingData.setProductStatus(products.get(i).getProductStatus());
	 * productProcessDatas.add(processingData); } return new BaseResponse(true,
	 * productProcessDatas, "success"); }
	 */

	/**
	 * web获取寄送到仓库的箱子
	 *
	 * @param memberId
	 * @param pageIndex
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getUncheckBoxes(Long memberId, Integer pageIndex) {

		Integer size = Integer.valueOf(Global.getPagesize());
		Integer startIndex = (pageIndex - 1) * size;

		long count = appBoxDao.countUncheckBoxes(memberId);

		List<Box> boxList = appBoxDao.findUncheckBoxesForWeb(memberId,
				startIndex, size);

		List<BoxUncheckData> list = boxList.parallelStream().map(p -> {
			BoxUncheckData data = new BoxUncheckData();
			BeanMapper.copy(p, data);
			return data;
		}).collect(Collectors.toList());

		/*
		 * <<<<<<< HEAD List<Product> products = appProductDao.findProductList(
		 * Long.parseLong(memberId), PRODUCT_DEL_DEFAULT,
		 * PRODUCT_STATUS_INSTOCK,
		 * PRODUCT_STATUS_PROCESSING,PRODUCT_STATUS_UNSEND, sinceId, size);
		 * List<ProductInstockData> productInstockDatas = new ArrayList<>(); for
		 * (int i = 0; i < products.size(); i++) { ProductInstockData
		 * instockData = new ProductInstockData();
		 * BeanMapper.copy(products.get(i), instockData);
		 * productInstockDatas.add(instockData); } return new BaseResponse(true,
		 * productInstockDatas, "success"); } =======
		 */
		PageEntity<BoxUncheckData> pageEntity = new PageEntity<BoxUncheckData>();
		pageEntity.setMaxSize((int) count);
		pageEntity.setList(list);
		pageEntity.setMaxPage((int) ((count + size - 1) / size));
		return new BaseResponse(true, pageEntity, "success");

	}

	/**
	 * web获取入库商品列表
	 *
	 * @param memberId
	 * @param pageIndex
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getInstockProducts(Long memberId, Integer pageIndex) {

		Integer size = Integer.valueOf(Global.getPagesize());
		Integer startIndex = (pageIndex - 1) * size;

		long count = appProductDao.findAllProductList(memberId,
				AppProductService.PRODUCT_DEL_DEFAULT,
				AppProductService.PRODUCT_STATUS_UNSEND,
				AppProductService.PRODUCT_STATUS_INSTOCK,
				AppProductService.PRODUCT_STATUS_PROCESSING).size();

		List<Product> products = appProductDao.findProductList(memberId,
				AppProductService.PRODUCT_DEL_DEFAULT,
				AppProductService.PRODUCT_STATUS_UNSEND,
				AppProductService.PRODUCT_STATUS_INSTOCK,
				AppProductService.PRODUCT_STATUS_PROCESSING, startIndex, size);
		List<ProductInstockData> productInstockDatas = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			ProductInstockData instockData = new ProductInstockData();
			BeanMapper.copy(products.get(i), instockData);
			productInstockDatas.add(instockData);
		}
		PageEntity<ProductInstockData> pageEntity = new PageEntity<ProductInstockData>();
		pageEntity.setMaxSize((int) count);
		pageEntity.setList(productInstockDatas);
		pageEntity.setMaxPage((int) ((count + size - 1) / size));
		return new BaseResponse(true, pageEntity, "success");
	}

	/**
	 * web获取商品信息
	 *
	 * @param productId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getProductInfoService(String productId) {

		Product product = appProductDao.get(Long.parseLong(productId));
		if (product != null) {

			ProductResponseInfo productResponseInfo = new ProductResponseInfo();
			BeanMapper.copy(product, productResponseInfo);
			return new BaseResponse(true, productResponseInfo, "success");
		} else {
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.PRODUCT_NOT_EXIST,
					ResultConstant.PRODUCT_NOT_EXIST_MSG));
		}
	}

	/**
	 * web修改商品信息
	 *
	 * @param productId
	 * @param productName
	 * @param productMemo
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse editProductInfoService(String productId,
			String productName, String productMemo) {

		Product product = appProductDao.get(Long.parseLong(productId));
		if (product != null) {

			if (product.getProductStatus().equals(
					AppProductService.PRODUCT_STATUS_DELIVERY)
					|| product.getProductStatus().equals(
							AppProductService.PRODUCT_STATUS_INSTOCK)
					|| product.getProductStatus().equals(
							AppProductService.PRODUCT_STATUS_PROCESSING)) {
				product.setProductName(productName);
				product.setProductMemo(productMemo);
				appProductDao.saveEntity(product);
			}
			if (product.getProductStatus().equals(
					AppProductService.PRODUCT_STATUS_SEND)) {
				product.setProductMemo(productMemo);
				appProductDao.saveEntity(product);
			}
			return new BaseResponse(true, "修正しました");
		} else {
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.PRODUCT_NOT_EXIST,
					ResultConstant.PRODUCT_NOT_EXIST_MSG));
		}
	}

	/**
	 * web删除商品信息
	 *
	 * @param productId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse deleteProductService(String productId) {

		Product product = appProductDao.get(Long.parseLong(productId));
		if (product != null) {

			product.setDelFlag(AppProductService.PRODUCT_DEL_TRUE);
			appProductDao.saveEntity(product);
			return new BaseResponse(true, "削除しました");
		} else {
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.PRODUCT_NOT_EXIST,
					ResultConstant.PRODUCT_NOT_EXIST_MSG));
		}
	}

	/**
	 * web用户提交发送商品请求
	 *
	 *@param memberId
	 *@param orderData
	 *@return
	 */
	@Transactional(readOnly = false)
	public BaseResponse shipRuquestService(Long memberId,
			OrdersRequestData orderData) {

		Orders orders = new Orders();// 新建一个订单
		Member member = new Member();
		member.setMemberId(memberId);
		List<String> productIdList = orderData.getProductIdList();
		orders.setMember(member);
		orders.setOrdersAddress(orderData.getOrderAddress());
		orders.setOrdersPrice(orderData.getOrderPrice());
		orders.setOrdersReceiverName(orderData.getOrderReceiverName());
		orders.setOrdersSenderName(orderData.getOrderSenderName());
		orders.setOrdersRequestDate(new Date());
		orders.setOrdersUpdateDate(new Date());
		orders.setOrdersZip(orderData.getOrderZip());
		orders.setOrdersTelphone(orderData.getOrderTelphone());
		orders.setOrdersProductsNumber(String.valueOf(orderData
				.getProductIdList().size()));
		orders.setOrdersBuliding(orderData.getOrderBuliding());
		orders.setOrdersLocation(orderData.getOrderLocation());
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_DEFAULT);
		orders.setOrdersFlag(AppProductService.ORDERS_FLAG_SEND);
		orders.setOrdersSenderAddress(orderData.getOrderSenderAddress());
		orders.setOrdersSenderTelphone(orderData.getOrderSenderTelphone());
		orders.setOrdersSenderZip(orderData.getOrderSenderZip());
		appOrdersDao.saveEntity(orders);

		String oldId = String.valueOf(orders.getOrdersId());
		// 创建订单番号
		String strNumber = StringUtils.leftPad(oldId, 15, "0");
		Caesar caesar = new Caesar();
		String ordersNumber = caesar.encode(32, strNumber);
		ordersNumber = "1" + ordersNumber;
		Orders oldOrders = appOrdersDao.get(Long.parseLong(oldId));
		oldOrders.setOrdersNumber(ordersNumber);
		appOrdersDao.saveEntity(orders);
		for (int i = 0; i < productIdList.size(); i++) {
			Product product = appProductDao.get(Long.parseLong(productIdList
					.get(i)));
			product.setOrders(orders);
			appProductDao.saveEntity(product);
		}
		return new BaseResponse(true, "success");
	}

	/**
	 * web用户获取发/取回商品信息
	 *
	 * @param memberId
	 * @param pageIndex
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getSendProductsService(Long memberId, Integer pageIndex) {

		Integer size = Integer.valueOf(Global.getPagesize());
		Integer startIndex = (pageIndex - 1) * size;

		long count = appProductDao.countProducts(memberId, AppProductService.PRODUCT_DEL_DEFAULT,
				AppProductService.PRODUCT_STATUS_SEND,
				AppProductService.PRODUCT_STATUS_RETURN).size();
		List<Product> products = appProductDao.findProducts(memberId,
				AppProductService.PRODUCT_DEL_DEFAULT,
				AppProductService.PRODUCT_STATUS_SEND,
				AppProductService.PRODUCT_STATUS_RETURN, startIndex, size);
		List<ProductSendData> productSendData = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			ProductSendData p = new ProductSendData();
			BeanMapper.copy(products.get(i), p);
			if (products.get(i) != null && products.get(i).getOrders() != null) {

				p.setSendCompany(products.get(i).getOrders().getOrdersCompany());
				p.setSendConsultNumber(products.get(i).getOrders()
						.getOrdersConsultNumber());
				p.setProductNumber(products.get(i).getOrders()
						.getOrdersProductsNumber());
			}
			productSendData.add(p);
		}
		PageEntity<ProductSendData> pageEntity = new PageEntity<ProductSendData>();
		pageEntity.setMaxSize((int) count);
		pageEntity.setList(productSendData);
		pageEntity.setMaxPage((int) ((count + size - 1) / size));
		return new BaseResponse(true, pageEntity, "success");
	}

	/**
	 * web用户取回请求
	 *
	 * @param memberId
	 * @param ordersData
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse returnProductService(Long memberId,
			OrdersRequestData ordersData) {

		Orders orders = new Orders();
		Member member = new Member();
		member.setMemberId(memberId);
		List<String> productIdList = ordersData.getProductIdList();
		orders.setMember(member);
		orders.setOrdersAddress(ordersData.getOrderAddress());
		orders.setOrdersPrice(ordersData.getOrderPrice());
		orders.setOrdersReceiverName(ordersData.getOrderReceiverName());
		orders.setOrdersSenderName(ordersData.getOrderSenderName());
		orders.setOrdersRequestDate(new Date());
		orders.setOrdersUpdateDate(new Date());
		orders.setOrdersZip(ordersData.getOrderZip());
		orders.setOrdersTelphone(ordersData.getOrderTelphone());
		orders.setOrdersProductsNumber(String.valueOf(ordersData
				.getProductIdList().size()));
		orders.setOrdersBuliding(ordersData.getOrderBuliding());
		orders.setOrdersLocation(ordersData.getOrderLocation());
		orders.setOrdersStatus(AppProductService.ORDERS_STATUS_DEFAULT);
		orders.setOrdersFlag(AppProductService.ORDERS_FLAG_RETUEN);
		orders.setOrdersSenderAddress(ordersData.getOrderSenderAddress());
		orders.setOrdersSenderTelphone(ordersData.getOrderSenderTelphone());
		orders.setOrdersSenderZip(ordersData.getOrderSenderZip());
		appOrdersDao.saveEntity(orders);
		String oldId = String.valueOf(orders.getOrdersId());
		// 创建订单番号
		String strNumber = "";
		if (oldId.length() < 15) {
			for (int i = 0; i < 15 - oldId.length(); i++) {
				strNumber = strNumber + "0";
			}
		}
		strNumber = strNumber + oldId;
		Caesar caesar = new Caesar();
		String ordersNumber = caesar.encode(32, strNumber);
		ordersNumber = "1" + ordersNumber;
		Orders oldOrders = appOrdersDao.get(Long.parseLong(oldId));
		oldOrders.setOrdersNumber(ordersNumber);
		appOrdersDao.saveEntity(orders);
		for (int i = 0; i < productIdList.size(); i++) {
			Product product = appProductDao.get(Long.parseLong(productIdList
					.get(i)));
			product.setOrders(orders);
			appProductDao.saveEntity(product);
		}
		return new BaseResponse(true, "success");
	}

	/**
	 * web用户搜索入库商品
	 *
	 * @param searchString
	 * @param memberId
	 * @param pageIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public BaseResponse searchService(String searchString, Long memberId,
			Integer pageIndex) {
		
		Integer size = Integer.valueOf(Global.getPagesize());
		Integer startIndex = (pageIndex - 1) * size;

		DetachedCriteria dc = appProductDao.createDetachedCriteria();
		dc.createAlias("member", "a").add(
				Restrictions.eq("a.memberId", memberId));

		dc.add(Restrictions.eq("productStatus",
				AppProductService.PRODUCT_STATUS_INSTOCK));
		dc.add(Restrictions
				.eq("delFlag", AppProductService.PRODUCT_DEL_DEFAULT));
		dc.add(Restrictions.or(
				Restrictions.like("productName", "%" + searchString + "%"),
				Restrictions.like("productBarcode", "%" + searchString + "%")));
		Criteria cri = dc.getExecutableCriteria(appProductDao.getSession());
		cri.setFirstResult(startIndex);
		cri.setMaxResults(size);
		List<Product> products = cri.list();

		long count = appProductDao.count(dc);
		
		List<ProductInstockData> productInstockDatas = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getOrders() == null) {

				ProductInstockData instockData = new ProductInstockData();
				BeanMapper.copy(products.get(i), instockData);
				productInstockDatas.add(instockData);
			}
		}
		PageEntity<ProductInstockData> pageEntity = new PageEntity<ProductInstockData>();
		pageEntity.setMaxSize((int) count);
		pageEntity.setList(productInstockDatas);
		pageEntity.setMaxPage((int) ((count + size - 1) / size));
		return new BaseResponse(true, productInstockDatas, "success");

	}

}
