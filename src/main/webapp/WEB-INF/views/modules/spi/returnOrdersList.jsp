<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>取回管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#btnExport").click(function(){
				top.$.jBox.confirm("ダウンロードしますか？"," システムダイアログ",function(v,h,f){
					if(v == "ok"){
						$("#searchForm").attr("action","${ctx}/spi/returnorders/exportcsv").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/spi/returnorders/").submit();
        	return false;
        }
	</script>
</head>
<body>
  
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/spi/returnorders/">引き取りリスト</a></li>
		<%-- <li><a href="${ctx}/spi/returnorders/form">引き取り詳細</a></li> --%>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	 <form:form id="searchForm" modelAttribute="orders" action="${ctx}/spi/returnorders/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>氏名 ：</label><form:input path="member.memberRealName"  maxlength="50" class="input-small"/>
		<label>ステータス：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="ordersStatus" items="${fns:getDictList('spi_returnorders_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検索"  onclick="return page();"/>
		<input id="btnExport" class="btn btn-primary" type="button" value="CSVダウンロード"/>  
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>受注番号</th>
			<th>ステータス</th>
			<th>決済ステータス</th>
			<th>氏名</th>
			<th>引き取り配送先</th>
			<th>引き取り電話番号</th>
			<th>受注日</th>
			<th>金額</th>
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:returnorders:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="orders">
			<tr>
			    <td><a href="${ctx}/spi/returnorders/form?ordersId=${orders.ordersId}">${orders.ordersNumber}</a></td>
				<td>
				  <c:if test="${orders.ordersStatus eq '1'}">引き取り待ち</c:if>
				  <c:if test="${orders.ordersStatus eq '0'}">引き取り済み</c:if> 
				</td>
				<td>
				  <c:if test="${orders.ordersAccountFlag eq '1'}">決済待ち</c:if>
				  <c:if test="${orders.ordersAccountFlag eq '0'}">決済済み</c:if> 
				</td>
				<td>${orders.member.memberRealName}</td>
				<td>${orders.ordersLocation}</td>
				<td>${orders.ordersTelphone}</td>
				<td><fmt:formatDate value="${orders.ordersRequestDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>${orders.ordersPrice}</td>
				
				<shiro:hasPermission name="spi:returnorders:edit"><td>
    				<c:if test="${orders.ordersStatus eq '0'}"><a href="${ctx}/spi/returnorders/form?ordersId=${orders.ordersId}">変更</a></c:if>
    				<c:if test="${orders.ordersStatus eq '1'}"><a href="${ctx}/spi/returnorders/form?ordersId=${orders.ordersId}">詳細</a></c:if>
					<%-- <a href="${ctx}/spi/sendorders/form?ordersId=${orders.ordersId}" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a> --%>
					<c:if test="${orders.ordersStatus eq '0'}"><a href="${ctx}/spi/returnorders/delete?ordersId=${orders.ordersId}" 
						onclick="return confirmx('削除しますか？', this.href)">削除</a></c:if>
					<c:if test="${orders.ordersAccountFlag eq '1'}"><a href="${ctx}/spi/returnorders/account?ordersId=${orders.ordersId}" onclick="return confirmx('決済を行いますか?', this.href)">決済</a></c:if>
					<a href="${ctx}/spi/sendorders/productlist?ordersId=${orders.ordersId}">商品リスト</a>
					
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
