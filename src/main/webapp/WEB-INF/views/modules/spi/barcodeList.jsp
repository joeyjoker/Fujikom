<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>申请バーコード管理</title>
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
		<li class="active"><a href="${ctx}/spi/barcode/">バーコードリスト</a></li>
		<%-- <li><a href="${ctx}/spi/barcode/form">订单一览</a></li> --%>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	 <%-- <form:form id="searchForm" modelAttribute="orders" action="${ctx}/spi/sendorders/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>申请人 ：</label><form:input path="member.memberRealName" htmlEscape="false" maxlength="50" class="input-small"/>
		<label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="ordersStatus" items="${fns:getDictList('spi_orders_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form> --%> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>依頼時間</th>
			<th>依頼個数</th>			
			<shiro:hasPermission name="spi:barcode:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="barcodeRequest">
			<tr>
			    <td><fmt:formatDate value="${barcodeRequest.barcodeRequestDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
				  ${barcodeRequest.barcodeNumber}
				</td>
				<shiro:hasPermission name="spi:barcode:edit"><td>
    				<a href="${ctx}/spi/barcode/generate?barcodeRequestId=${barcodeRequest.barcodeRequestId}"  target="_blank">生成</a>
					<a href="${ctx}/spi/barcode/delete?barcodeRequestId=${barcodeRequest.barcodeRequestId}" onclick="return confirmx('削除しますか？', this.href)">削除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
