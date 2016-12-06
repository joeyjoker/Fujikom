<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>费用管理</title>
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
		<li class="active"><a href="${ctx}/spi/expense/">费用リスト</a></li>
	</ul>
	 <form:form id="searchForm" modelAttribute="expense" action="${ctx}/spi/expense/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>费用名：</label><form:input path="expenseName" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検 索"/>
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>费用size</th>
			<th>全权委托费</th>
			<th>入库费</th>
			<th>保管费</th>
			<th>配送费</th>
			<th>退货费</th>
			<th>退货手续费</th>
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:expense:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="expense">
			<tr>
			    <td>
			      <a href="${ctx}/spi/expense/form?expenseId=${expense.expenseId}">${expense.expenseSize} </a> 
			    </td>
				<td>
				  ${expense.expenseEntrust}
				</td>	
				<td>
				  ${expense.expenseInstock}
				</td>
				<td>
				  ${expense.expenseStorage}
				</td>
				<td>
				  ${expense.expenseDelivery}
				</td>
				<td>
				  ${expense.expenseReject}
				</td>
				<td>
				  ${expense.expenseRejectHandling}
				</td>
				<shiro:hasPermission name="spi:expense:edit"><td>
    				<a href="${ctx}/spi/expense/form?expenseId=${expense.expenseId}">変更</a>	
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
