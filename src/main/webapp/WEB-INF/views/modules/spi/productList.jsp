<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>商品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("ダウンロードしますか？"," システムダイアログ",function(v,h,f){
					if(v == "ok"){
						$("#searchForm").attr("action","${ctx}/spi/product/export").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/spi/product/").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/spi/product/">商品リスト</a></li>
		<%-- <li ><a href="${ctx}/spi/product/form">商品詳細</a></li> --%>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	 <form:form id="searchForm" modelAttribute="product" action="${ctx}/spi/product/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>商品名 ：</label><form:input path="productName" htmlEscape="false" maxlength="50" class="input-small"/>
		<label>バーコード ：</label><form:input path="productBarcode" htmlEscape="false" maxlength="50" class="input-small"/>
		<label>ユーザー名 ：</label><form:input path="member.memberRealName" htmlEscape="false" maxlength="50" class="input-small"/>
		<label>ステータス：</label><form:radiobuttons onclick="return page();" path="productStatus" items="${fns:getDictList('spi_product_status')}"
		 itemLabel="label" itemValue="value" htmlEscape="false" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検索" onclick="return page();"/>
		&nbsp;<input id="btnExport" class="btn btn-primary" type="button" value="CSVダウンロード"/>
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th width="25%">商品名</th>
			<th>氏名</th>
			<th>ステータス</th>
			<th>サイズ</th>
			<th>バーコード</th>
			<th>商品登録時間</th>
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:product:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="product">
			<tr>
			    <td><a href="${ctx}/spi/product/form?productId=${product.productId}">${product.productName}</a></td>
				<td>
				  ${product.member.memberRealName}
				  
				</td>
				<td>
				  <c:if test="${product.productStatus eq 'D'}">倉庫へ送り中</c:if>
				  <c:if test="${product.productStatus eq 'P'}">処理中</c:if>
				  <c:if test="${product.productStatus eq 'I'}">倉庫</c:if> 
				  <c:if test="${product.productStatus eq 'NS'}">処理中</c:if>
				  <c:if test="${product.productStatus eq 'S'}">発送済み</c:if> 
				  <c:if test="${product.productStatus eq 'R'}">引き取り済み</c:if> 
				</td>
				<td>${product.productSize}</td>
				<td>${product.productBarcode}</td>
				<td><fmt:formatDate value="${product.productCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<shiro:hasPermission name="spi:product:edit"><td>
    				<a href="${ctx}/spi/product/form?productId=${product.productId}">編集</a>
					<%-- <a href="${ctx}/spi/product/form?productId=${product.productId}" onclick="return confirmx('削除しますか？', this.href)">削除</a>		 --%>	
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
