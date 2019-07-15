$(function(){
	//执行分页逻辑
	//定义一些全局变量
	var orderMap = {};//准备一个map格式的仓库，等待存储从后台返回过来的数据
	var optionStr;//选项参数
	var pageSize;//页码条数
	var pageNo;//当前页
	var url;//查询url
	var keyword;//关键字
	var search_status;//查询状态
	var fromTime;
	var toTime;
	//加载模板内容进html
	//获取模板内容
	var orderListTemplate = $("#orderListTemplate").html();
	//将内容交给mustache来处理
	Mustache.parse(orderListTemplate);
	
	//调用分页函数
	loadOrderList();
	//点击刷新时也要调用分页函数
	$(".research").click(function(e) {
		e.preventDefault();
		$("#orderPage .pageNo").val(1);
		loadOrderList();
	});
	//定义调用分页函数，一定包含查询条件keyword，search_status。。
	function loadOrderList(urlnew) {
		//获取页面当前需要查询的还留在页码上的信息
		//在当前页中找到需要调用的页码条数
		pageSize = $("#pageSize").val();
		//当前页
		pageNo = $("#orderPage .pageNo").val() || 1;
		if (urlnew) {
			url = urlnew;
		} else {
			url = "/order/order.json";
		}
		keyword = $("#keyword").val();
		fromTime = $("#fromTime").val();
		toTime = $("#toTime").val();
		search_status = $("#search_status").val();
		//发送请求
		$.ajax({
			url : url,
			data : {//左面是数据名称-键，右面是值
				pageNo : pageNo,
				pageSize : pageSize,
				keyword : keyword,
				fromTime : fromTime,
				toTime : toTime,
				search_status : search_status,
			},
			type : 'POST',
			success : function(result) {//jsondata  jsondata.getData=pageResult  pageResult.getData=list
				//渲染order列表和页面--列表+分页一起填充数据显示条目
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
			url = "/order/order.json";
			keyword = $("#keyword").val();
			fromTime = $("#fromTime").val();
			toTime = $("#toTime").val();
			search_status = $("#search_status").val();
			//如果查询到数据库中有符合条件的order列表
			if (result.data.total > 0) {
				//为订单赋值--在对orderlisttemplate模板进行数据填充--视图渲染
//				//Mustache.render({"name":"李四","gender":"男"});
//				//Mustache.render(list=new ArrayList<String>(){"a01","a02"},{"name":"list[i].name","gender":list[i].gender});
				var rendered = Mustache.render(
								orderListTemplate,//<script id="orderListTemplate" type="x-tmpl-mustache">
								{
									"orderList" : result.data.data,//{{#orderList}}--List-(result.data.data-list<MesOrder>)
									"come_date" : function() {
										return function(text, render) {
											return new Date(
													this.orderCometime)
													.Format("yyyy-MM-dd");
										}
									},
									"commit_date" : function() {
										return function(text, render) {
											return new Date(
													this.orderCommittime)
													.Format("yyyy-MM-dd");
										}
									},
									"showStatus" : function() {
										return this.orderStatus == 1 ? '有效'
												: (this.orderStatus == 0 ? '无效'
														: '删除');
									},
									"bold" : function() {
										return function(text, render) {
											var status = render(text);
											if (status == '有效') {
												return "<span class='label label-sm label-success'>有效</span>";
											} else if (status == '无效') {
												return "<span class='label label-sm label-warning'>无效</span>";
											} else {
												return "<span class='label'>删除</span>";
											}
										}
									}
								});
				
				$.each(result.data.data, function(i, order) {//java-增强for
					order.orderCometime = new Date(order.orderCometime)
							.Format("yyyy-MM-dd");
					order.orderCommittime = new Date(order.orderCommittime)
							.Format("yyyy-MM-dd");
					orderMap[order.id] = order;//result.data.data等同于List<mesOrder>
					//order.id-order  map key-value
				});
				$('#orderList').html(rendered);
			} else {
				$('#orderList').html('');
			}
			bindOrderClick();//更新操作
			var pageSize = $("#pageSize").val();
			var pageNo = $("#orderPage .pageNo").val() || 1;
			//渲染页码
			renderPage(
					url,
					result.data.total,
					pageNo,
					pageSize,
					result.data.total > 0 ? result.data.data.length : 0,
					"orderPage", loadOrderList);
		} else {
			showMessage("获取订单列表", result.msg, false);
		}
	}
	function bindOrderClick(){
		   $(".order-edit").click(function(e) {
			//阻止默认事件
         e.preventDefault();
			//阻止事件传播
         e.stopPropagation();
			//获取orderid
         var orderId = $(this).attr("data-id");
			//弹出order的修改弹窗 
         $("#dialog-orderUpdate-form").dialog({
             model: true,
             title: "编辑订单",
             open: function(event, ui) {
          	    $(".ui-dialog").css("width","600px");
                 $(".ui-dialog-titlebar-close", $(this).parent()).hide();
               	//将form表单中的数据清空，使用jquery转dom对象
                 $("#orderUpdateForm")[0].reset();
               	//拿到map中以键值对，id-order对象结构的对象,用来向form表单中传递数据
                 var targetOrder = orderMap[orderId];
               	//如果取出这个对象
                 if (targetOrder) {
						/////////////////////////////////////////////////////////////////
						$("#input-Id2").val(targetOrder.id);
						$("#input-orderId2").val(targetOrder.orderId);
						$("#input-orderClientname2").val(targetOrder.orderClientname);
						$("#input-orderProductname2").val(targetOrder.orderProductname);
						$("#input-orderContractid2").val(targetOrder.orderContractid);
						$("#input-orderImgid2").val(targetOrder.orderImgid);
						$("#input-orderMaterialname2").val(targetOrder.orderMaterialname);
						$("#input-orderCometime2").val(targetOrder.orderCometime);
						$("#input-orderCommittime2").val(targetOrder.orderCommittime);
						$("#input-orderInventorystatus2").val(targetOrder.orderInventorystatus);
						$("#input-orderSalestatus2").val(targetOrder.orderSalestatus);
						$("#input-orderMaterialsource2").val(targetOrder.orderMaterialsource);
						$("#input-orderHurrystatus2").val(targetOrder.orderHurrystatus);
						$("#input-orderStatus2").val(targetOrder.orderStatus);
						$("#input-orderRemark2").val(targetOrder.orderRemark);
						/////////////////////////////////////////////////////////////////
                 }
             },
             buttons : {
                 "更新": function(e) {
                     e.preventDefault();
                     updateOrder(false, function (data) {
                         $("#dialog-orderUpdate-form").dialog("close");
         				$("#orderPage .pageNo").val(1);
                         loadOrderList();
                     }, function (data) {
                         showMessage("更新订单", data.msg, false);
                     })
                 },
                 "取消": function (data) {
                     $("#dialog-orderUpdate-form").dialog("close");
                 }
             }
         });
     });
	   } 
	
	//更新
	function updateOrder(isCreate, successCallbak, failCallbak) {
		$.ajax({
			url : isCreate ? "/order/insert.json"
					: "/order/update.json",
			data : isCreate ? $("#orderBatchForm").serializeArray() : $(
					"#orderUpdateForm").serializeArray(),
			type : 'POST',
			success : function(result) {
				//数据执行成功返回的消息
				if (result.ret) {
					console.log(66);
                	loadOrderList(); // 带参数回调
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
	
//////////////////////////////////////////////////////////////
	//日期显示
	$('.datepicker').datepicker({
		dateFormat : 'yy-mm-dd',
		showOtherMonths : true,
		selectOtherMonths : false
	});
	
});