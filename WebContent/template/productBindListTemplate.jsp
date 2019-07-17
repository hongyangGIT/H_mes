<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script id="productListTemplate" type="x-tmpl-mustache">
{{#productList}}
 <tr role="row" class="order-name odd" data-id="{{id}}"><!--even -->
	<td><a href="#" data-id="{{productId}}">{{productId}}</a></td>
	<td>{{pId}}</td>
	<td>{{productHeatid}}</td>
	<td>{{productMaterialname}}</td>
	<td>{{productMaterialsource}}</td>
	<td>{{productTargetweight}}</td>
	<td>{{productRealweight}}</td>
	<td>{{productLeftweight}}</td>
	<td>{{productBakweight}}</td>
	<td>{{productImgid}}</td> 
	<td>{{productIrontype}}</td>
    <td>{{productIrontypeweight}}</td>
    <td>{{#bold}}{{showStatus}}{{/bold}}</td> 
    <td>{{productRemark}}</td>
	<td>
		<div class="hidden-sm hidden-xs action-buttons" id="dynamic-table_length">
			<a class="blue bind-btn btn btn-info fa fa-check" href="#" data-id="{{id}}" product_leftweight_F="{{productLeftweight}}">
				点击绑定
			</a>
		</div>
	</td>
</tr>
{{/productList}}
</script>