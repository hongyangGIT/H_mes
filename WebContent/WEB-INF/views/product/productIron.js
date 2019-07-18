$(function(){
	//执行分页逻辑
	//定义一些全局变量
	var productMap = {};//准备一个map格式的仓库，等待存储从后台返回过来的数据
	var pageSize;//页码条数
	var pageNo;//当前页
	var url;//查询url
	var keyword;//关键字
	var search_materialsource;//查询状态  钢材  废材  外协件 外购件  "" 
	var search_status;
	//加载模板内容进html
	//获取模板内容
	var productListTemplate = $("#productListTemplate").html();
	//将内容交给mustache来处理
	Mustache.parse(productListTemplate);
	
	//调用分页函数
	loadProductList();
	//点击刷新时也要调用分页函数
	$(".research").click(function(e) {
		e.preventDefault();
		$("#productPage .pageNo").val(1);
		loadProductList();
	});
	//定义调用分页函数，一定包含查询条件keyword，search_status。。
	function loadProductList(urlnew) {
		//获取页面当前需要查询的还留在页码上的信息
		//在当前页中找到需要调用的页码条数
		pageSize = $("#pageSize").val();
		//当前页
		pageNo = $("#productPage .pageNo").val() || 1;
		if (urlnew) {
			url = urlnew;
		} else {
			url = "/product/productIron.json";
		}
		keyword = $("#keyword").val();
		search_materialsource = $("#search_materialsource").val();
		search_status=$("#search_status").val();
		//发送请求
		$.ajax({
			url : url,
			data : {//左面是数据名称-键，右面是值
				pageNo : pageNo,
				pageSize : pageSize,
				keyword : keyword,
				search_materialsource : search_materialsource,
				search_status:search_status
			},
			type : 'POST',
			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
				//渲染order列表和页面--列表+分页一起填充数据显示条目
//				alert(6666)
//				alert(result.ret)
				renderOrderListAndPage(result, url);
			}
		});
	}
	
	
	//渲染所有模板
	//result中的存储数据，就是一个list<MesOrder>集合,是由service访问数据库后返回给controller的数据模型
	function renderOrderListAndPage(result, url) {
		//从数据库返回过来的数据集合result
		if (result.ret) {
			//再次初始化查询条件
			url = "/product/productIron.json";
			keyword = $("#keyword").val();
			search_materialsource = $("#search_materialsource").val();
			
			//如果查询到数据库中有符合条件的order列表
			if (result.data.total > 0) {
				//为订单赋值--在对orderlisttemplate模板进行数据填充--视图渲染
//				//Mustache.render({"name":"李四","gender":"男"});
//				//Mustache.render(list=new ArrayList<String>(){"a01","a02"},{"name":"list[i].name","gender":list[i].gender});
				var rendered = Mustache.render(
						productListTemplate,//<script id="orderListTemplate" type="x-tmpl-mustache">
								{
									"productList" : result.data.data,//{{#orderList}}--List-(result.data.data-list<MesOrder>)
									
									"showStatus" : function() {
										return this.productStatus == 1 ? '已到库'
												: (this.productStatus == 0 ? '未到库'
														: '删除');
									},
									"bold" : function() {
										return function(text, render) {
											var status = render(text);
											if (status == '已到库') {
												return "<span class='label label-sm label-success'>已到库</span>";
											} else if (status == '未到库') {
												return "<span class='label label-sm label-warning'>未到库</span>";
											} else {
												return "<span class='label'>删除</span>";
											}
										}
									}
								});
				
				$.each(result.data.data, function(i, product) {//java-增强for
					productMap[product.id] = product;//result.data.data等同于List<mesOrder>
					//order.id-order  map key-value
				});
				$('#productList').html(rendered);
			} else {
				$('#productList').html('');
			}
			bindOrderClick();//更新操作
			var pageSize = $("#pageSize").val();
			var pageNo = $("#productPage .pageNo").val() || 1;
			//渲染页码
			renderPage(
					url,
					result.data.total,
					pageNo,
					pageSize,
					result.data.total > 0 ? result.data.data.length : 0,
					"productPage", loadProductList);
		} else {
			showMessage("获取订单列表", result.msg, false);
		}
	}
	//点击修改
	function bindOrderClick(){
		   $(".order-edit").click(function(e) {
			 //阻止默认事件
	         e.preventDefault();
				//阻止事件传播
	         e.stopPropagation();
	     	//获取productid
	         var productId = $(this).attr("data-id");
	         $("#dialog-productUpdate-form").dialog({
	             modal: true,
	             title: "编辑订单",
	             open: function(event, ui) {
	           	    $(".ui-dialog").css("width","600px");
	                  $(".ui-dialog-titlebar-close", $(this).parent()).hide();
	                	//将form表单中的数据清空，使用jquery转dom对象
	                  $("#productUpdateForm")[0].reset();
	                //拿到map中以键值对，id-product对象结构的对象,用来向form表单中传递数据
	                  var targetProduct = productMap[productId];
	                //如果取出这个对象
	                  if (targetProduct) {
	 						/////////////////////////////////////////////////////////////////
	                	  	$("#input-Id").val(targetProduct.id);
	 						$("#input-productId").val(targetProduct.productId);
	 						$("#input-productHeatid").val(targetProduct.productHeatid);
	 						$("#input-productImgid").val(targetProduct.productImgid);
	 						$("#input-productMaterialname").val(targetProduct.productMaterialname);
	 						$("#input-productMaterialsource").val(targetProduct.productMaterialsource);
	 						$("#input-productTargetweight").val(targetProduct.productTargetweight);
	 						$("#input-productRealweight").val(targetProduct.productRealweight);
	 						$("#input-productLeftweight").val(targetProduct.productLeftweight);
	 						$("#input-productIrontypeweight").val(targetProduct.productIrontypeweight);
	 						$("#input-productIrontype").val(targetProduct.productIrontype);
	 						$("#input-productStatus").val(targetProduct.productStatus);
	 						$("#input-productRemark").val(targetProduct.productRemark);
	 						/////////////////////////////////////////////////////////////////
	                  }
	                  
	              },
	              buttons : {
	                  "更新": function(e) {
	                      e.preventDefault();
	                      updateOrder( function (data) {
	                          $("#dialog-productUpdate-form").dialog("close");
	          				$("#productPage .pageNo").val(1);
	          				 showMessage("更新订单", data.msg, true);
	          				loadProductList();
	                      }, function (data) {
	                          showMessage("更新订单", data.msg, false);
	                      })
	                  },
	                  "取消": function (data) {
	                      $("#dialog-productUpdate-form").dialog("close");
	                  }
	              }
	             
	         });
		   	});
	   } 
	
	function updateOrder(successCallbak,failCallbak){
//		alert($("#productUpdateForm").serializeArray())
		$.ajax({
			url:"/product/update.json",
			data:$("#productUpdateForm").serializeArray(),
			type:'POST',
			success : function(result) {
				//数据执行成功返回的消息
				if (result.ret) {
//					console.log(66);
//                	loadProductList(); // 带参数回调
					//带参数回调
					if (successCallbak) {
						
						successCallbak(result);
					}
				} else {
					//执行失败后返回的内容
					if (failCallbak) {
						failCallbak(result);
					}
				}
			}
		});
	}
});