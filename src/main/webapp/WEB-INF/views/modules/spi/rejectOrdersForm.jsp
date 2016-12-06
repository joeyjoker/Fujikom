<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>返品詳細</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {

		$("#inputForm").validate({
			submitHandler : function(form) {

				loading('提出中...少々お待ちください。');
				form.submit();

			}
		});
	});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/spi/rejectorders/">返品リスト</a></li>
		<li class="active"><a href="">返品<shiro:hasPermission
					name="spi:rejectorders:edit">${not empty orders.ordersId?'変更':'詳細'}</shiro:hasPermission>
				<shiro:lacksPermission name="spi:rejectorders:edit">詳細</shiro:lacksPermission></a></li>
	</ul>

	<form:form id="inputForm" modelAttribute="orders"
		action="${ctx}/spi/rejectorders/save" method="post"
		class="form-horizontal">
		<form:hidden path="ordersId" />
		<tags:message content="${message}" />
		<div class="control-group">
			<label class="control-label">受注番号:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersNumber}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">返品パターン:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersRejectPattern}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">受注氏名:</label>
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
			<label class="control-label">購入者名:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersReceiverName}</label>
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
			<label class="control-label">返品金額:</label>
			<div class="controls">
				<label class="lbl">${orders.ordersPrice}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">受注日:</label>
			<div class="controls">
				<label class="lbl"><fmt:formatDate
						value="${orders.ordersRequestDate}" type="both" /></label>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">物流会社:</label>
			<div class="controls">
				<form:input path="ordersCompany" maxlength="200" class="required" />
			</div>
		</div>


		<div class="control-group">
			<label class="control-label"> お問い合わせ:</label>
			<div class="controls">
				<form:input path="ordersConsultNumber" maxlength="200"
					class="required" />
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">返品時間:</label>
			<div class="controls">
				<label class="lbl"><fmt:formatDate
						value="${orders.ordersDeliveryDate}" type="both" /></label>
			</div>
		</div>

		<div class="form-actions">

			<shiro:hasPermission name="spi:rejectorders:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="保 存" />&nbsp;</shiro:hasPermission>

			<input id="btnCancel" class="btn" type="button" value="戻  る"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>
