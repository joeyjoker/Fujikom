<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>出荷詳細</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#inputForm").validate({
				submitHandler: function(form){	
						loading('提出中...少々お待ちください。');
						form.submit();
					
				}
			});
			
		});
			function aaa(){
				alert($("#ordersId").val());
			}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/spi/sendorders/">発送リスト</a></li>
		<li class="active"><a href="">発送<shiro:hasPermission
					name="spi:returnorders:edit">${not empty orders.ordersId?'変更':'詳細'}</shiro:hasPermission>
				<shiro:lacksPermission name="spi:returnorders:edit">詳細</shiro:lacksPermission></a></li>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	
	<form:form id="inputForm" modelAttribute="orders" action="${ctx}/spi/sendorders/save" method="post" class="form-horizontal">
		<form:hidden path="ordersId"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">受注番号:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersNumber}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">氏名:</label>
			<div class="controls">
		    	<label class="lbl">${orders.member.memberRealName}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">発送先名:</label>
			<div class="controls">				
				<label class="lbl">${orders.ordersSenderName}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">購入者住所:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersAddress}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">購入者建物名:</label>
			<div class="controls">
			    <label class="lbl">${orders.ordersBuliding}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">購入者配送先:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersLocation}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">発送先住所:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersSenderAddress}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">購入者電話番号:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersTelphone}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">購入者郵便番号:</label>
			<div class="controls">
			    <label class="lbl">${orders.ordersZip}</label>
				
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">金額:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersPrice}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">受注日:</label>
			<div class="controls">
				<label class="lbl"><fmt:formatDate value="${orders.ordersRequestDate}" type="both" /></label>
			</div>
		</div>
		
		<c:if test="${orders.ordersStatus eq '0'}">
		<div class="control-group">
			<label class="control-label">物流会社:</label>
			<div class="controls">
				<form:input path="ordersCompany"  maxlength="200" class="required" />
			</div>
		</div>
		</c:if>
		<c:if test="${orders.ordersStatus eq '0'}">
		<div class="control-group">
			<label class="control-label">お問い合わせ:</label>
			<div class="controls">
				<form:input path="ordersConsultNumber"  maxlength="200" class="required" />
			</div>
		</div>
		</c:if>
		<c:if test="${orders.ordersStatus eq '0'}">
		<div class="control-group">
			<label class="control-label">発送時間:</label>
			<div class="controls">
				<label class="lbl"><fmt:formatDate value="${orders.ordersDeliveryDate}" type="both" /></label>
			</div>
		</div>
		</c:if>
		<div class="form-actions">
			<c:if test="${!empty orders.ordersId && orders.ordersStatus eq '0'}">	   
				<shiro:hasPermission name="spi:sendorders:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			</c:if>
			
			<input id="btnCancel" class="btn" type="button" value="戻  る" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
