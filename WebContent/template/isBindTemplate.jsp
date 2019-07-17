<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script id="isBindTemplate" type="x-tmpl-mustache">
{{#productList}}
 <tr role="row" class="order-name odd" data-id="{{id}}"><!--even -->
	<td><a href="#" data-id="{{productId}}">{{productId}}</a></td>
	<td>{{productMaterialname}}</td>
	<td>{{productMaterialsource}}</td>
	<td>{{productTargetweight}}</td>
	<td>
		<div class="hidden-sm hidden-xs action-buttons" id="dynamic-table_length">
			<a class="btn blue unBind-btn  btn-danger  fa fa-check " href="#" data-id="{{id}}" product_targetweight="{{productTargetweight}}">
				解除
			</a>
		</div>
	</td>
</tr>
{{/productList}}
</script>