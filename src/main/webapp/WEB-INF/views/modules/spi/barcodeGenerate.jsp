<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>申请条形码管理</title>
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

		/* generate();
		var barcodevalue = $("#barcodeValues").val();
		var barcodes = new Array();
		barcodes = barcodevalue.split(",");
		for(var i =0;i<barcodes.length;i++){
		$("#barcode_"+i).barcode(barcodes[i], "ean13",{barWidth:2, barHeight:90});
		} */
	});
	/* function generate(){
		
		var barcodevalue = $("#barcodeValues").val();
		var barcodes = new Array();
		barcodes = barcodevalue.split(",");
		for(i =0;i<barcodes.length;i++){
			var objdiv = document.createElement("DIV");
			   var objname="barcode_" + i
			   objdiv.id = objname;
			   document.body.appendChild(objdiv);
			   
		}   
			
		
	}*/
</script>
</head>
<body>
	<%-- <input id="barcodeValues" type="hidden" value = "${ordersBarcodeValue }"> --%>

	<input id="barcodeValues" type="hidden" value=${ordersBarcodeValue } />
	<!-- 	<form class="form-horizontal"> -->

	<div class="row-fluid">

		<div class="span12">
			<div class="row-fluid">
				<c:forEach items="${barcodeValueList}" var="barcodeView"
					varStatus="s">
					<div class="span6">
						<div id="barcode_${s.index}"></div>
						<br /> <br />
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<!-- </form> -->

</body>
</html>
