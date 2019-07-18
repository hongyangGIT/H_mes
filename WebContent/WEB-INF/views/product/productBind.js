$(function(){
	$(".two").hide();
	//执行分页逻辑
	//定义一些全局变量
	var productMap = {};//准备一个map格式的仓库，等待存储从后台返回过来的数据
	var pageSize;//页码条数
	var pageNo;//当前页
	var url;//查询url
	var keyword;//关键字
	var search_materialsource;//查询状态  钢材  废材  外协件 外购件  "" 
	var search_status;
	var bindPage_status;
	var bindIcon_status;
	 var productId_F;//存放productid-父id
	 var product_leftweight_F;
	 var productId;
	//加载模板内容进html
	//获取模板内容
	var productListTemplate = $("#productListTemplate").html();
	//将内容交给mustache来处理
	Mustache.parse(productListTemplate);
	//未绑定页面模板
	var unboundedTemplate=$("#unboundedTemplate").html();
	Mustache.parse(unboundedTemplate);
	//已绑定页面模板
	var isBindTemplate=$("#isBindTemplate").html();
	Mustache.parse(isBindTemplate);
	
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
			url = "/product/product.json";
		}
		keyword = $("#keyword").val();
		search_materialsource = $("#search_materialsource").val();
		bindPage_status=$("#bindPage_status").val();
		search_status=1;
		//发送请求
		$.ajax({
			url : url,
			data : {//左面是数据名称-键，右面是值
				pageNo : pageNo,
				pageSize : pageSize,
				keyword : keyword,
				search_materialsource : search_materialsource,
				search_status:search_status,
				bindPage_status:bindPage_status
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
			url = "/product/product.json";
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
			bindProductClick();//点击绑定
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
//---------------------------------------------------------------
//	//点击绑定
	function bindProductClick(){
		   $(".bind-btn").click(function(e) {
			 //阻止默认事件
	         e.preventDefault();
				//阻止事件传播
	         e.stopPropagation();
	       //获取productid
	         productId = $(this).attr("data-id");
	         var product_leftweight=$(this).attr("product_leftweight_F");
	 	        if(productId){
	 	        	productId_F=productId;
	 	        }
	 	        if(product_leftweight){
	 	        	product_leftweight_F=product_leftweight;
	 	        }
	         searchAjax(productId);
	        
//	        $(".one").hide();
//	        $(".two").show();
//	      //拿到map中以键值对，id-product对象结构的对象,用来向form表单中传递数据
//            var targetProduct = productMap[productId];
//            //如果取出这个对象
//            if (targetProduct) {
//					/////////////////////////////////////////////////////////////////
//          	  		$("#productId").val(targetProduct.productId);
//					$("#productBakweight").val(targetProduct.productBakweight);
//					$("#productLeftweight").val(targetProduct.productLeftweight);
//					$("#productMaterialsource").val(targetProduct.productMaterialsource);
//					/////////////////////////////////////////////////////////////////
//            }
//	        //调用分页方法，只查钢锭
//            loadProductList_Iron();
//            //调用已绑定页面分页方法
//            isBindProductList();
		   	});
			
	   } 
	//查询数据方法
	function searchAjax(productId){
		
//        //去数据中查询数据填充在页面上
        $.ajax({
       	 url:"/product/bindOneSearch.json",
       	 data : {//左面是数据名称-键，右面是值
       		 id:productId
			},
			type : 'POST',
			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
				//渲染order列表和页面--列表+分页一起填充数据显示条目
				if(result.ret){
					$(".one").hide();
					$(".two").show();
					$("#productId").val(result.data.productId);
					$("#productBakweight").val(result.data.productBakweight);
					$("#productLeftweight").val(result.data.productLeftweight);
					//调用分页方法，只查钢锭
		            loadProductList_Iron();
		            //调用已绑定页面分页方法
		            isBindProductList();
				}
			}
        });
	}
	//已绑定页面分页
	function isBindProductList(urlnew){
		if (urlnew) {
			url = urlnew;
		} else {
			url = "/product/productIron.json";
		}
		//用来做查询已绑定材料的条件
		isbindIcon_status=$("#isbindIcon_status").val();
//		alert(isbindIcon_status);
		//父id
//		alert(productId_F);
			//默认7条数据
		var bindPageSize =7;
		//当前页
		pageNo = $("#isBindListPage .pageNo").val() || 1;
		//
		search_status=1;
		$.ajax({
			url:url,
			data : {
				pageNo : pageNo,
				pageSize : bindPageSize,
				search_status,
				isbindIcon_status:isbindIcon_status,
				productId_F:productId_F
			},
			type : 'POST',
			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
				//渲染模板
//				alert("返回成功")
				renderisBind(result, url);
			}
		});
		
	}
	function renderisBind(result,url){
		if (result.ret) {
			//再次初始化查询条件
			url="/product/productIron.json";
			//如果查询到数据库中有符合条件的product列表
			if (result.data.total > 0) {
				var rendered = Mustache.render(
						isBindTemplate,
						{
							"productList" : result.data.data
						});
				
				$('#isBindList').html(rendered);
			}
			else{
				$('#isBindList').html('');
			}
			unbind();//解绑方法
			var bindPageSize = 7;
			var pageNo = $("#isBindListPage .pageNo").val() || 1;
//			alert(url);
			//渲染页码
			renderisBindPage(
					url,
					result.data.total,
					pageNo,
					bindPageSize,
					result.data.total > 0 ? result.data.data.length : 0,
					"isBindListPage", isBindProductList);
		}else{
			showMessage("获取已绑定列表", result.msg, false);
		}
	}
	//解绑
	function unbind(){
		$(".unBind-btn").click(function(e){
			//阻止默认事件
	         e.preventDefault();
				//阻止事件传播
	         e.stopPropagation();
	         //获取子材料的备份剩余重量
	         var product_bakweight=$(this).attr("product_bakweight");
//	         alert("子材料备份重量"+product_bakweight);
	         //获取父材料的备份剩余重量
	        var productBakweight_F= $("#productBakweight").val()
//	        alert("父材料备份重量"+productBakweight_F);
	        //获取子材料id
	        var id=$(this).attr("data-id");
	        //获取父材料id
	        //拼接id
	        var ids=id+"&"+productId_F;
//	        alert(ids);
	        $.ajax({
	        	url:"/product/unBindProduct.json",
	        	 data : {//左面是数据名称-键，右面是值
	        		 bakweight:product_bakweight,
	        		 bakweight_F:productBakweight_F,
	        		 ids:ids
	 			},
	 			type : 'POST',
	 			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
//	 				alert("返回成功");
	 				searchAjax(productId);
	 				loadProductList_Iron();
	 				
	 				 //调用已绑定页面分页方法
	 	            isBindProductList();
	 			}
	        });
	        
		});
	}
	
	
//------------------------------------------------------------------------	
	//未绑定页面分页
	function loadProductList_Iron(urlnew){
		if (urlnew) {
			url = urlnew;
		} else {
			url = "/product/unboundedProduct.json";
		}
		bindIcon_status=$("#bindIcon_status").val();
		var productMaterialsource=$("#productMaterialsource").val();
			//默认7条数据
		var bindPageSize =7;
		//当前页
		pageNo = $("#unboundedListPage .pageNo").val() || 1;
		$.ajax({
			url:url,
			data : {
				pageNo : pageNo,
				pageSize : bindPageSize,
				bindIcon_status:bindIcon_status,
				search_materialsource:productMaterialsource
			},
			type : 'POST',
			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
				//渲染模板
				renderUnbounded(result, url);
			}
		});
		
	}
	//点击刷新按钮
	$(".researchBind").click(function(e) {
		e.preventDefault();
		$("#unboundedListPage .pageNo").val(1);
		loadProductList_Iron();
	});
	function renderUnbounded(result,url){
		if (result.ret) {
			//再次初始化查询条件
			url="/product/productIron.json";
			//如果查询到数据库中有符合条件的order列表
			if (result.data.total > 0) {
				var rendered = Mustache.render(
						unboundedTemplate,
						{
							"productList" : result.data.data
						});
				
				$('#unboundedList').html(rendered);
			}
			else{
				$('#unboundedList').html('');
			}
			realBind();//真正的绑定方法
			var bindPageSize = 7;
			var pageNo = $("#unboundedListPage .pageNo").val() || 1;
//			alert(url);
			//渲染页码
			renderBindPage(
					url,
					result.data.total,
					pageNo,
					bindPageSize,
					result.data.total > 0 ? result.data.data.length : 0,
					"unboundedListPage", loadProductList_Iron);
		}else{
			showMessage("获取未绑定列表", result.msg, false);
		}
	}
//
	
	//真正的绑定事件
	function realBind(){
		$(".realBind-btn").click(function(e){
			 //阻止默认事件
	         e.preventDefault();
				//阻止事件传播
	         e.stopPropagation();
			  //获取productid_Iron 钢锭id
	         //获取子材料id
	         var productId_Iron = $(this).attr("data-id");
	         //获取子材料工艺重量
	         var product_targetweight=$(this).attr("product_targetweight");
	         //获取父材料id  
//	         alert(productId_Iron);
//	         alert(productId_F);
//	         alert(product_targetweight);
//	         alert(product_leftweight_F);
	         var ids=productId_Iron+"&"+productId_F;
//	         alert(ids);
//	        ($("#productBakweight").val()-product_targetweight)>=0? 
//	        	 $("#productBakweight").val($("#productBakweight").val()-product_targetweight);
	        
	        var status=$("#productBakweight").val()-product_targetweight;
	        status>=0?$("#productBakweight").val($("#productBakweight").val()-product_targetweight):alert("不能在减少了，抽干了");
	         if(status>=0){
	         $.ajax({
	        	 url:"/product/realBind.json",
	 			data : {
	 				ids:ids,
	 				status:status
	 			},
	 			type : 'POST',
	 			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
	 				//渲染模板
//	 				renderUnbounded(result, url);
	 				loadProductList_Iron();
	 				 //调用已绑定页面分页方法
	 	            isBindProductList();
	 			}
	         });
	         }
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