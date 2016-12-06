<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>申请条形码</title>
	<meta name="decorator" content="default"/>
	
	<script type="text/javascript">
		$(document).ready(function() {
			generate();
			var barcodevalue = $("#barcodeValues").val();
			var barcodes = new Array();
			barcodes = barcodevalue.split(",");
			for(var i =0;i<barcodes.length;i++){
			$("#barcode_"+i).barcode(barcodes[i], "ean13");
			}
		});
		function generate(){
			  
			var barcodevalue = $("#barcodeValues").val();
			var barcodes = new Array();
			barcodes = barcodevalue.split(",");
			for(i =0;i<barcodes.length;i++){
				var objdiv = document.createElement("DIV");
				   var objname="barcode_" + i
				   objdiv.id = objname;
				   document.body.appendChild(objdiv);
				   
			} 
			
		}
	</script>
</head>
<body>
	<input id="barcodeValues" type="hidden" value = "${param.barcode_value}">
	  
	
</body>
</html>
