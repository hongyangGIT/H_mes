<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <title>材料管理</title>
<%--     <jsp:include page="/common/backend_common.jsp"/> --%>
<%--     <jsp:include page="/common/page.jsp"/> --%>
</head>
<%@ include file="/common/backend_common.jsp" %>
<link rel="stylesheet" href="/css/my.css"/>
<%-- <%@ include file="/common/page.jsp" %> --%>
<%-- <%@ include file="/template/orderBatchListTemplate.jsp" %> --%>
<body class="no-skin" youdao="bind" style="background: white">
<input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5"/>
<div class="page-header">
    <h1>
       材料管理
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i>
            创建与查询
        </small>
    </h1>
</div>


    <div class="col-sm-12">
        <div class="col-xs-12">
            <div class="table-header">
               材料列表&nbsp;&nbsp;
<!--                 <a class="green" href="#"> -->
<!--                     <i class="ace-icon fa fa-plus-circle orange bigger-130 order-add"></i> -->
<!--                 </a> -->
            </div>
            <form id="productForm">
            <div id="mydiv">
                <div id="dynamic-table_wrapper" class="dataTables_wrapper form-inline no-footer">
                    <div class="row myrowtwo">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                炉号
                               
								</div>
                            </div>
                            <div class="col-xs-4">
                            <input type="text" id="product_heatid" name="productHeatid" value="">
                            </div>
                    </div>
                    <div class="row myrowone">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                图号
                               
								</div>
                            </div>
                            <div class="col-xs-4">
                            <input type="text" id="product_imgid" name="productImgid" value="">
                            </div>
                             <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                材料名称
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id="product_materialname" name="productMaterialname" value="">
                        </div>
                    </div>
                    <div class="row myrowtwo">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                材料来源
								</div>
                            </div>
                            <div class="col-xs-4">
                            <select id="product_materialsource" name="productMaterialsource" aria-controls="dynamic-table" style="width:165px;">
<!--                                 	   钢锭，外协件，外购件，废料，余料，半成品 -->
                                    <option value="钢材">钢材</option>
                                    <option value="废料">废料</option>
                                    <option value="外购件">外购件</option>
                                    <option value="外协件">外协件</option>
                                    <option value="钢锭">钢锭</option>
                                </select> 
                            </div>
                             <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                工艺重量
                            </div>
                        </div>
                        <div class="col-xs-4" style=" padding-left: 15px;">
                            <input type="number" id="product_targetweight" name="productTargetweight" value="">
                        </div>
                    </div>
                   <div class="row myrowone">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                投料重量
                               
								</div>
                            </div>
                            <div class="col-xs-4" style=" padding-left: 15px;">
                            <input type="number" id="product_realweight" name="productRealweight" value="">
                            </div>
                             <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                               剩余重量
                            </div>
                        </div>
                        <div class="col-xs-4" style=" padding-left: 15px;">
                            <input type="number" id="product_leftweight" name="productLeftweight" value="0">
                        </div>
                    </div> 
                <div class="row myrowtwo">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                锭型
                               
								</div>
                            </div>
                            <div class="col-xs-4">
                            <input type="text" id="product_irontypeweight" name="productIrontypeweight"value="">
                            </div>
                             <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                               锭型类别
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id="product_irontype" name="productIrontype" value="" >
                        </div>
                    </div>     
                  <div class="row myrowone">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                是否启用
                               
								</div>
                            </div>
                            <div class="col-xs-4">
                            <select id="product_status" name="productStatus" aria-controls="dynamic-table" style="width:165px;">
                                    <option value="0">未启用</option>
                                    <option value="1">启用</option>
                                </select> 
                            </div>
                             <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                               备注
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <input type="text" id="product_remark"name="productRemark" value="">
                        </div>
                    </div>  
                    <div class="row myrowtwo">
                        <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                                批量生成个数
                               
								</div>
                            </div>
                            <div class="col-xs-4" style=" padding-left: 15px;">
                            <input type="number" id="count" name="count" value="">
                            </div>
                             <div class="col-xs-2">
                            <div class="dataTables_length" id="dynamic-table_length">
                              生成材料
                            </div>
                        </div>
                        <div class="col-xs-4" style="padding-left: 15px;">
                           <button class="btn btn-info  submit-btn"
										style="margin-bottom: 6px; " type="button">点击按钮</button>
                        </div>
                    </div> 
                   </div>
            </div>
                    
                 </form>   
                </div>
            </div>
</body>
<script type="text/javascript" src="productinsert.js"></script>
</html>
