<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>仓库管理</title>
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
		<li class="active"><a href="${ctx}/spi/warehouse/">倉庫リスト</a></li>
		<shiro:hasPermission name="spi:warehouse:edit"><li ><a href="${ctx}/spi/warehouse/form">倉庫追加</a></li></shiro:hasPermission>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	 <form:form id="searchForm" modelAttribute="warehouse" action="${ctx}/spi/warehouse/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>倉庫名：</label><form:input path="warehouseName" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検 索"/>
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>倉庫名</th>
			<th>住所</th>
			<th>郵便番号</th>
			<th>連絡先</th>
			<th>備考</th>
			
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:warehouse:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="warehouse">
			<tr>
			    <td>
			      <a href="${ctx}/spi/warehouse/form?warehouseId=${warehouse.warehouseId}">${warehouse.warehouseName} </a> 
			    </td>
				<td>
				  ${warehouse.warehouseAddress}
				</td>
				<td>
				  ${warehouse.warehouseZip}
				</td>
				<td>
				  ${warehouse.warehouseTelphone}
				</td>
				<td>
				  ${warehouse.warehouseRemarks}
				</td>
				<shiro:hasPermission name="spi:warehouse:edit"><td>
    				<a href="${ctx}/spi/warehouse/form?warehouseId=${warehouse.warehouseId}">変更</a>	
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
