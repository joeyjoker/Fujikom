<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>ユーザー詳細</title>
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
		<li><a href="${ctx}/spi/warehouse/">倉庫リスト</a></li>
		<li class="active"><a href="">倉庫<shiro:hasPermission name="spi:warehouse:edit">${not empty warehouse.warehouseId?'編集':'追加'}</shiro:hasPermission><shiro:lacksPermission name="spi:warehouse:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	
	<form:form id="inputForm" modelAttribute="warehouse" action="${ctx}/spi/warehouse/save" method="post" class="form-horizontal">
		<form:hidden path="warehouseId"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">倉庫名:</label>
			<div class="controls">
				<form:input path="warehouseName" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">住所:</label>
			<div class="controls">
				<form:input path="warehouseAddress" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">郵便番号:</label>
			<div class="controls">
				<form:input path="warehouseZip" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">連絡先:</label>
			<div class="controls">
				<form:input path="warehouseTelphone" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">備考:</label>
			<div class="controls">
				<form:input path="warehouseRemarks" htmlEscape="false" maxlength="200" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="spi:warehouse:edit"><input id="btnSubmit" class="ladda-button ladda-button-demo btn btn-primary"  data-style="zoom-in" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="戻 る" onclick="history.go(-1)"/>
		</div> 
	</form:form>
</body>
</html>
