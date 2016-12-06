<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发货订单管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
<script type="text/javascript">
		$(document).ready(function() {
			
			/* $("#btnExport").click(function(){
				top.$.jBox.confirm("ダウンロードしますか？"," システムダイアログ",function(v,h,f){
					if(v == "ok"){
						$("#searchForm").attr("action","${ctx}/spi/sendorders/exportcsv").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			}); */
			
			$("#btnImport2").click(function(){
				$.jBox($("#importBox2").html(), {title:"CSVインポート", buttons:{"クローズ":true}, 
					bottomText:"csv形式のファイルをインポートしてください。"});
			});
			
            $("#btnExport").click(function(){
            	top.$.jBox.confirm("ダウンロードしますか？"," システムダイアログ",function(v,h,f){
					if(v == "ok"){
        				$("#exportForm").submit();
						
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
            		  
            	  
				
			}); 
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/spi/sendorders/exportcsv").submit();
        	return false;
        }
	</script>
</head>
<body>
	<div id="importBox2" class="hide">
		<form id="importForm" action="${ctx}/spi/sendorders/importcsv"
			method="post" enctype="multipart/form-data"
			style="padding-left: 20px; text-align: center;" class="form-search"
			onsubmit="loading('インポートしています、少々お待ちください...');">
			<br /> <input id="uploadFile" name="file" type="file"
				style="width: 330px" /><br /> <br /> <input id="btnImportSubmit"
				class="btn btn-primary" type="submit" value="   インポート   " />

		</form>
	</div>

	<div id="exportBox2" class="hide">
		<form id="exportForm" action="${ctx}/spi/sendorders/exportcsv"
			method="post" enctype="multipart/form-data"
			style="padding-left: 20px; text-align: center;" class="form-search"
			onsubmit="loading('インポートしています、少々お待ちください...');"></form>
	</div>


	<%-- <div class="modal fade" id="importBox" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"  style="display:none;">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<!-- <button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button> -->
					<h4 class="modal-title" id="myModalLabel">CSVインポート</h4>
				</div>
				<div class="modal-body">
					<form id="importForm" method="post"
						action="${ctx}/spi/sendorders/importcsv"
						enctype="multipart/form-data"
						style="padding-left: 20px; text-align: center;"
						class="form-horizontal">
						 <div class="control-group">
							<label class="control-label">ファイルの選択：</label>
							<div class="controls">

								<input id="uploadFile" name="file" type="file"
									class="form-control" required />

							</div>
						</div>
						 <br /> <br /> <input id="btnImportSubmit"
							class="btn btn-primary" type="submit" value="ダウンロード" />
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">クローズ
					</button>
				</div>
			</div>
		</div>
	</div> --%>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/spi/sendorders/">発送リスト</a></li>
		<%-- <li><a href="${ctx}/spi/sendorders/form">発送詳細</a></li> --%>
		<%-- <shiro:hasPermission name="spi:sendorders:edit"><li><a href="${ctx}/spi/sendorders/form">项目添加</a></li></shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="orders"
		action="${ctx}/spi/sendorders/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<label>氏名 ：</label>
		<form:input path="member.memberRealName" htmlEscape="false"
			maxlength="50" class="input-small" />
		<label>ステータス：</label>
		<form:radiobuttons onclick="$('#searchForm').submit();"
			path="ordersStatus" items="${fns:getDictList('spi_orders_status')}"
			itemLabel="label" itemValue="value" htmlEscape="false" />
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit"
			value="検索" onclick="return page();" />
		<input id="btnExport" class="btn btn-primary" type="button"
			value="CSVダウンロード" />
		<!-- <a id="btnImport" class="btn btn-primary" data-toggle="modal"
			data-target="#importBox">CSVインポート</a>  -->
		&nbsp;<input id="btnImport2" class="btn btn-primary" type="button"
			value="CSVインポート" />
	</form:form>
	<tags:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>受注番号</th>
				<th>ステータス</th>
				<th>決済ステータス</th>
				<th>氏名</th>
				<th>購入者配送先</th>
				<th>購入者電話番号</th>
				<th>受注日</th>
				<th>金額</th>
				<%-- <c:if test="${orders.delFlag ne '2'}"></c:if> --%>
				<shiro:hasPermission name="spi:sendorders:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="orders">
				<tr>
					<td><a
						href="${ctx}/spi/sendorders/form?ordersId=${orders.ordersId}">${orders.ordersNumber}</a></td>
					<td><c:if test="${orders.ordersStatus eq '1'}">発送待ち</c:if> <c:if
							test="${orders.ordersStatus eq '0'}">発送済み</c:if></td>
					<td><c:if test="${orders.ordersAccountFlag eq '1'}">決済待ち</c:if>
						<c:if test="${orders.ordersAccountFlag eq '0'}">決済済み</c:if></td>
					<td>${orders.member.memberRealName}</td>
					<td>${orders.ordersLocation}</td>
					<td>${orders.ordersTelphone}</td>
					<td><fmt:formatDate value="${orders.ordersRequestDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${orders.ordersPrice}</td>

					<shiro:hasPermission name="spi:sendorders:edit">
						<td><c:if test="${orders.ordersStatus eq '0'}">
								<a href="${ctx}/spi/sendorders/form?ordersId=${orders.ordersId}">変更</a>
							</c:if> <c:if test="${orders.ordersStatus eq '1'}">
								<a href="${ctx}/spi/sendorders/form?ordersId=${orders.ordersId}">詳細</a>
							</c:if> <%-- <a href="${ctx}/spi/sendorders/form?ordersId=${orders.ordersId}" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a> --%>
							<c:if test="${orders.ordersAccountFlag eq '0'}">
								<a
									href="${ctx}/spi/sendorders/delete?ordersId=${orders.ordersId}"
									onclick="return confirmx('削除しますか？', this.href)">削除</a>
							</c:if> <c:if test="${orders.ordersAccountFlag eq '1'}">
								<a
									href="${ctx}/spi/sendorders/account?ordersId=${orders.ordersId}"
									onclick="return confirmx('決済を行いますか?', this.href)">決済</a>
							</c:if> <a
							href="${ctx}/spi/sendorders/productlist?ordersId=${orders.ordersId}">商品リスト</a>
						</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
