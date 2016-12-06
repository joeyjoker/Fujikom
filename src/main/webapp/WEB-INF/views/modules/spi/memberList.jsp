<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>ユーザー管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/spi/member/">ユーザーリスト</a></li>
		<%-- <li ><a href="${ctx}/spi/member/form">ユーザー詳細</a></li> --%>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	 <form:form id="searchForm" modelAttribute="member" action="${ctx}/spi/member/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>氏名 ：</label><form:input path="memberRealName" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検 索"/>
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>ユーザー名</th>
			<th>氏名</th>
			<th>電話番号</th>
			<th>郵便番号</th>
			<th>お宅の住所</th>
			<th>倉庫の住所</th>
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:member:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="member">
			<tr>
			    <td>${member.memberName}</td>
				<td>
				  <a href="${ctx}/spi/member/form?memberId=${member.memberId}">${member.memberRealName} </a> 
				</td>
				<td>
				  ${member.memberTelphone}
				</td>
				<td>${member.memberZip}</td>
				<td>${member.memberAddress}</td>
				<td>${member.warehouse.warehouseAddress}</td>
				<shiro:hasPermission name="spi:member:edit"><td>
    				<a href="${ctx}/spi/member/form?memberId=${member.memberId}">詳細</a>	
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
