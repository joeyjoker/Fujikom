<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单商品列表</title>
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
		<li class="active"><a>受注商品リスト</a></li>
	</ul>
	 <form:form id="searchForm" modelAttribute="product" action="${ctx}/spi/product/" method="post" class="breadcrumb form-search">
		<%-- <ul class="unstyled inline">
		    <li><label>订单编号:${ordersNumber}</label></li>
		</ul> --%>
		
		 <label>受注番号:${ordersNumber}</label>
			
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>商品名</th>
			<th>氏名</th>
			<th>ステータス</th>
			<th>商品登録時間</th>
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="product">
			<tr>
			    <td><a href="${ctx}/spi/sendorders/productform?productId=${product.productId}">${product.productName}</a></td>
				<td>
				  ${product.member.memberRealName}
				  
				</td>
				<td>
				  <c:if test="${product.productStatus eq 'D'}">倉庫へ送り中</c:if>
				  <c:if test="${product.productStatus eq 'P'}">処理中</c:if>
				  <c:if test="${product.productStatus eq 'I'}">倉庫</c:if> 
				  <c:if test="${product.productStatus eq 'S'}">発送済み</c:if> 
				  <c:if test="${product.productStatus eq 'R'}">引き取り済み</c:if> 
				</td>
				<td><fmt:formatDate value="${product.productCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
    				<a href="${ctx}/spi/sendorders/productform?productId=${product.productId}">編集</a>
						
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
