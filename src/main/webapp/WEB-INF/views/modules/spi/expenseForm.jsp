<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>费用詳細</title>
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/spi/expense/">费用リスト</a></li>
		<li class="active"><a href="">费用<shiro:hasPermission name="spi:expense:edit">${not empty expense.expenseId?'編集':'追加'}</shiro:hasPermission><shiro:lacksPermission name="spi:expense:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	
	<form:form id="inputForm" modelAttribute="expense" action="${ctx}/spi/expense/save" method="post" class="form-horizontal">
		<form:hidden path="expenseId"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">费用size:</label>
			<div class="controls">
				<form:input path="expenseSize" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">全权委托费:</label>
			<div class="controls">
				<form:input path="expenseEntrust" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">入库费:</label>
			<div class="controls">
				<form:input path="expenseInstock" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">保管费:</label>
			<div class="controls">
				<form:input path="expenseStorage" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">配送费:</label>
			<div class="controls">
				<form:input path="expenseDelivery" htmlEscape="false" maxlength="200" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退货费:</label>
			<div class="controls">
				<form:input path="expenseReject" htmlEscape="false" maxlength="200" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">退货手续费:</label>
			<div class="controls">
				<form:input path="expenseRejectHandling" htmlEscape="false" maxlength="200" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="spi:expnese:edit"><input id="btnSubmit" class="ladda-button ladda-button-demo btn btn-primary"  data-style="zoom-in" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="戻 る" onclick="history.go(-1)"/>
		</div> 
	</form:form>
</body>
</html>
