<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>申请条形码</title>
<meta name="decorator" content="default" />
<style type="text/css">
.row-fluid [class*="span"] {
	margin-left: 0;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		var barcodevalue = $("#barcodeValues").val();
		var barcodes = new Array();
		barcodes = barcodevalue.split(",");
		for (var i = 0; i < barcodes.length; i++) {
			$("#barcode_" + i).barcode(barcodes[i], "ean13", {
				barWidth : 2,
				barHeight : 90
			});
		}
	});
</script>
</head>
<body>
	<input id="barcodeValues" type="hidden" value=${barcodeValue } />


	<c:forEach items="${barcodeViewList}" var="barcodeView" varStatus="s">
		
			&nbsp;&nbsp;&nbsp;<label class="form-control">${barcodeView.productName}</label>
			<div id="barcode_${s.index}"></div>
			<br /> <br />
		
	</c:forEach>


	&nbsp;&nbsp;&nbsp;
	<label>送付先：</label>
	<label class="lbl">${member.warehouse.warehouseAddress}</label>
	<br> &nbsp;&nbsp;&nbsp;
	<label>郵便番号：</label>
	<label class="lbl">${member.warehouse.warehouseZip}</label>
	<br> &nbsp;&nbsp;&nbsp;
	<label>電話番号：</label>
	<label class="lbl">${member.warehouse.warehouseTelphone}</label>

</body>
</html>
