<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script id="unboundedTemplate" type="x-tmpl-mustache">
{{#productList}}
 <tr role="row" class="order-name odd" data-id="{{id}}"><!--even -->
	<td><a href="#" data-id="{{productId}}">{{productId}}</a></td>
	<td>{{productMaterialname}}</td>
	<td>{{productMaterialsource}}</td>
	<td>{{productTargetweight}}</td>
	<td>
		<div class="dataTables_length" id="dynamic-table_length">
			<a class="blue realBind-btn" href="#" data-id="{{id}}">
				<button class="btn btn-info fa fa-check "
			style="margin-bottom: 6px;" type="button">绑定</button>
			</a>
		</div>
	</td>
</tr>
{{/productList}}
</script>