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
	 var productId_F;//存放productid-父id
	//加载模板内容进html
	//获取模板内容
	var productListTemplate = $("#productListTemplate").html();
	//将内容交给mustache来处理
	Mustache.parse(productListTemplate);
	
	var unboundedTemplate=$("#unboundedTemplate").html();
	
	Mustache.parse(unboundedTemplate);
	
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
		search_status=1;
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
//	//点击绑定
	function bindProductClick(){
		   $(".bind-btn").click(function(e) {
			 //阻止默认事件
	         e.preventDefault();
				//阻止事件传播
	         e.stopPropagation();
	       //获取productid
	         var productId = $(this).attr("data-id");
	        if(productId){
	        	productId_F=productId;
	        }
	         
	        $(".one").hide();
	        $(".two").show();
	      //拿到map中以键值对，id-product对象结构的对象,用来向form表单中传递数据
            var targetProduct = productMap[productId];
            //如果取出这个对象
            if (targetProduct) {
					/////////////////////////////////////////////////////////////////
          	  		$("#productId").val(targetProduct.productId);
					$("#productBakweight").val(targetProduct.productBakweight);
					$("#productLeftweight").val(targetProduct.productLeftweight);
					$("#productMaterialsource").val(targetProduct.productMaterialsource);
					/////////////////////////////////////////////////////////////////
            }
	        //调用分页方法，只查钢锭
            loadProductList_Iron();
		   	});
			
	   } 
	
	//只查询钢锭的分页方法
	function loadProductList_Iron(urlnew){
		if (urlnew) {
			url = urlnew;
		} else {
			url = "/product/productIron.json";
		}
			//默认7条数据
		var bindPageSize =7;
		//当前页
		pageNo = $("#unboundedListPage .pageNo").val() || 1;
		$.ajax({
			url:url,
			data : {
				pageNo : pageNo,
				pageSize : bindPageSize
			},
			type : 'POST',
			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
				//渲染模板
				renderUnbounded(result, url);
			}
		});
	}
	
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
				$('#productList').html('');
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
	         var productId_Iron = $(this).attr("data-id");
	         //获取子材料id
	         alert(productId_Iron);
	         //获取父材料id  
	         alert(productId_F);
	         var ids=productId_Iron+"&"+productId_F;
	         alert(ids);
	         $.ajax({
	        	 url:"/product/realBind.json",
	 			data : {
	 				ids : ids
	 			},
	 			type : 'POST',
	 			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
	 				//渲染模板
//	 				renderUnbounded(result, url);
	 				alert("返回成功");
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