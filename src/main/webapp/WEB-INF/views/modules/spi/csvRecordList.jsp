<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>CSV记录</title>
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
		<li class="active"><a href="${ctx}/spi/csvrecord/">CSV履歴リスト</a></li>
	</ul>
	 <form:form id="searchForm" modelAttribute="csvRecord" action="${ctx}/spi/csvrecord/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>CSVファイル名：</label><form:input path="csvFileName" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="検 索"/>
	</form:form> 
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		    <th>CSVファイル名</th>
			<th>時間</th>			
			<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
			<shiro:hasPermission name="spi:csvrecord:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="csvRecord">
			<tr>
			    <td>
			      ${csvRecord.csvFileName} 
			    </td>
				<td>
				  ${csvRecord.csvCreateDate}
				</td>
				<shiro:hasPermission name="spi:csvrecord:edit"><td>
    				<a href="${ctx}/spi/csvrecord/download?csvId=${csvRecord.csvId}">ダウンロード</a>	
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
