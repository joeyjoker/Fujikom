package com.joey.Fujikom.modules.spi.app;

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

import com.joey.Fujikom.modules.spi.constant.ResultConstant;
import com.joey.Fujikom.modules.spi.dao.MemberDao;
import com.joey.Fujikom.modules.spi.entity.Member;
import com.joey.Fujikom.modules.spi.request.OrdersRequestData;
import com.joey.Fujikom.modules.spi.request.ProductRequestData;
import com.joey.Fujikom.modules.spi.response.BarcodeView;
import com.joey.Fujikom.modules.spi.response.BaseResponse;
import com.joey.Fujikom.modules.spi.response.ErrorResponse;
import com.joey.Fujikom.modules.spi.service.WebMemberService;
import com.joey.Fujikom.modules.spi.service.WebProductService;

/**
 * webProductController
 *
 * @author JoeyHuang
 * @version 2016-4-12
 */
@Controller
@RequestMapping(value = "/spi/web/product")
public class WebProductController {

	private static Logger logger = LoggerFactory
			.getLogger(WebProductController.class);

	@Autowired
	private WebMemberService webMemberService;

	@Autowired
	private WebProductService webProductService;

	@Autowired
	private MemberDao memberdao;

	/**
	 * web全权委托
	 *
	 * @param productNumber
	 * @return
	 */
	@RequestMapping(value = "entrust", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse entrustProduct(
			@RequestParam("productNumber") String productNumber,
			HttpServletRequest request) {

		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {

					return webProductService.entrustProduct(memberId,
							productNumber);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}
		} catch (Exception e) {
			logger.error("AppMemberController.memberLogin error", e);
			return new BaseResponse(false, "fail");
		}
	}

	/**
	 * web获取全权委托列表
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value = "entrustlist", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse entrustList(@RequestParam("pageIndex") Integer pageIndex, HttpServletRequest request) {

		try {
			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.entrustListService(memberId,pageIndex);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}
		} catch (Exception e) {
			logger.error("get entrustList error", e);
			return new BaseResponse(false, "fail");
		}
	}

	/**
	 * web删除箱子
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping(value = "deletebox", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse deleteBox(@RequestParam("boxId") String boxId,
			HttpServletRequest request) {
		try {
			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.delBoxService(boxId);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}
		} catch (Exception e) {
			logger.error("delete entrustbox error", e);
			return new BaseResponse(false, "fail");
		}
	}

	/**
	 * web普通方案生成条形码
	 *
	 * @param productNumber
	 * @return
	 *//*
	@RequestMapping(value = "genbarcode", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse genBarcode(
			@RequestParam("productNumber") String productNumber,
			HttpServletRequest request) {
		try {
			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.barcodeGenService(productNumber,
							memberId);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {

			logger.error("generdate barcode Error", e);
			return new BaseResponse(false);
		}

	}*/

	/**
	 * web普通方案生成条形码
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "barcodeview", method = RequestMethod.GET)
	public String barcodeView(
			@RequestParam("barcodeValue") String barcodeValue,
			@RequestParam("memberId") String memberId,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Member member = memberdao.get(Long.parseLong(memberId));
		List<BarcodeView> barcodeViewList = webProductService
				.barcodeViewService(barcodeValue);
		model.addAttribute("barcodeViewList", barcodeViewList)
				.addAttribute("barcodeValue", barcodeValue)
				.addAttribute("member", member);
		return "modules/spi/barcodeView";
	}

	/**
	 * web普通方案商品注册接口
	 *
	 * @param products
	 * @return
	 */
	@RequestMapping(value = "productregister", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse productRegister(
			@RequestBody List<ProductRequestData> products,
			HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.normalLoginProduct(memberId,
							products);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("Normal Product Register Error ", e);
			return new BaseResponse(false,new ErrorResponse(
					ResultConstant.SYSTEM_ERROR,
					ResultConstant.SYSTEM_ERROR_MSG));
		}

	}

	/**
	 * web获取运往仓库未检查箱子列表接口
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "getuncheckboxes", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getUncheckBoxes(
			@RequestParam("pageIndex") Integer pageIndex,
			HttpServletRequest request) {
		try {
			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.getUncheckBoxes(memberId,
							pageIndex);

				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("get Uncheck Boxes Error", e);
			return new BaseResponse(false, new ErrorResponse(
					ResultConstant.SYSTEM_ERROR,
					ResultConstant.SYSTEM_ERROR_MSG));
		}

	}

	/**
	 * web获取已入库商品列表接口
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "getinstockproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getInstockProduct(@RequestParam("pageIndex") Integer pageIndex, HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.getInstockProducts(memberId,pageIndex);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("get Instock Products Error ", e);
			return new BaseResponse(false);
		}

	}

	/**
	 * web获取商品信息接口
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "getproductinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getProductInfo(
			@RequestParam("productId") String productId,
			HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.getProductInfoService(productId);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("get Instock Products Error ", e);
			return new BaseResponse(false);
		}

	}

	/**
	 * web修改商品信息
	 *
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "editproductinfo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editProductInfo(
			@RequestParam("productId") String productId,
			@RequestParam("productName") String productName,
			@RequestParam("productMemo") String productMemo,
			HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.editProductInfoService(productId,
							productName, productMemo);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("Update Product info Error ", e);
			return new BaseResponse(false);
		}

	}

	/**
	 * web删除商品信息
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "deleteproduct", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse editProductInfo(
			@RequestParam("productId") String productId,
			HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.deleteProductService(productId);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("delete Product  Error ", e);
			return new BaseResponse(false, "delete Product  Error ");
		}

	}

	/**
	 * web获取发货/取回商品列表
	 *
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "getsendproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse getSendProducts(
			@RequestParam("pageIndex") Integer pageIndex, HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.getSendProductsService(memberId,pageIndex);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("Get Send Products  Error ", e);
			return new BaseResponse(false, "delete Product  Error ");
		}

	}

	/**
	 * web用户发货请求接口
	 *
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "shipordersrequest", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse shipOrdersRequest(
			@RequestBody OrdersRequestData ordersData,
			HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.shipRuquestService(memberId,
							ordersData);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("ship Orders Data  Error ", e);
			return new BaseResponse(false);
		}

	}

	/**
	 * web用户退货请求接口
	 *
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "returnordersrequest", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse returnOrdersRequest(
			@RequestBody OrdersRequestData ordersData,
			HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.returnProductService(memberId,
							ordersData);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("return orders  Error ", e);
			return new BaseResponse(false);
		}

	}

	/**
	 *web用户搜索商品接口
	 *
	 * @param searchString
	 * @param memberId
	 * @param sinceId
	 * @param size
	 * @return
	 */
	@RequestMapping(value = "searchproducts", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse searchProducts(
			@RequestParam("searchString") String searchString,
			@RequestParam("pageIndex") Integer pageIndex , HttpServletRequest request) {
		try {

			if (request.getSession() != null) {

				
				Long memberId = (Long) request.getSession().getAttribute(
						"memberId");
				if (memberId != null) {
					return webProductService.searchService(searchString,
							memberId, pageIndex);
				} else {
					return new BaseResponse(false, new ErrorResponse(
							ResultConstant.MEMBER_NOT_EXIST,
							ResultConstant.MEMBER_NOT_EXIST_MSG));
				}
			} else {
				return new BaseResponse(false, new ErrorResponse(
						ResultConstant.SESSION_ERROR,
						ResultConstant.SESSION_ERROR_MSG));
			}

		} catch (Exception e) {
			logger.error("return orders  Error ", e);
			return new BaseResponse(false);
		}

	}

}
