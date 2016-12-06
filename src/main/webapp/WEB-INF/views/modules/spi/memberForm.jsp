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
		<li><a href="${ctx}/spi/product/">ユーザーリスト</a></li>
		<li class="active"><a>ユーザー詳細</a></li>
	</ul>
	
	<form:form id="inputForm" modelAttribute="member" action="${ctx}/spi/member/save" method="post" class="form-horizontal">
		<form:hidden path="memberId"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">ユーザー名:</label>
			<div class="controls">
				<label class="lbl">${member.memberName}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">氏名:</label>
			<div class="controls">
				<label class="lbl">${member.memberRealName}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">発送先名:</label>
			<div class="controls">
				<label class="lbl">${member.memberSendName}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">郵便番号:</label>
			<div class="controls">
				<label class="lbl">${member.memberZip}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">電話番号:</label>
			<div class="controls">
				<label class="lbl">${member.memberTelphone}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">お宅の住所:</label>
			<div class="controls">
				<label class="lbl">${member.memberAddress}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">建物名:</label>
			<div class="controls">
				<label class="lbl">${member.memberBuliding}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">倉庫の住所:</label>
			<div class="controls">
				<form:select path="warehouse.warehouseId">
				       <form:option value="" label="選択してください"/>
					<c:forEach var="warehouse" items="${warehouseList}">
						<form:option value="${warehouse.warehouseId}">${warehouse.warehouseAddress}</form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		

		<div class="form-actions">
			<shiro:hasPermission name="spi:member:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="戻 る" onclick="history.go(-1)"/>
		</div> 
	</form:form>
</body>
</html>
