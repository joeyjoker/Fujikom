<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>受注返品</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
						loading('少々お待ちくださいませ');
						form.submit();
					
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">受注出荷</a></li>
	</ul><br/>
	
	<form:form id="inputForm" modelAttribute="orders" action="${ctx}/spi/rejectorders/delivery" method="post" class="form-horizontal">
		<form:hidden path="ordersId"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">受注番号:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersNumber}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">受注氏名:</label>
			<div class="controls">
				<form:input path="member.memberRealName" htmlEscape="false" maxlength="200" class="required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">発送先名:</label>
			<div class="controls">
				<form:input path="ordersSenderName" htmlEscape="false" maxlength="200" class="required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">購入者名:</label>
			<div class="controls">
				<form:input path="ordersReceiverName" htmlEscape="false" maxlength="200" class="required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">返品Pattren:</label>
			<div class="controls">
				<form:input path="ordersRejectPattern" htmlEscape="false" maxlength="200" class="required" disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">受注日:</label>
			<div class="controls">
				<label class="lbl"><fmt:formatDate
						value="${orders.ordersRequestDate}" type="both" dateStyle="full" /></label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">物流会社:</label>
			<div class="controls">
				<form:input path="ordersCompany" htmlEscape="false" maxlength="200" class="required"/>
				
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">お問い合わせ:</label>
			<div class="controls">
				<form:input path="ordersConsultNumber" htmlEscape="false"  maxlength="200" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="spi:rejectorders:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="返 品"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="戻  る" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
