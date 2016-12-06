<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>Title</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
	
	 $("#form1").submit();
});
</script>

</head>
<body>
	<form id="form1" method="post" action="https://stbfep.sps-system.com/f04/FepPayInfoReceive.do">
		<input id="pay_method" name="pay_method" type="hidden" value="${card.payMethod}">
		<!-- 付款方式-->
		<input id="merchant_id" name="merchant_id" type="hidden" value="${card.merchantId }">
		<!-- merchant_id-->
		<input id="service_id" name="service_id" type="hidden" value="${card.serviceId }">
		<!-- service_id-->
		<input id="cust_code" name="cust_code" type="hidden" value="${card.custCode}">
		<!-- 用户ID-->
		<input id="sps_cust_no" name="sps_cust_no" type="hidden" value="${card.spsCustNo }">
		<!-- 空-->
		<input id="terminal_type" name="terminal_type" type="hidden" value="${card.terminalType}">
		<!-- 0：PC／1：手机 -->
		<input id="success_url" name="success_url" type="hidden"
			value="${card.successUrl }">
		<!-- 成功后跳转的页面 -->
		<input id="cancel_url" name="cancel_url" type="hidden"
			value=" ${card.cancelUrl }">
		<!-- 取消后跳转的页面 -->
		<input id="error_url" name="error_url" type="hidden"
			value="${card.errorUrl }">
		<!-- 失败后跳转的页面 -->
		<input id="pagecon_url" name="pagecon_url" type="hidden"
			value="${card.pageconUrl}">
		<!-- 处理结果页面 -->
		<input id="free1" name="free1" type="hidden" value="${card.free1 }">
		<!-- 空-->
		<input id="free2" name="free2" type="hidden" value="${card.free2 }">
		<!-- 空-->
		<input id="free3" name="free3" type="hidden" value="${card.free3 }">
		<!-- 空-->
		<input id="request_date" name="request_date" type="hidden" value="${card.requestDate }">
		<!-- 请求日期 -->
		<input id="limit_second" name="limit_second" type="hidden" value="${card.limitSecond }">
		<!-- 超时时间 -->
		<input id="sps_hashcode" name="sps_hashcode" type="hidden" value="${card.spsHashcode }">
		<!-- 计算后的hashcode -->

	</form>

</body>
</html>
