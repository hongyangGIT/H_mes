<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<script id="orderBatchListTemplate" type="x-tmpl-mustache">
{{#orderList}}
	<tr role="row" class="user-name odd" data-id="{{id}}"><!--even -->
	    <td><a href="#" class="user-edit" data-id="{{id}}">{{username}}</a></td>
	    <td>{{showDeptName}}</td>
	    <td>{{mail}}</td>
	    <td>{{telephone}}</td>
	    <td>{{#bold}}{{showStatus}}{{/bold}}</td> <!-- 此处套用函数对status做特殊处理 -->
	    <td>
	        <div class="hidden-sm hidden-xs action-buttons">
	            <a class="green user-edit" href="#" data-id="{{id}}">
	                <i class="ace-icon fa fa-pencil bigger-100"></i>
	            </a>
	            <a class="red user-acl" href="#" data-id="{{id}}">
	                <i class="ace-icon fa fa-flag bigger-100"></i>
	            </a>
	        </div>
	    </td>
	</tr>
{{/orderList}}
</script>