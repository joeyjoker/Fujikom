<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>返品管理</title>
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
		<li class="active"><a href="${ctx}/spi/rejectorders/">返品リスト</a></li>
		<%-- <li><a href="${ctx}/spi/rejectorders/form">返品詳細</a></li> --%>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	 <form:form id="searchForm" modelAttribute="orders" action="${ctx}/spi/rejectorders/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>受注氏名 ：</label><form:input path="member.memberRealName" maxlength="50" class="input-small"/>
		<label>ステータス：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="ordersStatus" items="${fns:getDictList('spi_rejectorders_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検索"/>
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>受注番号</th>
			<th>ステータス</th>
			<th>決済ステータス</th>
			<th>返品パターン</th>
			<th>受注氏名</th>
			<th>受注日</th>
			<th>金額</th>
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:rejectorders:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="orders">
			<tr>
			    <td><a href="${ctx}/spi/rejectorders/form?ordersId=${orders.ordersId}">${orders.ordersNumber}</a></td>
				<td>
				  <c:if test="${orders.ordersStatus eq '1'}">返品待ち</c:if>
				  <c:if test="${orders.ordersStatus eq '0'}">返品済み</c:if> 
				</td>
				<td>
				  <c:if test="${orders.ordersAccountFlag eq '1'}">決済待ち</c:if>
				  <c:if test="${orders.ordersAccountFlag eq '0'}">決済済み</c:if> 
				</td>
				<td>${orders.ordersRejectPattern}</td>
				<td>${orders.member.memberRealName}</td>
				<td><fmt:formatDate value="${orders.ordersRequestDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${orders.ordersPrice}</td>
				
				<shiro:hasPermission name="spi:rejectorders:edit"><td>
    				<c:if test="${orders.ordersStatus eq '0'}"><a href="${ctx}/spi/rejectorders/form?ordersId=${orders.ordersId}">変更</a></c:if>
    				<c:if test="${orders.ordersStatus eq '1'}"><a href="${ctx}/spi/rejectorders/reject?ordersId=${orders.ordersId}"
    				    onclick="return confirmx('返品しますか？', this.href)">返品</a></c:if>
					<%-- <a href="${ctx}/spi/sendorders/form?ordersId=${orders.ordersId}" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a> --%>
					<c:if test="${orders.ordersAccountFlag eq '0'}"><a href="${ctx}/spi/rejectorders/delete?ordersId=${orders.ordersId}" 
						onclick="return confirmx('削除しますか？', this.href)">削除</a></c:if>
					<c:if test="${orders.ordersAccountFlag eq '1'}"><a href="${ctx}/spi/rejectorders/account?ordersId=${orders.ordersId}" onclick="return confirmx('決済を行いますか?', this.href)">決済</a></c:if>
					<a href="${ctx}/spi/sendorders/productlist?ordersId=${orders.ordersId}">商品リスト</a>
					
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
