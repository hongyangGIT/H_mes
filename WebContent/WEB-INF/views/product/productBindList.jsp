<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>材料查询</title>
<!-- jsp动态导入 -->
<%-- <jsp:include page="/common/backend_common.jsp" />
<jsp:include page="/common/page.jsp" />
<jsp:include page="/template/orderListTemplate.jsp" /> --%>
<%@ include file="/common/backend_common.jsp" %>
<%@ include file="/common/page.jsp" %>
<%@ include file="/common/bindPage.jsp" %>
<%@ include file="/common/isBindPage.jsp" %>
<%@ include file="/template/productBindListTemplate.jsp" %>
<%@ include file="/template/unboundedTemplate.jsp" %>
<%@ include file="/template/isBindTemplate.jsp" %>
</head>
<body class="no-skin" youdao="bind" style="background: white">
	<input id="gritter-light" checked="" type="checkbox"
		class="ace ace-switch ace-switch-5" />
	<div class="page-header">
		<h1>
			材料管理<small><i class="ace-icon fa fa-angle-double-right"></i>
				材料绑定 </small>
		</h1>
	</div>
	<div class="main-content-inner">
		<div class="col-sm-12 one">
			<div class="col-xs-12">
				<div class="table-header">
					材料列表&nbsp;&nbsp; <!-- <a class="green" href="#"> <i
						class="ace-icon fa fa-plus-circle orange bigger-130 order-add"></i>
					</a> -->
				</div>
				<div>
					<div id="dynamic-table_wrapper"
						class="dataTables_wrapper form-inline no-footer">
						<div class="row">

							<div class="col-xs-12">
								<div class="dataTables_length" id="dynamic-table_length">
									<label> 展示 <select id="pageSize"
										name="dynamic-table_length" aria-controls="dynamic-table"
										class="form-control input-sm">
											<option value="8">8</option>
									</select> 条记录
									</label> <input id="keyword" type="search" name="keyword"
										class="form-control input-sm" placeholder="关键词"
										aria-controls="dynamic-table"> <label>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;材料来源 <select
										id="search_materialsource" name="search_materialsource"
										aria-controls="dynamic-table" class="form-control input-sm">
											<option value="钢材">钢材</option>
											<option value="废料">废料</option>
											<option value="外协件">外协件</option>
											<option value="外购件">外购件</option>
									</select>
									<input type="hidden" name="bindPage_status" id="bindPage_status" value="1">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<button class="btn btn-info fa fa-check research"
										style="margin-bottom: 6px;" type="button">刷新</button>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</div>
							</div>
							
						</div>
						<table id="dynamic-table"
							class="table table-striped table-bordered table-hover dataTable no-footer"
							role="grid" aria-describedby="dynamic-table_info"
							style="font-size: 14px">
							<thead>
								<tr role="row">
									<input type="hidden" id="id" name="id" class="id" />
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料自编号</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">父级自编号</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">炉号</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料名称</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料来源</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">工艺重量</th>
									<th tabindex="0" class="batchStart-th" aria-controls="dynamic-table" rowspan="1"
										colspan="1">投料重量</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">剩余重量</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">绑定剩余</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">图号</th>
									<th tabindex="0" class="batchStart-th" aria-controls="dynamic-table" rowspan="1"
										colspan="1">锭型类别</th>
									<th tabindex="0" class="batchStart-th" aria-controls="dynamic-table" rowspan="1"
										colspan="1">锭型</th>
									<th tabindex="0" class="batchStart-th" aria-controls="dynamic-table" rowspan="1"
										colspan="1">状态</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">备注</th>
									<th class="sorting_disabled" rowspan="1" colspan="1"
										aria-label="">操作</th>
								</tr>
							</thead>
							<tbody id="productList">
							
							
							</tbody>
						</table>
						<div class="row" id="productPage">
						
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		
		
<!-- 		----------------------------------------------------------------------------------------------- -->
		
		<div class="col-sm-6 two">
			<div class="col-xs-12">
				<div class="table-header">
					未绑定材料列表
				</div>
				<div>
					<div id="dynamic-table_wrapper"
						class="dataTables_wrapper form-inline no-footer">
						<div class="row">

							<div class="col-xs-12">
								<div class="dataTables_length" id="dynamic-table_length">
									<label> 原料编号 
									<input type="text" id="productId" name="productId" value=""/>
									</label> 
									理论剩余重量
									<input id="productBakweight" type="number" name="productBakweight"
										class="form-control input-sm" value=""> 
								</div>
							</div>
							
							
							<div class="col-xs-12">
								<div class="dataTables_length" id="dynamic-table_length">
									
									剩余重量
									<input id="productLeftweight" type="number" name="productLeftweight"
										class="form-control input-sm"  value=""> 
										<label>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;材料来源 <select
										id="productMaterialsource" name="productMaterialsource"
										aria-controls="dynamic-table" class="form-control input-sm">
											<option value="钢锭">钢锭</option>
											<option value="钢材">钢材</option>
											<option value="废料">废料</option>
											<option value="外协件">外协件</option>
											<option value="外购件">外购件</option>
									</select>
									<input type="hidden" name="bindIcon_status" id="bindIcon_status" value="1">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<button class="btn btn-info fa fa-check researchBind"
										style="margin-bottom: 6px;" type="button">刷新</button>
									&nbsp;&nbsp;&nbsp;&nbsp;
								</div>
							</div>
						</div>
						<table id="dynamic-table"
							class="table table-striped table-bordered table-hover dataTable no-footer"
							role="grid" aria-describedby="dynamic-table_info"
							style="font-size: 14px">
							<thead>
								<tr role="row">
									<input type="hidden" id="id" name="id" class="id" />
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料自编号</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料名称</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料来源</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">工艺重量</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">操作</th>
								</tr>
							</thead>
							<tbody id="unboundedList">
							
							
							</tbody>
						</table>
							<div class="row" id="unboundedListPage">
						
							</div>
					</div>
				</div>
			</div>
		</div>
		
		
<!-- ------------------------------------------------------------------------------		 -->
		<div class="col-sm-6 two">
			<div class="col-xs-12">
				<div class="table-header">
					已绑定材料列表
				</div>
				<div>
				<input type="hidden" name="isbindIcon_status" id="isbindIcon_status" value="1">
					<div id="dynamic-table_wrapper"
						class="dataTables_wrapper form-inline no-footer">
						<table id="dynamic-table"
							class="table table-striped table-bordered table-hover dataTable no-footer"
							role="grid" aria-describedby="dynamic-table_info"
							style="font-size: 14px">
							<thead>
								<tr role="row">
									<input type="hidden" id="id" name="id" class="id" />
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料自编号</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料名称</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">材料来源</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">工艺重量</th>
									<th tabindex="0" aria-controls="dynamic-table" rowspan="1"
										colspan="1">操作</th>
								</tr>
							</thead>
							<tbody id="isBindList">
							
							
							</tbody>
						</table>
						<div class="row" id="isBindListPage">
						
							</div>
					</div>
				</div>
			</div>
		</div>
		
	</div>
</body>
<script type="text/javascript" src="productBind.js"></script>
</html>