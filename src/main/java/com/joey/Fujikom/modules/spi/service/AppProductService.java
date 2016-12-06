package com.joey.Fujikom.modules.spi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joey.Fujikom.common.mapper.BeanMapper;
import com.joey.Fujikom.common.utils.StringUtils;
import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.dao.AppBoxDao;
import com.joey.Fujikom.modules.spi.dao.AppMemberDao;
import com.joey.Fujikom.modules.spi.dao.AppOrdersDao;
import com.joey.Fujikom.modules.spi.dao.AppProductDao;
import com.joey.Fujikom.modules.spi.dao.BarcodeDao;
import com.joey.Fujikom.modules.spi.dao.ExpenseDao;
import com.joey.Fujikom.modules.spi.entity.Barcode;
import com.joey.Fujikom.modules.spi.entity.Box;
import com.joey.Fujikom.modules.spi.entity.Expense;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.entity.Orders;
import com.joey.Fujikom.modules.spi.entity.Product;
import com.joey.Fujikom.modules.spi.request.OrdersRequestData;
import com.joey.Fujikom.modules.spi.request.ProductRequestData;
import com.joey.Fujikom.modules.spi.response.BarcodeResponse;
import com.joey.Fujikom.modules.spi.response.BarcodeView;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.BoxUncheckData;
import com.joey.Fujikom.modules.spi.response.CostResponseData;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.response.MemberInfoData;
import com.joey.Fujikom.modules.spi.response.MemberResponseData;
import com.joey.Fujikom.modules.spi.response.ProductInstockData;
import com.joey.Fujikom.modules.spi.response.ProductProcessingData;
import com.joey.Fujikom.modules.spi.response.ProductResponseInfo;
import com.joey.Fujikom.modules.spi.response.ProductSendData;
import com.joey.Fujikom.modules.spi.utils.BarcodeGen;
import com.joey.Fujikom.modules.spi.utils.Caesar;
import com.joey.Fujikom.modules.spi.utils.CostUtil;
import com.joey.Fujikom.modules.spi.utils.FileS3Utils;

/**
 * AppProductService
 * 
 * @author JoeyHuang
 * @version 2016-3-07
 */
@Service
@Transactional()
public class AppProductService {

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
	
	@Autowired
	private ExpenseDao expenseDao;
	
	

	public static String BOX_FLAG_NORMAL = "N";// 普通方案
	public static String BOX_FLAG_ENTRUST = "T";// 全权委托方案

	public static String BOX_STATUS_DEFAULT = "NL";// 箱子未登录状态(全权委托)
	public static String BOX_STATUS_UNCHECKED = "NC";// 箱子未检查状态(普通)
	public static String BOX_STATUS_CHECKED = "C";// 箱子已检查状态
	public static String BOX_STATUS_INSTCOKED = "I";// 箱子已入库状态

	public static String PRODUCT_STATUS_DELIVERY = "D";
	public static String PRODUCT_STATUS_PROCESSING = "P";
	public static String PRODUCT_STATUS_INSTOCK = "I";
	public static String PRODUCT_STATUS_SEND = "S";
	public static String PRODUCT_STATUS_UNSEND = "NS";//商品待发送状态
	public static String PRODUCT_STATUS_RETURN = "R";

	public static String PRODUCT_DEL_DEFAULT = "0";
	public static String PRODUCT_DEL_TRUE = "1";

	public static String ORDERS_STATUS_DEFAULT = "1";// 订单未结算状态
	public static String ORDERS_STATUS_SEND = "0";   // 订单已结算状态

	public static String ORDERS_FLAG_SEND = "0";// 订单发货标记
	public static String ORDERS_FLAG_RETUEN = "1";// 订单取回标记
	public static String ORDERS_FLAG_REJECT = "2";// 订单退货标记
	
	public static String ORDERS_ACCOUNT_DEFAULT = "1";// 未结算标记
	
	public static String ORDERS_EXPORT_DEFAULT="1";// 未导出
	public static String ORDERS_EXPORT_DONE = "0"; // 已导出
	
	

	/**
	 * App全权委托
	 * 
	 * @param memberId
	 * @param productNumber
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse entrustProduct(String memberId, String productNumber) {
		Box box = new Box();
		Member member = new Member();
		member.setMemberId(Long.valueOf(memberId));
		box.setMember(member);
		box.setBoxProductNumber(productNumber);
		box.setBoxFlag(BOX_FLAG_ENTRUST);
		box.setBoxStatus(BOX_STATUS_DEFAULT);
		box.setBoxCreateDate(new Date());
		box.setBoxDelFlag(PRODUCT_DEL_DEFAULT);
		appBoxDao.saveEntity(box);
		String oldId = String.valueOf(box.getBoxId());
		// 创建箱子管理番号
		String strNumber = "";
		if (oldId.length() < 11) {
			for (int i = 0; i < 11 - oldId.length(); i++) {
				strNumber = strNumber + "0";
			}
		}
		strNumber = strNumber + oldId;
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
	 * App获取全权委托列表
	 * 
	 * @param memberId
	 * @param productNumber
	 * @return
	 */
	public BaseResponse entrustListService(String memberId,String sinceId,String size){
		
		List<Box> boxes = appBoxDao.findEntrustBoxes(Long.parseLong(memberId), Integer.parseInt(sinceId),Integer.parseInt(size) );
		List<BoxUncheckData> boxdata = new ArrayList<>();
		for (int i = 0; i < boxes.size(); i++) {
			BoxUncheckData data = new BoxUncheckData();
			if (boxes.get(i)!=null) {
				
			BeanMapper.copy(boxes.get(i), data);
			boxdata.add(data);
			}
		}
		return new BaseResponse(true,boxdata,"success");
	}
	
	/**
	 * App删除全权委托箱子
	 * 
	 * @param memberId
	 * @param productNumber
	 * @return
	 */
	public BaseResponse delBoxService(String boxId){
		Box box = appBoxDao.get(Long.parseLong(boxId));
		if (box!=null) {
			
		box.setBoxDelFlag(PRODUCT_DEL_TRUE);
		appBoxDao.saveEntity(box);
		return new BaseResponse(true,"success");
		}else {
			return new BaseResponse(false,"not exist");
		}
	}

	/**
	 * App普通方案生成条形码
	 * 
	 * @param productNumber
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse barcodeGenService(String productNumber,String memberId) {

		BarcodeResponse barcodeRes = new BarcodeResponse();
		HashSet<String> barcodeset = new HashSet<>();
		String barcodeValue = "";
		while (barcodeset.size() < Integer.parseInt(productNumber)) {
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
		barcodeRes.setBarcodeSet(barcodeset);
		barcodeRes
				.setBarcodeUrl("/Fujikom/spi/product/barcodeview?barcodeValue="
						+ barcodeValue+"&memberId="+memberId);
		return new BaseResponse(true, barcodeRes, "success");
	}

	public List<BarcodeView> barcodeViewService(String barcodeValue){
		
		String[] barcodeStrings = barcodeValue.split(",");
		List<BarcodeView> barcodeViewList = new ArrayList<>();
		for (int i = 0; i < barcodeStrings.length; i++) {
			BarcodeView barcodeView = new BarcodeView();
			Product product = appProductDao.getFromBarcode(barcodeStrings[i]);
			if (product!=null) {
				barcodeView.setProductbarcode(product.getProductBarcode());
				barcodeView.setProductName(product.getProductName());	
			}
			barcodeViewList.add(barcodeView);
		}
		return barcodeViewList;
				
	}
	/**
	 * App普通方案注册商品
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false,rollbackFor=Exception.class)
	public BaseResponse normalLoginProduct(List<ProductRequestData> products) throws Exception
			 {

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
			Box box = new Box();
			Member member = new Member();
			member.setMemberId(Long.parseLong(products.get(0).getMemberId()));
			box.setBoxFlag(BOX_FLAG_NORMAL);
			box.setBoxStatus(BOX_STATUS_UNCHECKED);
			box.setBoxProductNumber(String.valueOf(products.size()));
			box.setMember(member);
			box.setBoxCreateDate(new Date());
			box.setBoxDelFlag(PRODUCT_DEL_DEFAULT);
			appBoxDao.saveEntity(box);
			String oldId = String.valueOf(box.getBoxId());
			// 创建箱子管理番号
			/*String strNumber = "";
			if (oldId.length() < 11) {
				for (int i = 0; i < 11 - oldId.length(); i++) {
					strNumber = strNumber + "0";
				}
			}
			strNumber = strNumber + oldId;*/
			String strNumber = StringUtils.leftPad(oldId, 11,"0");
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
				if (StringUtils.check(product.getProductImageOne())
						&& StringUtils.check(products.get(i)
								.getProductImageOneType())) {
					byte[] b = base64.decode(product.getProductImageOne());
					product.setProductImageOne(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageOneType()));
				}
				if (StringUtils.check(product.getProductImageTwo())
						&& StringUtils.check(products.get(i)
								.getProductImageTwoType())) {
					byte[] b = base64.decode(product.getProductImageTwo());
					product.setProductImageTwo(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageTwoType()));
				}
				if (StringUtils.check(product.getProductImageThree())
						&& StringUtils.check(products.get(i)
								.getProductImageThreeType())) {
					byte[] b = base64.decode(product.getProductImageThree());
					product.setProductImageThree(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageThreeType()));
				}
				if (StringUtils.check(product.getProductImageFour())
						&& StringUtils.check(products.get(i)
								.getProductImageFourType())) {
					byte[] b = base64.decode(product.getProductImageFour());
					product.setProductImageFour(FileS3Utils.ImageUpload(b,
							products.get(i).getProductImageFourType()));
				}
				product.setProductName(products.get(i).getProductName());
				product.setProductSize("");
				product.setProductStatus(PRODUCT_STATUS_DELIVERY);
				product.setDelFlag(PRODUCT_DEL_DEFAULT);
				product.setMember(member);
				product.setBox(oldBox);
				product.setProductCreateDate(createDate);
				appProductDao.saveEntity(product);

			}

			return new BaseResponse(true, adminNumber, "登録完了しました");
		} else {
			return new BaseResponse(false, "既に存在するバーコードです");
		}
	}

	/**
	 * App获取处理中商品列表
	 * 
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getProcessingProducts(String memberId, Integer sinceId,
			Integer size) {

		List<Product> products = appProductDao.findProcessingProductList(
				Long.parseLong(memberId), PRODUCT_DEL_DEFAULT,
				PRODUCT_STATUS_DELIVERY, sinceId, size);
		List<ProductProcessingData> productProcessDatas = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			ProductProcessingData processingData = new ProductProcessingData();
			processingData.setBox(products.get(i).getBox());
			processingData.setProductId(products.get(i).getProductId());
			processingData.setProductBarcode(products.get(i)
					.getProductBarcode());
			processingData.setProductCreateDate(products.get(i)
					.getProductCreateDate());
			processingData.setProductImageFour(products.get(i)
					.getProductImageFour());
			processingData.setProductImageOne(products.get(i)
					.getProductImageOne());
			processingData.setProductImageTwo(products.get(i)
					.getProductImageTwo());
			processingData.setProductImageThree(products.get(i)
					.getProductImageThree());
			processingData.setProductMemo(products.get(i).getProductMemo());
			processingData.setProductName(products.get(i).getProductName());
			processingData.setProductSize(products.get(i).getProductSize());
			processingData.setProductStatus(products.get(i).getProductStatus());
			productProcessDatas.add(processingData);
		}
		return new BaseResponse(true, productProcessDatas, "success");
	}
	
	/**
	 * App获取寄送到仓库的箱子
	 * 
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getUncheckBoxes(String memberId, Integer sinceId,
			Integer size) {
		List<Box> boxes = appBoxDao.findUncheckBoxes(Long.parseLong(memberId), sinceId, size);
		List<BoxUncheckData> boxdata = new ArrayList<>();
		for (int i = 0; i < boxes.size(); i++) {
			BoxUncheckData data = new BoxUncheckData();
			DetachedCriteria dc = appProductDao.createDetachedCriteria();
			dc.add(Restrictions.eq("box", boxes.get(i)));
			List<Product> products = appProductDao.find(dc);
			for (Product p:products) {
				data.getBarcodeList().add(p.getProductBarcode());
			}
			if (boxes.get(i)!=null) {
				
			BeanMapper.copy(boxes.get(i), data);
			boxdata.add(data);
			}
		}
		return new BaseResponse(true,boxdata,"success");
	}
	

	/**
	 * App获取仓库商品列表
	 * 
	 * @param productNumber
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getInstockProducts(String memberId, Integer sinceId,
			Integer size) {

		List<Product> products = appProductDao.findProductList(
				Long.parseLong(memberId), PRODUCT_DEL_DEFAULT,
				PRODUCT_STATUS_INSTOCK,PRODUCT_STATUS_PROCESSING,PRODUCT_STATUS_UNSEND, sinceId, size);
		List<ProductInstockData> productInstockDatas = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			ProductInstockData instockData = new ProductInstockData();
			BeanMapper.copy(products.get(i), instockData);
			productInstockDatas.add(instockData);
		}
		return new BaseResponse(true, productInstockDatas, "success");
	}

	/**
	 * App获取商品信息
	 * 
	 * @param productId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getProductInfoService(String productId) {

		Product product = appProductDao.get(Long.parseLong(productId));
		ProductResponseInfo productResponseInfo = new ProductResponseInfo();
		BeanMapper.copy(product, productResponseInfo);
		return new BaseResponse(true, productResponseInfo, "success");
	}

	/**
	 * App修改商品信息
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
		if (product.getProductStatus().equals(PRODUCT_STATUS_DELIVERY)
				|| product.getProductStatus().equals(PRODUCT_STATUS_INSTOCK)||product.getProductStatus().equals(PRODUCT_STATUS_PROCESSING)) {
			product.setProductName(productName);
			product.setProductMemo(productMemo);
			appProductDao.saveEntity(product);
		}
		if (product.getProductStatus().equals(PRODUCT_STATUS_SEND)) {
			product.setProductMemo(productMemo);
			appProductDao.saveEntity(product);
		}
		return new BaseResponse(true, "修正しました");
	}

	/**
	 * App删除商品信息
	 * 
	 * @param productId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse deleteProductService(String productId) {

		Product product = appProductDao.get(Long.parseLong(productId));
		product.setDelFlag(PRODUCT_DEL_TRUE);
		appProductDao.saveEntity(product);
		return new BaseResponse(true, "削除しました");
	}
	
	/**
	 * App用户寄送费计算
	 * 
	 * @param OrdersRequestData
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse deliveryCostService(List<String> sizeList) {
		CostUtil costUtil = new CostUtil();

		DetachedCriteria dc = expenseDao.createDetachedCriteria();
		List<Expense> expenses = expenseDao.find(dc);
		CostResponseData data = costUtil.deliveryCal(sizeList,expenses);
		if (!data.getCost().equals("false")) {
			return new BaseResponse(true,data,"success");
		}else {
			return new BaseResponse(false,new ErrorResponse(ResultConstant.BOX_ERROR_CODE, ResultConstant.BOX_ERROR_CODE_MSG));
			
		}
		
	}

	/**
	 * App用户提交发送商品请求
	 * 
	 * @param OrdersRequestData
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse shipRuquestService(OrdersRequestData orderData) {

		Orders orders = new Orders();// 新建一个订单
		Member member = new Member();
		member.setMemberId(Long.parseLong(orderData.getMemberId()));
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
		orders.setOrdersStatus(ORDERS_STATUS_DEFAULT);
		orders.setOrdersFlag(ORDERS_FLAG_SEND);
		orders.setOrdersSize(orderData.getOrderSize());
		orders.setOrdersAccountFlag(ORDERS_ACCOUNT_DEFAULT);
		orders.setOrdersExportFlag(ORDERS_EXPORT_DEFAULT);
		orders.setOrdersSenderAddress(orderData.getOrderSenderAddress());
		orders.setOrdersSenderTelphone(orderData.getOrderSenderTelphone());
		orders.setOrdersSenderZip(orderData.getOrderSenderZip());
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
			product.setProductStatus(PRODUCT_STATUS_UNSEND);
			product.setOrders(orders);
			appProductDao.saveEntity(product);
		}
		return new BaseResponse(true, "success");
	}

	/**
	 * App用户获取发/退货商品信息
	 * 
	 * @param memberId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse getSendProductsService(String memberId,
			Integer sinceId, Integer size) {

		List<Product> products = appProductDao.findProducts(
				Long.parseLong(memberId), PRODUCT_DEL_DEFAULT,
				PRODUCT_STATUS_SEND, PRODUCT_STATUS_RETURN, sinceId, size);
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
		return new BaseResponse(true, productSendData, "success");
	}

	/**
	 * App用户退货请求
	 * 
	 * @param productId
	 * @return
	 */
	@Transactional(readOnly = false)
	public BaseResponse returnProductService(OrdersRequestData ordersData) {

		Orders orders = new Orders();
		Member member = new Member();
		member.setMemberId(Long.parseLong(ordersData.getMemberId()));
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
		orders.setOrdersStatus(ORDERS_STATUS_DEFAULT);
		orders.setOrdersFlag(ORDERS_FLAG_RETUEN);
		orders.setOrdersSize(ordersData.getOrderSize());
		orders.setOrdersAccountFlag(ORDERS_ACCOUNT_DEFAULT);
		orders.setOrdersExportFlag(ORDERS_EXPORT_DEFAULT);
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
			product.setProductStatus(PRODUCT_STATUS_UNSEND);
			appProductDao.saveEntity(product);
		}
		return new BaseResponse(true, "success");
	}

	/**
	 * App用户搜索
	 * 
	 * @param productId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public BaseResponse searchService(String searchString, String memberId,
			String sinceId, String size) {

		DetachedCriteria dc = appProductDao.createDetachedCriteria();
		dc.createAlias("member", "a").add(
				Restrictions.eq("a.memberId", Long.parseLong(memberId)));
		
		dc.add(Restrictions.or(Restrictions.eq("productStatus", PRODUCT_STATUS_UNSEND),Restrictions.or(Restrictions.eq("productStatus", PRODUCT_STATUS_INSTOCK),
				Restrictions.eq("productStatus", PRODUCT_STATUS_PROCESSING))));
		dc.add(Restrictions.or(
				Restrictions.like("productName", "%" + searchString + "%"),
				Restrictions.like("productBarcode", "%" + searchString + "%")));
		Criteria cri = dc.getExecutableCriteria(appProductDao.getSession());
		cri.setFirstResult(Integer.parseInt(sinceId));
		cri.setMaxResults(Integer.parseInt(size));
		List<Product> products = cri.list();

		
		List<ProductInstockData> productInstockDatas = new ArrayList<>();
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getOrders()==null) {
				
			ProductInstockData instockData = new ProductInstockData();
			BeanMapper.copy(products.get(i), instockData);
			productInstockDatas.add(instockData);
			}
		}
		return new BaseResponse(true, productInstockDatas, "success");

	}

	/*
	 * @Autowired private UserDao userDao;
	 * 
	 * @Transactional(readOnly = false) public BaseResponse login(String
	 * username, String password) { // 判断用户名是否存在 User user =
	 * userDao.findByLoginName(username);
	 * 
	 * if (user == null) { return new BaseResponse(false, new ErrorResponse(
	 * ResultConstant.USERNAME_NOT_EXIST,
	 * ResultConstant.USERNAME_NOT_EXIST_MSG)); }
	 * 
	 * if (!SystemService.validatePassword(password, user.getPassword())) {
	 * return new BaseResponse(false, new ErrorResponse(
	 * ResultConstant.PASSWORD_ERROR, ResultConstant.PASSWORD_ERROR_MSG)); } //
	 * 返回获取正确的信息 String userType = user.getUserType(); return new
	 * BaseResponse(true, "success", userType); }
	 */
	/**
	 * 查询设备信息根据equipmentNumber
	 *
	 * @param equipmentNumber
	 *
	 * @return
	 */
	/*
	 * @Transactional(readOnly = false) public BaseResponse findByNumber(String
	 * equipmentNumber) { Equipment equipment = equipmentDao
	 * .findEquipmentByEquipmentNumber(equipmentNumber); // 判断equipment是否存在 if
	 * (equipment == null) { return new BaseResponse(false, new ErrorResponse(
	 * ResultConstant.EQUIPMENT_NOT_EXIST,
	 * ResultConstant.EQUIPMENT_NOT_EXIST_MSG)); } Program program =
	 * equipment.getProgram(); List<Goods> goodsList = equipment.getGoodss();
	 * List<Contract> contractList = program.getContracts(); List<Receiving>
	 * receivingList = program.getReceivings();
	 * 
	 * ProgramData programData = new ProgramData(); EquipmentData equipmentData
	 * = new EquipmentData();
	 * 
	 * List<GoodsData> goodsData = BeanMapper.mapList(goodsList,
	 * GoodsData.class); List<ContractData> contractData =
	 * BeanMapper.mapList(contractList, ContractData.class); List<ReceivingData>
	 * receivingData = BeanMapper.mapList(receivingList, ReceivingData.class);
	 * BeanMapper.copy(program, programData); BeanMapper.copy(equipment,
	 * equipmentData);
	 * 
	 * programData.setReceivingDatas(receivingData);
	 * programData.setContractDatas(contractData);
	 * equipmentData.setGoodsDatas(goodsData);
	 * equipmentData.setProgramData(programData);
	 * 
	 * 
	 * return new BaseResponse(true, "success", equipmentData);
	 * 
	 * }
	 */

	/**
	 * 扫码确认
	 * 
	 * @param equipmentNumber
	 * @param username
	 * @param location
	 * @return
	 */
	/*
	 * @Transactional(readOnly = false) public BaseResponse confirm(Integer
	 * equipmentId, String username, String location) { Date date = new Date();
	 * Confirm confirm = new Confirm(); Equipment equipment =new Equipment();
	 * equipment.setEquipmentId(equipmentId); confirm.setEquipment(equipment);
	 * confirm.setUsername(username); confirm.setLocation(location);
	 * confirm.setTime(date);
	 * 
	 * confirmDao.save(confirm);
	 * 
	 * // 返回获取正确的信息
	 * 
	 * return new BaseResponse(true, "success");
	 * 
	 * }
	 */

}
