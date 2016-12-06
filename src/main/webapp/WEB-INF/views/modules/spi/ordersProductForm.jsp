<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>ing但商品详情</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document)
			.ready(
					function() {
						if ($("#productImageOne").attr("value") != "") {
							$("#image1")
									.attr("src",
											"${serverPath}/spi/file/downloadFileGet?url=${product.productImageOne}")
						}
						if ($("#productImageTwo").attr("value") != "") {
							$("#image2")
									.attr("src",
											"${serverPath}/spi/file/downloadFileGet?url=${product.productImageTwo}")
						}
						if ($("#productImageThree").attr("value") != "") {
							$("#image3")
									.attr("src",
											"${serverPath}/spi/file/downloadFileGet?url=${product.productImageThree}")
						}
						if ($("#productImageFour").attr("value") != "") {
							$("#image4")
									.attr("src",
											"${serverPath}/spi/file/downloadFileGet?url=${product.productImageFour}")
						}
						/* switch($("#productStatus").attr("value"))
						{
						    case "P":
						    	$("#status").attr("value","处理中");
							  break;
							case "I":
								$("#status").attr("value","仓库");
							  break;
							case "S":
								$("#status").attr("value","以寄送");
							  break;
							case "R":
								$("#status").attr("value","已退货");
							  break;  
							
						} */
						$("#inputForm").validate({
							submitHandler : function(form) {
								loading('少々お待ちくださいませ');
								form.submit();
							}
						});
					});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>受注商品詳細</a></li>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>

	<form:form id="inputForm" modelAttribute="product"
		action="${ctx}/spi/product/save" method="post" class="form-horizontal">
		<form:hidden path="productId" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">氏名:</label>
			<div class="controls">
				<form:input path="member.memberRealName" htmlEscape="false"
					maxlength="200" disabled="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品名:</label>
			<div class="controls">
				<form:input path="productName" htmlEscape="false" maxlength="200"
					disabled="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">サイズ:</label>
			<div class="controls">
				<form:input path="productSize" htmlEscape="false" maxlength="200"
					disabled="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">メモ:</label>
			<div class="controls">
				<form:textarea path="productMemo" rows="6" htmlEscape="false"
					class="input-xlarge" disabled="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品バーコード:</label>
			<div class="controls">
				<form:input path="productBarcode" htmlEscape="false" maxlength="200"
					disabled="true" />

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品登録時間:</label>
			<div class="controls">
				<label class="lbl"> <fmt:setLocale value="ja-JP" />
					<fmt:formatDate value="${product.productCreateDate}" type="both"
						dateStyle="full" /></label>
			</div>
		</div>
		<c:if
			test="${!empty product.productId && product.productStatus ne 'P'}">
			<div class="control-group">
				<label class="control-label">商品入庫時間:</label>
				<div class="controls">
					<label class="lbl"><fmt:formatDate
							value="${product.productInstockDate}" type="both"
							dateStyle="full" /></label>
				</div>
			</div>
		</c:if>
		<c:if
			test="${!empty product.productId && product.productStatus ne 'P'}">
			<div class="control-group">
				<label class="control-label">棚番:</label>
				<div class="controls">
					<form:input path="productShelfNumber" htmlEscape="false"
						maxlength="200" />
				</div>
			</div>
		</c:if>
		<div class="control-group">
			<label class="control-label">ステータス:</label>
			<div class="controls">
				<label class="lbl">
				<c:if test="${product.productStatus eq 'D'}">倉庫へ送り中</c:if>
				<c:if test="${product.productStatus eq 'P'}">処理中</c:if> <c:if
						test="${product.productStatus eq 'I'}">倉庫</c:if> <c:if
						test="${product.productStatus eq 'S'}">発送済み</c:if> <c:if
						test="${product.productStatus eq 'R'}">引き取り済み</c:if></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">商品画像：</label>
			<div class="controls">
				<form:hidden id="productImageOne" path="productImageOne"
					htmlEscape="false" maxlength="256" class="input-xlarge" />
			</div>
			<div class="controls">
				<form:hidden id="productImageTwo" path="productImageTwo"
					htmlEscape="false" maxlength="256" class="input-xlarge" />
			</div>
			<div class="controls">
				<form:hidden id="productImageThree" path="productImageThree"
					htmlEscape="false" maxlength="256" class="input-xlarge" />
			</div>
			<div class="controls">
				<form:hidden id="productImageFour" path="productImageFour"
					htmlEscape="false" maxlength="256" class="input-xlarge" />
			</div>
			<div class="controls col-md-1">
				<img id="image1" width="200" height="200"> <img id="image2"
					width="200" height="200"> <img id="image3" width="200"
					height="200"> <img id="image4" width="200" height="200">
			</div>
		</div>

		<div class="form-actions">
			<c:if test="${!empty product.productId && product.productStatus ne 'P'}">
				<shiro:hasPermission name="prj:project:edit">
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="戻  る" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
