$(function(){
	
	
	//定义全局变量
	//分页
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
	var orderBatchListTemplate=$("#orderBatchListTemplate").html();
	//使用mustache模板加载内容
	Mustache.parse(orderBatchListTemplate);
	loadOrderList();
	//点击刷新的时候也需要调用分页函数
	$(".research").click(function(e) {
		e.preventDefault();
		$("#orderPage .pageNo").val(1);
		loadOrderList();
	});
	//定义调用分页函数，一定是当前的查询条件下（keyword，search_status。。）的分页
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
	
	//渲染所有的mustache模板页面
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
						orderBatchListTemplate,//<script id="orderListTemplate" type="x-tmpl-mustache">
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
           // bindOrderClick();//更新操作
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
	
	
	
	
	
	
	
	$(".order-add").click(function(){
		//弹出框
		$("#dialog-order-form").dialog(
				{
					model : true,//背景不可点击
					title : "新建订单",//模态框标题
					open : function(event, ui) {
						$(".ui-dialog").css("width", "700px");//增加模态框的宽高
						$(".ui-dialog-titlebar-close",
								$(this).parent()).hide();//关闭默认叉叉
						optionStr = "";
						$("#orderBatchForm")[0].reset();//清空模态框--jquery 将指定对象封装成了dom对象
					},
					buttons : {
						"添加" : function(e) {
							//阻止一下默认事件
							e.preventDefault();
							//发送新增order的数据和接收添加后的回收信息
							//添加操作，将来会和更新操作共用
							updateOrder(true, function(data) {
								//增加成功了
								//提示增加用户成功信息
								showMessage("新增订单", data.msg,
										true);
								$("#dialog-order-form").dialog(
										"close");
	                         	loadOrderList();//根据参数查看
							}, function(data) {
								//增加失败了
								//						alert("添加失败了");
								//信息显示
								showMessage("新增订单", data.msg,
										false);
								//						$("#dialog-order-form").dialog("close");
							});
						},
						"取消" : function() {
							$("#dialog-order-form").dialog(
									"close");
						}
					}
				});
	});
	//更新  添加
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
                	//loadOrderList(); // 带参数回调
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