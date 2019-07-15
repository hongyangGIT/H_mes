<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div id="dialog-productUpdate-form" style="display: none;">
		<form id="productUpdateForm">
			<table
				class="table table-striped table-bordered table-hover dataTable no-footer"
				role="grid">
				<input type="hidden" name="id" id="input-Id" value="" />
				<tr>
					<td><label for="productId">材料id</label></td>
					<td><input id="input-productId" type="text" name="productId"
						value="" class="text ui-widget-content ui-corner-all" readonly="true"></td>
				</tr>
				<tr>
					<td><label for="productHeatid">炉号</label></td>
					<td><input id="input-productHeatid" type="text" name="productHeatid"
						value="" class="text ui-widget-content ui-corner-all"></td>
					<td><label for="orderProductname">图号</label></td>
					<td><input id="input-productImgid" type="text"
						name="productImgid" value=""
						class="text ui-widget-content ui-corner-all"></td>
				</tr>
				<tr>
					<td><label for="orderContractid">材料名称</label></td>
					<td><input id="input-productMaterialname" type="text" name="productMaterialname"
						value="" class="text ui-widget-content ui-corner-all"></td>
					<td><label for="orderImgid">材料来源</label></td>
					<td><select id="input-productMaterialsource" name="productMaterialsource" aria-controls="dynamic-table" style="width:165px;">
<!--                                 	   钢锭，外协件，外购件，废料，余料，半成品 -->
                                    <option value="钢材">钢材</option>
                                    <option value="废料">废料</option>
                                    <option value="外购件">外购件</option>
                                    <option value="外协件">外协件</option>
                                    <option value="钢锭">钢锭</option>
                                </select> </td>
				</tr>
				<tr>
					<td><label for="productTargetweight">工艺重量</label></td>
					<td><input id="input-productTargetweight" type="number"
						name="productTargetweight" value=""
						class="text ui-widget-content ui-corner-all"></td>
					<td><label for="productRealweight">投料重量</label></td>
					<td><input id="input-productRealweight" type="number" name="productRealweight"
						value="" class="datepicker text ui-widget-content ui-corner-all"
						></td>
				</tr>
				<tr>
					<td><label for="productLeftweight">剩余重量</label></td>
					<td><input id="input-productLeftweight" type="number" name="productLeftweight"
						value="" class="datepicker text ui-widget-content ui-corner-all"
						></td>
					<td><label for="productIrontypeweight">锭型</label></td>
					<td>
					<input type="text" id="input-productIrontypeweight" name="productIrontypeweight"value="">
					</td>
				</tr>
				<tr>
					<td><label for="productIrontype">锭型类别</label></td>
					<td>
					 <input type="text" id="input-productIrontype" name="productIrontype" value="" >
					 </td>
					<td><label for="productStatus">状态</label></td>
					<td> <select id="input-productStatus" name="productStatus" aria-controls="dynamic-table" style="width:165px;">
                                    <option value="0">未启用</option>
                                    <option value="1">启用</option>
                                </select> </td>
				</tr>
				<tr>
					<td><label for="productRemark">备注</label></td>
					<td>
					  <input type="text" id="input-productRemark"name="productRemark" value="">
					</td>
				</tr>
			</table>
		</form>
	</div>